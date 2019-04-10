/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R1;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.impl.reflect.ReflectMinecraftManager;
import org.shulker.core.impl.reflect.entity.ReflectShulkerPlayer;
import org.shulker.core.impl.reflect.wrappers.ReflectChatComponentWrapper;
import org.shulker.core.impl.reflect.wrappers.ReflectPlayerWrapper;
import org.shulker.core.impl.reflect.wrappers.ReflectServerPingWrapper;
import org.shulker.core.wrappers.*;

public class MinecraftManagerV113R1 extends ReflectMinecraftManager
{
    private static final WrapperManagerV113R1 WRAPPER_MANAGER = new WrapperManagerV113R1();

    @Override
    public void init()
    {
        if (init)
            throw new IllegalStateException("MinecraftManager is already initialized!");
        // Status
        decoders.put("PacketStatusOutServerInfo", this::new_packet_status_out_server_info);
        // Play
        decoders.put("PacketPlayOutChat", this::new_packet_play_out_chat);
        decoders.put("PacketPlayOutPlayerListHeaderFooter", this::new_packet_play_out_playerlist_header_footer);
        decoders.put("PacketPlayOutOpenWindow", this::new_packet_play_out_open_window);
        decoders.put("PacketPlayOutTitle", this::new_packet_title);
        decoders.put("PacketPlayOutWindowData", this::new_packet_play_out_window_property);
        decoders.put("PacketPlayOutWindowItems", this::new_packet_play_out_window_items);
        init = true;
    }

    @Override
    public ShulkerPlayer<Player> get_player(@Nullable Player player)
    {
        if (player == null)
            return null;
        var shulker_player = get_player(player.getUniqueId());
        if (shulker_player == null) {
            add_player(player);
            shulker_player = get_player(player.getUniqueId());
        }
        return shulker_player;
    }

    @Override
    public void add_player(@NotNull Player player)
    {
        if (!players.containsKey(player.getUniqueId()))
            players.put(player.getUniqueId(), new ReflectShulkerPlayer(player));
    }

    @Override
    public WrapperManager get_wrapper_manager()
    {
        return WRAPPER_MANAGER;
    }

    @Override
    public @NotNull String get_name()
    {
        return "Minecraft v1.13.R1";
    }

    public static class WrapperManagerV113R1 implements WrapperManager
    {
        @Override
        public ChatComponentWrapper get_chat_componenet_wrapper()
        {
            return ReflectChatComponentWrapper.INSTANCE;
        }

        @Override
        public ChatMessageTypeWrapper get_chat_message_type_wrapper()
        {
            return null;
        }

        @Override
        public ChatVisibilityWrapper get_chat_visibility_wrapper()
        {
            return null;
        }

        @Override
        public TitleActionWrapper get_title_action_wrapper()
        {
            return null;
        }

        @Override
        public ItemStackWrapper get_item_stack_wrapper()
        {
            return null;
        }

        @Override
        public PlayerWrapper get_player_wrapper()
        {
            return ReflectPlayerWrapper.INSTANCE;
        }

        @Override
        public ServerPingWrapper get_server_ping_wrapper()
        {
            return ReflectServerPingWrapper.INSTANCE;
        }

        @Override
        public @NotNull String get_name()
        {
            return "WrapperManager v1.13.R1";
        }
    }
}
