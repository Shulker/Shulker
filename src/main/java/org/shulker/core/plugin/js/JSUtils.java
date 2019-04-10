/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
    public static void load_class(NashornScriptEngine currentEngine, String name, Class<?> clazz)
    {
        currentEngine.put(name, clazz);
    }

    public static void load_class_static(NashornScriptEngine currentEngine, String name, Class<?> clazz) throws ScriptException
    {
        load_class(currentEngine, name, clazz);
        currentEngine.put(name, currentEngine.eval(name + ".static"));
    }
}
