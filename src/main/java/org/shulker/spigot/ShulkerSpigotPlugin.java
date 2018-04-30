/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
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
import org.aperlambda.lambdacommon.utils.LambdaReflection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.MinecraftManager;
import org.shulker.core.Shulker;
import org.shulker.core.ShulkerLibrary;
import org.shulker.core.ShulkerPlugin;
import org.shulker.core.commands.BukkitCommandManager;
import org.shulker.core.commands.HelpSubCommand;
import org.shulker.core.commands.defaults.LibrariesCommand;
import org.shulker.core.commands.defaults.ShulkerCommand;
import org.shulker.core.commands.defaults.TestCommand;
import org.shulker.core.commands.defaults.shulker.AboutCommand;
import org.shulker.core.commands.defaults.shulker.ReloadCommand;
import org.shulker.core.config.ShulkerConfiguration;
import org.shulker.core.config.ShulkerSymbols;
import org.shulker.core.impl.reflect.ReflectMinecraftManager;
import org.shulker.core.impl.v112R1.MinecraftManagerV112R1;
import org.shulker.core.plugin.js.JSPluginLoader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.ansi;

public class ShulkerSpigotPlugin extends JavaPlugin implements ShulkerPlugin
{
	private static Optional<Method> addUrl;

	static
	{
		try
		{
			addUrl = Optional.ofNullable(URLClassLoader.class.getDeclaredMethod("addURL", URL.class));
		}
		catch (NoSuchMethodException e)
		{
			addUrl = Optional.empty();
		}
	}

	private File pluginsDir = getDataFolder().getParentFile();
	private File baseDir    = pluginsDir.getParentFile();

	private BukkitCommandManager commandManager;
	private ShulkerConfiguration config  = new ShulkerConfiguration();
	private ShulkerSymbols       symbols = new ShulkerSymbols();

	/**
	 * Minecraft manager
	 */
	private MinecraftManager mcManager;

	private List<ShulkerLibrary> libraries = new ArrayList<>();

	@Override
	public void onLoad()
	{
		super.onLoad();
		Shulker.init(this);
		config.load();

		var file = new File(baseDir, "addons/libs");
		file.mkdirs();
		File[] libs = file.listFiles();
		if (libs != null)
			for (var lib : libs)
			{
				if (!addUrl.isPresent())
				{
					logError(getPrefix(), "Cannot load libraries. (Cannot access to method addURL in URLClassLoader)");
					break;
				}
				if (lib.getName().endsWith(".jar"))
				{
					ShulkerLibrary library;
					try
					{
						var url = lib.toURI().toURL();
						var classLoader = (URLClassLoader) getClassLoader();

						boolean next = false;
						for (URL it : classLoader.getURLs())
							if (it.equals(url))
								next = true;

						if (next)
							continue;

						library = new ShulkerLibrary(lib.getName().replace(".jar", ""), lib, ShulkerLibrary.LibraryLoadState.SUCCESS);

						logInfo(getPrefix(), "Loading library '" +
								ansi().fg(Color.YELLOW).a(lib.getName().replace(".jar", "")).fg(Color.WHITE).toString() +
								"'...");

						LambdaReflection.invokeMethod(classLoader, addUrl.get(), url);
					}
					catch (MalformedURLException e)
					{
						library = new ShulkerLibrary(lib.getName().replace(".jar", ""), lib, ShulkerLibrary.LibraryLoadState.FAILED);
					}
					libraries.add(library);
				}
			}

		commandManager = new BukkitCommandManager();

		if (config.useJavascriptSupport())
		{
			getServer().getPluginManager().registerInterface(JSPluginLoader.class);
			logInfo(getPrefix(), "Loading Javascript plugins...");
			Arrays.stream(Objects.requireNonNull(pluginsDir.listFiles(), "An unexpected error occurred (Cannot lists plugins folder)!"))
					.filter(f -> f.getName().endsWith(".jsar")).forEach(f -> {
				try
				{
					getServer().getPluginManager().loadPlugin(f);
				}
				catch (InvalidPluginException | InvalidDescriptionException e)
				{
					e.printStackTrace();
				}
			});
		}
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		symbols.load();

		switch (getServerVersion())
		{
			case "v1_12_R1":
				mcManager = new MinecraftManagerV112R1();
				break;
			default:
				mcManager = new ReflectMinecraftManager();
				logInfo(getPrefix(), ansi().fg(Color.YELLOW).a("WARNING: ").fgBright(Color.MAGENTA).a("Shulker ").fg(Color.YELLOW).a("uses ReflectMinecraftManager due to unsupported Minecraft version. Please contact the developer.").reset().toString());
				break;
		}
		logInfo(getPrefix(), ansi().a("Using ").fg(Color.CYAN).a(mcManager.getName()).fgBright(Color.WHITE).a(" with ").fg(Color.CYAN).a(mcManager.getWrapperManager().getName()).fgBright(Color.WHITE).a("...").reset().toString());
		Bukkit.getPluginManager().registerEvents(new ShulkerListener(), this);
		Bukkit.getOnlinePlayers().forEach(p -> mcManager.addPlayer(p));

		Metrics metrics = new Metrics(this);
		metrics.addCustomChart(new Metrics.SimplePie("internal_version", ShulkerSpigotPlugin::getServerVersion));
		metrics.addCustomChart(new Metrics.SimplePie("minecraft_manager_used", () -> mcManager.getName()));

		/*
			COMMANDS SECTION
		 */

		LibrariesCommand librariesCommand = new LibrariesCommand();
		commandManager.register(new CommandBuilder<CommandSender>(new ResourceName(getName(), "libraries"))
										.usage("<command> [library]").description("Lists libraries or display info about one.")
										.permission("shulker.commands.libraries").executor(librariesCommand)
										.tabCompleter(librariesCommand).build());
		ShulkerCommand shulkerCommandExecutor = new ShulkerCommand();
		var shulkerCommand = new CommandBuilder<CommandSender>(new ResourceName(getName(), "shulker"))
				.usage("<command> [subcommand [args]]").description("The Shulker command.")
				.executor(shulkerCommandExecutor).tabCompleter(shulkerCommandExecutor).build();
		shulkerCommand.addSubCommand(new AboutCommand().getResultCommand());
		var reloadSubCommand = new ReloadCommand();
		shulkerCommand.addSubCommand(new ResourceName(getName(), "reload"), "<command>", "Reloads Shulker", "shulker.reload", new ArrayList<>(), reloadSubCommand, reloadSubCommand);
		var shulkerHelpCommand = new HelpSubCommand("shulker.commands.help", ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, ChatColor.GOLD);
		shulkerHelpCommand.setTitle("Shulker");
		shulkerCommand.addSubCommand(shulkerHelpCommand.getResultCommand());
		shulkerCommand.setPermissionRequired(shulkerHelpCommand.getResultCommand().getPermissionRequired());
		commandManager.register(shulkerCommand);

		if (config.hasDebug())
			commandManager.register(new TestCommand().getResultCommand());
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		libraries.clear();
	}

