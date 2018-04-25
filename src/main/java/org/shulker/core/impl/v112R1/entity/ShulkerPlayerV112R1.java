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
	public void sendMessage(BaseComponent ... message)
	{
		player.spigot().sendMessage(message);
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
		return mcPlayer.locale;
	}

	@Override
	public @NotNull Player getPlayerHandle()
	{
		return player;
	}
}