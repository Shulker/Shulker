/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcelytra.core.GameMode;
import org.mcelytra.core.ServerPing;
import org.shulker.core.MinecraftManager;
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.impl.reflect.entity.ReflectShulkerPlayer;
import org.shulker.core.impl.reflect.packets.*;
import org.shulker.core.impl.reflect.wrappers.ReflectChatComponentWrapper;
import org.shulker.core.impl.reflect.wrappers.ReflectPlayerWrapper;
import org.shulker.core.impl.reflect.wrappers.ReflectServerPingWrapper;
import org.shulker.core.packets.mc.play.*;
import org.shulker.core.packets.mc.status.ShulkerPacketStatusOutServerInfo;
import org.shulker.core.wrappers.*;

import java.util.List;

public class ReflectMinecraftManager extends MinecraftManager
{
    private static final ReflectWrapperManager wrapper_manager = new ReflectWrapperManager();

    @Override
    public void init()
    {
        Bukkit.getOnlinePlayers().forEach(this::add_player);

        if (init)
            throw new IllegalStateException("MinecraftManager is already initialized!");
        // Status
        decoders.put("PacketStatusOutServerInfo", this::new_packet_status_out_server_info);
        // Play
        decoders.put("PacketPlayOutChat", this::new_packet_play_out_chat);
        decoders.put("PacketPlayOutLogin", this::new_packet_join_game);
        decoders.put("PacketPlayOutOpenWindow", this::new_packet_play_out_open_window);
        decoders.put("PacketPlayOutPlayerListHeaderFooter", this::new_packet_play_out_playerlist_header_footer);
        decoders.put("PacketPlayOutSetSlot", this::new_packet_play_out_set_slot);
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
    public ShulkerPacketPlayOutChat<?> new_packet_play_out_chat()
    {
        return new ReflectPacketPlayOutChat();
    }

    @Override
    public ShulkerPacketPlayOutChat<?> new_packet_play_out_chat(BaseComponent... components)
    {
        return new ReflectPacketPlayOutChat(components);
    }

    @Override
    public ShulkerPacketPlayOutChat<?> new_packet_play_out_chat(Object packet)
    {
        return new ReflectPacketPlayOutChat(packet);
    }

    @Override
    public ShulkerPacketPlayerListHeaderFooter<?> new_packet_play_out_playerlist_header_footer()
    {
        return new ReflectPacketPlayerListHeaderFooter();
    }

    @Override
    public ShulkerPacketPlayerListHeaderFooter<?> new_packet_play_out_playerlist_header_footer(BaseComponent[] header, BaseComponent[] footer)
    {
        return new ReflectPacketPlayerListHeaderFooter(header, footer);
    }

    @Override
    public ShulkerPacketPlayerListHeaderFooter<?> new_packet_play_out_playerlist_header_footer(Object packet)
    {
        return new ReflectPacketPlayerListHeaderFooter(packet);
    }

    @Override
    public ShulkerPacketTitle<?> new_packet_title()
    {
        return new ReflectPacketTitle();
    }

    @Override
    public ShulkerPacketTitle<?> new_packet_title(@NotNull ShulkerPacketTitle.TitleAction action)
    {
        return new ReflectPacketTitle(action);
    }

    @Override
    public ShulkerPacketTitle<?> new_packet_title(@NotNull ShulkerPacketTitle.TitleAction action, @NotNull BaseComponent... chatValue)
    {
        return new ReflectPacketTitle(action, chatValue);
    }

    @Override
    public ShulkerPacketTitle<?> new_packet_title(@NotNull ShulkerPacketTitle.TitleAction action, int fadeIn, int stay, int fadeOut)
    {
        return new ReflectPacketTitle(action, fadeIn, stay, fadeOut);
    }

    @Override
    public ShulkerPacketTitle<?> new_packet_title(Object packet)
    {
        return new ReflectPacketTitle(packet);
    }

    @Override
    public ShulkerPacketJoinGame<?> new_packet_join_game(int entity_id, GameMode game_mode, boolean hardcore, int dimension, int max_players, String level_type, int render_distance, boolean reduced_debug_info)
    {
        return null;
    }

    @Override
    public ShulkerPacketJoinGame<?> new_packet_join_game(Object packet)
    {
        return null;
    }

    @Override
    public ShulkerPacketOutOpenWindow<?> new_packet_play_out_open_window()
    {
        return new ReflectPacketOutOpenWindow();
    }

    @Override
    public ShulkerPacketOutOpenWindow<?> new_packet_play_out_open_window(int windowId, String windowType, BaseComponent[] title, int slots)
    {
        return new ReflectPacketOutOpenWindow(windowId, windowType, title, slots);
    }

    @Override
    public ShulkerPacketOutOpenWindow<?> new_packet_play_out_open_window(int windowId, String windowType, BaseComponent[] title, int slots, int entityId)
    {
        return new ReflectPacketOutOpenWindow(windowId, windowType, title, slots, entityId);
    }

    @Override
    public ShulkerPacketOutOpenWindow<?> new_packet_play_out_open_window(Object packet)
    {
        return new ReflectPacketOutOpenWindow(packet);
    }

    @Override
    public ShulkerPacketOutWindowItems<?> new_packet_play_out_window_items()
    {
        return new ReflectPacketOutWindowItems();
    }

    @Override
    public ShulkerPacketOutWindowItems<?> new_packet_play_out_window_items(int windowId, List<ItemStack> items)
    {
        return new ReflectPacketOutWindowItems(windowId, items);
    }

    @Override
    public ShulkerPacketOutWindowItems<?> new_packet_play_out_window_items(int windowId, ItemStack... items)
    {
        return new ReflectPacketOutWindowItems(windowId, items);
    }

    @Override
    public ShulkerPacketOutWindowItems<?> new_packet_play_out_window_items(Object packet)
    {
        return new ReflectPacketOutWindowItems(packet);
    }

    @Override
    public ShulkerPacketOutWindowProperty<?> new_packet_play_out_window_property()
    {
        return new ReflectPacketOutWindowProperty();
    }

    @Override
    public ShulkerPacketOutWindowProperty<?> new_packet_play_out_window_property(int windowId, short property, short value)
    {
        return new ReflectPacketOutWindowProperty(windowId, property, value);
    }

    @Override
    public ShulkerPacketOutWindowProperty<?> new_packet_play_out_window_property(Object packet)
    {
        return new ReflectPacketOutWindowProperty(packet);
    }

    @Override
    public ShulkerPacketOutSetSlot<?> new_packet_play_out_set_slot()
    {
        return new ReflectPacketOutSetSlot();
    }

    @Override
    public ShulkerPacketOutSetSlot<?> new_packet_play_out_set_slot(int windowId, int slot, ItemStack item)
    {
        return new ReflectPacketOutSetSlot(windowId, slot, item);
    }

    @Override
    public ShulkerPacketOutSetSlot<?> new_packet_play_out_set_slot(Object packet)
    {
        return new ReflectPacketOutSetSlot(packet);
    }

    @Override
    public ShulkerPacketStatusOutServerInfo<?> new_packet_status_out_server_info()
    {
        return new ReflectPacketStatusOutServerInfo();
    }

    @Override
    public ShulkerPacketStatusOutServerInfo<?> new_packet_status_out_server_info(ServerPing serverPing)
    {
        return new ReflectPacketStatusOutServerInfo(serverPing);
    }

    @Override
    public ShulkerPacketStatusOutServerInfo<?> new_packet_status_out_server_info(Object packet)
    {
        return new ReflectPacketStatusOutServerInfo(packet);
    }

    @Override
    public WrapperManager get_wrapper_manager()
    {
        return wrapper_manager;
    }

    @Override
    public @NotNull String get_name()
    {
        return "MinecraftManager reflection mode";
    }

    public static class ReflectWrapperManager implements WrapperManager
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
            return "WrapperManager reflection mode";
        }
    }
}
