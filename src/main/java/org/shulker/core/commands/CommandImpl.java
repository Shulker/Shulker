/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands;

import net.md_5.bungee.api.ChatColor;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.aperlambda.lambdacommon.utils.Pair;
import org.bukkit.command.CommandSender;
import org.shulker.core.Shulker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandImpl extends org.bukkit.command.Command
{
	protected Command parentCommand;

	protected CommandImpl(Command parentCommand)
	{
		super(parentCommand.getName());
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		Pair<CommandResult, String> result = parentCommand.handleExecution(sender, null, label, args);

		switch (result.getKey())
		{
			case SUCCESS:
				break;
			case ERROR_RUNTIME:
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorRuntimeMessage()));
				break;
			case ERROR_USAGE:
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorUsageMessage().replace("${command.usage}", result.getValue().get())));
				break;
			case ERROR_PERMISSION:
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorPermissionMessage()));
				break;
			case ERROR_OBJECT_NOT_FOUND:
				break;
			case ERROR_ONLY_PLAYER:
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorOnlyPlayerMessage()));
				break;
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException
	{
		if (args.length == 1)
		{
			var subCommands = parentCommand.getSubCommands();
			if (subCommands.isEmpty())
				return parentCommand.onTabComplete(sender, null, alias, args);
			List<String> subCommands2 = subCommands.stream()
					.filter(sc -> sc.getPermissionRequired() == null ||
							sender.hasPermission(sc.getPermissionRequired()))
					.map(Nameable::getName).collect(Collectors.toList());
			List<String> additionalCompletion = parentCommand.onTabComplete(sender, null, alias, args);
			if (additionalCompletion != null)
				subCommands2.addAll(additionalCompletion);
			return subCommands2.stream()
					.filter(sc -> sc.startsWith(args[0]))
					.sorted().collect(Collectors.toList());
		}
		else if (args.length > 1)
			if (parentCommand.hasSubCommand(args[0]))
			{
				var subCommand = parentCommand.getSubCommand(args[0]);
				if (subCommand.get().getPermissionRequired() == null ||
						sender.hasPermission(subCommand.get().getPermissionRequired()))
					return subCommand.get().onTabComplete(sender, parentCommand, alias, Arrays.copyOfRange(args, 1,
																									 args.length));
			}
		return super.tabComplete(sender, alias, args);
	}
}