/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v112R1.wrappers;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.shulker.core.wrappers.ItemStackWrapper;

public class ItemStackWrapperV112R1 extends ItemStackWrapper
{
    public static final ItemStackWrapperV112R1 INSTANCE = new ItemStackWrapperV112R1();

    @Override
    public Object from_shulker(ItemStack shulker_object)
    {
        return CraftItemStack.asNMSCopy(shulker_object);
    }

    @Override
    public ItemStack to_shulker(Object object)
    {
        if (object == null)
            return null;
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) object);
    }

    @Override
    public Class<?> get_object_class()
    {
        return net.minecraft.server.v1_12_R1.ItemStack.class;
    }
}
