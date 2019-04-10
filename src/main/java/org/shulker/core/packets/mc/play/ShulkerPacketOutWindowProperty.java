/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
    public abstract short get_property();

    /**
     * Sets the property of the window.
     *
     * @param property The property of the window.
     */
    public abstract void set_property(short property);

    /**
     * Gets the value of the property.
     *
     * @return The value of the property.
     */
    public abstract short get_value();

    /**
     * Sets the value of the property.
     *
     * @param value The value of the property.
     */
    public abstract void set_value(short value);

    @Override
    public void reset()
    {
        set_window_id(0);
        set_property((short) 0);
        set_value((short) 0);
    }

    @Override
    public String toString()
    {
        return "ShulkerPacketOutSetSlot{id:" + get_window_id() + ",property:" + get_property() + ",value:" + get_value() +
                "}";
    }
}
