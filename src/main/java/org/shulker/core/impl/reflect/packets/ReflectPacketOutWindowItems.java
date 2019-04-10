/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.packets;

import org.bukkit.inventory.ItemStack;
import org.shulker.core.packets.mc.play.ShulkerPacketOutWindowItems;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.get_mc;

public class ReflectPacketOutWindowItems extends ShulkerPacketOutWindowItems<Object>
{
    private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
    private static final Optional<Field>                    WINDOW_ID_FIELD;
    private static final Optional<Field>                    WINDOW_ITEMS_FIELD;

    static {
        final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.get_nms_class("PacketPlayOutWindowItems");
        Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketOutWindowItems: NMS class cannot be found!");
        CONSTRUCTOR = get_constructor(PACKET_CLASS);
        WINDOW_ID_FIELD = get_first_field_of_type(PACKET_CLASS, int.class);
        WINDOW_ITEMS_FIELD = get_first_field_of_type(PACKET_CLASS, List.class);
    }

    public ReflectPacketOutWindowItems()
    {
        super(CONSTRUCTOR.map(constructor -> new_instance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketOutWindowItems: constructor cannot be found!")));
    }

    public ReflectPacketOutWindowItems(int windowId, List<ItemStack> items)
    {
        this();
        set_window_id(windowId);
        set_items(items);
    }

    public ReflectPacketOutWindowItems(int windowId, ItemStack... items)
    {
        this();
        set_window_id(windowId);
        set_items(items);
    }

    public ReflectPacketOutWindowItems(Object packet)
    {
        super(packet);
    }

    @Override
    public int get_window_id()
    {
        return WINDOW_ID_FIELD.map(field -> get_field_value(packet, field, 0)).orElse(0);
    }

    @Override
    public void set_window_id(int id)
    {
        WINDOW_ID_FIELD.ifPresentOrElse(field -> set_value(packet, field, id),
                () -> {
                    throw new RuntimeException("Cannot set window id (Field not found).");
                });
    }

    @Override
    public List<ItemStack> get_items()
    {
        return WINDOW_ITEMS_FIELD.map(field -> get_field_value(packet, field, Collections.emptyList())).orElse(Collections.emptyList())
                .stream().map(object -> get_mc().get_wrapper_manager().get_item_stack_wrapper().to_shulker(object)).collect(Collectors.toList());
    }

    @Override
    public void set_items(List<ItemStack> items)
    {
        WINDOW_ITEMS_FIELD.ifPresentOrElse(field -> set_value(packet, field, items.stream().map(item -> get_mc().get_wrapper_manager().get_item_stack_wrapper().from_shulker(item)).collect(Collectors.toList())),
                () -> {
                    throw new RuntimeException("Cannot set window items (Field not found).");
                });
    }
}
