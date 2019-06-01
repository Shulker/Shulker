/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R2.packets;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayOutChat;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.get_mc;

public class ShulkerPacketPlayOutChatV113R2 extends ShulkerPacketPlayOutChat<PacketPlayOutChat>
{
    private static final Optional<Field> mc_component_field = get_field(PacketPlayOutChat.class, "a", true);
    private static final Optional<Field> position_field     = get_last_field_of_type(PacketPlayOutChat.class, net.minecraft.server.v1_13_R2.ChatMessageType.class);

    public ShulkerPacketPlayOutChatV113R2()
    {
        this(new PacketPlayOutChat());
        mc_component_field.ifPresentOrElse(field -> set_value(packet, field, new ChatComponentText("")), () -> packet.components = new BaseComponent[0]);
        position_field.ifPresent(field -> set_value(packet, field, net.minecraft.server.v1_13_R2.ChatMessageType.SYSTEM));
    }

    public ShulkerPacketPlayOutChatV113R2(@NotNull BaseComponent... components)
    {
        this(new PacketPlayOutChat((IChatBaseComponent) get_mc().get_wrapper_manager().get_chat_componenet_wrapper().from_shulker(components)));
    }

    public ShulkerPacketPlayOutChatV113R2(PacketPlayOutChat packet)
    {
        super(packet);
    }

    @Override
    public BaseComponent[] get_message()
    {
        return mc_component_field.map(field -> get_mc().get_wrapper_manager().get_chat_componenet_wrapper().to_shulker(get_field_value(packet, field))).orElse(packet.components);
    }

    @Override
    public void set_message(BaseComponent... components)
    {
        mc_component_field.ifPresentOrElse(field -> set_value(packet, field, get_mc().get_wrapper_manager().get_chat_componenet_wrapper().from_shulker(components)),
                () -> packet.components = components);
    }

    @Override
    public String get_message_raw()
    {
        return mc_component_field.map(field -> IChatBaseComponent.ChatSerializer.a((IChatBaseComponent) get_field_value(packet, field))).orElse(ComponentSerializer.toString(packet.components));
    }

    @Override
    public void set_message_raw(String raw)
    {
        mc_component_field.ifPresentOrElse(field -> set_value(packet, field, IChatBaseComponent.ChatSerializer.a(raw)),
                () -> packet.components = ComponentSerializer.parse(raw));
    }

    @Override
    public ChatMessageType get_position()
    {
        return get_mc().get_wrapper_manager().get_chat_message_type_wrapper().to_shulker(get_field_value(packet, position_field.get()));
    }

    @Override
    public void set_position(ChatMessageType position)
    {
        set_value(packet, position_field.get(), get_mc().get_wrapper_manager().get_chat_message_type_wrapper().from_shulker(position));
    }
}
