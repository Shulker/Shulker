/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.packets;

import org.bukkit.inventory.ItemStack;
import org.shulker.core.packets.mc.play.ShulkerPacketOutSetSlot;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class ReflectPacketOutSetSlot extends ShulkerPacketOutSetSlot<Object>
{
	private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
	private static final Optional<Field>                    WINDOW_ID_FIELD;
	private static final Optional<Field>                    WINDOW_SLOT_FIELD;
	private static final Optional<Field>                    WINDOW_ITEM_FIELD;

	static
	{
		final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.getNmsClass("PacketPlayOutSetSlot");
		Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketOutSetSlot: NMS class cannot be found!");
		CONSTRUCTOR = getConstructor(PACKET_CLASS);
		WINDOW_ID_FIELD = getFirstFieldOfType(PACKET_CLASS, int.class);
		WINDOW_SLOT_FIELD = getLastFieldOfType(PACKET_CLASS, int.class);
		WINDOW_ITEM_FIELD = getFirstFieldOfType(PACKET_CLASS, getMCManager().getWrapperManager().getItemStackWrapper().getObjectClass());
	}

	public ReflectPacketOutSetSlot()
	{
		super(CONSTRUCTOR.map(constructor -> newInstance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketOutSetSlot: constructor cannot be found!")));
	}

	public ReflectPacketOutSetSlot(int windowId, int slot, ItemStack item)
	{
		this();
		setWindowId(windowId);
		setSlot(slot);
		setItem(item);
	}

	public ReflectPacketOutSetSlot(Object packet)
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
		WINDOW_ID_FIELD.ifPresentOrElse(field -> setValue(packet, field, id), () -> {
			throw new RuntimeException("Cannot set window id (Field not found).");
		});
	}

	@Override
	public int getSlot()
	{
		return WINDOW_SLOT_FIELD.map(field -> getFieldValue(packet, field, 0)).orElse(0);
	}

	@Override
	public void setSlot(int slot)
	{
		WINDOW_SLOT_FIELD.ifPresentOrElse(field -> setValue(packet, field, slot),
										  () -> {
											  throw new RuntimeException("Cannot set slot (Field not found).");
										  });
	}

	@Override
	public ItemStack getItem()
	{
		return getMCManager().getWrapperManager().getItemStackWrapper().toShulker(WINDOW_ITEM_FIELD.map(field -> getFieldValue(packet, field)).orElse(null));
	}

	@Override
	public void setItem(ItemStack item)
	{
		WINDOW_ITEM_FIELD.ifPresentOrElse(field -> setValue(packet, field, getMCManager().getWrapperManager().getItemStackWrapper().fromShulker(item)),
										  () -> {
											  throw new RuntimeException("Cannot set item (Field not found).");
										  });
	}
}