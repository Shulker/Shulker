/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.wrappers;

import com.google.gson.Gson;
import org.aperlambda.lambdacommon.LambdaConstants;
import org.mcelytra.core.ServerPing;
import org.shulker.core.wrappers.ServerPingWrapper;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.getFieldValue;
import static org.aperlambda.lambdacommon.utils.LambdaReflection.getFirstFieldOfType;

public class ReflectServerPingWrapper extends ServerPingWrapper
{
	public static final ReflectServerPingWrapper INSTANCE = new ReflectServerPingWrapper();

	private static final Class<?> WRAPPER_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.getNmsClass("ServerPing"), "Cannot initialize ServerPingWrapper without the targeted class.");
	private static final Gson     MC_GSON;

	static
	{
		final Field FIELD_SERVERPING_GSON = getFirstFieldOfType(Objects.requireNonNull(ShulkerSpigotPlugin.getNmsClass("PacketStatusOutServerInfo")), Gson.class).orElseThrow();
		MC_GSON = Objects.requireNonNull(getFieldValue(null, FIELD_SERVERPING_GSON, (Gson) null), "Cannot wrap ServerPing without NMS' Gson");
	}

	@Override
	public Object fromShulker(ServerPing shulkerObject)
	{
		if (shulkerObject == null)
			return null;
		return MC_GSON.fromJson(LambdaConstants.JSON_PARSER.parse(ServerPing.Serializer.toString(shulkerObject)), WRAPPER_CLASS);
	}

	@Override
	public ServerPing toShulker(Object object)
	{
		if (object == null)
			return null;
		return ServerPing.Serializer.parse(MC_GSON.toJson(object));
	}

	@Override
	public Class<?> getObjectClass()
	{
		return WRAPPER_CLASS;
	}
}