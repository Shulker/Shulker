/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.config;

import org.aperlambda.lambdacommon.config.FileConfig;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.shulker.core.Shulker;

import java.io.File;
import java.io.IOException;

public class YamlConfig extends FileConfig<YamlConfiguration>
{
	private YamlConfiguration config = new YamlConfiguration();

	public YamlConfig(File file)
	{
		super(file);
	}

	@Override
	public void load()
	{
		if (config == null)
			config = new YamlConfiguration();
		try
		{
			config.load(file);
		}
		catch (IOException | InvalidConfigurationException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void save()
	{
		try
		{
			config.save(file);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T get(String key, T def, Class<T> type)
	{
		return at(key, def, type);
	}

	@Override
	public void set(String key, Object value)
	{
		config.set(key, value);
		if (autoSave)
			save();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T at(String path, T def, Class<T> type)
	{
		return (T) config.get(path, def);
	}

	public YamlConfiguration getConfig()
	{
		return config;
	}
}