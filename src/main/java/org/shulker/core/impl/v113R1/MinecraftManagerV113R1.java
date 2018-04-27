/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R1;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.MinecraftManager;
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.impl.reflect.entity.ReflectShulkerPlayer;
import org.shulker.core.impl.reflect.packets.ReflectPacketPlayOutChat;
import org.shulker.core.impl.reflect.packets.ReflectPacketPlayerListHeaderFooter;
import org.shulker.core.impl.reflect.packets.ReflectPacketTitle;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayOutChat;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayerListHeaderFooter;
import org.shulker.core.packets.mc.play.ShulkerPacketTitle;
import org.shulker.core.wrappers.ChatComponentWrapper;
import org.shulker.core.wrappers.ChatMessageTypeWrapper;
import org.shulker.core.wrappers.ChatVisibilityWrapper;
import org.shulker.core.wrappers.TitleActionWrapper;

import java.util.HashMap;
import java.util.UUID;

public class MinecraftManagerV113R1 implements MinecraftManager
{
	private static final WrapperManagerV113R1                 WRAPPER_MANAGER = new WrapperManagerV113R1();
	private final        HashMap<UUID, ShulkerPlayer<Player>> players         = new HashMap<>();

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
		return new ReflectPacketPlayOutChat();
	}

	@Override
	public ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(BaseComponent... components)
	{
		return new ReflectPacketPlayOutChat(components);
	}

	@Override
	public ShulkerPacketPlayOutChat<?> newPacketPlayOutChat(Object packet)
	{
		return new ReflectPacketPlayOutChat(packet);
	}

	@Override
	public ShulkerPacketPlayerListHeaderFooter<?> newPacketPlayOutPlayerListHeaderFooter()
	{
		return new ReflectPacketPlayerListHeaderFooter();
	}

	@Override
	public ShulkerPacketPlayerListHeaderFooter<?> newPacketPlayOutPlayerListHeaderFooter(BaseComponent[] header, BaseComponent[] footer)
	{
		return new ReflectPacketPlayerListHeaderFooter(header, footer);
	}

	@Override
	public ShulkerPacketPlayerListHeaderFooter<?> newPacketPlayOutPlayerListHeaderFooter(Object packet)
	{
		return new ReflectPacketPlayerListHeaderFooter(packet);
	}

	@Override
	public ShulkerPacketTitle newPacketTitle()
	{
		return new ReflectPacketTitle();
	}

	@Override
	public ShulkerPacketTitle newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action)
	{
		return new ReflectPacketTitle(action);
	}

	@Override
	public ShulkerPacketTitle newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action, @NotNull BaseComponent... chatValue)
	{
		return new ReflectPacketTitle(action, chatValue);
	}

	@Override
	public ShulkerPacketTitle newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action, int fadeIn, int stay, int fadeOut)
	{
		return new ReflectPacketTitle(action, fadeIn, stay, fadeOut);
	}

	@Override
	public ShulkerPacketTitle newPacketTitle(Object packet)
	{
		return new ReflectPacketTitle(packet);
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
			return null;
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
		public @NotNull String getName()
		{
			return "WrapperManager v1.13.R1";
		}
	}
}