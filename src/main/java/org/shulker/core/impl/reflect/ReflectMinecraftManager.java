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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
	private static final ReflectWrapperManager wrapperManager = new ReflectWrapperManager();

	@Override
	public void init()
	{
		Bukkit.getOnlinePlayers().forEach(this::addPlayer);

		if (init)
			throw new IllegalStateException("MinecraftManager is already initialized!");
		// Status
		decoders.put("PacketStatusOutServerInfo", this::newPacketStatusOutServerInfo);
		// Play
		decoders.put("PacketPlayOutChat", this::newPacketPlayOutChat);
		decoders.put("PacketPlayOutPlayerListHeaderFooter", this::newPacketPlayOutPlayerListHeaderFooter);
		decoders.put("PacketPlayOutOpenWindow", this::newPacketPlayOutOpenWindow);
		decoders.put("PacketPlayOutSetSlot", this::newPacketPlayOutSetSlot);
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
	public ShulkerPacketTitle<?> newPacketTitle()
	{
		return new ReflectPacketTitle();
	}

	@Override
	public ShulkerPacketTitle<?> newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action)
	{
		return new ReflectPacketTitle(action);
	}

	@Override
	public ShulkerPacketTitle<?> newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action, @NotNull BaseComponent... chatValue)
	{
		return new ReflectPacketTitle(action, chatValue);
	}

	@Override
	public ShulkerPacketTitle<?> newPacketTitle(@NotNull ShulkerPacketTitle.TitleAction action, int fadeIn, int stay, int fadeOut)
	{
		return new ReflectPacketTitle(action, fadeIn, stay, fadeOut);
	}

	@Override
	public ShulkerPacketTitle<?> newPacketTitle(Object packet)
	{
		return new ReflectPacketTitle(packet);
	}

	@Override
	public ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow()
	{
		return new ReflectPacketOutOpenWindow();
	}

	@Override
	public ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots)
	{
		return new ReflectPacketOutOpenWindow(windowId, windowType, title, slots);
	}

	@Override
	public ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots, int entityId)
	{
		return new ReflectPacketOutOpenWindow(windowId, windowType, title, slots, entityId);
	}

	@Override
	public ShulkerPacketOutOpenWindow<?> newPacketPlayOutOpenWindow(Object packet)
	{
		return new ReflectPacketOutOpenWindow(packet);
	}

	@Override
	public ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems()
	{
		return new ReflectPacketOutWindowItems();
	}

	@Override
	public ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems(int windowId, List<ItemStack> items)
	{
		return new ReflectPacketOutWindowItems(windowId, items);
	}

	@Override
	public ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems(int windowId, ItemStack... items)
	{
		return new ReflectPacketOutWindowItems(windowId, items);
	}

	@Override
	public ShulkerPacketOutWindowItems<?> newPacketPlayOutWindowItems(Object packet)
	{
		return new ReflectPacketOutWindowItems(packet);
	}

	@Override
	public ShulkerPacketOutWindowProperty<?> newPacketPlayOutWindowProperty()
	{
		return new ReflectPacketOutWindowProperty();
	}

	@Override
	public ShulkerPacketOutWindowProperty<?> newPacketPlayOutWindowProperty(int windowId, short property, short value)
	{
		return new ReflectPacketOutWindowProperty(windowId, property, value);
	}

	@Override
	public ShulkerPacketOutWindowProperty<?> newPacketPlayOutWindowProperty(Object packet)
	{
		return new ReflectPacketOutWindowProperty(packet);
	}

	@Override
	public ShulkerPacketOutSetSlot<?> newPacketPlayOutSetSlot()
	{
		return new ReflectPacketOutSetSlot();
	}

	@Override
	public ShulkerPacketOutSetSlot<?> newPacketPlayOutSetSlot(int windowId, int slot, ItemStack item)
	{
		return new ReflectPacketOutSetSlot(windowId, slot, item);
	}

	@Override
	public ShulkerPacketOutSetSlot<?> newPacketPlayOutSetSlot(Object packet)
	{
		return new ReflectPacketOutSetSlot(packet);
	}

	@Override
	public ShulkerPacketStatusOutServerInfo<?> newPacketStatusOutServerInfo()
	{
		return new ReflectPacketStatusOutServerInfo();
	}

	@Override
	public ShulkerPacketStatusOutServerInfo<?> newPacketStatusOutServerInfo(ServerPing serverPing)
	{
		return new ReflectPacketStatusOutServerInfo(serverPing);
	}

	@Override
	public ShulkerPacketStatusOutServerInfo<?> newPacketStatusOutServerInfo(Object packet)
	{
		return new ReflectPacketStatusOutServerInfo(packet);
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

	public static class ReflectWrapperManager implements WrapperManager
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
			return "WrapperManager reflection mode";
		}
	}
}