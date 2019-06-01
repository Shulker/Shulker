/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R2.entity;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.Packet;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.mcelytra.chat.ChatVisibility;
import org.shulker.core.Shulker;
import org.shulker.core.entity.ShulkerPlayer;

public class ShulkerPlayerV113R2 implements ShulkerPlayer<Player>
{
    @NotNull
    private Player       player;
    @NotNull
    private EntityPlayer mc_player;

    public ShulkerPlayerV113R2(@NotNull Player player)
    {
        this.player = player;
        mc_player = ((CraftPlayer) player).getHandle();
    }

    @Override
    public void send_message(ChatMessageType type, BaseComponent... message)
    {
        var chatVisibility = get_chat_visibility();
        if ((chatVisibility == ChatVisibility.HIDDEN && type != ChatMessageType.ACTION_BAR) || (chatVisibility == ChatVisibility.SYSTEM && type == ChatMessageType.CHAT))
            return;
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.valueOf(type.name()), message);
    }

    @Override
    public void send_raw_packet(Object raw_packet)
    {
        if (raw_packet instanceof Packet)
            mc_player.playerConnection.networkManager.sendPacket((Packet<?>) raw_packet);
    }

    @Override
    public int get_ping()
    {
        return mc_player.ping;
    }

    @Override
    public String get_locale()
    {
        return player.getLocale();
    }

    @Override
    public ChatVisibility get_chat_visibility()
    {
        return Shulker.get_mc().get_wrapper_manager().get_chat_visibility_wrapper().to_shulker(mc_player.getChatFlags());
    }

    @Override
    public @NotNull Player get_player_handle()
    {
        return player;
    }
}
