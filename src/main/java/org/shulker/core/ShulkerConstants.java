/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core;

import org.aperlambda.lambdacommon.resources.ResourceName;

import java.io.File;

import static net.md_5.bungee.api.ChatColor.*;

public class ShulkerConstants
{
	public static final String SHULKER_DOMAIN = "shulker";
	public static final String SHULKER_PREFIX = "[Shulker]";
	public static final String SHULKER_IG_PREFIX = GRAY + "[" + LIGHT_PURPLE + "Shulker" + GRAY + "]" + RESET;

	/*
		Resources
	 */
	public static final File BASE_DIR = new File(".").getAbsoluteFile();

	public static final ResourceName RES_CONFIG = new ResourceName(SHULKER_DOMAIN, "config");
	public static final ResourceName RES_SYMBOLS = new ResourceName(SHULKER_DOMAIN, "symbols");
}