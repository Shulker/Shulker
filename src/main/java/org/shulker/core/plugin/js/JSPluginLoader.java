/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.plugin.js;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.Validate;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.*;
import org.shulker.core.Shulker;
import org.shulker.core.commands.BukkitCommandResult;
import org.yaml.snakeyaml.error.YAMLException;

import javax.script.ScriptException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class JSPluginLoader implements PluginLoader
{
    private final Pattern[] file_filters = new Pattern[]{Pattern.compile("_jsplugin$")};
    private       Server    server;

    public JSPluginLoader(Server server)
    {
        this.server = server;
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException
    {
        Validate.notNull(file, "File cannot be null");
        if (!file.exists())
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));

        PluginDescriptionFile description;
        try {
            description = this.getPluginDescription(file);
        } catch (InvalidDescriptionException e) {
            throw new InvalidPluginException(e);
        }

        var config_dir = Shulker.get_configuration_dir();
        var data_folder = new File(config_dir, description.getName());

        if (data_folder.exists() && !data_folder.isDirectory())
            throw new InvalidPluginException(String.format("Projected datafolder: `%s' for %s (%s) exists and is not a directory", data_folder, description.getFullName(), file));
        else {
            for (var plugin_name : description.getDepend()) {
                Plugin current = Bukkit.getPluginManager().getPlugin(plugin_name);
                if (current == null)
                    throw new UnknownDependencyException(plugin_name);
            }

            String script;

            try {
                var main_file = new File(file, description.getMain());
                BufferedReader br = new BufferedReader(new FileReader(main_file));
                StringBuilder sb = new StringBuilder();
                String line;

                sb.append("load(\"").append(Shulker.get_plugins_dir().getAbsolutePath()).append("/shulker/shulker.js\");\n");
                sb.append("importPackage(org.aperlambda.kimiko);\n");
                sb.append("importPackage(org.shulker.core);\n");
                sb.append("importPackage(org.shulker.core.commands);\n");
                sb.append("importPackage(org.shulker.core.config);\n");
                sb.append("importPackage(org.bukkit);\n");
                sb.append("\n");

                while ((line = br.readLine()) != null)
                    sb.append(line).append('\n');

                script = sb.toString();

                br.close();
            } catch (IOException e) {
                throw new InvalidPluginException(e);
            }

            JSPlugin plugin;
            NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
            // --language=es6 provides the support of ECMAScript 6
            NashornScriptEngine js = (NashornScriptEngine) factory.getScriptEngine("--language=es6");
            try {
                js.put("server", Bukkit.getServer());
                js.put("description", description);
                js.put("dataFolder", data_folder);
                js.put("__SHULKER__", Shulker.get());

                JSUtils.load_class_static(js, "CommandResult", BukkitCommandResult.class);
                JSUtils.load_class_static(js, "Bukkit", Bukkit.class);
                JSUtils.load_class(js, "Listener", Listener.class);
                JSUtils.load_class_static(js, "ChatColor", ChatColor.class);
                JSUtils.load_class(js, "ResourceName", ResourceName.class);
                JSUtils.load_class_static(js, "ChatComponentBuilder", ComponentBuilder.class);

                js.eval(script);
            } catch (ScriptException e) {
                throw new InvalidPluginException(e);
            }
            plugin = new JSPlugin();
            plugin.init(this, server, description, data_folder, file, js);
            plugin.getLogger().info("Loading " + description.getFullName());
            plugin.onLoad();
            return plugin;
        }
    }

    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException
    {
        Validate.notNull(file, "File cannot be null");
        InputStream descr_stream = null;
        PluginDescriptionFile pdf;
        if (file.isDirectory()) {
            try {
                var plugin_yaml_file = new File(file, "plugin.yml");
                if (!plugin_yaml_file.exists())
                    throw new InvalidDescriptionException(new FileNotFoundException("JS plugin does not contain plugin.yml"));

                descr_stream = new FileInputStream(plugin_yaml_file);
                pdf = new PluginDescriptionFile(descr_stream);
            } catch (IOException | YAMLException e) {
                throw new InvalidDescriptionException(e);
            } finally {
                if (descr_stream != null) {
                    try {
                        descr_stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else
            throw new InvalidDescriptionException("Plugin is a file, expected a folder.");
        return pdf;
    }

    @Override
    public Pattern[] getPluginFileFilters()
    {
        return file_filters;
    }

    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin)
    {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(listener, "Listener can not be null");

        boolean use_timings = server.getPluginManager().useTimings();
        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] public_methods = listener.getClass().getMethods();
            Method[] private_methods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<>(public_methods.length + private_methods.length, 1.0f);
            methods.addAll(Arrays.asList(public_methods));
            methods.addAll(Arrays.asList(private_methods));
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().severe(
                    "Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " +
                            listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            // Do not register bridge or synthetic methods to avoid event duplication
            // Fixes SPIGOT-893
            if (method.isBridge() || method.isSynthetic())
                continue;

            final Class<?> check_class;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(check_class = method.getParameterTypes()[0])) {
                plugin.getLogger().severe(plugin.getDescription().getFullName() +
                        " attempted to register an invalid EventHandler method signature \"" +
                        method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            final Class<? extends Event> event_class = check_class.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> event_set = ret.computeIfAbsent(event_class, k -> new HashSet<>());

            for (Class<?> clazz = event_class; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                // This loop checks for extending deprecated events
                if (clazz.getAnnotation(Deprecated.class) != null) {
                    Warning warning = clazz.getAnnotation(Warning.class);
                    Warning.WarningState warning_state = server.getWarningState();
                    if (!warning_state.printFor(warning)) {
                        break;
                    }
                    plugin.getLogger().log(
                            Level.WARNING,
                            String.format(
                                    "\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated." +
                                            " \"%s\"; please notify the authors %s.",
                                    plugin.getDescription().getFullName(),
                                    clazz.getName(),
                                    method.toGenericString(),
                                    (warning != null && warning.reason().length() != 0) ? warning.reason() :
                                            "Server performance will be affected",
                                    Arrays.toString(plugin.getDescription().getAuthors().toArray())),
                            warning_state == Warning.WarningState.ON ? new AuthorNagException(null) : null);
                    break;
                }
            }

            EventExecutor executor = (listener1, event) -> {
                try {
                    if (!event_class.isAssignableFrom(event.getClass()))
                        return;
                    method.invoke(listener1, event);
                } catch (InvocationTargetException ex) {
                    throw new EventException(ex.getCause());
                } catch (Throwable t) {
                    throw new EventException(t);
                }
            };

            if (use_timings)
                event_set.add(new TimedRegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
            else
                event_set.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
        }
        return ret;
    }

    @Override
    public void enablePlugin(Plugin plugin)
    {
        Validate.isTrue(plugin instanceof JSPlugin, "Plugin is not associated with this PluginLoader");
        if (!plugin.isEnabled()) {
            plugin.getLogger().info("Enabling " + plugin.getDescription().getFullName());
            JSPlugin js_plugin = (JSPlugin) plugin;

            try {
                js_plugin.set_enabled(true);
            } catch (Throwable var5) {
                server.getLogger().log(Level.SEVERE,
                        "Error occurred while enabling " + plugin.getDescription().getFullName() +
                                " (Is it up to date?)", var5);
            }

            server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    @Override
    public void disablePlugin(Plugin plugin)
    {
        Validate.isTrue(plugin instanceof JSPlugin, "Plugin is not associated with this PluginLoader");
        if (plugin.isEnabled()) {
            String message = String.format("Disabling %s", plugin.getDescription().getFullName());
            plugin.getLogger().info(message);
            server.getPluginManager().callEvent(new PluginDisableEvent(plugin));
            JSPlugin js_plugin = (JSPlugin) plugin;

            try {
                js_plugin.set_enabled(false);
            } catch (Throwable var9) {
                this.server.getLogger().log(Level.SEVERE,
                        "Error occurred while disabling " + plugin.getDescription().getFullName() +
                                " (Is it up to date?)", var9);
            }
        }
    }
}
