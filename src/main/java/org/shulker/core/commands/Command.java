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
import org.aperlambda.lambdacommon.utils.Pair;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command represents a command.
 */
public abstract class Command implements Nameable
{
	private final @NotNull String        name;
	private final    List<Command> subCommands = new ArrayList<>();

	public Command(@NotNull String name)
	{
		this.name = name;
	}

	/**
	 * Gets the usage of the command.
	 * <p>The format is {@code <command> <required_argument> [optional_argument]} you can simply put ${@code <command>} for the command name, Shulker will replace it automatically in the usage error message.</p>
	 *
	 * @return The usage.
	 */
	public abstract @NotNull String getUsage();

	/**
	 * Gets the usage of the command depending of the sender..
	 * <p>The format is {@code <command> <required_argument> [optional_argument]} you can simply put ${@code <command>} for the command name, Shulker will replace it automatically in the usage error message.</p>
	 *
	 * @return The usage.
	 * @see Command#getUsage()
	 */
	public @NotNull String getUsage(CommandSender sender)
	{
		return getUsage();
	}

	/**
	 * Gets the description of the command.
	 *
	 * @return The description.
	 */
	public abstract @NotNull String getDescription();

	/**
	 * Gets the description of the command depending of the sender.
	 * <p>By default it returns the value of {@link Command#getDescription()} ()}</p>
	 *
	 * @param sender The command sender.
	 * @return The description of the command.
	 * @see Command#getDescription()
	 */
	public @NotNull String getDescription(CommandSender sender)
	{
		return getDescription();
	}

	/**
	 * Gets the aliases of the command. Can be empty but not null.
	 *
	 * @return The aliases.
	 */
	public abstract @NotNull List<String> getAliases();

	/**
	 * Gets the permission that is required to execute this sub-command.
	 *
	 * @return The required permission.
	 */
	public @Nullable String getPermissionRequired()
	{
		return null;
	}

	/**
	 * Represents the execution process of the command.
	 * <p>The {@code args} argument represents the arguments of the command and not the arguments of the parent command.</p>
	 *
	 * @param sender The sender of the command.
	 * @param parent The parent command.
	 * @param label  The label used to call the command.
	 * @param args   The arguments of the command.
	 * @return The result of the execution of the command.
	 */
	public abstract @NotNull CommandResult execute(CommandSender sender, @Nullable Command parent, String label, String[] args);

	public final @NotNull Pair<CommandResult, String> handleExecution(CommandSender sender, @Nullable Command parent, String label, String[] args)
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
				return command.handleExecution(sender, this, subLabel, Arrays.copyOfRange(args, 1, args.length));
			usage = getName() + " " + command.getUsage(sender).replace("<command>", command.getName());
		}
		else
			result = execute(sender, null, label, args);

		if (result == CommandResult.ERROR_USAGE)
			return new Pair<>(result, usage);

		return new Pair<>(result, null);
	}

	public abstract List<String> onTabComplete(CommandSender sender, @Nullable Command parent, String label, String[] args);

	/**
	 * Adds a new command to the command.
	 *
	 * @param subCommand The command to add.
	 */
	public void addSubCommand(@NotNull Command subCommand)
	{
		if (subCommands.contains(subCommand))
			return;
		subCommands.add(subCommand);
	}

	/**
	 * Adds a new command to the command.
	 *
	 * @param name         The name of the command.
	 * @param usage        The usage of the command.
	 * @param description  The description of the command.
	 * @param executor     The executor of the command.
	 * @param tabCompleter The tab-completer of the command.
	 */
	public void addSubCommand(@NotNull String name, @NotNull String usage, @NotNull String description, @NotNull SubCommand.SubCommandExecutor executor, @Nullable SubCommand.SubCommandTabCompleter tabCompleter)
	{
		//addSubCommand(new SimpleSubCommand(name, usage, description, executor, tabCompleter));
	}

	/**
	 * Adds a new command to the command.
	 *
	 * @param name         The name of the command.
	 * @param usage        The usage of the command.
	 * @param description  The description of the command.
	 * @param permission   The permission required to execute the command, may be null.
	 * @param aliases      The aliases of the command.
	 * @param executor     The executor of the command.
	 * @param tabCompleter The tab-completer of the command.
	 */
	public void addSubCommand(@NotNull String name, @NotNull String usage, @NotNull String description, @Nullable String permission, @NotNull List<String> aliases, @NotNull SubCommand.SubCommandExecutor executor, @Nullable SubCommand.SubCommandTabCompleter tabCompleter)
	{
		//addSubCommand(new SimpleSubCommand(name, usage, description, permission, aliases, executor, tabCompleter));
	}

	/**
	 * Checks whether the command has the specified command.
	 *
	 * @param subCommand The command to check.
	 * @return True if found, else false.
	 */
	public boolean hasSubCommand(@NotNull Command subCommand)
	{
		return subCommands.contains(subCommand);
	}

	/**
	 * Checks whether the command has the specified command.
	 *
	 * @param label The name (or alias) of the command to check.
	 * @return True if found, else false.
	 */
	public boolean hasSubCommand(@NotNull String label)
	{
		var finalLabel = label.toLowerCase();
		return subCommands.stream().anyMatch(cmd -> cmd.getName().equalsIgnoreCase(finalLabel) ||
				cmd.getAliases().contains(finalLabel));
	}

	/**
	 * Removes the specified command.
	 *
	 * @param subCommand The command to remove.
	 */
	public void removeSubCommand(@NotNull Command subCommand)
	{
		subCommands.remove(subCommand);
	}

	/**
	 * Gets a command by it's name or alias.
	 *
	 * @param label The name or the alias of the command.
	 * @return The optional command.
	 */
	public @NotNull Optional<Command> getSubCommand(@NotNull String label)
	{
		var finalLabel = label.toLowerCase();
		return Optional.fromJava(subCommands.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(finalLabel) ||
				cmd.getAliases().contains(finalLabel)).findFirst());
	}

	/**
	 * Gets the commands of the command.
	 *
	 * @return A list of the commands.
	 */
	public @NotNull List<Command> getSubCommands()
	{
		return new ArrayList<>(subCommands);
	}

	@Override
	public @NotNull String getName()
	{
		return name;
	}
}