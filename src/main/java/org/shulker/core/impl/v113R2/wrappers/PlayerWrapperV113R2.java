/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R2.wrappers;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.shulker.core.wrappers.PlayerWrapper;

public class PlayerWrapperV113R2 implements PlayerWrapper
{
    public static final PlayerWrapperV113R2 INSTANCE = new PlayerWrapperV113R2();

    @Override
    public Object from_shulker(Player shulker_object)
    {
        return ((CraftPlayer) shulker_object).getHandle();
    }

    @Override
    public Player to_shulker(Object object)
    {
        return ((EntityPlayer) object).getBukkitEntity();
    }

    @Override
    public Class<?> get_object_class()
    {
        return EntityPlayer.class;
    }
}