	@Override
	public String getVersion()
	{
		return getDescription().getVersion();
	}

	@Override
	public String getPrefix()
	{
		return ansi().fg(Color.WHITE).a("[").fgBright(Color.MAGENTA).a("Shulker").fg(Color.WHITE).a("]").reset().toString();
	}

	@Override
	public void logInfo(@Nullable String prefix, @NotNull String message)
	{
		String p = "";
		if (prefix != null)
			p = prefix + " ";
		System.out.println(p + ansi().fg(Color.WHITE).a(message).reset().toString());
	}

	@Override
	public void logDebug(@Nullable String prefix, @NotNull String message)
	{
		if (config.hasDebug())
		{
			StringBuilder string = new StringBuilder();
			for (int i = 0; i < message.length(); i++)
			{
				char c = message.charAt(i);
				if (i + 1 != message.length())
				{
					if (c == '=' || c == ':')
					{
						if (Character.isDigit(message.charAt(i + 1)))
						{
							i++;
							string.append(c).append(ansi().fg(Color.CYAN).toString());
							while (i < message.length() && Character.isDigit(message.charAt(i)))
							{
								string.append(message.charAt(i));
								i++;
							}
							string.append(ansi().fg(Color.WHITE)).append(message.charAt(i));
							continue;
						}
						else if (Character.isLetter(message.charAt(i + 1)))
						{
							i++;
							if (((i + 3) < message.length()) && message.charAt(i) == 'n' &&
									message.charAt(i + 1) == 'u' && message.charAt(i + 2) == 'l' &&
									message.charAt(i + 3) == 'l')
							{
								string.append(c).append(ansi().fg(Color.YELLOW).a("null").fg(Color.WHITE).toString());
								i = i + 3;
								continue;
							}
							else
							{
								string.append(c).append(message.charAt(i));
								continue;
							}
						}
					}
					else if (c == '"' || c == '\'')
					{
						if (i == 0 || message.charAt(i - 1) == ':' || message.charAt(i - 1) == '=')
						{
							string.append(c).append(ansi().fgBright(Color.GREEN));
							continue;
						}
						else if (i + 1 < message.length() &&
								(message.charAt(i + 1) == ',' || message.charAt(i + 1) == '}' ||
										message.charAt(i + 1) == ']'))
							string.append(ansi().fg(Color.WHITE));
					}
				}
				string.append(c);
			}

			String p = " ";
			if (prefix != null)
				p = prefix + " ";
			System.out.println("[DEBUG]" + p + ansi().fg(Color.WHITE).a(string.toString()).reset().toString());
		}
	}

	@Override
	public void logError(@Nullable String prefix, @NotNull String message)
	{
		String p = "";
		if (prefix != null)
			p = prefix + "";
		p += ansi().fg(Color.RED).a("[Error] ").reset().toString();
		System.out.println(p + ansi().fgBright(Color.RED).a(message).reset().toString());
	}

	@Override
	public File getBaseDirectory()
	{
		return baseDir;
	}

	@Override
	public File getPluginsDirectory()
	{
		return pluginsDir;
	}

	@Override
	public @NotNull BukkitCommandManager getCommandManager()
	{
		return commandManager;
	}

	@Override
	public ShulkerSymbols getSymbolsManager()
	{
		return symbols;
	}

	@Override
	public ShulkerConfiguration getConfiguration()
	{
		return config;
	}

	@Override
	public MinecraftManager getMinecraftManager()
	{
		return mcManager;
	}

	@Override
	public List<ShulkerLibrary> getLibraries()
	{
		return ImmutableList.copyOf(libraries);
	}

	/**
	 * Gets the version of your server by the packages
	 *
	 * @return The server version
	 */
	public static String getServerVersion()
	{
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);

	}

	public static Class<?> getNmsClass(String name)
	{
		try
		{
			return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}