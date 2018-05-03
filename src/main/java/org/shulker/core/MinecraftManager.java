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
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.packets.mc.play.*;
import org.shulker.core.wrappers.*;

import java.util.List;
import java.util.UUID;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.newInstance;

public interface MinecraftManager extends Nameable
{
	default ShulkerPlayer<Player> getPlayer(@Nullable Player player)
	{
		if (player == null)
			return null;
		return getPlayer(player.getUniqueId());
	}

	ShulkerPlayer<Player> getPlayer(@NotNull UUID uuid);

	void addPlayer(@NotNull Player player);

	void removePlayer(@NotNull UUID player);

	ShulkerPacketPlayOutChat<?> newPacketPlayOutChat();

	ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(BaseComponent... components);

	ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(Object packet);

	ShulkerPacketPlayerListHeaderFooter<?> newPacketPlayOutPlayerListHeaderFooter();

	ShulkerPacketPlayerListHeaderFooter<?> newPacketPlayOutPlayerListHeaderFooter(BaseComponent[] header, BaseComponent[] footer);

	ShulkerPacketPlayerListHeaderFooter<?> newPacketPlayOutPlayerListHeaderFooter(Object packet);

	ShulkerPacketTitle<?> newPacketTitle();

	ShulkerPacketTitle<?> newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action);

	ShulkerPacketTitle<?> newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action, @NotNull BaseComponent... chatValue);

	ShulkerPacketTitle<?> newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action, int fadeIn, int stay, int fadeOut);

	ShulkerPacketTitle<?> newPacketTitle(Object packet);

	/*
		Inventory
	 */

	ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow();

	ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots);

	ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots, int entityId);

	ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow(Object packet);

	ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems();

	ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems(int windowId, List<ItemStack> items);

	ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems(int windowId, ItemStack... items);

	ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems(Object packet);

	ShulkerPacketOutWindowProperty<?> newPacketPlayOutWindowProperty();

	ShulkerPacketOutWindowProperty<?> newPacketPlayOutWindowProperty(int windowId, short property, short value);

	ShulkerPacketOutWindowProperty<?> newPacketPlayOutWindowProperty(Object packet);

	ShulkerPacketOutSetSlot<?> newPacketPlayOutSetSlot();

	ShulkerPacketOutSetSlot<?> newPacketPlayOutSetSlot(int windowId, int slot, ItemStack item);

	ShulkerPacketOutSetSlot<?> newPacketPlayOutSetSlot(Object packet);

	WrapperManager getWrapperManager();

	static interface WrapperManager extends Nameable
	{
		ChatComponentWrapper getChatComponentWrapper();

		ChatMessageTypeWrapper getChatMessageTypeWrapper();

		ChatVisibilityWrapper getChatVisibilityWrapper();

		TitleActionWrapper getTitleActionWrapper();

		ItemStackWrapper getItemStackWrapper();

		PlayerWrapper getPlayerWrapper();
	}
}