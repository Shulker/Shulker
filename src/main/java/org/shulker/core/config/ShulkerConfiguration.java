/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.config;

import org.aperlambda.lambdacommon.resources.ResourcesManager;
import org.shulker.core.Shulker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.shulker.core.ShulkerConstants.RES_CONFIG;
import static org.shulker.core.config.ConfigManager.get;

public class ShulkerConfiguration
{
    private static final String DEBUG_KEY                      = "debug.base";
    private static final String DEBUG_PACKETS_KEY              = "debug.packets";
    private static final String DEBUG_CONNECTIONS_KEY          = "debug.connection";
    private static final String JAVASCRIPT_SUPPORT_KEY         = "enable_js_support";
    private static final String COMMANDS_ERROR_USAGE_KEY       = "commands.error.usage";
    private static final String COMMANDS_ERROR_USAGE_DEF       = "&4[Usage] &c/${command.usage}";
    private static final String COMMANDS_ERROR_PERMISSION_KEY  = "commands.error.permission";
    private static final String COMMANDS_ERROR_PERMISSION_DEF  = "&4[Permission] &cYou don't have enough permissions!";
    private static final String COMMANDS_ERROR_RUNTIME_KEY     = "commands.error.runtime";
    private static final String COMMANDS_ERROR_RUNTIME_DEF     = "&4[Error] &cAn error occurred!";
    private static final String COMMANDS_ERROR_ONLY_PLAYER_KEY = "commands.error.only_player";
    private static final String COMMANDS_ERROR_ONLY_PLAYER_DEF = "&4[Error] &cYou must be a player to execute this.";

    private YamlConfig config;

    /**
     * Loads or reloads the configuration file.
     */
    @SuppressWarnings("unchecked")
    public void load()
    {
        if (config == null)
            config = get().new_yaml_config(RES_CONFIG, ResourcesManager.get_default_resources_manager().get_resource_from_jar("config.yml"));

        Shulker.log_info(Shulker.get_prefix(), "Loading configuration...");
        config.load();

        if (!config.get("version", "error").equalsIgnoreCase(Shulker.get_version())) {
            try {
                Files.copy(config.get_file().toPath(), new File(config.get_file().getParent(), "config.yml.backup").toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                Shulker.log_error(Shulker.get_prefix(), "Cannot backup config file!");
                e.printStackTrace();
            }

            // Replacing
            get().save_resource(ResourcesManager.get_default_resources_manager().get_resource_from_jar("config.yml"), RES_CONFIG, "yml", true);

            boolean old_enable_debug = has_debug();
            boolean old_debug_packets = does_debug_packets();
            boolean old_debug_conn = does_debug_connections();
            boolean old_js_support = use_js_support();
            var old_cmd_error_usage = get_error_usage_message();
            var old_cmd_error_perm = get_error_permission_message();
            var old_cmd_error_runtime = get_error_runtime_message();
            var old_cmd_error_only_player = get_error_only_player_message();
            config.load();
            config.set(DEBUG_KEY, old_enable_debug);
            config.set(DEBUG_PACKETS_KEY, old_debug_packets);
            config.set(DEBUG_CONNECTIONS_KEY, old_debug_conn);
            config.set(JAVASCRIPT_SUPPORT_KEY, old_js_support);
            config.set(COMMANDS_ERROR_USAGE_KEY, old_cmd_error_usage);
            config.set(COMMANDS_ERROR_PERMISSION_KEY, old_cmd_error_perm);
            config.set(COMMANDS_ERROR_RUNTIME_KEY, old_cmd_error_runtime);
            config.set(COMMANDS_ERROR_ONLY_PLAYER_KEY, old_cmd_error_only_player);
            config.save();
        }
    }

    /**
     * Checks whether Shulker use the JavaScript plugins support mode or not.
     *
     * @return True if Shulker supports JavaScript plugins, else false.
     */
    public boolean use_js_support()
    {
        return config.at(JAVASCRIPT_SUPPORT_KEY, false, boolean.class);
    }

    public boolean has_debug()
    {
        return config.at(DEBUG_KEY, false, boolean.class);
    }

    public boolean does_debug_packets()
    {
        return config.at(DEBUG_PACKETS_KEY, false, boolean.class);
    }

    public boolean does_debug_connections()
    {
        return config.at(DEBUG_CONNECTIONS_KEY, true, boolean.class);
    }

    /**
     * Gets the format of the usage message.
     *
     * @return The format of the usage message.
     */
    public String get_error_usage_message()
    {
        return config.at(COMMANDS_ERROR_USAGE_KEY, COMMANDS_ERROR_USAGE_DEF);
    }

    public String get_error_permission_message()
    {
        return config.at(COMMANDS_ERROR_PERMISSION_KEY, COMMANDS_ERROR_PERMISSION_DEF);
    }

    public String get_error_runtime_message()
    {
        return config.at(COMMANDS_ERROR_RUNTIME_KEY, COMMANDS_ERROR_RUNTIME_DEF);
    }

    public String get_error_only_player_message()
    {
        return config.at(COMMANDS_ERROR_ONLY_PLAYER_KEY, COMMANDS_ERROR_ONLY_PLAYER_DEF);
    }
}