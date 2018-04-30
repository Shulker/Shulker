/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.wrappers;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.shulker.core.Shulker;
import org.shulker.core.wrappers.ChatComponentWrapper;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.getMethod;
import static org.aperlambda.lambdacommon.utils.LambdaReflection.invokeMethod;

public class ReflectedChatComponentWrapper extends ChatComponentWrapper
{
	public static final ReflectedChatComponentWrapper INSTANCE = new ReflectedChatComponentWrapper();

	private static final Class<?>         WRAPPER_CLASS;
	private static final Optional<Method> FROM_SHULKER_METHOD;
	private static final Optional<Method> TO_SHULKER_METHOD;

	static
	{
		WRAPPER_CLASS = ShulkerSpigotPlugin.getNmsClass("IChatBaseComponent");
		Objects.requireNonNull(WRAPPER_CLASS, "Cannot initialize ReflectedChatComponentWrapper: NMS class cannot be found!");
		final Class<?> SERIALIZER_CLASS = ShulkerSpigotPlugin.getNmsClass("IChatBaseComponent$ChatSerializer");
		Objects.requireNonNull(SERIALIZER_CLASS, "Cannot initialize ReflectedChatComponentWrapper: NMS serializer class cannot be found!");
		FROM_SHULKER_METHOD = getMethod(SERIALIZER_CLASS, "a", String.class);
		TO_SHULKER_METHOD = getMethod(SERIALIZER_CLASS, "a", WRAPPER_CLASS);
	}

	@Override
	public Object fromShulker(BaseComponent... shulkerObject)
	{
		if (shulkerObject == null)
			return null;
		var start = System.currentTimeMillis();
		var temp = FROM_SHULKER_METHOD.map(method -> invokeMethod(null, method, ComponentSerializer.toString(shulkerObject)))
				.orElse(null);
		Shulker.logDebug("ChatComponentWrapper#fromShulker in " + (System.currentTimeMillis() - start) + "ms");
		return temp;
	}

	@Override
	public BaseComponent[] toShulker(Object object)
	{
		var start = System.currentTimeMillis();
		if (!WRAPPER_CLASS.isInstance(object))
			return null;
		var temp = ComponentSerializer.parse(TO_SHULKER_METHOD.map(method -> (String) invokeMethod(null, method, object))
													 .orElse("{}"));
		Shulker.logDebug("ChatComponentWrapper#toShulker in " + (System.currentTimeMillis() - start) + "ms");
		return temp;
		//return ComponentSerializer.parse(TO_SHULKER_METHOD.map(method -> (String) invokeMethod(null, method, object))
		//										 .getOrElse("{}"));
	}

	@Override
	public Class<?> getObjectClass()
	{
		return IChatBaseComponent.class;
	}
}