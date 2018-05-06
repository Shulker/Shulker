/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
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
		decoders.put("PacketStatusOutServerInfo", this::newPacketStatusOutServerInfo);
		// Play
		decoders.put("PacketPlayOutChat", this::newPacketPlayOutChat);
		decoders.put("PacketPlayOutPlayerListHeaderFooter", this::newPacketPlayOutPlayerListHeaderFooter);
		decoders.put("PacketPlayOutOpenWindow", this::newPacketPlayOutOpenWindow);
		decoders.put("PacketPlayOutTitle", this::newPacketTitle);
		decoders.put("PacketPlayOutWindowData", this::newPacketPlayOutWindowProperty);
		decoders.put("PacketPlayOutWindowItems", this::newPacketPlayOutWindowItems);
		init = true;
	}

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
			players.put(player.getUniqueId(), new ReflectShulkerPlayer(player));
	}

	@Override
	public WrapperManager getWrapperManager()
	{
		return WRAPPER_MANAGER;
	}

	@Override
	public @NotNull String getName()
	{
		return "Minecraft v1.13.R1";
	}

	public static class WrapperManagerV113R1 implements WrapperManager
	{
		@Override
		public ChatComponentWrapper getChatComponentWrapper()
		{
			return ReflectChatComponentWrapper.INSTANCE;
		}

		@Override
		public ChatMessageTypeWrapper getChatMessageTypeWrapper()
		{
			return null;
		}

		@Override
		public ChatVisibilityWrapper getChatVisibilityWrapper()
		{
			return null;
		}

		@Override
		public TitleActionWrapper getTitleActionWrapper()
		{
			return null;
		}

		@Override
		public ItemStackWrapper getItemStackWrapper()
		{
			return null;
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
			return "WrapperManager v1.13.R1";
		}
	}
}