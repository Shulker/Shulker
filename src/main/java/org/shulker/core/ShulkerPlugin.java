/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.commands.BukkitCommandManager;
import org.shulker.core.config.ConfigManager;
import org.shulker.core.config.ShulkerConfiguration;
import org.shulker.core.config.ShulkerSymbols;
import org.shulker.core.events.PacketEvent;
import org.shulker.core.events.PacketListener;

import java.io.File;
import java.util.List;

import static org.shulker.core.ShulkerConstants.SHULKER_IG_PREFIX;
import static org.shulker.core.ShulkerConstants.SHULKER_PREFIX;

public interface ShulkerPlugin
{
    /**
     * Gets Shulker's version.
     *
     * @return The version of Shulker.
     */
    String get_version();

    /**
     * Gets the prefix of Shulker.
     *
     * @return Prefix of Shulker.
     */
    default String get_prefix()
    {
        return SHULKER_PREFIX;
    }

    /**
     * Gets the prefix of Shulker displayed in-game.
     *
     * @return The prefix of Shulker.
     */
    default String get_ig_prefix()
    {
        return SHULKER_IG_PREFIX;
    }

    default void log_info(@NotNull String message)
    {
        log_info(null, message);
    }

    void log_info(@Nullable String prefix, @NotNull String message);

    default void log_debug(@NotNull DebugType type, @NotNull String message)
    {
        log_debug(type, null, message);
    }

    void log_debug(@NotNull DebugType type, @Nullable String prefix, @NotNull String message);

    default void log_error(@NotNull String message)
    {
        log_error(null, message);
    }

    void log_error(@Nullable String prefix, @NotNull String message);

    /**
     * Returns whether Shulker has debug mode enabled.
     *
     * @return True if Shulker has debug mode enabled, else false.
     */
    default boolean has_debug() {
        return this.get_configuration().has_debug();
    }

    /**
     * Gets the base directory of the server.
     *
     * @return The base directory.
     */
    File get_base_dir();

    /**
     * Gets the configuration directory.
     *
     * @return The configuration directory.
     */
    default File get_config_dir()
    {
        return new File(get_base_dir(), "configs/");
    }

    /**
     * Gets the plugins directory of the server.
     *
     * @return Plugins directory.
     */
    File get_plugins_dir();

    /**
     * Gets the configuration manager.
     *
     * @return The configuration manager.
     */
    default @NotNull ConfigManager get_configs()
    {
        return ConfigManager.get();
    }

    /**
     * Gets the Shulker's command manager.
     *
     * @return The command manager.
     */
    @NotNull BukkitCommandManager get_commands();

    /**
     * Gets the symbols manager.
     *
     * @return Symbols manager.
     */
    ShulkerSymbols get_symbols_manager();

    /**
     * Gets the configuration of Shulker.
     *
     * @return Shulker's configuration.
     */
    ShulkerConfiguration get_configuration();

    /**
     * Gets the Minecraft manager of Shulker.
     * Greets access to some Minecraft features like NMS, etc...
     *
     * @return Shulker's Minecraft manager.
     */
    MinecraftManager get_mc();

    /**
     * Registers a packet listener.
     *
     * @param listener The packet listener.
     */
    void register_packet_listener(@NotNull PacketListener listener);

    /**
     * Fires the packet event.
     *
     * @param event       The event to fire.
     * @param from_client True if the packet is sent by the client, else false.
     */
    void fire_packet_event(@NotNull PacketEvent event, boolean from_client);

    /**
     * Lists the libraries loaded by Shulker.
     *
     * @return The libraries loaded by Shulker.
     */
    List<ShulkerLibrary> get_libraries();
}
