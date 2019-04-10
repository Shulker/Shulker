/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.spigot;

import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.ChatColor;
import org.aperlambda.kimiko.CommandBuilder;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.aperlambda.lambdacommon.resources.ResourcesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.*;
import org.shulker.core.commands.BukkitCommandManager;
import org.shulker.core.commands.HelpSubCommand;
import org.shulker.core.commands.defaults.LibrariesCommand;
import org.shulker.core.commands.defaults.ShulkerCommand;
import org.shulker.core.commands.defaults.TestCommand;
import org.shulker.core.commands.defaults.shulker.AboutCommand;
import org.shulker.core.commands.defaults.shulker.ReloadCommand;
import org.shulker.core.config.ShulkerConfiguration;
import org.shulker.core.config.ShulkerSymbols;
import org.shulker.core.events.PacketEvent;
import org.shulker.core.events.PacketListener;
import org.shulker.core.impl.reflect.ReflectMinecraftManager;
import org.shulker.core.impl.v112R1.MinecraftManagerV112R1;
import org.shulker.core.packets.handler.NMSPacketHandler;
import org.shulker.core.packets.handler.PacketHandler;
import org.shulker.core.plugin.js.JSEventsManager;
import org.shulker.core.plugin.js.JSPluginLoader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.ansi;

public class ShulkerSpigotPlugin extends JavaPlugin implements ShulkerPlugin
{
    private static Optional<Method> ADD_URL;

    static {
        try {
            ADD_URL = Optional.ofNullable(URLClassLoader.class.getDeclaredMethod("addURL", URL.class));
        } catch (NoSuchMethodException e) {
            ADD_URL = Optional.empty();
        }
    }

    private File plugins_dir = this.getDataFolder().getParentFile();
    private File base_dir    = this.plugins_dir.getParentFile();
    private File addons_dir  = new File(base_dir, "addons/");

    private BukkitCommandManager command_manager;
    private ShulkerConfiguration config  = new ShulkerConfiguration();
    private ShulkerSymbols       symbols = new ShulkerSymbols();

    /**
     * Minecraft manager
     */
    private MinecraftManager     mc_manager;
    private PacketHandler        packet_handler;
    private List<PacketListener> packet_listeners = new ArrayList<>();

    private List<ShulkerLibrary> libraries = new ArrayList<>();

    @Override
    public void onLoad()
    {
        super.onLoad();
        Shulker.init(this);
        config.load();

        var file = new File(addons_dir, "libs");
        file.mkdirs();
        File[] libs = file.listFiles();
        if (libs != null)
            for (var lib : libs) {
                if (!ADD_URL.isPresent()) {
                    log_error(get_prefix(), "Cannot load libraries. (Cannot access to method addURL in URLClassLoader)");
                    break;
                }
                if (lib.getName().endsWith(".jar")) {
                    ShulkerLibrary library;
                    try {
                        var url = lib.toURI().toURL();
                        var class_loader = (URLClassLoader) getClassLoader();

                        boolean next = false;
                        for (URL it : class_loader.getURLs())
                            if (it.equals(url))
                                next = true;

                        if (next)
                            continue;

                        library = new ShulkerLibrary(lib.getName().replace(".jar", ""), lib, ShulkerLibrary.LibraryLoadState.SUCCESS);

                        log_info(get_prefix(), "Loading library '" +
                                ansi().fg(Color.YELLOW).a(lib.getName().replace(".jar", "")).fg(Color.WHITE).toString() +
                                "'...");

                        ADD_URL.get().setAccessible(true);
                        ADD_URL.get().invoke(class_loader, url);
                    } catch (Exception e) {
                        e.printStackTrace();
                        library = new ShulkerLibrary(lib.getName().replace(".jar", ""), lib, ShulkerLibrary.LibraryLoadState.FAILED);
                    }
                    libraries.add(library);
                }
            }

        command_manager = new BukkitCommandManager();

        if (config.use_js_support()) {
            JSEventsManager.register();

            ResourcesManager.get_default_resources_manager().save_resource_from_jar("shulker.js", new File(plugins_dir, "shulker/"), true);

            getServer().getPluginManager().registerInterface(JSPluginLoader.class);
            log_info(get_prefix(), "Loading Javascript plugins...");
            if (!addons_dir.exists())
                addons_dir.mkdirs();
            getServer().getPluginManager().loadPlugins(addons_dir);
        }
    }

