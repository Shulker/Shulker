/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R2.wrappers;

import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.shulker.core.wrappers.ItemStackWrapper;

public class ItemStackWrapperV113R2 extends ItemStackWrapper
{
    public static final ItemStackWrapperV113R2 INSTANCE = new ItemStackWrapperV113R2();

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
        return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_13_R2.ItemStack) object);
    }

    @Override
    public Class<?> get_object_class()
    {
        return net.minecraft.server.v1_13_R2.ItemStack.class;
    }
}
