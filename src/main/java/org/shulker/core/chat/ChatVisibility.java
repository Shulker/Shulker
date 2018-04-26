/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.chat;

import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;

/**
 * Lists all the chat visibility flags available.
 *
 * @author lambdaurora
 * @version 1.0.0
 * @since 1.0.0
 */
public enum ChatVisibility implements Nameable
{
	FULL(0, "Full", "FULL"),
	SYSTEM(1, "System", "SYSTEM"),
	HIDDEN(2, "Hidden", "HIDDEN");

	private int    id;
	private String name;
	private String nmsEquivalent;

	ChatVisibility(int id, String name, String nmsEquivalent)
	{
		this.id = id;
		this.name = name;
		this.nmsEquivalent = nmsEquivalent;
	}

	/**
	 * Gets the id of the chat visibility flag.
	 *
	 * @return The id of the flag.
	 */
	public int getId()
	{
		return id;
	}

	@Override
	public @NotNull String getName()
	{
		return name;
	}

	public String getNmsEquivalent()
	{
		return nmsEquivalent;
	}
}