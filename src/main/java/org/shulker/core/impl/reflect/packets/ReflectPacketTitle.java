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
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;
import org.shulker.core.packets.mc.play.ShulkerPacketTitle;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.get_mc;

public class ReflectPacketTitle extends ShulkerPacketTitle<Object>
{
    private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
    private static final Optional<Field>                    ACTION_FIELD;
    private static final Optional<Field>                    CHAT_FIELD;
    private static final Optional<Field>                    FADEIN_FIELD;
    private static final Optional<Field>                    STAY_FIELD;
    private static final Optional<Field>                    FADEOUT_FIELD;

    static {
        final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.get_nms_class("PacketPlayOutTitle");
        Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketTitle: NMS class cannot be found!");
        CONSTRUCTOR = get_constructor(PACKET_CLASS);
        ACTION_FIELD = get_first_field_of_type(PACKET_CLASS, get_mc().get_wrapper_manager().get_title_action_wrapper().get_object_class());
        CHAT_FIELD = get_first_field_of_type(PACKET_CLASS, get_mc().get_wrapper_manager().get_chat_componenet_wrapper().get_object_class());
        FADEIN_FIELD = get_first_field_of_type(PACKET_CLASS, int.class);
        STAY_FIELD = get_field(PACKET_CLASS, "d", true);
        FADEOUT_FIELD = get_last_field_of_type(PACKET_CLASS, int.class);
    }

    public ReflectPacketTitle()
    {
        super(new_instance(CONSTRUCTOR.get()));
    }

    public ReflectPacketTitle(@NotNull TitleAction action)
    {
        this();
        set_action(action);
    }

    public ReflectPacketTitle(@NotNull TitleAction action, @NotNull BaseComponent[] chatValue)
    {
        this(action);
        set_chat_component_value(chatValue);
    }

    public ReflectPacketTitle(@NotNull TitleAction action, int fadeIn, int stay, int fadeOut)
    {
        this(action);
        set_times(fadeIn, stay, fadeOut);
    }

    public ReflectPacketTitle(Object packet)
    {
        super(packet);
    }

    @Override
    public TitleAction get_action()
    {
        return ACTION_FIELD.map(field -> get_field_value(packet, field))
                .map(o -> Shulker.get_mc().get_wrapper_manager().get_title_action_wrapper().to_shulker(o))
                .orElse(TitleAction.RESET);
    }

    @Override
    public void set_action(TitleAction action)
    {
        ACTION_FIELD.ifPresent(field -> set_value(packet, field, get_mc().get_wrapper_manager().get_title_action_wrapper().from_shulker(action)));
    }

    @Override
    public @Nullable BaseComponent[] get_chat_component_value()
    {
        return CHAT_FIELD.map(field -> get_field_value(packet, field))
                .map(o -> Shulker.get_mc().get_wrapper_manager().get_chat_componenet_wrapper().to_shulker(o))
                .orElse(new BaseComponent[0]);
    }

    @Override
    public void set_chat_component_value(@Nullable BaseComponent... components)
    {
        CHAT_FIELD.ifPresent(field -> set_value(packet, field, get_mc().get_wrapper_manager().get_chat_componenet_wrapper().from_shulker(components)));
    }

    @Override
    public int get_fade_in()
    {
        return FADEIN_FIELD.map(field -> get_field_value(packet, field, 0)).orElse(0);
    }

    @Override
    public void set_fade_in(int fade_in)
    {
        FADEIN_FIELD.ifPresent(field -> set_value(packet, field, fade_in));
    }

    @Override
    public int get_stay()
    {
        return STAY_FIELD.map(field -> get_field_value(packet, field, 0)).orElse(0);
    }

    @Override
    public void set_stay(int stay)
    {
        STAY_FIELD.ifPresent(field -> set_value(packet, field, stay));
    }

    @Override
    public int get_fade_out()
    {
        return FADEOUT_FIELD.map(field -> get_field_value(packet, field, 0)).orElse(0);
    }

    @Override
    public void set_fade_out(int fade_out)
    {
        FADEOUT_FIELD.ifPresent(field -> set_value(packet, field, fade_out));
    }
}
