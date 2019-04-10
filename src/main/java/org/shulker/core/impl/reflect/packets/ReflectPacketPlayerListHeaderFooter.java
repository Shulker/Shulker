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
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayerListHeaderFooter;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.get_mc;

public class ReflectPacketPlayerListHeaderFooter extends ShulkerPacketPlayerListHeaderFooter<Object>
{
    private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
    private static final Optional<Field>                    HEADER_FIELD;
    private static final Optional<Field>                    FOOTER_FIELD;

    static {
        final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.get_nms_class("PacketPlayOutPlayerListHeaderFooter");
        Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketPlayerListHeaderFooter: NMS class cannot be found!");
        CONSTRUCTOR = get_constructor(PACKET_CLASS);
        HEADER_FIELD = get_first_field_of_type(PACKET_CLASS, get_mc().get_wrapper_manager().get_chat_componenet_wrapper().get_object_class());
        FOOTER_FIELD = get_last_field_of_type(PACKET_CLASS, get_mc().get_wrapper_manager().get_chat_componenet_wrapper().get_object_class());
    }

    public ReflectPacketPlayerListHeaderFooter()
    {
        this(new BaseComponent[0], new BaseComponent[0]);
    }

    public ReflectPacketPlayerListHeaderFooter(@NotNull BaseComponent[] header, @NotNull BaseComponent[] footer)
    {
        this(new_instance(CONSTRUCTOR.get()));
        set_header(header);
        set_footer(footer);
    }

    public ReflectPacketPlayerListHeaderFooter(Object packet)
    {
        super(packet);
    }

    @Override
    public BaseComponent[] get_header()
    {
        return HEADER_FIELD.map(field -> get_field_value(packet, field))
                .map(o -> Shulker.get_mc().get_wrapper_manager().get_chat_componenet_wrapper().to_shulker(o))
                .orElse(new BaseComponent[0]);
    }

    @Override
    public void set_header(BaseComponent... header)
    {
        HEADER_FIELD.ifPresent(field -> set_value(packet, field, Shulker.get_mc().get_wrapper_manager().get_chat_componenet_wrapper().from_shulker(header)));
    }

    @Override
    public BaseComponent[] get_footer()
    {
        return FOOTER_FIELD.map(field -> get_field_value(packet, field))
                .map(o -> Shulker.get_mc().get_wrapper_manager().get_chat_componenet_wrapper().to_shulker(o))
                .orElse(new BaseComponent[0]);
    }

    @Override
    public void set_footer(BaseComponent... footer)
    {
        FOOTER_FIELD.ifPresent(field -> set_value(packet, field, Shulker.get_mc().get_wrapper_manager().get_chat_componenet_wrapper().from_shulker(footer)));
    }
}
