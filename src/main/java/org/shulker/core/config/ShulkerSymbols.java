/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.config;

import org.aperlambda.lambdacommon.config.JsonConfig;
import org.aperlambda.lambdacommon.resources.ResourcesManager;
import org.shulker.core.Shulker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.shulker.core.ShulkerConstants.RES_SYMBOLS;
import static org.shulker.core.config.ConfigManager.getConfigManager;

public class ShulkerSymbols
{
	private JsonConfig              config;
	private HashMap<String, String> symbols = new HashMap<>();

	/**
	 * Loads or reloads the configuration file.
	 */
	@SuppressWarnings("unchecked")
	public void load()
	{
		if (config == null)
			config = getConfigManager().newJsonConfig(RES_SYMBOLS, ResourcesManager.getDefaultResourcesManager().getResourceFromJar("symbols.json"));

		Shulker.logInfo(Shulker.getPrefix(), "Loading symbols...");
		config.load();
		symbols = config.get("symbols", new HashMap<>(), HashMap.class);
	}

	/**
	 * Gets all the symbols stored in the configuration.
	 *
	 * @return An HashMap.
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getSymbols()
	{
		return symbols;
	}

	public String replaceWithSymbols(String input)
	{
		return replaceWithSymbols(input, null);
	}

	public String replaceWithSymbols(String input, List<String> ignored)
	{
		for (Map.Entry<String, String> entry : symbols.entrySet())
		{
			if (ignored != null)
			{
				if (!ignored.contains(entry.getKey()))
					input = input.replace(entry.getKey(), entry.getValue());
			}
			else
				input = input.replace(entry.getKey(), entry.getValue());
		}
		return input;
	}
}