    @Override
    public void onEnable()
    {
        super.onEnable();

        symbols.load();

        switch (get_server_version()) {
            case "v1_12_R1":
                mc_manager = new MinecraftManagerV112R1();
                break;
            default:
                mc_manager = new ReflectMinecraftManager();
                log_info(get_prefix(), ansi().fg(Color.YELLOW).a("WARNING: ").fgBright(Color.MAGENTA).a("Shulker ").fg(Color.YELLOW).a("uses ReflectMinecraftManager due to unsupported Minecraft version. Please contact the developer.").reset().toString());
                break;
        }
        mc_manager.init();
        log_info(get_prefix(), ansi().a("Using ").fg(Color.CYAN).a(mc_manager.get_name()).fgBright(Color.WHITE).a(" with ").fg(Color.CYAN).a(mc_manager.get_wrapper_manager().get_name()).fgBright(Color.WHITE).a("...").reset().toString());
        Bukkit.getPluginManager().registerEvents(new ShulkerListener(), this);

        packet_handler = new NMSPacketHandler();
        packet_handler.enable();
        Bukkit.getPluginManager().registerEvents(packet_handler, this);

        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SimplePie("internal_version", ShulkerSpigotPlugin::get_server_version));
        metrics.addCustomChart(new Metrics.SimplePie("minecraft_manager_used", () -> mc_manager.get_name()));

		/*
			COMMANDS SECTION
		 */

        LibrariesCommand libraries_command = new LibrariesCommand();
        command_manager.register(new CommandBuilder<CommandSender>(new ResourceName(getName(), "libraries"))
                .usage("<command> [library]")
                .description("Lists libraries or display info about one.")
                .permission("shulker.commands.libraries")
                .executor(libraries_command)
                .tab_completer(libraries_command)
                .build());
        ShulkerCommand shulker_command_executor = new ShulkerCommand();
        var shulker_command = new CommandBuilder<CommandSender>(new ResourceName(getName(), "shulker"))
                .usage("<command> [subcommand [args]]")
                .description("The Shulker command.")
                .executor(shulker_command_executor)
                .tab_completer(shulker_command_executor)
                .build();
        shulker_command.add_sub_command(new AboutCommand().get_result_command());
        var reload_sub_command = new ReloadCommand();
        shulker_command.add_sub_command(new ResourceName(getName(), "reload"), "<command>", "Reloads Shulker", "shulker.reload", new ArrayList<>(), reload_sub_command, reload_sub_command);
        var shulker_help_command = new HelpSubCommand("shulker.commands.help", ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, ChatColor.GOLD);
        shulker_help_command.set_title("Shulker");
        shulker_command.add_sub_command(shulker_help_command.get_result_command());
        shulker_command.set_required_permission(shulker_help_command.get_result_command().get_required_permission());
        command_manager.register(shulker_command);

