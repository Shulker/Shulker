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
import org.aperlambda.lambdacommon.utils.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an advanced {@link org.bukkit.command.TabExecutor}
 */
public abstract class ShulkerCommandExecutor implements TabExecutor
{
	private final List<SubCommand> subCommands = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		CommandResult result;
		var usage = command.getUsage().replace("<command>", command.getName());
		if (args.length == 0)
			result = execute(sender, command, label);
		else
		{
			var subLabel = args[0];
			if (hasSubCommand(subLabel))
			{
				SubCommand sc = getSubCommand(subLabel).get();
				result = sc.execute(sender, command, subLabel, Arrays.copyOfRange(args, 1, args.length));
				usage = command.getName() + " " + sc.getUsage().replace("<command>", sc.getName());
			}
			else
				result = execute(sender, command, label, args);
		}

		if (result == CommandResult.ERROR_USAGE)
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorUsageMessage().replace("${command.usage}", usage)));
		else if (result == CommandResult.ERROR_PERMISSION)
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorPermissionMessage()));
		else if (result == CommandResult.ERROR_RUNTIME)
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorRuntimeMessage()));
		else if (result == CommandResult.ERROR_ONLY_PLAYER)
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorOnlyPlayerMessage()));
		return true;
	}

	/**
	 * Executes without parameters.
	 *
	 * @param sender  Source of the command.
	 * @param command Command which was executed.
	 * @param label   Alias of the command which was used.
	 * @return SUCCESS if a valid command.
	 */
	public abstract @NotNull CommandResult execute(CommandSender sender, Command command, String label);

	public abstract @NotNull CommandResult execute(CommandSender sender, Command command, String label, String[] args);

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 1)
		{
			if (subCommands.isEmpty())
				return onTabCompletion(sender, command, label, args);
			List<String> subCommands2 = subCommands.stream().map(Nameable::getName).collect(Collectors.toList());
			List<String> additionalCompletion = onTabCompletion(sender, command, label, args);
			if (additionalCompletion != null)
				subCommands2.addAll(additionalCompletion);
			return subCommands2.stream()
					.filter(sc -> sc.startsWith(args[0]))
					.sorted().collect(Collectors.toList());
		}
		else if (args.length > 1)
			if (hasSubCommand(args[0]))
				return getSubCommand(args[0]).get().onTabComplete(sender, command, label, Arrays.copyOfRange(args, 1,
																											 args.length));
		return onTabCompletion(sender, command, label, args);
	}

	public abstract List<String> onTabCompletion(CommandSender sender, Command command, String label, String[] args);

	/**
	 * Adds a new subcommand to the command.
	 *
	 * @param subCommand The subcommand to add.
	 */
	public void addSubCommand(@NotNull SubCommand subCommand)
	{
		if (subCommands.contains(subCommand))
			return;
		subCommands.add(subCommand);
	}

	/**
	 * Checks whether the command has the specified subcommand.
	 *
	 * @param subCommand The subcommand to check.
	 * @return True if found, else false.
	 */
	public boolean hasSubCommand(@NotNull SubCommand subCommand)
	{
		return subCommands.contains(subCommand);
	}

	/**
	 * Checks whether the command has the specified subcommand.
	 *
	 * @param label The name (or alias) of the subcommand to check.
	 * @return True if found, else false.
	 */
	public boolean hasSubCommand(@NotNull String label)
	{
		var finalLabel = label.toLowerCase();
		return subCommands.stream().anyMatch(cmd -> cmd.getName().equalsIgnoreCase(finalLabel) ||
				cmd.getAliases().contains(finalLabel));
	}

	/**
	 * Removes the specified subcommand.
	 *
	 * @param subCommand The subcommand to remove.
	 */
	public void removeSubCommand(@NotNull SubCommand subCommand)
	{
		subCommands.remove(subCommand);
	}

	/**
	 * Gets a subcommand by it's name or alias.
	 *
	 * @param label The name or the alias of the subcommand.
	 * @return The optional subcommand.
	 */
	public @NotNull Optional<SubCommand> getSubCommand(@NotNull String label)
	{
		var finalLabel = label.toLowerCase();
		return Optional.fromJava(subCommands.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(finalLabel) ||
				cmd.getAliases().contains(finalLabel)).findFirst());
	}

	/**
	 * Gets the subcommands of the command.
	 *
	 * @return A list of the subcommands.
	 */
	public @NotNull List<SubCommand> getSubCommands()
	{
		return new ArrayList<>(subCommands);
	}
}