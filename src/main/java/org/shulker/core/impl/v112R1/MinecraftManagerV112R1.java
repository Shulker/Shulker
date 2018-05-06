/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v112R1;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.impl.reflect.ReflectMinecraftManager;
import org.shulker.core.impl.reflect.wrappers.ReflectPlayerWrapper;
import org.shulker.core.impl.reflect.wrappers.ReflectServerPingWrapper;
import org.shulker.core.impl.v112R1.entity.ShulkerPlayerV112R1;
import org.shulker.core.impl.v112R1.packets.ShulkerPacketPlayOutChatV112R1;
import org.shulker.core.impl.v112R1.wrappers.*;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayOutChat;
import org.shulker.core.wrappers.*;

public class MinecraftManagerV112R1 extends ReflectMinecraftManager
{
	private static final WrapperManagerV112R1 wrapperManager = new WrapperManagerV112R1();

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
	public void addPlayer(@NotNull Player player)
	{
		if (!players.containsKey(player.getUniqueId()))
			players.put(player.getUniqueId(), new ShulkerPlayerV112R1(player));
	}

	@Override
	public ShulkerPacketPlayOutChat<?> newPacketPlayOutChat()
	{
		return new ShulkerPacketPlayOutChatV112R1();
	}

	@Override
	public ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(BaseComponent... components)
	{
		return new ShulkerPacketPlayOutChatV112R1(components);
	}

	@Override
	public ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(Object packet)
	{
		if (!(packet instanceof PacketPlayOutChat))
			throw new IllegalArgumentException("packet must be of type PacketPlayOutChat.");
		return new ShulkerPacketPlayOutChatV112R1((PacketPlayOutChat) packet);
	}

	@Override
	public WrapperManager getWrapperManager()
	{
		return wrapperManager;
	}

	@Override
	public @NotNull String getName()
	{
		return "MinecraftManager v1.12.R1";
	}

	public static class WrapperManagerV112R1 implements WrapperManager
	{
		@Override
		public ChatComponentWrapper getChatComponentWrapper()
		{
			return ChatComponentWrapperV112R1.INSTANCE;
		}

		@Override
		public ChatMessageTypeWrapper getChatMessageTypeWrapper()
		{
			return ChatMessageTypeWrapperV112R1.INSTANCE;
		}

		@Override
		public ChatVisibilityWrapper getChatVisibilityWrapper()
		{
			return ChatVisibilityWrapperV112R1.INSTANCE;
		}

		@Override
		public TitleActionWrapper getTitleActionWrapper()
		{
			return TitleActionWrapperV112R1.INSTANCE;
		}

		@Override
		public ItemStackWrapper getItemStackWrapper()
		{
			return ItemStackWrapperV112R1.INSTANCE;
		}

		@Override
		public PlayerWrapper getPlayerWrapper()
		{
			return ReflectPlayerWrapper.INSTANCE;
		}

		@Override
		public ServerPingWrapper getServerPingWrapper()
		{
			return ReflectServerPingWrapper.INSTANCE;
		}

		@Override
		public @NotNull String getName()
		{
			return "WrapperManager v1.12.R1";
		}
	}
}