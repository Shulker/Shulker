/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands.defaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;
import org.shulker.core.ShulkerLibrary;
import org.shulker.core.commands.CommandResult;
import org.shulker.core.commands.ShulkerCommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatColor.GOLD;
import static net.md_5.bungee.api.ChatColor.RED;
import static net.md_5.bungee.api.ChatColor.RESET;

public class LibrariesCommand extends ShulkerCommandExecutor
{
	@NotNull
	@Override
	public CommandResult execute(CommandSender sender, Command command, String label)
	{
		if (!sender.hasPermission("shulker.commands.libraries"))
			return CommandResult.ERROR_PERMISSION;

		var libraries = Shulker.getShulker().getLibraries();
		var string = new StringBuilder();
		string.append("Libraries (").append(libraries.size()).append("): ");

		for (var library : libraries)
			string.append(library.getLoadState().getPrefixColor()).append(library.getName()).append(RESET).append(", ");

		sender.sendMessage(string.substring(0, string.length() - 4));

		return CommandResult.SUCCESS;
	}

	@NotNull
	@Override
	public CommandResult execute(CommandSender sender, Command command, String label, String[] args)
	{
		if (!sender.hasPermission("shulker.commands.libraries"))
			return CommandResult.ERROR_PERMISSION;

		if (args.length > 1)
			return CommandResult.ERROR_USAGE;

		var libraries = Shulker.getShulker().getLibraries();
		Optional<ShulkerLibrary> o = libraries.stream().filter(l -> l.getName().equalsIgnoreCase(args[0])).findFirst();
		if (o.isPresent())
		{
			sender.sendMessage(new String[]{"Library " + GOLD + o.get().getName() + RESET + ":",
											"File: " + GOLD + o.get().getFile().getPath() + RESET,
											"Load state: " + o.get().getLoadState().getPrefixColor() +
													o.get().getLoadState()});
		}
		else
			sender.sendMessage(RED + "Cannot find the library '" + args[0] + RED + "'.");
		return CommandResult.SUCCESS;
	}

	@Override
	public List<String> onTabCompletion(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender.hasPermission("shulker.commands.libraries") && args.length == 1)
			return Shulker.getShulker().getLibraries().stream()
					.map(ShulkerLibrary::getName)
					.filter(l -> l.startsWith(args[0]))
					.sorted().collect(Collectors.toList());
		else
			return new ArrayList<>();
	}
}