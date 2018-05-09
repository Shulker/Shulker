/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.config;

import org.aperlambda.lambdacommon.config.Config;
import org.aperlambda.lambdacommon.config.json.JsonConfig;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import static org.aperlambda.lambdacommon.resources.ResourcesManager.getDefaultResourcesManager;

/**
 * Represents the manager of the configurations.
 * Supported configuration types:
 * <ul>
 * <li>YAML</li>
 * <li>JSON</li>
 * </ul>
 */
public class ConfigManager
{
	private static final ConfigManager                 CONFIG_MANAGER = new ConfigManager();
	private final        HashMap<ResourceName, Config> configs        = new HashMap<>();

	private ConfigManager()
	{}

	public static ConfigManager getConfigManager()
	{
		return CONFIG_MANAGER;
	}

	public @NotNull JsonConfig newJsonConfig(@NotNull ResourceName name)
	{
		return newJsonConfig(name, null);
	}

	/**
	 * Adds a new Json Configuration.
	 *
	 * @param name Resource name of the configuration.
	 * @param def  InputStream of the default configuration, can be null.
	 * @return The new Json Configuration.
	 */
	public @NotNull JsonConfig newJsonConfig(@NotNull ResourceName name, @Nullable InputStream def)
	{
		if (hasConfig(name))
		{
			var existingConfig = getConfiguration(name);
			if (existingConfig instanceof JsonConfig)
				return (JsonConfig) existingConfig;
			else
				throw new IllegalArgumentException("A configuration with the name '" + name.toString() +
														   "' already exists in another type than JsonConfig!");
		}

		var file = new File(Shulker.getConfigurationDirectory(), name.getDomain() + "/" + name.getName() + ".json");

		if (def != null)
			saveResource(def, name, "json", false);

		var config = new JsonConfig(file);
		configs.put(name, config);
		return config;
	}

	public @NotNull YamlConfig newYamlConfig(@NotNull ResourceName name)
	{
		return newYamlConfig(name, null);
	}

	/**
	 * Adds a new Yaml Configuration.
	 *
	 * @param name Resource name of the configuration.
	 * @param def  InputStream of the default configuration, can be null.
	 * @return The new Json Configuration.
	 */
	public @NotNull YamlConfig newYamlConfig(@NotNull ResourceName name, @Nullable InputStream def)
	{
		if (hasConfig(name))
		{
			var existingConfig = getConfiguration(name);
			if (existingConfig instanceof YamlConfig)
				return (YamlConfig) existingConfig;
			else
				throw new IllegalArgumentException("A configuration with the name '" + name.toString() +
														   "' already exists in another type than YamlConfig!");
		}

		var file = new File(Shulker.getConfigurationDirectory(), name.getDomain() + "/" + name.getName() + ".yml");

		if (def != null)
			saveResource(def, name, "yml", false);

		var config = new YamlConfig(file);
		configs.put(name, config);
		return config;
	}

	/**
	 * Checks whether the configuration exists or not.
	 *
	 * @param name The resource name of the configuration.
	 * @return True if the configuration exists else false.
	 */
	public boolean hasConfig(@NotNull ResourceName name)
	{
		return configs.containsKey(name);
	}

	/**
	 * Removes the configuration.
	 *
	 * @param name The resource name of the configuration.
	 * @return True if the configuration was removed else false.
	 */
	public boolean removeConfig(@NotNull ResourceName name)
	{
		if (!hasConfig(name))
			return false;
		return configs.remove(name) != null;
	}

	public @Nullable Config getConfiguration(@NotNull ResourceName name)
	{
		return configs.get(name);
	}

	public boolean saveResource(InputStream resource, ResourceName name, String ext, boolean replace)
	{
		return saveResource(resource, name.getDomain(), name.getName() + "." + ext, replace);
	}

	public boolean saveResource(InputStream resource, String domain, String name, boolean replace)
	{
		return getDefaultResourcesManager().saveResource(resource, name, new File(Shulker.getConfigurationDirectory(), domain), replace);
	}
}