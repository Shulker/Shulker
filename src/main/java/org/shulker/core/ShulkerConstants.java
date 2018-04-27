/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.aperlambda.lambdacommon.resources.ResourceName;

import javax.script.ScriptEngineManager;
import java.io.File;

import static net.md_5.bungee.api.ChatColor.*;

public class ShulkerConstants
{
	public static final String SHULKER_DOMAIN = "shulker";
	public static final String SHULKER_PREFIX = "[Shulker]";
	public static final String SHULKER_IG_PREFIX = GRAY + "[" + LIGHT_PURPLE + "Shulker" + GRAY + "]" + RESET;

	public static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
	public static final NashornScriptEngine JS_ENGINE             = (NashornScriptEngine) SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn");

	/*
		Resources
	 */
	public static final File BASE_DIR = new File(".").getAbsoluteFile();

	public static final ResourceName RES_CONFIG = new ResourceName(SHULKER_DOMAIN, "config");
	public static final ResourceName RES_SYMBOLS = new ResourceName(SHULKER_DOMAIN, "symbols");
}