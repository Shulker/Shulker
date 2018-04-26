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
import org.shulker.core.packets.ShulkerPacket;

/**
 * ShulkerPacketPlayerListHeaderFooter represents the packet which modifies the header and the footer of the player list.
 * This packet may be used by custom servers to display additional information above/below the player list. It is never sent by the Notchian server.
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketPlayerListHeaderFooter<T> extends ShulkerPacket<T>
{
	public ShulkerPacketPlayerListHeaderFooter()
	{
		super();
	}

	public ShulkerPacketPlayerListHeaderFooter(@NotNull BaseComponent[] header, @NotNull BaseComponent[] footer)
	{
		this();
	}

	public ShulkerPacketPlayerListHeaderFooter(T packet)
	{
		super(packet);
	}

	/**
	 * Gets the header of the player list.
	 *
	 * @return Player list's header.
	 */
	public abstract BaseComponent[] getHeader();

	/**
	 * Sets the header of the player list.
	 * To remove the header, send a empty translatable component.
	 *
	 * @param header Player list's header.
	 */
	public abstract void setHeader(BaseComponent... header);

	/**
	 * Gets the footer of the player list.
	 *
	 * @return Player list's footer.
	 */
	public abstract BaseComponent[] getFooter();

	/**
	 * Sets the footer of the player list.
	 * To remove the footer, send a empty translatable component.
	 *
	 * @param footer Player list's footer.
	 */
	public abstract void setFooter(BaseComponent... footer);

	@Override
	public void reset()
	{
		setHeader();
		setFooter();
	}
}