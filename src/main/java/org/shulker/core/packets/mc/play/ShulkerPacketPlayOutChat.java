/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.mc.play;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.shulker.core.packets.ShulkerPacket;

import java.util.Arrays;

/**
 * ShulkerPacketPlayOutChat represents the packet which send a message to the player.
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketPlayOutChat<T> extends ShulkerPacket<T>
{
	public ShulkerPacketPlayOutChat()
	{
		super();
	}

	public ShulkerPacketPlayOutChat(@NotNull BaseComponent... components)
	{
		this();
	}

	public ShulkerPacketPlayOutChat(T packet)
	{
		super(packet);
	}

	/**
	 * Gets the message.
	 *
	 * @return The message as a component.
	 */
	public abstract BaseComponent[] getMessage();

	/**
	 * Sets the message.
	 *
	 * @param components The component of the message.
	 */
	public abstract void setMessage(BaseComponent... components);

	/**
	 * Gets the message in the raw JSON format.
	 *
	 * @return The message in JSON.
	 */
	public abstract String getMessageRaw();

	/**
	 * Sets the message in the raw format.
	 * <p>
	 * Please use JSON for the 0 and 1 position. Use the deprecated §-based format for the 2 position or use {@link ShulkerPacketTitle Title packet}
	 * instead.
	 *
	 * @param raw The raw message.
	 */
	public abstract void setMessageRaw(String raw);

	/**
	 * Gets the position of the message to display.
	 * <ul>
	 * <li>0: Chat (Chat box)</li>
	 * <li>1: System message (Chat box)</li>
	 * <li>2: Game info (Above hotbar).</li>
	 * </ul>
	 *
	 * @return The position of the message.
	 */
	public abstract ChatMessageType getPosition();

	/**
	 * Sets the position of the message to display.
	 * <ul>
	 * <li>0: Chat (Chat box)</li>
	 * <li>1: System message (Chat box)</li>
	 * <li>2: Game info (Above hotbar).</li>
	 * </ul>
	 * Warning: If you attempt to display a message in Game info, please not that it doesn't accept JSON (And all Component),
	 * although the deprecated §-based formatting works. Please use Title packet instead.
	 *
	 * @param position The position of the message.
	 */
	public abstract void setPosition(ChatMessageType position);

	@Override
	public void reset()
	{
		setMessage();
		setPosition(null);
	}

	@Override
	public String toString()
	{
		return "ShulkerPacketPlayOutChat{message:" + Arrays.toString(getMessage()) + ",position:" + getPosition() + "}";
	}
}