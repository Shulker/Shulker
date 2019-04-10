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
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayOutChat;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.get_mc;

public class ReflectPacketPlayOutChat extends ShulkerPacketPlayOutChat<Object>
{
    private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
    private static final Optional<Field>                    MESSAGE_FIELD;
    private static final Optional<Field>                    COMPONENTS_FIELD;
    private static final Optional<Field>                    POSITION_FIELD;

    static {
        final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.get_nms_class("PacketPlayOutChat");
        Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketPlayOutChat: NMS class cannot be found!");
        CONSTRUCTOR = get_constructor(PACKET_CLASS);
        MESSAGE_FIELD = get_first_field_of_type(PACKET_CLASS, get_mc().get_wrapper_manager().get_chat_componenet_wrapper().get_object_class());
        COMPONENTS_FIELD = get_first_field_of_type(PACKET_CLASS, BaseComponent[].class);
        POSITION_FIELD = get_first_field_of_type(PACKET_CLASS, get_mc().get_wrapper_manager().get_chat_message_type_wrapper().get_object_class());
    }

    public ReflectPacketPlayOutChat()
    {
        this(new_instance(CONSTRUCTOR.get()));
        set_message();
        set_position(ChatMessageType.SYSTEM);
    }

    public ReflectPacketPlayOutChat(@NotNull BaseComponent... components)
    {
        this();
        set_message(components);
    }

    public ReflectPacketPlayOutChat(Object packet)
    {
        super(packet);
    }

    @Override
    public BaseComponent[] get_message()
    {
        return MESSAGE_FIELD.map(field -> get_mc().get_wrapper_manager().get_chat_componenet_wrapper().to_shulker(get_field_value(packet, field)))
                .orElse(COMPONENTS_FIELD.map(field -> get_field_value(packet, field, new BaseComponent[0])).orElse(new BaseComponent[0]));
    }

    @Override
    public void set_message(BaseComponent... components)
    {
        MESSAGE_FIELD.ifPresentOrElse(field -> set_value(packet, field, get_mc().get_wrapper_manager().get_chat_componenet_wrapper().from_shulker(components)),
                () -> COMPONENTS_FIELD.ifPresent(field -> set_value(packet, field, components)));
    }

    @Override
    public String get_message_raw()
    {
        return ComponentSerializer.toString(get_message());
    }

    @Override
    public void set_message_raw(String raw)
    {
        set_message(ComponentSerializer.parse(raw));
    }

    @Override
    public ChatMessageType get_position()
    {
        return get_mc().get_wrapper_manager().get_chat_message_type_wrapper().to_shulker(POSITION_FIELD.map(field -> get_field_value(packet, field)).get());
    }

    @Override
    public void set_position(ChatMessageType position)
    {
        POSITION_FIELD.ifPresent(field -> set_value(packet, field, get_mc().get_wrapper_manager().get_chat_message_type_wrapper().from_shulker(position)));
    }
}
