/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.packets;

import org.mcelytra.core.ServerPing;
import org.shulker.core.packets.mc.status.ShulkerPacketStatusOutServerInfo;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.get_mc;

public class ReflectPacketStatusOutServerInfo extends ShulkerPacketStatusOutServerInfo<Object>
{
    private static final Class<?>                           PACKET_CLASS;
    private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
    private static final Optional<Field>                    SERVERPING_FIELD;

    static {
        PACKET_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.get_nms_class("PacketStatusOutServerInfo"), "Cannot initialize ReflectPacketStatusOutServerInfo: NMS class cannot be found!");
        CONSTRUCTOR = get_constructor(PACKET_CLASS);
        SERVERPING_FIELD = get_first_field_of_type(PACKET_CLASS, get_mc().get_wrapper_manager().get_server_ping_wrapper().get_object_class());
    }

    public ReflectPacketStatusOutServerInfo()
    {
        this(CONSTRUCTOR.map(constructor -> new_instance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketStatusOutServerInfo: constructor cannot be found!")));
    }

    public ReflectPacketStatusOutServerInfo(ServerPing serverPing)
    {
        this();
        set_server_ping(serverPing);
    }

    public ReflectPacketStatusOutServerInfo(Object packet)
    {
        super(packet);
    }

    @Override
    public ServerPing get_server_ping()
    {
        return SERVERPING_FIELD.map(field -> get_mc().get_wrapper_manager().get_server_ping_wrapper().to_shulker(get_field_value(packet, field))).orElse(null);
    }

    @Override
    public void set_server_ping(ServerPing serverPing)
    {
        SERVERPING_FIELD.ifPresent(field -> set_value(packet, field, get_mc().get_wrapper_manager().get_server_ping_wrapper().from_shulker(serverPing)));
    }
}
