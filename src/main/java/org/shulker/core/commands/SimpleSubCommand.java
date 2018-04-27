/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * SimpleSubCommand represents a very simple implementation of the {@link SubCommand} interface.
 */
public class SimpleSubCommand implements SubCommand
{
	private @NotNull  String       name;
	private @NotNull  String       usage;
	private @NotNull  String       description;
	private @Nullable String       permission;
	private @NotNull SubCommandExecutor executor;
	private @Nullable SubCommandTabCompleter tabCompleter;
	private @NotNull  List<String> aliases = new ArrayList<>();

	public SimpleSubCommand(@NotNull String name, @NotNull String usage, @NotNull String description, @NotNull SubCommandExecutor executor, @Nullable SubCommandTabCompleter tabCompleter)
	{
		this(name, usage, description, null, new ArrayList<>(), executor, tabCompleter);
	}

	public SimpleSubCommand(@NotNull String name, @NotNull String usage, @NotNull String description, @Nullable String permission, @NotNull List<String> aliases, @NotNull SubCommandExecutor executor, @Nullable SubCommandTabCompleter tabCompleter)
	{
		this.name = name;
		this.usage = usage;
		this.description = description;
		this.permission = permission;
		this.aliases = aliases;
		this.executor = executor;
		this.tabCompleter = tabCompleter;
	}

	@Override
	public @NotNull String getUsage()
	{
		return usage;
	}

	@Override
	public @NotNull String getDescription()
	{
		return description;
	}

	@Override
	public @NotNull List<String> getAliases()
	{
		return aliases;
	}

	@Override
	public @Nullable String getPermissionRequired()
	{
		return permission;
	}

	@Override
	public @NotNull CommandResult execute(CommandSender sender, Command parent, String label, String[] args)
	{
		return executor.execute(sender, parent, label, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command parent, String label, String[] args)
	{
		if (tabCompleter != null)
			return tabCompleter.complete(sender, parent, label, args);
		return null;
	}

	@Override
	public @NotNull String getName()
	{
		return name;
	}
}