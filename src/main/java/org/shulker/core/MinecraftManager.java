/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core;

import net.md_5.bungee.api.chat.BaseComponent;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

	public ShulkerPlayer<Player> getPlayer(@Nullable Player player)
	{
		if (player == null)
			return null;
		return getPlayer(player.getUniqueId());
	}

	public ShulkerPlayer<Player> getPlayer(@NotNull UUID uuid)
	{
		return players.get(uuid);
	}

	public abstract void addPlayer(@NotNull Player player);

	public void removePlayer(@NotNull UUID player)
	{
		players.remove(player);
	}

	/**
	 * Gets the packet from the server packet object.
	 *
	 * @param packet The server packet object.
	 * @return The Shulker packet.
	 */
	public ShulkerPacket<?> fromPacket(Object packet)
	{
		var decoder = decoders.get(packet.getClass().getSimpleName());
		if (decoder == null)
			decoder = DefaultShulkerPacket::new;
		return decoder.apply(packet);
	}

	public abstract ShulkerPacketPlayOutChat<?> newPacketPlayOutChat();

	public abstract ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(BaseComponent... components);

	public abstract ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(Object packet);

	public abstract ShulkerPacketPlayerListHeaderFooter<?> newPacketPlayOutPlayerListHeaderFooter();

	public abstract ShulkerPacketPlayerListHeaderFooter<?> newPacketPlayOutPlayerListHeaderFooter(BaseComponent[] header, BaseComponent[] footer);

	public abstract ShulkerPacketPlayerListHeaderFooter<?> newPacketPlayOutPlayerListHeaderFooter(Object packet);

	public abstract ShulkerPacketTitle<?> newPacketTitle();

	public abstract ShulkerPacketTitle<?> newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action);

	public abstract ShulkerPacketTitle<?> newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action, @NotNull BaseComponent... chatValue);

	public abstract ShulkerPacketTitle<?> newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action, int fadeIn, int stay, int fadeOut);

	public abstract ShulkerPacketTitle<?> newPacketTitle(Object packet);

	/*
		Inventory
	 */

	public abstract ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow();

	public abstract ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots);

	public abstract ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots, int entityId);

	public abstract ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow(Object packet);

	public abstract ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems();

	public abstract ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems(int windowId, List<ItemStack> items);

	public abstract ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems(int windowId, ItemStack... items);

	public abstract ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems(Object packet);

	public abstract ShulkerPacketOutWindowProperty<?> newPacketPlayOutWindowProperty();

	public abstract ShulkerPacketOutWindowProperty<?> newPacketPlayOutWindowProperty(int windowId, short property, short value);

	public abstract ShulkerPacketOutWindowProperty<?> newPacketPlayOutWindowProperty(Object packet);

	public abstract ShulkerPacketOutSetSlot<?> newPacketPlayOutSetSlot();

	public abstract ShulkerPacketOutSetSlot<?> newPacketPlayOutSetSlot(int windowId, int slot, ItemStack item);

	public abstract ShulkerPacketOutSetSlot<?> newPacketPlayOutSetSlot(Object packet);

	/*
		PACKET - STATUS
	 */

	public abstract ShulkerPacketStatusOutServerInfo<?> newPacketStatusOutServerInfo();

	public abstract ShulkerPacketStatusOutServerInfo<?> newPacketStatusOutServerInfo(ServerPing serverPing);

	public abstract ShulkerPacketStatusOutServerInfo<?> newPacketStatusOutServerInfo(Object packet);

	/**
	 * Gets the Wrapper Manager.
	 *
	 * @return The wrapper manager.
	 */
	public abstract WrapperManager getWrapperManager();

	/**
	 * Represents the manager of wrappers.
	 */
	public static interface WrapperManager extends Nameable
	{
		ChatComponentWrapper getChatComponentWrapper();

		ChatMessageTypeWrapper getChatMessageTypeWrapper();

		ChatVisibilityWrapper getChatVisibilityWrapper();

		TitleActionWrapper getTitleActionWrapper();

		ItemStackWrapper getItemStackWrapper();

		PlayerWrapper getPlayerWrapper();

		ServerPingWrapper getServerPingWrapper();
	}
}