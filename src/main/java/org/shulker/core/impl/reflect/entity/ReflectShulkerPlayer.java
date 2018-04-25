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
import org.shulker.core.entity.ShulkerPlayer;

public class ReflectShulkerPlayer implements ShulkerPlayer<Player>
{
	@NotNull
	private Player player;

	private Object nmsPlayer;

	public ReflectShulkerPlayer(@NotNull Player player)
	{
		this.player = player;
	}

	@Override
	public void sendMessage(BaseComponent ... message)
	{
		player.spigot().sendMessage(message);
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
		return null;
	}

	@NotNull
	@Override
	public Player getPlayerHandle()
	{
		return player;
	}
}