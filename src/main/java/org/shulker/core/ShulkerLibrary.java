/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core;

import net.md_5.bungee.api.ChatColor;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static net.md_5.bungee.api.ChatColor.GREEN;
import static net.md_5.bungee.api.ChatColor.RED;

public class ShulkerLibrary implements Nameable
{
	private String           name;
	private File             file;
	private LibraryLoadState loadState;

	public ShulkerLibrary(String name, File file, LibraryLoadState loadState)
	{
		this.name = name;
		this.file = file;
		this.loadState = loadState;
	}

	@Override
	public @NotNull String getName()
	{
		return name;
	}

	public File getFile()
	{
		return file;
	}

	public LibraryLoadState getLoadState()
	{
		return loadState;
	}

	public static enum LibraryLoadState
	{
		SUCCESS("Success", GREEN),
		FAILED("Failed", RED);

		private String toString;
		private ChatColor prefixColor;

		LibraryLoadState(String str, ChatColor prefixColor)
		{
			toString = str;
			this.prefixColor = prefixColor;
		}

		@Override
		public String toString()
		{
			return toString;
		}

		public ChatColor getPrefixColor()
		{
			return prefixColor;
		}
	}
}