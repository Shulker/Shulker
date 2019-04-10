/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.packets;

import org.aperlambda.lambdacommon.utils.LambdaReflection;
import org.shulker.core.packets.mc.play.ShulkerPacketOutWindowProperty;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;

public class ReflectPacketOutWindowProperty extends ShulkerPacketOutWindowProperty<Object>
{
    private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
    private static final Optional<Field>                    WINDOW_ID_FIELD;
    private static final Optional<Field>                    WINDOW_PROPERTY_FIELD;
    private static final Optional<Field>                    WINDOW_VALUE_FIELD;

    static {
        final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.get_nms_class("PacketPlayOutWindowData");
        Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketOutWindowProperty: NMS class cannot be found!");
        CONSTRUCTOR = get_constructor(PACKET_CLASS);
        WINDOW_ID_FIELD = get_first_field_of_type(PACKET_CLASS, int.class);
        WINDOW_PROPERTY_FIELD = get_field(PACKET_CLASS, "b", true);
        WINDOW_VALUE_FIELD = get_last_field_of_type(PACKET_CLASS, int.class);
    }

    public ReflectPacketOutWindowProperty()
    {
        super(CONSTRUCTOR.map(constructor -> new_instance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketOutWindowProperty: constructor cannot be found!")));
    }

    public ReflectPacketOutWindowProperty(int windowId, short property, short value)
    {
        this();
        set_window_id(windowId);
        set_property(property);
        set_value(value);
    }

    public ReflectPacketOutWindowProperty(Object packet)
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
        WINDOW_ID_FIELD.ifPresentOrElse(field -> LambdaReflection.set_value(packet, field, id), () -> {
            throw new RuntimeException("Cannot set window id (Field not found).");
        });
    }

    @Override
    public short get_property()
    {
        return WINDOW_PROPERTY_FIELD.map(field -> get_field_value(packet, field, (short) 0)).orElse((short) 0);
    }

    @Override
    public void set_property(short property)
    {
        WINDOW_PROPERTY_FIELD.ifPresentOrElse(field -> LambdaReflection.set_value(packet, field, property),
                () -> {
                    throw new RuntimeException("Cannot set property (Field not found).");
                });
    }

    @Override
    public short get_value()
    {
        return WINDOW_VALUE_FIELD.map(field -> get_field_value(packet, field, (short) 0)).orElse((short) 0);
    }

    @Override
    public void set_value(short value)
    {
        WINDOW_VALUE_FIELD.ifPresentOrElse(field -> LambdaReflection.set_value(packet, field, value),
                () -> {
                    throw new RuntimeException("Cannot set value (Field not found).");
                });
    }
}
