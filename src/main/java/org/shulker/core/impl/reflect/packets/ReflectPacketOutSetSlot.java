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
import org.shulker.core.packets.mc.play.ShulkerPacketOutSetSlot;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.get_mc;

public class ReflectPacketOutSetSlot extends ShulkerPacketOutSetSlot<Object>
{
    private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
    private static final Optional<Field>                    WINDOW_ID_FIELD;
    private static final Optional<Field>                    WINDOW_SLOT_FIELD;
    private static final Optional<Field>                    WINDOW_ITEM_FIELD;

    static {
        final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.get_nms_class("PacketPlayOutSetSlot");
        Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketOutSetSlot: NMS class cannot be found!");
        CONSTRUCTOR = get_constructor(PACKET_CLASS);
        WINDOW_ID_FIELD = get_first_field_of_type(PACKET_CLASS, int.class);
        WINDOW_SLOT_FIELD = get_last_field_of_type(PACKET_CLASS, int.class);
        WINDOW_ITEM_FIELD = get_first_field_of_type(PACKET_CLASS, get_mc().get_wrapper_manager().get_item_stack_wrapper().get_object_class());
    }

    public ReflectPacketOutSetSlot()
    {
        super(CONSTRUCTOR.map(constructor -> new_instance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketOutSetSlot: constructor cannot be found!")));
    }

    public ReflectPacketOutSetSlot(int windowId, int slot, ItemStack item)
    {
        this();
        set_window_id(windowId);
        set_slot(slot);
        set_item(item);
    }

    public ReflectPacketOutSetSlot(Object packet)
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
        WINDOW_ID_FIELD.ifPresentOrElse(field -> set_value(packet, field, id), () -> {
            throw new RuntimeException("Cannot set window id (Field not found).");
        });
    }

    @Override
    public int get_slot()
    {
        return WINDOW_SLOT_FIELD.map(field -> get_field_value(packet, field, 0)).orElse(0);
    }

    @Override
    public void set_slot(int slot)
    {
        WINDOW_SLOT_FIELD.ifPresentOrElse(field -> set_value(packet, field, slot),
                () -> {
                    throw new RuntimeException("Cannot set slot (Field not found).");
                });
    }

    @Override
    public ItemStack get_item()
    {
        return get_mc().get_wrapper_manager().get_item_stack_wrapper().to_shulker(WINDOW_ITEM_FIELD.map(field -> get_field_value(packet, field)).orElse(null));
    }

    @Override
    public void set_item(ItemStack item)
    {
        WINDOW_ITEM_FIELD.ifPresentOrElse(field -> set_value(packet, field, get_mc().get_wrapper_manager().get_item_stack_wrapper().from_shulker(item)),
                () -> {
                    throw new RuntimeException("Cannot set item (Field not found).");
                });
    }
}
