/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core;

import org.aperlambda.lambdacommon.system.LambdaSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.commands.BukkitCommandManager;
import org.shulker.core.config.ConfigManager;
import org.shulker.core.config.ShulkerConfiguration;
import org.shulker.core.config.ShulkerSymbols;
import org.shulker.core.events.PacketListener;

import java.io.File;

import static org.fusesource.jansi.Ansi.*;

public class Shulker
{
    private static ShulkerPlugin SHULKER;

    public static void init(@NotNull ShulkerPlugin plugin)
    {
        if (SHULKER != null)
            throw new RuntimeException("Shulker is already initialized!");
        SHULKER = plugin;

        log_info(get_prefix(), "Loading Shulker...");
        log_info(get_prefix(), ansi().a("Running on: ").fg(Color.CYAN).a(LambdaSystem.get_os().get_name()).fg(Color.WHITE).a(" arch ").fg(Color.CYAN).a(System.getProperty("os.arch")).reset().toString());
        log_info(get_prefix(), ansi().a("Java version: ").fg(Color.CYAN).a(System.getProperty("java.version")).reset().toString());
    }

    /**
     * Gets the Shulker implementation.
     *
     * @return Shulker's implementation.
     */
    public static ShulkerPlugin get()
    {
        return SHULKER;
    }

    /**
     * Gets Shulker's version.
     *
     * @return The version of Shulker.
     */
    public static String get_version()
    {
        return SHULKER.get_version();
    }

    /**
     * Gets the prefix of Shulker.
     *
     * @return Prefix of Shulker.
     */
    public static String get_prefix()
    {
        return SHULKER.get_prefix();
    }

    /**
     * Gets the prefix of Shulker displayed in-game.
     *
     * @return The prefix of Shulker.
     */
    public static String get_prefix_ig()
    {
        return SHULKER.get_ig_prefix();
    }

    public static void log_info(@NotNull String message)
    {
        SHULKER.log_info(message);
    }

    public static void log_info(@Nullable String prefix, @NotNull String message)
    {
        SHULKER.log_info(prefix, message);
    }

    public static void log_debug(@NotNull DebugType type, @NotNull String message)
    {
        SHULKER.log_debug(type, message);
    }

    public static void log_debug(@NotNull DebugType type, @Nullable String prefix, @NotNull String message)
    {
        SHULKER.log_debug(type, prefix, message);
    }

    public static void log_error(@NotNull String message)
    {
        SHULKER.log_error(message);
    }

    public static void log_error(@Nullable String prefix, @NotNull String message)
    {
        SHULKER.log_error(prefix, message);
    }

    /**
     * Gets the base directory of the server.
     *
     * @return The base directory.
     */
    public static File get_base_dir()
    {
        return SHULKER.get_base_dir();
    }

    public static File get_configuration_dir()
    {
        return SHULKER.get_config_dir();
    }

    /**
     * Gets the plugins directory of the server.
     *
     * @return Plugins directory.
     */
    public static File get_plugins_dir()
    {
        return SHULKER.get_plugins_dir();
    }

    /**
     * Gets the configuration manager.
     *
     * @return The configuration manager.
     */
    public static @NotNull ConfigManager get_configs()
    {
        return SHULKER.get_configs();
    }

    /**
     * Gets the Shulker's command manager.
     *
     * @return The command manager.
     */
    public static @NotNull BukkitCommandManager get_commands()
    {
        return SHULKER.get_commands();
    }

    /**
     * Gets the symbols manager.
     *
     * @return Symbols manager.
     */
    public static ShulkerSymbols get_symbols_manager()
    {
        return SHULKER.get_symbols_manager();
    }

    /**
     * Gets the configuration of Shulker.
     *
     * @return Shulker's configuration.
     */
    public static ShulkerConfiguration get_configuration()
    {
        return SHULKER.get_configuration();
    }

    /**
     * Gets the Minecraft manager of Shulker.
     * Greets access to some Minecraft features like NMS, etc...
     *
     * @return Shulker's Minecraft manager.
     */
    public static MinecraftManager get_mc()
    {
        return SHULKER.get_mc();
    }

    /**
     * Registers a packet listener.
     *
     * @param listener The packet listener.
     */
    public static void register_packet_listener(@NotNull PacketListener listener)
    {
        SHULKER.register_packet_listener(listener);
    }
}
