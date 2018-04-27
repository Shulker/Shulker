/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands.defaults;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.commands.CommandResult;
import org.shulker.core.commands.HelpSubCommand;
import org.shulker.core.commands.ShulkerCommandExecutor;
import org.shulker.core.commands.defaults.shulker.ReloadCommand;

import java.util.ArrayList;
import java.util.List;

public class ShulkerCommand extends ShulkerCommandExecutor
{
	private HelpSubCommand helpCommand;

	public ShulkerCommand()
	{
		helpCommand = new HelpSubCommand(this, "shulker.commands.help", ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, ChatColor.GOLD);
		helpCommand.setTitle("Shulker Help");
		addSubCommand(helpCommand);
		addSubCommand(new ReloadCommand());
	}

	@Override
	public @NotNull CommandResult execute(CommandSender sender, Command command, String label)
	{
		return helpCommand.execute(sender, command, label, new String[0]);
	}

	@Override
	public @NotNull CommandResult execute(CommandSender sender, Command command, String label, String[] args)
	{
		return CommandResult.ERROR_USAGE;
	}

	@Override
	public List<String> onTabCompletion(CommandSender sender, Command command, String label, String[] args)
	{
		return new ArrayList<>();
	}
}