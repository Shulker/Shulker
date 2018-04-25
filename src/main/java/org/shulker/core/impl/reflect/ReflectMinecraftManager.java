/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.MinecraftManager;
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.impl.reflect.entity.ReflectShulkerPlayer;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayOutChat;
import org.shulker.core.wrappers.ChatComponentWrapper;
import org.shulker.core.wrappers.ChatMessageTypeWrapper;

import java.util.HashMap;
import java.util.UUID;

public class ReflectMinecraftManager implements MinecraftManager
{
	private static final ReflectWrapperManager wrapperManager = new ReflectWrapperManager();
	private final HashMap<UUID, ShulkerPlayer<Player>> players = new HashMap<>();

	@Override
	public ShulkerPlayer<Player> getPlayer(@Nullable Player player)
	{
		if (player == null)
			return null;
		var shulkerPlayer = getPlayer(player.getUniqueId());
		if (shulkerPlayer == null)
		{
			addPlayer(player);
			shulkerPlayer = getPlayer(player.getUniqueId());
		}
		return shulkerPlayer;
	}

	@Override
	public ShulkerPlayer<Player> getPlayer(@NotNull UUID uuid)
	{
		return players.get(uuid);
	}

	@Override
	public void addPlayer(@NotNull Player player)
	{
		if (!players.containsKey(player.getUniqueId()))
			players.put(player.getUniqueId(), new ReflectShulkerPlayer(player));
	}

	@Override
	public void removePlayer(@NotNull UUID player)
	{
		players.remove(player);
	}

	@Override
	public ShulkerPacketPlayOutChat<?> newPacketPlayOutChat()
	{
		throw new UnsupportedOperationException("newPacketPlayOutChat() is not implemented.");
	}

	@Override
	public ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(BaseComponent... components)
	{
		throw new UnsupportedOperationException("newPacketPlayOutChat(BaseComponent...) is not implemented.");
	}

	@Override
	public ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(Object packet)
	{
		throw new UnsupportedOperationException("newPacketPlayOutChat(Object) is not implemented.");
	}

	@Override
	public WrapperManager getWrapperManager()
	{
		return wrapperManager;
	}

	@Override
	public @NotNull String getName()
	{
		return "MinecraftManager reflection mode";
	}

	static class ReflectWrapperManager implements WrapperManager
	{

		@Override
		public ChatComponentWrapper getChatComponentWrapper()
		{
			return null;
		}

		@Override
		public ChatMessageTypeWrapper getChatMessageTypeWrapper()
		{
			return null;
		}

		@Override
		public @NotNull String getName()
		{
			return "WrapperManager reflection mode";
		}
	}
}