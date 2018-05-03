/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.shulker.core.DebugType;
import org.shulker.core.Shulker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class PacketHandler implements Listener
{
	private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);

	public abstract void addHandler(Player player);

	public abstract void removeHandler(Player player);

	public abstract void addServerHandler();

	public abstract void removeServerHandler();

	public void enable()
	{
		Shulker.logDebug(DebugType.CONNECTIONS, Shulker.getPrefix(), "Enabling PacketHandler '" + getClass().getSimpleName() + "'...");
		Bukkit.getOnlinePlayers().forEach(this::addHandler);
		addServerHandler();
	}

	public void disable()
	{
		Shulker.logDebug(DebugType.CONNECTIONS, Shulker.getPrefix(), "Disabling PacketHandler '" + getClass().getSimpleName() + "'...");
		Bukkit.getOnlinePlayers().forEach(this::removeHandler);
		removeServerHandler();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerLoginEvent e)
	{
		final var player = e.getPlayer();
		if ((!player.isBanned()) && (e.getResult() == PlayerLoginEvent.Result.ALLOWED))
			threadPool.schedule(() -> addHandler(player), 100, TimeUnit.MILLISECONDS);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLeave(final PlayerQuitEvent e)
	{
		threadPool.execute(() -> removeHandler(e.getPlayer()));
	}
}