/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.js;

import com.google.common.base.Charsets;
import org.aperlambda.lambdacommon.config.FileConfig;
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

	}

	@Override
	public void onLoad()
	{

	}

	@Override
	public void onEnable()
	{

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

	final void init(JSPluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file, ClassLoader classLoader)
	{
		this.loader = loader;
		this.file = file;
		this.description = description;
		this.dataFolder = dataFolder;
		this.configFile = new File(dataFolder, "config.yml");
		this.logger = new PluginLogger(this);
	}
}