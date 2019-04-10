/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.wrappers;

import com.google.gson.Gson;
import org.aperlambda.lambdacommon.LambdaConstants;
import org.mcelytra.core.ServerPing;
import org.shulker.core.wrappers.ServerPingWrapper;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;

public class ReflectServerPingWrapper extends ServerPingWrapper
{
    public static final ReflectServerPingWrapper INSTANCE = new ReflectServerPingWrapper();

    private static final Class<?> WRAPPER_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.get_nms_class("ServerPing"), "Cannot initialize ServerPingWrapper without the targeted class.");
    private static final Gson     MC_GSON;

    static {
        final Field FIELD_SERVERPING_GSON = get_first_field_of_type(Objects.requireNonNull(ShulkerSpigotPlugin.get_nms_class("PacketStatusOutServerInfo")), Gson.class).orElseThrow();
        MC_GSON = Objects.requireNonNull(get_field_value(null, FIELD_SERVERPING_GSON, (Gson) null), "Cannot wrap ServerPing without NMS' Gson");
    }

    @Override
    public Object from_shulker(ServerPing shulker_object)
    {
        if (shulker_object == null)
            return null;
        return MC_GSON.fromJson(LambdaConstants.JSON_PARSER.parse(ServerPing.Serializer.to_string(shulker_object)), WRAPPER_CLASS);
    }

    @Override
    public ServerPing to_shulker(Object object)
    {
        if (object == null)
            return null;
        return ServerPing.Serializer.parse(MC_GSON.toJson(object));
    }

    @Override
    public Class<?> get_object_class()
    {
        return WRAPPER_CLASS;
    }
}
