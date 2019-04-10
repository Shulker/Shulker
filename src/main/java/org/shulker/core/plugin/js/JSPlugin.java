/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.plugin.js;

import com.google.common.base.Charsets;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.aperlambda.kimiko.CommandBuilder;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.aperlambda.lambdacommon.resources.ResourcesManager;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;
import org.shulker.core.Shulker;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSPlugin extends PluginBase
{
    private boolean               is_enabled = false;
    private JSPluginLoader        loader     = null;
    private PluginDescriptionFile description;
    private File                  file;
    private File                  data_folder;
    private PluginLogger          logger;

    private File              config_file;
    private FileConfiguration new_config;

    private JSEventsManager events_manager;

    private NashornScriptEngine script_engine;

    @Override
    public final File getDataFolder()
    {
        return data_folder;
    }

    @Override
    public PluginDescriptionFile getDescription()
    {
        return description;
    }

    @Override
    public FileConfiguration getConfig()
    {
        if (new_config == null)
            reloadConfig();

        return new_config;
    }

    @Override
    public InputStream getResource(String s)
    {
        try {
            return ResourcesManager.get_default_resources_manager().get_resource(new File(file, s).toURI().toURL());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public final void saveConfig()
    {
        try {
            getConfig().save(config_file);
        } catch (IOException var2) {
            logger.log(Level.SEVERE, "Could not save config to " + config_file, var2);
        }
    }

    @Override
    public void saveDefaultConfig()
    {
        if (!config_file.exists())
            saveResource("config.yml", false);
    }

    @Override
    public void saveResource(String s, boolean replace)
    {
        Shulker.get_configs().save_resource(getResource(s), getName(), s, replace);
    }

    @Override
    public final void reloadConfig()
    {
        new_config = YamlConfiguration.loadConfiguration(config_file);
        InputStream defConfigStream = this.getResource("config.yml");
        if (defConfigStream != null)
            new_config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    @Override
    public final PluginLoader getPluginLoader()
    {
        return loader;
    }

    @Override
    public final Server getServer()
    {
        return Bukkit.getServer();
    }

    @Override
    public final boolean isEnabled()
    {
        return is_enabled;
    }

    protected final void set_enabled(boolean enabled)
    {
        if (is_enabled != enabled) {
            is_enabled = enabled;
            if (is_enabled)
                onEnable();
            else
                onDisable();

        }
    }

    @Override
    public void onDisable()
    {
        try {
            script_engine.invokeFunction("on_disable");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException ignored) {
        }
    }

    @Override
    public void onLoad()
    {
        try {
            script_engine.invokeFunction("on_load");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException ignored) {
        }
    }

    @Override
    public void onEnable()
    {
        try {
            script_engine.invokeFunction("on_enable");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException ignored) {
        }
    }

    public CommandBuilder<CommandSender> new_command(String name)
    {
        return new CommandBuilder<>(new ResourceName(getName(), name));
    }

    public void register_command(org.aperlambda.kimiko.Command<CommandSender> command)
    {
        Shulker.get_commands().register(command);
    }

    public void require_access(String name, String className) throws ClassNotFoundException, ScriptException
    {
        JSUtils.load_class(script_engine, name, Class.forName(className));
    }

    @Override
    public boolean isNaggable()
    {
        return false;
    }

    @Override
    public void setNaggable(boolean b)
    {

    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String s, String s1)
    {
        return null;
    }

    @Override
    public final Logger getLogger()
    {
        return logger;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        return null;
    }

    protected NashornScriptEngine get_script_engine()
    {
        return script_engine;
    }

    final void init(JSPluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file, NashornScriptEngine engine)
    {
        this.loader = loader;
        this.file = file;
        this.description = description;
        this.data_folder = dataFolder;
        this.config_file = new File(dataFolder, "config.yml");
        this.logger = new PluginLogger(this);
        events_manager = new JSEventsManager(this);
        this.script_engine = engine;
        script_engine.put("plugin", this);
        script_engine.put("logger", logger);
        script_engine.put("events", events_manager);
    }
}
