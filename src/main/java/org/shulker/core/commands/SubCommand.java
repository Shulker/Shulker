/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands;

import org.aperlambda.lambdacommon.utils.Nameable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * SubCommand represents a sub-command in the command hierarchy.
 * <p>
 * A sub-command is defined by:
 * <ul>
 * <li>An usage.</li>
 * <li>A description.</li>
 * <li>Some aliases.</li>
 * <li>An execution method.</li>
 * <li>A tab completion method.</li>
 * </ul>
 * </p>
 */
public interface SubCommand extends Nameable
{
	/**
	 * Gets the usage of the sub-command.
	 *
	 * @return The usage.
	 */
	@NotNull String getUsage();

	/**
	 * Gets the description of the sub-command.
	 *
	 * @return The description.
	 */
	@NotNull String getDescription();

	/**
	 * Gets the aliases of the sub-command. Can be empty but not null.
	 *
	 * @return The aliases.
	 */
	@NotNull List<String> getAliases();

	/**
	 * Represents the execution process of the sub-command.
	 * <p>The {@code args} argument represents the arguments of the sub-command and not the arguments of the parent command.</p>
	 *
	 * @param sender The sender of the sub-command.
	 * @param parent The parent command.
	 * @param label  The label used to call the sub-command.
	 * @param args   The arguments of the sub-command.
	 * @return The result of the execution of the sub-command.
	 */
	@NotNull CommandResult execute(CommandSender sender, Command parent, String label, String[] args);

	List<String> onTabComplete(CommandSender sender, Command parent, String label, String[] args);
}