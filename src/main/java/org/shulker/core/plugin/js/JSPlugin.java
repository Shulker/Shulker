/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
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
	private boolean               isEnabled = false;
	private JSPluginLoader        loader    = null;
	private PluginDescriptionFile description;
	private File                  file;
	private File                  dataFolder;
	private PluginLogger          logger;

	private File              configFile;
	private FileConfiguration newConfig;

	private JSEventsManager eventsManager;

	private NashornScriptEngine scriptEngine;

	@Override
	public final File getDataFolder()
	{
		return dataFolder;
	}

	@Override
	public PluginDescriptionFile getDescription()
	{
		return description;
	}

	@Override
	public FileConfiguration getConfig()
	{
		if (newConfig == null)
			reloadConfig();

		return newConfig;
	}

	@Override
	public InputStream getResource(String s)
	{
		try
		{
			return ResourcesManager.getDefaultResourcesManager().getResource(new File(file, s).toURI().toURL());
		}
		catch (MalformedURLException e)
		{
			return null;
		}
	}

	@Override
	public final void saveConfig()
	{
		try
		{
			getConfig().save(configFile);
		}
		catch (IOException var2)
		{
			logger.log(Level.SEVERE, "Could not save config to " + configFile, var2);
		}
	}

	@Override
	public void saveDefaultConfig()
	{
		if (!configFile.exists())
			saveResource("config.yml", false);
	}

	@Override
	public void saveResource(String s, boolean replace)
	{
		Shulker.getConfigManager().saveResource(getResource(s), getName(), s, replace);
	}

	@Override
	public final void reloadConfig()
	{
		newConfig = YamlConfiguration.loadConfiguration(configFile);
		InputStream defConfigStream = this.getResource("config.yml");
		if (defConfigStream != null)
			newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
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
		return isEnabled;
	}

	protected final void setEnabled(boolean enabled)
	{
		if (isEnabled != enabled)
		{
			isEnabled = enabled;
			if (isEnabled)
				onEnable();
			else
				onDisable();

		}
	}

	@Override
	public void onDisable()
	{
		try
		{
			scriptEngine.invokeFunction("onDisable");
		}
		catch (ScriptException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException ignored)
		{
		}
	}

	@Override
	public void onLoad()
	{
		try
		{
			scriptEngine.invokeFunction("onLoad");
		}
		catch (ScriptException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException ignored)
		{
		}
	}

	@Override
	public void onEnable()
	{
		try
		{
			scriptEngine.invokeFunction("onEnable");
		}
		catch (ScriptException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException ignored)
		{
		}
	}

	public CommandBuilder<CommandSender> newCommand(String name)
	{
		return new CommandBuilder<>(new ResourceName(getName(), name));
	}

	public void registerCommand(org.aperlambda.kimiko.Command<CommandSender> command)
	{
		Shulker.getCommandManager().register(command);
	}

	public void requireAccess(String name, String className) throws ClassNotFoundException, ScriptException
	{
		JSUtils.loadClass(scriptEngine, name, Class.forName(className));
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

	protected NashornScriptEngine getScriptEngine()
	{
		return scriptEngine;
	}

	final void init(JSPluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file, NashornScriptEngine engine)
	{
		this.loader = loader;
		this.file = file;
		this.description = description;
		this.dataFolder = dataFolder;
		this.configFile = new File(dataFolder, "config.yml");
		this.logger = new PluginLogger(this);
		eventsManager = new JSEventsManager(this);
		this.scriptEngine = engine;
		scriptEngine.put("plugin", this);
		scriptEngine.put("logger", logger);
		scriptEngine.put("events", eventsManager);
	}
}