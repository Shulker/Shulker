/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.wrappers;

import org.aperlambda.lambdacommon.utils.LambdaReflection;
import org.bukkit.entity.Player;
import org.shulker.core.Shulker;
import org.shulker.core.wrappers.PlayerWrapper;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;

public class ReflectPlayerWrapper implements PlayerWrapper
{
    public static final ReflectPlayerWrapper INSTANCE = new ReflectPlayerWrapper();

    private static final Class<?>         WRAPPER_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.get_nms_class("EntityPlayer"), "Cannot initialize PlayerWrapper without the targeted class.");
    private static final Optional<Method> FROM_SHULKER;
    private static final Optional<Method> TO_SHULKER;

    static {
        final Class<?> CRAFTPLAYER_CLASS = Objects.requireNonNull(LambdaReflection.get_class(
                "org.bukkit.craftbukkit." + ShulkerSpigotPlugin.get_server_version() +
                        ".entity.CraftPlayer").orElse(null), "Cannot initialize PlayerWrapper without CraftPlayer class.");
        FROM_SHULKER = get_method(CRAFTPLAYER_CLASS, "getHandle", true);
        TO_SHULKER = get_method(WRAPPER_CLASS, "getBukkitEntity", true);
    }

    private ReflectPlayerWrapper()
    {
    }

    @Override
    public Object from_shulker(Player shulker_object)
    {
        return FROM_SHULKER.map(method -> invoke_method(shulker_object, method)).orElse(null);
    }

    @Override
    public Player to_shulker(Object nms)
    {
        return TO_SHULKER.map(method -> (Player) invoke_method(nms, method)).orElse(null);
    }

    @Override
    public Class<?> get_object_class()
    {
        return WRAPPER_CLASS;
    }
}
