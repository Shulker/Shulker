/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.mc.play;

import org.mcelytra.core.Difficulty;
import org.shulker.core.packets.ShulkerPacket;

public abstract class ShulkerPacketDifficulty<T> extends ShulkerPacket<T>
{
    public ShulkerPacketDifficulty(T packet)
    {
        super(packet);
    }

    public abstract Difficulty get_difficulty();

    public abstract void set_difficulty(Difficulty difficulty);

    public abstract boolean is_difficulty_locked();

    public abstract void set_difficulty_locked(boolean locked);

    @Override
    public void reset()
    {
        set_difficulty(Difficulty.NORMAL);
        set_difficulty_locked(false);
    }
}
