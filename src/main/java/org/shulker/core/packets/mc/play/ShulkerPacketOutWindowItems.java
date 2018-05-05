/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.mc.play;

import org.bukkit.inventory.ItemStack;
import org.shulker.core.inventory.Windowable;
import org.shulker.core.packets.ShulkerPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the packet {@code PacketPlayOutWindowItems}. Sent by the server when items in multiple slots (in a window) are added/removed.
 * This includes the main inventory, equipped armour and crafting slots.
 * <p>See: <a href="http://wiki.vg/Protocol#Window_Items">wiki.vg information about this packet.</a></p>
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketOutWindowItems<T> extends ShulkerPacket<T> implements Windowable
{
	public ShulkerPacketOutWindowItems(T packet)
	{
		super(packet);
	}

	/**
	 * Gets the items of the window.
	 *
	 * @return The window items.
	 */
	public abstract List<ItemStack> getItems();

	/**
	 * Sets the items of the window.
	 *
	 * @param items The window items.
	 */
	public abstract void setItems(List<ItemStack> items);

	/**
	 * Sets the items of the window.
	 *
	 * @param items The window items.
	 * @see #setItems(List)
	 */
	public void setItems(ItemStack... items)
	{
		setItems(Arrays.asList(items));
	}

	@Override
	public void reset()
	{
		setWindowId(0);
		setItems(new ArrayList<>());
	}

	@Override
	public String toString()
	{
		return "ShulkerPacketOutWindowItems{id:" + getWindowId() + ",items:" + getItems() + "}";
	}
}