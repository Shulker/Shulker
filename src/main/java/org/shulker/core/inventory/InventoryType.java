/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.inventory;

import org.aperlambda.lambdacommon.resources.ResourceName;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the different types of inventory in Minecraft.
 */
public enum InventoryType
{
    /**
     * Fallback for unknown types; also used for Ender chests. Behaves the same as {@link #CHEST minecraft:chest}.
     */
    CONTAINER(new ResourceName("minecraft", "container")),
    CHEST(new ResourceName("minecraft", "chest")),
    CRAFTING_TABLE(new ResourceName("minecraft", "crafting_table")),
    FURNACE(new ResourceName("minecraft", "furnace")),
    DISPENSER(new ResourceName("minecraft:dispenser")),
    ENCHANTING_TABLE(new ResourceName("minecraft:enchanting_table")),
    BREWING_STAND(new ResourceName("minecraft:brewing_stand")),
    VILLAGER(new ResourceName("minecraft", "villager")),
    BEACON(new ResourceName("minecraft", "beacon")),
    ANVIL(new ResourceName("minecraft", "anvil")),
    HOPPER(new ResourceName("minecraft", "hopper")),
    DROPPER(new ResourceName("minecraft", "dropper")),
    SHULKER_BOX(new ResourceName("minecraft", "shulker_box")),
    /**
     * Horse, donkey, mule or Llama.
     */
    ENTITYHORSE(new ResourceName("minecraft", "EntityHorse"));

    private ResourceName id;

    InventoryType(ResourceName id)
    {
        this.id = id;
    }

    /**
     * Gets the id of the inventory type.
     *
     * @return The id of the inventory type.
     */
    public @NotNull ResourceName getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        if (id.get_name().equals("EntityHorse"))
            return "EntityHorse";
        return id.toString();
    }
}
