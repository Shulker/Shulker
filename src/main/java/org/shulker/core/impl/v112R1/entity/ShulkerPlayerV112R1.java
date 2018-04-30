/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v112R1.entity;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.mcelytra.chat.ChatVisibility;
import org.shulker.core.Shulker;
import org.shulker.core.entity.ShulkerPlayer;

public class ShulkerPlayerV112R1 implements ShulkerPlayer<Player>
{
	@NotNull
	private Player       player;
	@NotNull
	private EntityPlayer mcPlayer;

	public ShulkerPlayerV112R1(@NotNull Player player)
	{
		this.player = player;
		mcPlayer = ((CraftPlayer) player).getHandle();
	}

	@Override
	public void sendMessage(ChatMessageType type, BaseComponent... message)
	{
		var chatVisibility = getChatVisibility();
		if ((chatVisibility == ChatVisibility.HIDDEN && type != ChatMessageType.ACTION_BAR) || (chatVisibility == ChatVisibility.SYSTEM && type == ChatMessageType.CHAT))
			return;
		player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.valueOf(type.name()), message);
	}

	@Override
	public void sendRawPacket(Object rawPacket)
	{
		if (rawPacket instanceof Packet)
			mcPlayer.playerConnection.networkManager.sendPacket((Packet<?>) rawPacket);
	}

	@Override
	public int getPing()
	{
		return mcPlayer.ping;
	}

	@Override
	public String getLocale()
	{
		return player.getLocale();
	}

	@Override
	public ChatVisibility getChatVisibility()
	{
		return Shulker.getMCManager().getWrapperManager().getChatVisibilityWrapper().toShulker(mcPlayer.getChatFlags());
	}

	@Override
	public @NotNull Player getPlayerHandle()
	{
		return player;
	}
}