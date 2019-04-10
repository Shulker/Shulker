/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.events;

import org.aperlambda.lambdacommon.utils.Cancellable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.packets.ShulkerPacket;

import java.net.SocketAddress;
import java.util.Optional;

public class PacketEvent implements Cancellable
{
    private SocketAddress    ip;
    private Optional<Player> player;
    private ShulkerPacket<?> packet;
    private boolean          cancelled = false;

    public PacketEvent(SocketAddress ip, ShulkerPacket<?> packet)
    {
        this.ip = ip;
        this.packet = packet;
        this.player = Optional.empty();
    }

    public PacketEvent(SocketAddress ip, Player player, ShulkerPacket<?> packet)
    {
        this(ip, packet);
        this.player = Optional.of(player);
    }

    /**
     * Gets the target name, or the target IP if player is null.
     *
     * @return The target name.
     */
    public String get_target()
    {
        return this.player.map(Player::getName).orElse(ip.toString());
    }

    /**
     * Gets the target IP.
     *
     * @return The IP of the target.
     */
    public SocketAddress get_target_ip()
    {
        return this.ip;
    }

    /**
     * Gets the optional targeted player. Empty if the packet isn't in the PLAY protocol.
     *
     * @return The optional targeted player.
     */
    public @NotNull Optional<Player> get_player_target()
    {
        return this.player;
    }

    /**
     * Gets the packet.
     *
     * @return The packet.
     */
    public @NotNull ShulkerPacket<?> get_packet()
    {
        return this.packet;
    }

    @Override
    public boolean is_cancelled()
    {
        return this.cancelled;
    }

    @Override
    public void set_cancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }
}
