/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.entity;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.packets.ShulkerPacket;

/**
 * Represents a player.
 */
public interface ShulkerPlayer<T>
{
	/**
	 * Sends a message to the player.
	 *
	 * @param components The component to send.
	 */
	void sendMessage(BaseComponent ... components);

	/**
	 * Sends a packet to the player.
	 *
	 * @param packet The packet to send.
	 */
	default void sendPacket(ShulkerPacket<?> packet)
	{
		sendRawPacket(packet.getHandle());
	}

	/**
	 * Sends a raw packet to the player.
	 *
	 * @param rawPacket The packet to send.
	 */
	void sendRawPacket(Object rawPacket);

	/**
	 * Gets the ping of the player.
	 *
	 * @return The ping of the player.
	 */
	int getPing();

	/**
	 * Gets the locale used by the player.
	 *
	 * @return The locale as a string.
	 */
	String getLocale();

	/**
	 * Gets the player handle.
	 *
	 * @return The handle.
	 */
	@NotNull T getPlayerHandle();
}