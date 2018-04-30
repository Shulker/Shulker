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
import org.mcelytra.chat.ChatMessageType;
import org.mcelytra.chat.ChatVisibility;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;
import org.shulker.core.packets.ShulkerPacket;
import org.shulker.core.packets.mc.play.ShulkerPacketTitle;

/**
 * ShulkerPlayer represents a Minecraft player.
 *
 * @param <T> The server object type.
 */
public interface ShulkerPlayer<T>
{
	/**
	 * Sends a message to the player.
	 * <p>NOTE: The default {@code ChatMessageType} here is {@code SYSTEM}. Please respect the use of the types.</p>
	 *
	 * @param message The message to send.
	 * @see ShulkerPlayer#sendMessage(ChatMessageType, BaseComponent...)
	 */
	default void sendMessage(BaseComponent... message)
	{
		sendMessage(ChatMessageType.SYSTEM, message);
	}

	/**
	 * Sends a message to the player.
	 * <p><b>NOTE:</b> Please use {@link #sendActionBar(BaseComponent...)} for {@link ChatMessageType#ACTION_BAR action bar type}.</p>
	 *
	 * @param type    The type of the message.
	 * @param message The message to send.
	 */
	void sendMessage(ChatMessageType type, BaseComponent... message);

	/**
	 * Sends a message in the action bar to the player.
	 *
	 * @param message The component to send.
	 */
	default void sendActionBar(BaseComponent... message)
	{
		sendPacket(Shulker.getMCManager().newPacketTitle(ShulkerPacketTitle.TitleAction.ACTION_BAR, message));
	}

	/**
	 * Sends a title to the player.
	 *
	 * @param title    The title.
	 * @param subTitle The subtitle.
	 * @param fadeIn   Ticks to spend fading in.
	 * @param stay     Ticks to keep the title displayed.
	 * @param fadeOut  Ticks to spend fading out.
	 */
	default void sendTitle(BaseComponent[] title, BaseComponent[] subTitle, int fadeIn, int stay, int fadeOut)
	{
		// Note: here I don't use the Spigot Title API because there isn't any instance of Player available here.

		// Reset
		resetTitle();
		// Title
		if (title != null)
			sendPacket(Shulker.getMCManager().newPacketTitle(ShulkerPacketTitle.TitleAction.TITLE, title));
		// Subtitle
		if (subTitle != null)
			sendPacket(Shulker.getMCManager().newPacketTitle(ShulkerPacketTitle.TitleAction.SUBTITLE, subTitle));

		sendPacket(Shulker.getMCManager().newPacketTitle(ShulkerPacketTitle.TitleAction.TIMES, fadeIn, stay, fadeOut));
	}

	/**
	 * Resets the title
	 */
	default void resetTitle()
	{
		sendPacket(Shulker.getMCManager().newPacketTitle(ShulkerPacketTitle.TitleAction.RESET));
	}

	/**
	 * Sends a Shulker's packet to the player.
	 *
	 * @param packet The packet to send.
	 */
	default void sendPacket(@NotNull ShulkerPacket<?> packet)
	{
		Shulker.logDebug(Shulker.getPrefix(), "Packet send '" + packet.toString() + "'.");
		sendRawPacket(packet.getHandle());
	}

	/**
	 * Sends a raw packet to the player.
	 *
	 * @param rawPacket The packet to send.
	 */
	void sendRawPacket(Object rawPacket);

	/**
	 * Gets the ping of the player in milliseconds.
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
	 * Gets the chat visibility flag of the player.
	 *
	 * @return The chat visibility flag.
	 */
	ChatVisibility getChatVisibility();

	/**
	 * Gets the player handle.
	 *
	 * @return The handle.
	 */
	@NotNull T getPlayerHandle();
}