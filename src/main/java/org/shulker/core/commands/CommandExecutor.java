/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CommandExecutor
{
	/**
	 * Represents the execution process of the command.
	 * <p>The {@code args} argument represents the arguments of the command and not the arguments of the parent command.</p>
	 *
	 * @param sender  The sender of the command.
	 * @param parent  The parent command.
	 * @param command The command which is executed.
	 * @param label   The label used to call the command.
	 * @param args    The arguments of the command.
	 * @return The result of the execution of the command.
	 */
	@NotNull CommandResult execute(CommandSender sender, @Nullable Command parent, @NotNull Command command, String label, String[] args);

	List<String> onTabComplete(CommandSender sender, @Nullable Command parent, @NotNull Command command, String label, String[] args);
}