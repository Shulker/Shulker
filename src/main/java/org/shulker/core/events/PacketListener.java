/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.events;

/**
 * Represents a listener of packets.
 */
public interface PacketListener
{
    /**
     * Fired when a packet is received by the server.
     *
     * @param event The event.
     */
    void on_packet_receive(PacketEvent event);

    /**
     * Fired when a packet is sent by the server.
     *
     * @param event The event.
     */
    void on_packet_send(PacketEvent event);
}
