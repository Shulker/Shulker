/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.mc.play;

import org.bukkit.Bukkit;
import org.mcelytra.core.GameMode;
import org.shulker.core.packets.ShulkerPacket;

public abstract class ShulkerPacketJoinGame<T> extends ShulkerPacket<T>
{
    public ShulkerPacketJoinGame(T packet)
    {
        super(packet);
    }

    public abstract int get_entity_id();

    public abstract void set_entity_id(int id);

    public abstract GameMode get_game_mode();

    public abstract void set_game_mode(GameMode game_mode);

    public abstract boolean is_hardcore();

    public abstract void set_hardcore(boolean hardcore);

    public abstract int get_dimension();

    public abstract void set_dimension(int dimension);

    public abstract int get_max_players();

    public abstract void set_max_players(int max_players);

    public abstract String get_level_type();

    public abstract void set_level_type(String level_type);

    public abstract int get_render_distance();

    public abstract void set_render_distance(int render_distance);

    public abstract boolean has_reduced_debug_info();

    public abstract void set_reduced_debug_info(boolean reduced_debug_info);

    @Override
    public void reset()
    {
        set_entity_id(0);
        set_game_mode(GameMode.SURVIVAL);
        set_dimension(0);
        set_max_players(Bukkit.getMaxPlayers());
        set_level_type("default");
        set_render_distance(Bukkit.getViewDistance());
        set_reduced_debug_info(false);
    }
}
