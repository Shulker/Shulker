/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketOutSetSlot<T> extends ShulkerPacket<T> implements Windowable
{
    public ShulkerPacketOutSetSlot(T packet)
    {
        super(packet);
    }

    public abstract int get_slot();

    public abstract void set_slot(int slot);

    public abstract ItemStack get_item();

    public abstract void set_item(ItemStack item);

    @Override
    public void reset()
    {
        set_window_id(0);
        set_slot(0);
        set_item(null);
    }

    @Override
    public String toString()
    {
        return "ShulkerPacketOutSetSlot{id:" + get_window_id() + ",slot:" + get_slot() + ",item:" + get_item() + "}";
    }
}