        if (config.has_debug())
            command_manager.register(new TestCommand().get_result_command());
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
        packet_handler.disable();
        libraries.clear();
    }

    public List<PacketListener> get_packet_listeners()
    {
        return packet_listeners;
    }

    @Override
    public String get_version()
    {
        return this.getDescription().getVersion();
    }

    @Override
    public String get_prefix()
    {
        return ansi().fg(Color.WHITE).a("[").fgBright(Color.MAGENTA).a("Shulker").fg(Color.WHITE).a("]").reset().toString();
    }

    @Override
    public void log_info(@Nullable String prefix, @NotNull String message)
    {
        String p = "";
        if (prefix != null)
            p = prefix + " ";
        System.out.println(p + ansi().fg(Color.WHITE).a(message).reset().toString());
    }

    @Override
    public void log_debug(@NotNull DebugType type, @Nullable String prefix, @NotNull String message)
    {
        String debug_prefix = "[DEBUG]";
        if (prefix != null)
            debug_prefix += (prefix + " ");
        boolean send = false;
        if (type == DebugType.BASE && this.has_debug())
            send = true;
        else if (type == DebugType.PACKETS && config.does_debug_packets())
            send = true;
        else if (type == DebugType.CONNECTIONS && config.does_debug_connections()) {
            String p = "";
            if (prefix != null)
                p = prefix;
            debug_prefix = p + ansi().fg(Color.WHITE).a("[").fg(Color.GREEN).a("Connections").fg(Color.WHITE).a("] ").toString();
            send = true;
        }

        if (send) {
            StringBuilder string = new StringBuilder();
            for (int i = 0; i < message.length(); i++) {
                char c = message.charAt(i);
                if (i + 1 != message.length()) {
                    if (c == '=' || c == ':') {
                        if (Character.isDigit(message.charAt(i + 1))) {
                            i++;
                            string.append(c).append(ansi().fg(Color.CYAN).toString());
                            while (i < message.length() && Character.isDigit(message.charAt(i))) {
                                string.append(message.charAt(i));
                                i++;
                            }
                            string.append(ansi().fg(Color.WHITE)).append(message.charAt(i));
                            continue;
                        } else if (Character.isLetter(message.charAt(i + 1))) {
                            i++;
                            if (((i + 3) < message.length()) && message.charAt(i) == 'n' &&
                                    message.charAt(i + 1) == 'u' && message.charAt(i + 2) == 'l' &&
                                    message.charAt(i + 3) == 'l') {
                                string.append(c).append(ansi().fg(Color.YELLOW).a("null").fg(Color.WHITE).toString());
                                i = i + 3;
                                continue;
                            } else {
                                string.append(c).append(message.charAt(i));
                                continue;
                            }
                        }
                    } else if (c == '"' || c == '\'') {
                        if (i == 0 || message.charAt(i - 1) == ':' || message.charAt(i - 1) == '=') {
                            string.append(c).append(ansi().fgBright(Color.GREEN));
                            continue;
                        } else if (i + 1 < message.length() &&
                                (message.charAt(i + 1) == ',' || message.charAt(i + 1) == '}' ||
                                        message.charAt(i + 1) == ']'))
                            string.append(ansi().fg(Color.WHITE));
                    }
                }
                string.append(c);
            }

            System.out.println(debug_prefix + ansi().fg(Color.WHITE).a(string.toString()).reset().toString());
        }
    }

    @Override
    public void log_error(@Nullable String prefix, @NotNull String message)
    {
        String p = "";
        if (prefix != null)
            p = prefix + "";
        p += ansi().fg(Color.RED).a("[Error] ").reset().toString();
        System.out.println(p + ansi().fgBright(Color.RED).a(message).reset().toString());
    }

    @Override
    public File get_base_dir()
    {
        return this.base_dir;
    }

    @Override
    public File get_plugins_dir()
    {
        return this.plugins_dir;
    }

    @Override
    public @NotNull BukkitCommandManager get_commands()
    {
        return this.command_manager;
    }

    @Override
    public ShulkerSymbols get_symbols_manager()
    {
        return this.symbols;
    }

    @Override
    public ShulkerConfiguration get_configuration()
    {
        return this.config;
    }

    @Override
    public MinecraftManager get_mc()
    {
        return this.mc_manager;
    }

    @Override
    public void register_packet_listener(@NotNull PacketListener listener)
    {
        if (this.packet_listeners.contains(listener))
            return;
        this.packet_listeners.add(listener);
    }

    @Override
    public void fire_packet_event(@NotNull PacketEvent event, boolean from_client)
    {
        if (from_client)
            this.packet_listeners.forEach(listener -> listener.on_packet_receive(event));
        else
            this.packet_listeners.forEach(listener -> listener.on_packet_send(event));
    }

    @Override
    public List<ShulkerLibrary> get_libraries()
    {
        return ImmutableList.copyOf(this.libraries);
    }

    /**
     * Gets the version of your server by the packages
     *
     * @return The server version
     */
    public static String get_server_version()
    {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);

    }

    public static Class<?> get_nms_class(String name)
    {
        try {
            return Class.forName("net.minecraft.server." + get_server_version() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
