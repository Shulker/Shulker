/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.shulker.core.DebugType;
import org.shulker.core.Shulker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class PacketHandler implements Listener
{
    private            ScheduledExecutorService thread_pool = Executors.newScheduledThreadPool(10);
    protected volatile boolean                  run         = false;

    public abstract void add_handler(Player player);

    public abstract void remove_handler(Player player);

    public abstract void add_server_handler();

    public abstract void remove_server_handler();

    public void enable()
    {
        run = true;
        Shulker.log_debug(DebugType.CONNECTIONS, Shulker.get_prefix(), "Enabling PacketHandler '" + getClass().getSimpleName() + "'...");
        Bukkit.getOnlinePlayers().forEach(this::add_handler);
        add_server_handler();
    }

    public void disable()
    {
        Shulker.log_debug(DebugType.CONNECTIONS, Shulker.get_prefix(), "Disabling PacketHandler '" + getClass().getSimpleName() + "'...");
        Bukkit.getOnlinePlayers().forEach(this::remove_handler);
        remove_server_handler();
        run = false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on_player_join(final PlayerLoginEvent e)
    {
        final var player = e.getPlayer();
        if ((!player.isBanned()) && (e.getResult() == PlayerLoginEvent.Result.ALLOWED))
            thread_pool.schedule(() -> add_handler(player), 100, TimeUnit.MILLISECONDS);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on_player_leave(final PlayerQuitEvent e)
    {
        thread_pool.execute(() -> remove_handler(e.getPlayer()));
    }
}
