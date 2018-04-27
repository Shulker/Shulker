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
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;

import java.util.Arrays;
import java.util.List;

public class SimpleCommand extends Command
{
	private CommandExecutor executor;

	public SimpleCommand(@NotNull String name)
	{
		super(name);
	}

	@Override
	public @NotNull String getUsage()
	{
		return null;
	}

	@Override
	public @NotNull String getDescription()
	{
		return null;
	}

	@Override
	public @NotNull List<String> getAliases()
	{
		return null;
	}

	@Override
	public @NotNull CommandResult execute(CommandSender sender, @Nullable Command parent, String label, String[] args)
	{
		CommandResult result;
		var usage = getUsage(sender).replace("<command>", getName());
		var subLabel = args[0];
		if (hasSubCommand(subLabel))
		{
			var command = getSubCommand(subLabel).get();
			if (command.getPermissionRequired() != null && !sender.hasPermission(command.getPermissionRequired()))
				result = CommandResult.ERROR_PERMISSION;
			else
				result = command.execute(sender, this, subLabel, Arrays.copyOfRange(args, 1, args.length));
			usage = getName() + " " + command.getUsage(sender).replace("<command>", command.getName());
		}
		else
			result = executor.execute(sender, null, this, label, args);

		if (result == CommandResult.ERROR_USAGE)
		{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorUsageMessage().replace("${command.usage}", usage)));
			// Don't write twice the usage message.
			result = CommandResult.SUCCESS;
		}

		return result;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, @Nullable Command parent, String label, String[] args)
	{
		return null;
	}
}