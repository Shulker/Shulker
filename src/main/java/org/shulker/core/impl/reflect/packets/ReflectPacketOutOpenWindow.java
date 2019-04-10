/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.packets;

import net.md_5.bungee.api.chat.BaseComponent;
import org.shulker.core.Shulker;
import org.shulker.core.packets.mc.play.ShulkerPacketOutOpenWindow;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.get_mc;

public class ReflectPacketOutOpenWindow extends ShulkerPacketOutOpenWindow<Object>
{
    private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
    private static final Optional<Field>                    WINDOW_ID_FIELD;
    private static final Optional<Field>                    WINDOW_TYPE_FIELD;
    private static final Optional<Field>                    TITLE_FIELD;
    private static final Optional<Field>                    SLOTS_FIELD;
    private static final Optional<Field>                    ENTITY_ID_FIELD;

    static {
        final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.get_nms_class("PacketPlayOutOpenWindow");
        Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketOutOpenWindow: NMS class cannot be found!");
        CONSTRUCTOR = get_constructor(PACKET_CLASS);
        WINDOW_ID_FIELD = get_first_field_of_type(PACKET_CLASS, int.class);
        WINDOW_TYPE_FIELD = get_first_field_of_type(PACKET_CLASS, String.class);
        TITLE_FIELD = get_first_field_of_type(PACKET_CLASS, get_mc().get_wrapper_manager().get_chat_componenet_wrapper().get_object_class());
        SLOTS_FIELD = get_field(PACKET_CLASS, "d", true);
        ENTITY_ID_FIELD = get_last_field_of_type(PACKET_CLASS, int.class);
    }

    public ReflectPacketOutOpenWindow()
    {
        this(CONSTRUCTOR.map(constructor -> new_instance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketOutOpenWindow: constructor cannot be found!")));
    }

    public ReflectPacketOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots)
    {
        this();
        set_window_id(windowId);
        set_window_type(windowType);
        set_title(title);
        set_slots(slots);
    }

    public ReflectPacketOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots, int entityId)
    {
        this(windowId, windowType, title, slots);
        set_entity_id(entityId);
    }

    public ReflectPacketOutOpenWindow(Object packet)
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
    public String get_window_type()
    {
        return WINDOW_TYPE_FIELD.map(field -> get_field_value(packet, field, "")).orElse("");
    }

    @Override
    public void set_window_type(String window_type)
    {
        WINDOW_TYPE_FIELD.ifPresentOrElse(field -> set_value(packet, field, window_type),
                () -> {
                    throw new RuntimeException("Cannot set window type (Field not found).");
                });
    }

    @Override
    public BaseComponent[] get_title()
    {
        return TITLE_FIELD.map(field -> get_field_value(packet, field))
                .map(o -> Shulker.get_mc().get_wrapper_manager().get_chat_componenet_wrapper().to_shulker(o))
                .orElse(new BaseComponent[0]);
    }

    @Override
    public void set_title(BaseComponent... title)
    {
        TITLE_FIELD.ifPresentOrElse(field -> set_value(packet, field, Shulker.get_mc().get_wrapper_manager().get_chat_componenet_wrapper().from_shulker(title)),
                () -> {
                    throw new RuntimeException("Cannot set window type (Field not found).");
                });
    }

    @Override
    public int get_slots()
    {
        return SLOTS_FIELD.map(field -> get_field_value(packet, field, 0)).orElse(0);
    }

    @Override
    public void set_slots(int slots)
    {
        SLOTS_FIELD.ifPresentOrElse(field -> set_value(packet, field, slots),
                () -> {
                    throw new RuntimeException("Cannot set window slots (Field not found).");
                });
    }

    @Override
    public int get_entity_id()
    {
        return ENTITY_ID_FIELD.map(field -> get_field_value(packet, field, 0)).orElse(0);
    }

    @Override
    public void set_entity_id(int entityId)
    {
        ENTITY_ID_FIELD.ifPresentOrElse(field -> set_value(packet, field, entityId), () -> {
            throw new RuntimeException("Cannot set window entity id (Field not found).");
        });
    }
}
