/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
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
	/**
	 * Sets the location of the explosion.
	 * @param location
	 */
	public abstract void setLocation(Location location);

	public abstract Location getLocation();

	public abstract void setRadius(float radius);

	public abstract float getRadius();

	@Override
	public void reset()
	{
		setLocation(null);
		setRadius(0.0F);
	}
}