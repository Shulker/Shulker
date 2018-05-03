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

/**
 * Represents the packet {@code PacketPlayOutSetSlot}.
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketOutSetSlot<T> extends ShulkerPacket<T> implements Windowable
{
	public ShulkerPacketOutSetSlot(T packet)
	{
		super(packet);
	}

	public abstract int getSlot();

	public abstract void setSlot(int slot);

	public abstract ItemStack getItem();

	public abstract void setItem(ItemStack item);

	@Override
	public void reset()
	{
		setWindowId(0);
		setSlot(0);
		setItem(null);
	}

	@Override
	public String toString()
	{
		return "ShulkerPacketOutSetSlot{id:" + getWindowId() + ",slot:" + getSlot() + ",item:" + getItem() + "}";
	}
}