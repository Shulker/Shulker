/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v112R1.wrappers;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.shulker.core.Shulker;
import org.shulker.core.wrappers.PlayerWrapper;

public class PlayerWrapperV112R1 implements PlayerWrapper
{
    public static final PlayerWrapperV112R1 INSTANCE = new PlayerWrapperV112R1();

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
