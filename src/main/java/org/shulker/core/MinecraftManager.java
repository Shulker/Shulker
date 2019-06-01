/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core;

import net.md_5.bungee.api.chat.BaseComponent;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcelytra.core.GameMode;
import org.mcelytra.core.ServerPing;
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.packets.DefaultShulkerPacket;
import org.shulker.core.packets.ShulkerPacket;
import org.shulker.core.packets.mc.play.*;
import org.shulker.core.packets.mc.status.ShulkerPacketStatusOutServerInfo;
import org.shulker.core.wrappers.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public abstract class MinecraftManager implements Nameable
{
    protected final HashMap<UUID, ShulkerPlayer<Player>>                players  = new HashMap<>();
    protected final HashMap<String, Function<Object, ShulkerPacket<?>>> decoders = new HashMap<>();
    protected       boolean                                             init     = false;

    /**
     * Initializes the MinecraftManager.
     */
    public abstract void init();

    public ShulkerPlayer<Player> get_player(@Nullable Player player)
    {
        if (player == null)
            return null;
        return get_player(player.getUniqueId());
    }

    public ShulkerPlayer<Player> get_player(@NotNull UUID uuid)
    {
        return players.get(uuid);
    }

    public abstract void add_player(@NotNull Player player);

    public void remove_player(@NotNull UUID player)
    {
        players.remove(player);
    }

    /**
     * Gets the packet from the server packet object.
     *
     * @param packet The server packet object.
     * @return The Shulker packet.
     */
    public ShulkerPacket<?> from_packet(Object packet)
    {
        var decoder = decoders.get(packet.getClass().getSimpleName());
        if (decoder == null)
            decoder = DefaultShulkerPacket::new;
        return decoder.apply(packet);
    }

    public abstract ShulkerPacketPlayOutChat<?> new_packet_play_out_chat();

    public abstract ShulkerPacketPlayOutChat<?> new_packet_play_out_chat(BaseComponent... components);

    public abstract ShulkerPacketPlayOutChat<?> new_packet_play_out_chat(Object packet);

    public abstract ShulkerPacketPlayerListHeaderFooter<?> new_packet_play_out_playerlist_header_footer();

    public abstract ShulkerPacketPlayerListHeaderFooter<?> new_packet_play_out_playerlist_header_footer(BaseComponent[] header, BaseComponent[] footer);

    public abstract ShulkerPacketPlayerListHeaderFooter<?> new_packet_play_out_playerlist_header_footer(Object packet);

    public abstract ShulkerPacketTitle<?> new_packet_title();

    public abstract ShulkerPacketTitle<?> new_packet_title(@NotNull ShulkerPacketTitle.TitleAction action);

    public abstract ShulkerPacketTitle<?> new_packet_title(@NotNull ShulkerPacketTitle.TitleAction action, @NotNull BaseComponent... chatValue);

    public abstract ShulkerPacketTitle<?> new_packet_title(@NotNull ShulkerPacketTitle.TitleAction action, int fadeIn, int stay, int fadeOut);

    public abstract ShulkerPacketTitle<?> new_packet_title(Object packet);

    /*
        Basics
     */

    public ShulkerPacketJoinGame<?> new_packet_join_game()
    {
        return this.new_packet_join_game(0, GameMode.SURVIVAL, false, 0, Bukkit.getMaxPlayers(), "default", Bukkit.getViewDistance(), false);
    }

    public abstract ShulkerPacketJoinGame<?> new_packet_join_game(int entity_id, GameMode game_mode, boolean hardcore, int dimension, int max_players, String level_type, int render_distance, boolean reduced_debug_info);

    public abstract ShulkerPacketJoinGame<?> new_packet_join_game(Object packet);

	/*
		Inventory
	 */

    public abstract ShulkerPacketOutOpenWindow<?> new_packet_play_out_open_window();

    public abstract ShulkerPacketOutOpenWindow<?> new_packet_play_out_open_window(int windowId, String windowType, BaseComponent[] title, int slots);

    public abstract ShulkerPacketOutOpenWindow<?> new_packet_play_out_open_window(int windowId, String windowType, BaseComponent[] title, int slots, int entityId);

    public abstract ShulkerPacketOutOpenWindow<?> new_packet_play_out_open_window(Object packet);

    public abstract ShulkerPacketOutWindowItems<?> new_packet_play_out_window_items();

    public abstract ShulkerPacketOutWindowItems<?> new_packet_play_out_window_items(int windowId, List<ItemStack> items);

    public abstract ShulkerPacketOutWindowItems<?> new_packet_play_out_window_items(int windowId, ItemStack... items);

    public abstract ShulkerPacketOutWindowItems<?> new_packet_play_out_window_items(Object packet);

    public abstract ShulkerPacketOutWindowProperty<?> new_packet_play_out_window_property();

    public abstract ShulkerPacketOutWindowProperty<?> new_packet_play_out_window_property(int windowId, short property, short value);

    public abstract ShulkerPacketOutWindowProperty<?> new_packet_play_out_window_property(Object packet);

    public abstract ShulkerPacketOutSetSlot<?> new_packet_play_out_set_slot();

    public abstract ShulkerPacketOutSetSlot<?> new_packet_play_out_set_slot(int windowId, int slot, ItemStack item);

    public abstract ShulkerPacketOutSetSlot<?> new_packet_play_out_set_slot(Object packet);

	/*
		PACKET - STATUS
	 */

    public abstract ShulkerPacketStatusOutServerInfo<?> new_packet_status_out_server_info();

    public abstract ShulkerPacketStatusOutServerInfo<?> new_packet_status_out_server_info(ServerPing serverPing);

    public abstract ShulkerPacketStatusOutServerInfo<?> new_packet_status_out_server_info(Object packet);

    /**
     * Gets the Wrapper Manager.
     *
     * @return The wrapper manager.
     */
    public abstract WrapperManager get_wrapper_manager();

    /**
     * Represents the manager of wrappers.
     */
    public static interface WrapperManager extends Nameable
    {
        ChatComponentWrapper get_chat_componenet_wrapper();

        ChatMessageTypeWrapper get_chat_message_type_wrapper();

        ChatVisibilityWrapper get_chat_visibility_wrapper();

        TitleActionWrapper get_title_action_wrapper();

        ItemStackWrapper get_item_stack_wrapper();

        PlayerWrapper get_player_wrapper();

        ServerPingWrapper get_server_ping_wrapper();
    }
}
