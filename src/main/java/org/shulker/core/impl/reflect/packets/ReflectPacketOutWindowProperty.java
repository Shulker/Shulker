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
import org.bukkit.inventory.ItemStack;
import org.shulker.core.packets.mc.play.ShulkerPacketOutWindowProperty;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class ReflectPacketOutWindowProperty extends ShulkerPacketOutWindowProperty<Object>
{
	private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
	private static final Optional<Field>                    WINDOW_ID_FIELD;
	private static final Optional<Field>                    WINDOW_PROPERTY_FIELD;
	private static final Optional<Field>                    WINDOW_VALUE_FIELD;

	static
	{
		final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.getNmsClass("PacketPlayOutWindowData");
		Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketOutWindowProperty: NMS class cannot be found!");
		CONSTRUCTOR = getConstructor(PACKET_CLASS);
		WINDOW_ID_FIELD = getFirstFieldOfType(PACKET_CLASS, int.class);
		WINDOW_PROPERTY_FIELD = getField(PACKET_CLASS, "b", true);
		WINDOW_VALUE_FIELD = getLastFieldOfType(PACKET_CLASS, int.class);
	}

	public ReflectPacketOutWindowProperty()
	{
		super(CONSTRUCTOR.map(constructor -> newInstance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketOutWindowProperty: constructor cannot be found!")));
	}

	public ReflectPacketOutWindowProperty(int windowId, short property, short value)
	{
		this();
		setWindowId(windowId);
		setProperty(property);
		setValue(value);
	}

	public ReflectPacketOutWindowProperty(Object packet)
	{
		super(packet);
	}

	@Override
	public int getWindowId()
	{
		return WINDOW_ID_FIELD.map(field -> getFieldValue(packet, field, 0)).orElse(0);
	}

	@Override
	public void setWindowId(int id)
	{
		WINDOW_ID_FIELD.ifPresentOrElse(field -> LambdaReflection.setValue(packet, field, id), () -> {
			throw new RuntimeException("Cannot set window id (Field not found).");
		});
	}

	@Override
	public short getProperty()
	{
		return WINDOW_PROPERTY_FIELD.map(field -> getFieldValue(packet, field, (short) 0)).orElse((short) 0);
	}

	@Override
	public void setProperty(short property)
	{
		WINDOW_PROPERTY_FIELD.ifPresentOrElse(field -> LambdaReflection.setValue(packet, field, property),
											  () -> {
												  throw new RuntimeException("Cannot set property (Field not found).");
											  });
	}

	@Override
	public short getValue()
	{
		return WINDOW_VALUE_FIELD.map(field -> getFieldValue(packet, field, (short) 0)).orElse((short) 0);
	}

	@Override
	public void setValue(short value)
	{
		WINDOW_VALUE_FIELD.ifPresentOrElse(field -> LambdaReflection.setValue(packet, field, value),
										   () -> {
											   throw new RuntimeException("Cannot set value (Field not found).");
										   });
	}
}