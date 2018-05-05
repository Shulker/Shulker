/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.packets;

import org.aperlambda.lambdacommon.utils.LambdaReflection;
import org.mcelytra.core.ServerPing;
import org.shulker.core.Shulker;
import org.shulker.core.impl.reflect.wrappers.ReflectServerPingWrapper;
import org.shulker.core.packets.mc.status.ShulkerPacketStatusOutServerInfo;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class ReflectPacketStatusOutServerInfo extends ShulkerPacketStatusOutServerInfo<Object>
{
	private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
	private static final Optional<Field>                    SERVERPING_FIELD;

	static
	{
		final Class<?> PACKET_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.getNmsClass("PacketStatusOutServerInfo"), "Cannot initialize ReflectPacketStatusOutServerInfo: NMS class cannot be found!");
		CONSTRUCTOR = getConstructor(PACKET_CLASS);
		SERVERPING_FIELD = getFirstFieldOfType(PACKET_CLASS, getMCManager().getWrapperManager().getServerPingWrapper().getObjectClass());
	}

	public ReflectPacketStatusOutServerInfo()
	{
		this(CONSTRUCTOR.map(constructor -> newInstance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketStatusOutServerInfo: constructor cannot be found!")));
	}

	public ReflectPacketStatusOutServerInfo(ServerPing serverPing)
	{
		this();
		setServerPing(serverPing);
	}

	public ReflectPacketStatusOutServerInfo(Object packet)
	{
		super(packet);
	}

	@Override
	public ServerPing getServerPing()
	{
		return SERVERPING_FIELD.map(field -> getMCManager().getWrapperManager().getServerPingWrapper().toShulker(getFieldValue(packet, field))).orElse(null);
	}

	@Override
	public void setServerPing(ServerPing serverPing)
	{
		SERVERPING_FIELD.ifPresent(field -> setValue(packet, field, getMCManager().getWrapperManager().getServerPingWrapper().fromShulker(serverPing)));
	}
}