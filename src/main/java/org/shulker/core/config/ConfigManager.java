/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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

import static org.aperlambda.lambdacommon.resources.ResourcesManager.get_default_resources_manager;

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
    {
    }

    public static ConfigManager get()
    {
        return CONFIG_MANAGER;
    }

    public @NotNull JsonConfig new_json_config(@NotNull ResourceName name)
    {
        return new_json_config(name, null);
    }

    /**
     * Adds a new Json Configuration.
     *
     * @param name Resource name of the configuration.
     * @param def  InputStream of the default configuration, can be null.
     * @return The new Json Configuration.
     */
    public @NotNull JsonConfig new_json_config(@NotNull ResourceName name, @Nullable InputStream def)
    {
        if (has_config(name)) {
            var existing_config = get_configuration(name);
            if (existing_config instanceof JsonConfig)
                return (JsonConfig) existing_config;
            else
                throw new IllegalArgumentException("A configuration with the name '" + name.toString() + "' already exists in another type than JsonConfig!");
        }

        var file = new File(Shulker.get_configuration_dir(), name.get_domain() + "/" + name.get_name() + ".json");

        if (def != null)
            save_resource(def, name, "json", false);

        var config = new JsonConfig(file);
        configs.put(name, config);
        return config;
    }

    public @NotNull YamlConfig new_yaml_config(@NotNull ResourceName name)
    {
        return new_yaml_config(name, null);
    }

    /**
     * Adds a new Yaml Configuration.
     *
     * @param name Resource name of the configuration.
     * @param def  InputStream of the default configuration, can be null.
     * @return The new Json Configuration.
     */
    public @NotNull YamlConfig new_yaml_config(@NotNull ResourceName name, @Nullable InputStream def)
    {
        if (has_config(name)) {
            var existing_config = get_configuration(name);
            if (existing_config instanceof YamlConfig)
                return (YamlConfig) existing_config;
            else
                throw new IllegalArgumentException("A configuration with the name '" + name.toString() + "' already exists in another type than YamlConfig!");
        }

        var file = new File(Shulker.get_configuration_dir(), name.get_domain() + "/" + name.get_name() + ".yml");

        if (def != null)
            save_resource(def, name, "yml", false);

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
    public boolean has_config(@NotNull ResourceName name)
    {
        return configs.containsKey(name);
    }

    /**
     * Removes the configuration.
     *
     * @param name The resource name of the configuration.
     * @return True if the configuration was removed else false.
     */
    public boolean remove_config(@NotNull ResourceName name)
    {
        if (!has_config(name))
            return false;
        return configs.remove(name) != null;
    }

    public @Nullable Config get_configuration(@NotNull ResourceName name)
    {
        return configs.get(name);
    }

    public boolean save_resource(InputStream resource, ResourceName name, String ext, boolean replace)
    {
        return save_resource(resource, name.get_domain(), name.get_name() + "." + ext, replace);
    }

    public boolean save_resource(InputStream resource, String domain, String name, boolean replace)
    {
        return get_default_resources_manager().save_resource(resource, name, new File(Shulker.get_configuration_dir(), domain), replace);
    }
}