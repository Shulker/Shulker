/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.entity;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.mcelytra.chat.ChatVisibility;
import org.shulker.core.entity.ShulkerPlayer;

public class ReflectShulkerPlayer implements ShulkerPlayer<Player>
{
    @NotNull
    private Player player;

    private Object mc_player;

    public ReflectShulkerPlayer(@NotNull Player player)
    {
        this.player = player;
    }

    @Override
    public void send_message(ChatMessageType type, BaseComponent... message)
    {
        var chat_visibility = get_chat_visibility();
        if ((chat_visibility == ChatVisibility.HIDDEN && type != ChatMessageType.ACTION_BAR) || (chat_visibility == ChatVisibility.SYSTEM && type == ChatMessageType.CHAT))
            return;
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.valueOf(type.name()), message);
    }

    @Override
    public void send_raw_packet(Object raw_packet)
    {

    }

    @Override
    public int get_ping()
    {
        return -1;
    }

    @Override
    public String get_locale()
    {
        return player.getLocale();
    }

    @Override
    public ChatVisibility get_chat_visibility()
    {
        return ChatVisibility.FULL;
    }

    @NotNull
    @Override
    public Player get_player_handle()
    {
        return player;
    }
}
