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
import org.shulker.core.packets.ShulkerPacket;

import java.util.Arrays;

/**
 * Represents the packet which modifies the header and the footer of the player list.
 * This packet may be used by custom servers to display additional information above/below the player list. It is never sent by the Notchian server.
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketPlayerListHeaderFooter<T> extends ShulkerPacket<T>
{
    public ShulkerPacketPlayerListHeaderFooter(T packet)
    {
        super(packet);
    }

    /**
     * Gets the header of the player list.
     *
     * @return Player list's header.
     */
    public abstract BaseComponent[] get_header();

    /**
     * Sets the header of the player list.
     * To remove the header, send a empty translatable component.
     *
     * @param header Player list's header.
     */
    public abstract void set_header(BaseComponent... header);

    /**
     * Gets the footer of the player list.
     *
     * @return Player list's footer.
     */
    public abstract BaseComponent[] get_footer();

    /**
     * Sets the footer of the player list.
     * To remove the footer, send a empty translatable component.
     *
     * @param footer Player list's footer.
     */
    public abstract void set_footer(BaseComponent... footer);

    @Override
    public void reset()
    {
        set_header();
        set_footer();
    }

    @Override
    public String toString()
    {
        return "ShulkerPacketPlayerListHeaderFooter{header:" +
                Arrays.toString(get_header()) + ",footer:" + Arrays.toString(get_footer()) + "}";
    }
}
