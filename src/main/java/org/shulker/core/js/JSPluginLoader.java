/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.js;

import org.apache.commons.lang.Validate;
import org.aperlambda.lambdacommon.resources.ResourcesManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;
import org.shulker.core.Shulker;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class JSPluginLoader implements PluginLoader
{
	private ResourcesManager rscManager = ResourcesManager.getDefaultResourcesManager();

	@Override
	public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException
	{
		Validate.notNull(file, "File cannot be null");
		if (!file.exists())
			throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));

		PluginDescriptionFile description;
		try
		{
			description = this.getPluginDescription(file);
		}
		catch (InvalidDescriptionException e)
		{
			throw new InvalidPluginException(e);
		}

		var configDir = Shulker.getConfigurationDirectory();
		var dataFolder = new File(configDir, description.getName());

		if (dataFolder.exists() && !dataFolder.isDirectory())
			throw new InvalidPluginException(String.format("Projected datafolder: `%s' for %s (%s) exists and is not a directory", dataFolder, description.getFullName(), file));
		else
		{
			for (var pluginName : description.getDepend())
			{
				Plugin current = Bukkit.getPluginManager().getPlugin(pluginName);
				if (current == null)
					throw new UnknownDependencyException(pluginName);
			}

			JSPlugin plugin;

		}
		return null;
	}

	@Override
	public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException
	{
		Validate.notNull(file, "File cannot be null");
		InputStream descrStream = null;
		PluginDescriptionFile pdf = null;
		if (file.isDirectory())
		{
			var pluginYamlFile = new File(file, "plugin.yml");
			if (!pluginYamlFile.exists())
				return null;

			try
			{
				descrStream = rscManager.getResource(pluginYamlFile.toURI().toURL());
				pdf = new PluginDescriptionFile(descrStream);
			}
			catch (IOException | YAMLException e)
			{
				throw new InvalidDescriptionException(e);
			}
			finally
			{
				if (descrStream != null)
				{
					try
					{
						descrStream.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		return pdf;
	}

	@Override
	public Pattern[] getPluginFileFilters()
	{
		return new Pattern[0];
	}

	@Override
	public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin)
	{
		return null;
	}

	@Override
	public void enablePlugin(Plugin plugin)
	{

	}

	@Override
	public void disablePlugin(Plugin plugin)
	{

	}
}