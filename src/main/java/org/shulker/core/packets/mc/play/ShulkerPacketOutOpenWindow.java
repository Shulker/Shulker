/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
    public abstract String get_window_type();

    /**
     * Sets the type of the window.
     *
     * @param window_type The window type id.
     * @see #set_window_type(InventoryType)
     */
    public abstract void set_window_type(String window_type);

    /**
     * Sets the type of the window.
     *
     * @param inventory_type The window type.
     * @see #set_window_type(String)
     */
    public void set_window_type(InventoryType inventory_type)
    {
        set_window_type(inventory_type.toString());
    }

    /**
     * Gets the title of the window.
     *
     * @return The title of the window.
     */
    public abstract BaseComponent[] get_title();

    /**
     * Sets the title of the window.
     *
     * @param title The title of the window.
     */
    public abstract void set_title(BaseComponent... title);

    /**
     * Gets the number of slots in the window (excluding the number of slots in the player inventory).
     * <p>Always 0 for non-storage windows (e.g. Workbench, Anvil).</p>
     *
     * @return The number of slots in the window.
     */
    public abstract int get_slots();

    /**
     * Sets the number of slots in the window (excluding the number of slots in the player inventory).
     * <p>Always 0 for non-storage windows (e.g. Workbench, Anvil).</p>
     *
     * @param slots The number of slots in the window.
     */
    public abstract void set_slots(int slots);

    /**
     * Gets the EntityHorse's entity id.
     * <p>Only sent when Window Type is “EntityHorse”.</p>
     *
     * @return The entity id.
     */
    public abstract int get_entity_id();

    /**
     * Sets the EntityHorse's entity id.
     * <p>Only sent when Window Type is “EntityHorse”.</p>
     *
     * @param entityId The entity id.
     */
    public abstract void set_entity_id(int entityId);

    @Override
    public void reset()
    {
        set_window_id(0);
        set_window_type("minecraft:container");
        set_title((BaseComponent[]) null);
        set_slots(0);
        set_entity_id(0);
    }

    @Override
    public String toString()
    {
        return "ShulkerPacketOutOpenWindow{id:" + get_window_id() + ",type:\"" + get_window_type() + "\",title:" +
                Arrays.toString(get_title()) + ",slots:" + get_slots() + ",entityId:" + get_entity_id() + "}";
    }
}
