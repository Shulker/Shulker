/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.config;

import org.aperlambda.lambdacommon.config.json.JsonConfig;
import org.aperlambda.lambdacommon.resources.ResourcesManager;
import org.shulker.core.Shulker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.shulker.core.ShulkerConstants.RES_SYMBOLS;
import static org.shulker.core.config.ConfigManager.get;

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
        if (this.config == null)
            this.config = get().new_json_config(RES_SYMBOLS, ResourcesManager.get_default_resources_manager().get_resource_from_jar("symbols.json"));

        Shulker.log_info(Shulker.get_prefix(), "Loading symbols...");
        this.config.load();
        this.symbols = this.config.get("symbols", new HashMap<>(), HashMap.class);
    }

    /**
     * Gets all the symbols stored in the configuration.
     *
     * @return An HashMap.
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> get_symbols()
    {
        return this.symbols;
    }

    public String replace_with_symbols(String input)
    {
        return this.replace_with_symbols(input, null);
    }

    public String replace_with_symbols(String input, List<String> ignored)
    {
        for (Map.Entry<String, String> entry : this.symbols.entrySet()) {
            if (ignored != null) {
                if (!ignored.contains(entry.getKey()))
                    input = input.replace(entry.getKey(), entry.getValue());
            } else
                input = input.replace(entry.getKey(), entry.getValue());
        }
        return input;
    }
}
