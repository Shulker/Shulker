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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayOutChat;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayerListHeaderFooter;
import org.shulker.core.wrappers.ChatComponentWrapper;
import org.shulker.core.wrappers.ChatMessageTypeWrapper;

import java.util.UUID;

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

	WrapperManager getWrapperManager();

	static interface WrapperManager extends Nameable
	{
		ChatComponentWrapper getChatComponentWrapper();

		ChatMessageTypeWrapper getChatMessageTypeWrapper();
	}
}