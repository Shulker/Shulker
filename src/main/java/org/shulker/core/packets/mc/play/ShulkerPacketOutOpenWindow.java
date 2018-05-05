/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.mc.play;

import net.md_5.bungee.api.chat.BaseComponent;
import org.shulker.core.inventory.InventoryType;
import org.shulker.core.inventory.Windowable;
import org.shulker.core.packets.ShulkerPacket;

import java.util.Arrays;

/**
 * Represents the packet {@code PacketPlayOutOpenWindow}.
 * <p>See: <a href="http://wiki.vg/Protocol#Open_Window">wiki.vg information about this packet.</a></p>
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketOutOpenWindow<T> extends ShulkerPacket<T> implements Windowable
{
	public ShulkerPacketOutOpenWindow(T packet)
	{
		super(packet);
	}

	/**
	 * Gets the window type to use for display.
	 *
	 * @return The window type id.
	 */
	public abstract String getWindowType();

	/**
	 * Sets the type of the window.
	 *
	 * @param windowType The window type id.
	 * @see #setWindowType(InventoryType)
	 */
	public abstract void setWindowType(String windowType);

	/**
	 * Sets the type of the window.
	 *
	 * @param inventoryType The window type.
	 * @see #setWindowType(String)
	 */
	public void setWindowType(InventoryType inventoryType)
	{
		setWindowType(inventoryType.toString());
	}

	/**
	 * Gets the title of the window.
	 *
	 * @return The title of the window.
	 */
	public abstract BaseComponent[] getTitle();

	/**
	 * Sets the title of the window.
	 *
	 * @param title The title of the window.
	 */
	public abstract void setTitle(BaseComponent... title);

	/**
	 * Gets the number of slots in the window (excluding the number of slots in the player inventory).
	 * <p>Always 0 for non-storage windows (e.g. Workbench, Anvil).</p>
	 *
	 * @return The number of slots in the window.
	 */
	public abstract int getSlots();

	/**
	 * Sets the number of slots in the window (excluding the number of slots in the player inventory).
	 * <p>Always 0 for non-storage windows (e.g. Workbench, Anvil).</p>
	 *
	 * @param slots The number of slots in the window.
	 */
	public abstract void setSlots(int slots);

	/**
	 * Gets the EntityHorse's entity id.
	 * <p>Only sent when Window Type is “EntityHorse”.</p>
	 *
	 * @return The entity id.
	 */
	public abstract int getEntityId();

	/**
	 * Sets the EntityHorse's entity id.
	 * <p>Only sent when Window Type is “EntityHorse”.</p>
	 *
	 * @param entityId The entity id.
	 */
	public abstract void setEntityId(int entityId);

	@Override
	public void reset()
	{
		setWindowId(0);
		setWindowType("minecraft:container");
		setTitle((BaseComponent[]) null);
		setSlots(0);
		setEntityId(0);
	}

	@Override
	public String toString()
	{
		return "ShulkerPacketOutOpenWindow{id:" + getWindowId() + ",type:\"" + getWindowType() + "\",title:" +
				Arrays.toString(getTitle()) + ",slots:" + getSlots() + ",entityId:" + getEntityId() + "}";
	}
}