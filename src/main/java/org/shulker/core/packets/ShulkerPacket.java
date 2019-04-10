/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets;

/**
 * Represents a packet.
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacket<T>
{
    protected T packet;

    public ShulkerPacket(T packet)
    {
        this.packet = packet;
    }

    /**
     * Gets the packet instance.
     *
     * @return Instance of the packet.
     */
    public T get_handle()
    {
        return packet;
    }

    /**
     * Gets the packet class name.
     *
     * @return The name of the packet class.
     */
    public String get_handle_name()
    {
        return packet.getClass().getSimpleName();
    }

    /**
     * Resets all the value of the packet.
     */
    public abstract void reset();
}
