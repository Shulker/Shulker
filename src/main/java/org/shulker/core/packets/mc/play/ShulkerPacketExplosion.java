/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.mc.play;

import org.bukkit.Location;
import org.shulker.core.packets.ShulkerPacket;

public abstract class ShulkerPacketExplosion<T> extends ShulkerPacket<T>
{
    public ShulkerPacketExplosion(T packet)
    {
        super(packet);
    }

    /**
     * Sets the location of the explosion.
     *
     * @param location
     */
    public abstract void set_location(Location location);

    public abstract Location get_location();

    public abstract void set_radius(float radius);

    public abstract float get_radius();

    @Override
    public void reset()
    {
        set_location(null);
        set_radius(0.0F);
    }
}
