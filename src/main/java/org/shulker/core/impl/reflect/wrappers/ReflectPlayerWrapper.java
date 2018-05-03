/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.wrappers;

import org.aperlambda.lambdacommon.utils.LambdaReflection;
import org.bukkit.entity.Player;
import org.shulker.core.wrappers.PlayerWrapper;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.getMethod;
import static org.aperlambda.lambdacommon.utils.LambdaReflection.invokeMethod;

public class ReflectPlayerWrapper implements PlayerWrapper
{
	public static final ReflectPlayerWrapper INSTANCE = new ReflectPlayerWrapper();

	private static final Class<?>         WRAPPER_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.getNmsClass("EntityPlayer"), "Cannot initialize PlayerWrapper without the targeted class.");
	private static final Optional<Method> FROM_SHULKER;
	private static final Optional<Method> TO_SHULKER;

	static
	{
		final Class<?> CRAFTPLAYER_CLASS = Objects.requireNonNull(LambdaReflection.getClass(
				"org.bukkit.craftbukkit." + ShulkerSpigotPlugin.getServerVersion() +
						".entity.CraftPlayer").orElse(null), "Cannot initialize PlayerWrapper without CraftPlayer class.");
		FROM_SHULKER = getMethod(CRAFTPLAYER_CLASS, "getHandle", true);
		TO_SHULKER = getMethod(WRAPPER_CLASS, "getBukkitEntity", true);
	}

	private ReflectPlayerWrapper() {}

	@Override
	public Object fromShulker(Player bukkit)
	{
		return FROM_SHULKER.map(method -> invokeMethod(bukkit, method)).orElse(null);
	}

	@Override
	public Player toShulker(Object nms)
	{
		return TO_SHULKER.map(method -> (Player) invokeMethod(nms, method)).orElse(null);
	}

	@Override
	public Class<?> getObjectClass()
	{
		return WRAPPER_CLASS;
	}
}