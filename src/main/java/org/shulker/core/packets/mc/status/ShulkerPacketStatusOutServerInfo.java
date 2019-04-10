/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.mc.status;

import org.mcelytra.core.ServerPing;
import org.shulker.core.packets.ShulkerPacket;

/**
 * Represents the packet which told the client some information about the server in the server list.
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketStatusOutServerInfo<T> extends ShulkerPacket<T>
{
    public ShulkerPacketStatusOutServerInfo(T packet)
    {
        super(packet);
    }

    /**
     * Gets the ping result.
     *
     * @return The server ping result.
     */
    public abstract ServerPing get_server_ping();

    /**
     * Sets the ping result.
     *
     * @param serverPing The server ping result.
     */
    public abstract void set_server_ping(ServerPing serverPing);

    @Override
    public void reset()
    {
        set_server_ping(null);
    }

    @Override
    public String toString()
    {
        return "ShulkerPacketStatusOutServerInfo{server_ping:" + ServerPing.Serializer.to_string(get_server_ping()) + "}";
    }
}
