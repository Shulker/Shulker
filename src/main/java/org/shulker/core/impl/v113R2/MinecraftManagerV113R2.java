/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R2;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PacketPlayOutLogin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcelytra.core.GameMode;
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.impl.reflect.ReflectMinecraftManager;
import org.shulker.core.impl.reflect.wrappers.ReflectServerPingWrapper;
import org.shulker.core.impl.v113R2.entity.ShulkerPlayerV113R2;
import org.shulker.core.impl.v113R2.packets.ShulkerPacketJoinGameV113R2;
import org.shulker.core.impl.v113R2.packets.ShulkerPacketPlayOutChatV113R2;
import org.shulker.core.impl.v113R2.wrappers.*;
import org.shulker.core.packets.mc.play.ShulkerPacketJoinGame;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayOutChat;
import org.shulker.core.wrappers.*;

public class MinecraftManagerV113R2 extends ReflectMinecraftManager
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
            players.put(player.getUniqueId(), new ShulkerPlayerV113R2(player));
    }

    @Override
    public ShulkerPacketPlayOutChat<?> new_packet_play_out_chat()
    {
        return new ShulkerPacketPlayOutChatV113R2();
    }

    @Override
    public ShulkerPacketPlayOutChat<?> new_packet_play_out_chat(BaseComponent... components)
    {
        return new ShulkerPacketPlayOutChatV113R2(components);
    }

    @Override
    public ShulkerPacketPlayOutChat<?> new_packet_play_out_chat(Object packet)
    {
        if (!(packet instanceof PacketPlayOutChat))
            throw new IllegalArgumentException("packet must be of type PacketPlayOutChat.");
        return new ShulkerPacketPlayOutChatV113R2((PacketPlayOutChat) packet);
    }

    @Override
    public ShulkerPacketJoinGame<?> new_packet_join_game(int entity_id, GameMode game_mode, boolean hardcore, int dimension, int max_players, String level_type, int render_distance, boolean reduced_debug_info)
    {
        return new ShulkerPacketJoinGameV113R2(entity_id, game_mode, hardcore, dimension, max_players, level_type, reduced_debug_info);
    }

    @Override
    public ShulkerPacketJoinGame<?> new_packet_join_game(Object packet)
    {
        if (!(packet instanceof PacketPlayOutLogin))
            throw new IllegalArgumentException("packet must be of type PacketPlayOutLogin.");
        return new ShulkerPacketJoinGameV113R2((PacketPlayOutLogin) packet);
    }

    @Override
    public WrapperManager get_wrapper_manager()
    {
        return WRAPPER_MANAGER;
    }

    @Override
    public @NotNull String get_name()
    {
        return "Minecraft v1.13.R2";
    }

    public static class WrapperManagerV113R1 implements WrapperManager
    {
        @Override
        public ChatComponentWrapper get_chat_componenet_wrapper()
        {
            return ChatComponentWrapperV113R2.INSTANCE;
        }

        @Override
        public ChatMessageTypeWrapper get_chat_message_type_wrapper()
        {
            return ChatMessageTypeWrapperV113R2.INSTANCE;
        }

        @Override
        public ChatVisibilityWrapper get_chat_visibility_wrapper()
        {
            return ChatVisibilityWrapperV113R2.INSTANCE;
        }

        @Override
        public TitleActionWrapper get_title_action_wrapper()
        {
            return TitleActionWrapperV113R2.INSTANCE;
        }

        @Override
        public ItemStackWrapper get_item_stack_wrapper()
        {
            return ItemStackWrapperV113R2.INSTANCE;
        }

        @Override
        public PlayerWrapper get_player_wrapper()
        {
            return PlayerWrapperV113R2.INSTANCE;
        }

        @Override
        public ServerPingWrapper get_server_ping_wrapper()
        {
            return ReflectServerPingWrapper.INSTANCE;
        }

        @Override
        public @NotNull String get_name()
        {
            return "WrapperManager v1.13.R2";
        }
    }
}
