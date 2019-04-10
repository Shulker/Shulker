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
    public abstract List<ItemStack> get_items();

    /**
     * Sets the items of the window.
     *
     * @param items The window items.
     */
    public abstract void set_items(List<ItemStack> items);

    /**
     * Sets the items of the window.
     *
     * @param items The window items.
     * @see #set_items(List)
     */
    public void set_items(ItemStack... items)
    {
        set_items(Arrays.asList(items));
    }

    @Override
    public void reset()
    {
        set_window_id(0);
        set_items(new ArrayList<>());
    }

    @Override
    public String toString()
    {
        return "ShulkerPacketOutWindowItems{id:" + get_window_id() + ",items:" + get_items() + "}";
    }
}
