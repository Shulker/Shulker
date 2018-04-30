/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class UnknownPlugin implements Plugin
{
	public static final  UnknownPlugin         INSTANCE            = new UnknownPlugin();
	private static final String                UNKNOWN_NAME        = "Unknown";
	private static final PluginDescriptionFile UNKNOWN_DESCRIPTION = new PluginDescriptionFile(UNKNOWN_NAME, UNKNOWN_NAME, UNKNOWN_NAME);

	@Override
	public File getDataFolder()
	{
		return null;
	}

	@Override
	public PluginDescriptionFile getDescription()
	{
		return UNKNOWN_DESCRIPTION;
	}

	@Override
	public FileConfiguration getConfig()
	{
		return null;
	}

	@Override
	public InputStream getResource(String s)
	{
		return null;
	}

	@Override
	public void saveConfig()
	{

	}

	@Override
	public void saveDefaultConfig()
	{

	}

	@Override
	public void saveResource(String s, boolean b)
	{

	}

	@Override
	public void reloadConfig()
	{

	}

	@Override
	public PluginLoader getPluginLoader()
	{
		return null;
	}

	@Override
	public Server getServer()
	{
		return Bukkit.getServer();
	}

	@Override
	public boolean isEnabled()
	{
		return false;
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
	public Logger getLogger()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return UNKNOWN_NAME;
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
}