/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.plugin.js;

import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptException;

/**
 * Represents a utility class for Javascript Nashorn Engine.
 */
public class JSUtils
{
	public static void loadClass(NashornScriptEngine currentEngine, String name, Class<?> clazz)
	{
		currentEngine.put(name, clazz);
	}

	public static void loadClassStatic(NashornScriptEngine currentEngine, String name, Class<?> clazz) throws ScriptException
	{
		loadClass(currentEngine, name, clazz);
		currentEngine.put(name, currentEngine.eval(name + ".static"));
	}
}