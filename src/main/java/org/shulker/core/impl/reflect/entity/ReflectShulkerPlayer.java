/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.entity;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.mcelytra.chat.ChatVisibility;
import org.shulker.core.entity.ShulkerPlayer;

public class ReflectShulkerPlayer implements ShulkerPlayer<Player>
{
	@NotNull
	private Player player;

	private Object mcPlayer;

	public ReflectShulkerPlayer(@NotNull Player player)
	{
		this.player = player;
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

	}

	@Override
	public int getPing()
	{
		return -1;
	}

	@Override
	public String getLocale()
	{
		return player.getLocale();
	}

	@Override
	public ChatVisibility getChatVisibility()
	{
		return ChatVisibility.FULL;
	}

	@NotNull
	@Override
	public Player getPlayerHandle()
	{
		return player;
	}
}