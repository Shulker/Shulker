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

public interface SubCommand extends Nameable
{
	@NotNull String getUsage();

	@NotNull String getDescription();

	@NotNull List<String> getAliases();

	@NotNull CommandResult execute(CommandSender sender, Command parent, String label, String[] args);

	List<String> onTabComplete(CommandSender sender, Command parent, String label, String[] args);
}