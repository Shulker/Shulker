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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.MinecraftManager;
import org.shulker.core.entity.ShulkerPlayer;
import org.shulker.core.impl.reflect.packets.*;
import org.shulker.core.impl.reflect.wrappers.ReflectChatComponentWrapper;
import org.shulker.core.impl.reflect.wrappers.ReflectPlayerWrapper;
import org.shulker.core.impl.v112R1.entity.ShulkerPlayerV112R1;
import org.shulker.core.impl.v112R1.packets.ShulkerPacketPlayOutChatV112R1;
import org.shulker.core.impl.v112R1.wrappers.*;
import org.shulker.core.packets.mc.play.*;
import org.shulker.core.wrappers.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MinecraftManagerV112R1 implements MinecraftManager
{
	private static final WrapperManagerV112R1                 wrapperManager = new WrapperManagerV112R1();
	private final        HashMap<UUID, ShulkerPlayer<Player>> players        = new HashMap<>();

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
			players.put(player.getUniqueId(), new ShulkerPlayerV112R1(player));
	}

	@Override
	public void removePlayer(@NotNull UUID player)
	{
		players.remove(player);
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
		public @NotNull String getName()
		{
			return "WrapperManager v1.12.R1";
		}
	}
}