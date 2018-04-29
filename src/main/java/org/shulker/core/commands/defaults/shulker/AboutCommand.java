/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands.defaults.shulker;

import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandContext;
import org.aperlambda.kimiko.CommandResult;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.commands.BukkitCommandExecutor;
import org.shulker.core.commands.BukkitCommandResult;
import org.shulker.core.commands.BukkitCommandTabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AboutCommand implements BukkitCommandExecutor, BukkitCommandTabCompleter
{
	@Override
	public @NotNull CommandResult execute(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
	{
		if(args.length != 0)
			return CommandResult.ERROR_USAGE;

		return BukkitCommandResult.SUCCESS;
	}

	@Override
	public List<String> onTabComplete(CommandContext<CommandSender> context,  @NotNull Command<CommandSender> command, String label, String[] args)
	{
		return new ArrayList<>();
	}
}