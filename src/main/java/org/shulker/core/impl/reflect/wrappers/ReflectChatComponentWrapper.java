/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.wrappers;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.shulker.core.wrappers.ChatComponentWrapper;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;

public class ReflectChatComponentWrapper extends ChatComponentWrapper
{
    public static final ReflectChatComponentWrapper INSTANCE = new ReflectChatComponentWrapper();

    private static final Class<?>         WRAPPER_CLASS;
    private static final Optional<Method> FROM_SHULKER_METHOD;
    private static final Optional<Method> TO_SHULKER_METHOD;

    static {
        WRAPPER_CLASS = ShulkerSpigotPlugin.get_nms_class("IChatBaseComponent");
        Objects.requireNonNull(WRAPPER_CLASS, "Cannot initialize ReflectChatComponentWrapper: NMS class cannot be found!");
        final Class<?> SERIALIZER_CLASS = ShulkerSpigotPlugin.get_nms_class("IChatBaseComponent$ChatSerializer");
        Objects.requireNonNull(SERIALIZER_CLASS, "Cannot initialize ReflectChatComponentWrapper: NMS serializer class cannot be found!");
        FROM_SHULKER_METHOD = get_method(SERIALIZER_CLASS, "a", String.class);
        TO_SHULKER_METHOD = get_method(SERIALIZER_CLASS, "a", WRAPPER_CLASS);
    }

    @Override
    public Object from_shulker(BaseComponent... shulker_object)
    {
        if (shulker_object == null)
            return null;
        return FROM_SHULKER_METHOD.map(method -> invoke_method(null, method, ComponentSerializer.toString(shulker_object)))
                .orElse(null);
    }

    @Override
    public BaseComponent[] to_shulker(Object object)
    {
        if (!WRAPPER_CLASS.isInstance(object))
            return null;
        return ComponentSerializer.parse(TO_SHULKER_METHOD.map(method -> (String) invoke_method(null, method, object))
                .orElse("{}"));
    }

    @Override
    public Class<?> get_object_class()
    {
        return ShulkerSpigotPlugin.get_nms_class("IChatBaseComponent");
    }
}
