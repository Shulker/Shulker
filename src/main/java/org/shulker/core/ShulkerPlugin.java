/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
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
	String getVersion();

	/**
	 * Gets the prefix of Shulker.
	 *
	 * @return Prefix of Shulker.
	 */
	default String getPrefix()
	{
		return SHULKER_PREFIX;
	}

	/**
	 * Gets the prefix of Shulker displayed in-game.
	 *
	 * @return The prefix of Shulker.
	 */
	default String getIGPrefix()
	{
		return SHULKER_IG_PREFIX;
	}

	default void logInfo(@NotNull String message)
	{
		logInfo(null, message);
	}

	void logInfo(@Nullable String prefix, @NotNull String message);

	default void logDebug(@NotNull DebugType type, @NotNull String message)
	{
		logDebug(type, null, message);
	}

	void logDebug(@NotNull DebugType type, @Nullable String prefix, @NotNull String message);

	default void logError(@NotNull String message)
	{
		logError(null, message);
	}

	void logError(@Nullable String prefix, @NotNull String message);

	/**
	 * Gets the base directory of the server.
	 *
	 * @return The base directory.
	 */
	File getBaseDirectory();

	/**
	 * Gets the configuration directory.
	 *
	 * @return The configuration directory.
	 */
	default File getConfigurationDirectory()
	{
		return new File(getBaseDirectory(), "configs/");
	}

	/**
	 * Gets the plugins directory of the server.
	 *
	 * @return Plugins directory.
	 */
	File getPluginsDirectory();

	/**
	 * Gets the configuration manager.
	 *
	 * @return The configuration manager.
	 */
	default @NotNull ConfigManager getConfigManager()
	{
		return ConfigManager.getConfigManager();
	}

	/**
	 * Gets the Shulker's command manager.
	 *
	 * @return The command manager.
	 */
	@NotNull BukkitCommandManager getCommandManager();

	/**
	 * Gets the symbols manager.
	 *
	 * @return Symbols manager.
	 */
	ShulkerSymbols getSymbolsManager();

	/**
	 * Gets the configuration of Shulker.
	 *
	 * @return Shulker's configuration.
	 */
	ShulkerConfiguration getConfiguration();

	/**
	 * Gets the Minecraft manager of Shulker.
	 * Greets access to some Minecraft features like NMS, etc...
	 *
	 * @return Shulker's Minecraft manager.
	 */
	MinecraftManager getMinecraftManager();

	/**
	 * Registers a packet listener.
	 *
	 * @param listener The packet listener.
	 */
	void registerPacketListener(@NotNull PacketListener listener);

	/**
	 * Fires the packet event.
	 * @param event The event to fire.
	 * @param fromClient True if the packet is sent by the client, else false.
	 */
	void firePacketEvent(@NotNull PacketEvent event, boolean fromClient);

	/**
	 * Lists the libraries loaded by Shulker.
	 *
	 * @return The libraries loaded by Shulker.
	 */
	List<ShulkerLibrary> getLibraries();
}