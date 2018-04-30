/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
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
import static org.shulker.core.config.ConfigManager.getConfigManager;

public class ShulkerConfiguration
{
	private static final String DEBUG_KEY = "enable_debug";
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
			config = getConfigManager().newYamlConfig(RES_CONFIG, ResourcesManager.getDefaultResourcesManager().getResourceFromJar("config.yml"));

		Shulker.logInfo(Shulker.getPrefix(), "Loading configuration...");
		config.load();

		if (!config.get("version", "error").equalsIgnoreCase(Shulker.getVersion()))
		{
			try
			{
				Files.copy(config.getFile().toPath(), new File(config.getFile().getParent(), "config.yml.backup").toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException e)
			{
				Shulker.logError(Shulker.getPrefix(), "Cannot backup config file!");
				e.printStackTrace();
			}

			// Replacing
			getConfigManager().saveResource(ResourcesManager.getDefaultResourcesManager().getResourceFromJar("config.yml"), RES_CONFIG, "yml", true);

			boolean oldEnableDebug = hasDebug();
			var oldCommandsErrorUsage = config.at(COMMANDS_ERROR_USAGE_KEY, COMMANDS_ERROR_USAGE_DEF);
			var oldCommandsErrorPermission = config.at(COMMANDS_ERROR_PERMISSION_KEY, COMMANDS_ERROR_PERMISSION_DEF);
			var oldCommandsErrorRuntime = config.at(COMMANDS_ERROR_RUNTIME_KEY, COMMANDS_ERROR_RUNTIME_DEF);
			var oldCommandsErrorOnlyPlayer = config.at(COMMANDS_ERROR_ONLY_PLAYER_KEY, COMMANDS_ERROR_ONLY_PLAYER_DEF);
			config.load();
			config.set(DEBUG_KEY, oldEnableDebug);
			config.set(COMMANDS_ERROR_USAGE_KEY, oldCommandsErrorUsage);
			config.set(COMMANDS_ERROR_PERMISSION_KEY, oldCommandsErrorPermission);
			config.set(COMMANDS_ERROR_RUNTIME_KEY, oldCommandsErrorRuntime);
			config.set(COMMANDS_ERROR_ONLY_PLAYER_KEY, oldCommandsErrorOnlyPlayer);
			config.save();
		}
	}

	// @TODO Support Javascript plugins
	public boolean useJavascriptSupport()
	{
		return false;
	}

	public boolean hasDebug()
	{
		return config.at(DEBUG_KEY, false, boolean.class);
	}

	/**
	 * Gets the format of the usage message.
	 *
	 * @return The format of the usage message.
	 */
	public String getErrorUsageMessage()
	{
		return config.at(COMMANDS_ERROR_USAGE_KEY, COMMANDS_ERROR_USAGE_DEF);
	}

	public String getErrorPermissionMessage()
	{
		return config.at(COMMANDS_ERROR_PERMISSION_KEY, COMMANDS_ERROR_PERMISSION_DEF);
	}

	public String getErrorRuntimeMessage()
	{
		return config.at(COMMANDS_ERROR_RUNTIME_KEY, COMMANDS_ERROR_RUNTIME_DEF);
	}

	public String getErrorOnlyPlayerMessage()
	{
		return config.at(COMMANDS_ERROR_ONLY_PLAYER_KEY, COMMANDS_ERROR_ONLY_PLAYER_DEF);
	}
}