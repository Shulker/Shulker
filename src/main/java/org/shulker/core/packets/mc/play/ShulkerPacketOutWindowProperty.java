/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.mc.play;

import org.shulker.core.inventory.Windowable;
import org.shulker.core.packets.ShulkerPacket;

/**
 * Represents the packet {@code PacketPlayOutWindowProperty}. Please see <a href="http://wiki.vg/Protocol#Window_Property">the page of wiki.vg</a> for more information.
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketOutWindowProperty<T> extends ShulkerPacket<T> implements Windowable
{
	public ShulkerPacketOutWindowProperty(T packet)
	{
		super(packet);
	}

	/**
	 * Gets the property of the window.
	 *
	 * @return The property of the window.
	 */
	public abstract short getProperty();

	/**
	 * Sets the property of the window.
	 *
	 * @param property The property of the window.
	 */
	public abstract void setProperty(short property);

	/**
	 * Gets the value of the property.
	 *
	 * @return The value of the property.
	 */
	public abstract short getValue();

	/**
	 * Sets the value of the property.
	 *
	 * @param value The value of the property.
	 */
	public abstract void setValue(short value);

	@Override
	public void reset()
	{
		setWindowId(0);
		setProperty((short) 0);
		setValue((short) 0);
	}

	@Override
	public String toString()
	{
		return "ShulkerPacketOutSetSlot{id:" + getWindowId() + ",property:" + getProperty() + ",value:" + getValue() +
				"}";
	}
}