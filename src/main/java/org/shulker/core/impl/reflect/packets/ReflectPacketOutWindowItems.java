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
import org.shulker.core.packets.mc.play.ShulkerPacketOutWindowItems;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class ReflectPacketOutWindowItems extends ShulkerPacketOutWindowItems<Object>
{
	private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
	private static final Optional<Field>                    WINDOW_ID_FIELD;
	private static final Optional<Field>                    WINDOW_ITEMS_FIELD;

	static
	{
		final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.getNmsClass("PacketPlayOutWindowItems");
		Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketOutWindowItems: NMS class cannot be found!");
		CONSTRUCTOR = getConstructor(PACKET_CLASS);
		WINDOW_ID_FIELD = getFirstFieldOfType(PACKET_CLASS, int.class);
		WINDOW_ITEMS_FIELD = getFirstFieldOfType(PACKET_CLASS, List.class);
	}

	public ReflectPacketOutWindowItems()
	{
		super(CONSTRUCTOR.map(constructor -> newInstance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketOutWindowItems: constructor cannot be found!")));
	}

	public ReflectPacketOutWindowItems(int windowId, List<ItemStack> items)
	{
		this();
		setWindowId(windowId);
		setItems(items);
	}

	public ReflectPacketOutWindowItems(int windowId, ItemStack... items)
	{
		this();
		setWindowId(windowId);
		setItems(items);
	}

	public ReflectPacketOutWindowItems(Object packet)
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
		WINDOW_ID_FIELD.ifPresentOrElse(field -> setValue(packet, field, id),
										() -> {
											throw new RuntimeException("Cannot set window id (Field not found).");
										});
	}

	@Override
	public List<ItemStack> getItems()
	{
		return WINDOW_ITEMS_FIELD.map(field -> getFieldValue(packet, field, Collections.emptyList())).orElse(Collections.emptyList())
				.stream().map(object -> getMCManager().getWrapperManager().getItemStackWrapper().toShulker(object)).collect(Collectors.toList());
	}

	@Override
	public void setItems(List<ItemStack> items)
	{
		WINDOW_ITEMS_FIELD.ifPresentOrElse(field -> setValue(packet, field, items.stream().map(item -> getMCManager().getWrapperManager().getItemStackWrapper().fromShulker(item)).collect(Collectors.toList())),
										   () -> {
											   throw new RuntimeException("Cannot set window items (Field not found).");
										   });
	}
}