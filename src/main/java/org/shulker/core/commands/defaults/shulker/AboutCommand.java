/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands.defaults.shulker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;
import org.shulker.core.commands.CommandResult;
import org.shulker.core.commands.SubCommand;

import java.util.ArrayList;
import java.util.List;

import static net.md_5.bungee.api.ChatColor.GOLD;
import static net.md_5.bungee.api.ChatColor.GREEN;

public class AboutCommand implements SubCommand
{
	@Override
	public @NotNull String getUsage()
	{
		return "<command>";
	}

	@Override
	public @NotNull String getDescription()
	{
		return "Displays information about Shulker.";
	}

	@Override
	public @NotNull List<String> getAliases()
	{
		return new ArrayList<>();
	}

	@Override
	public @NotNull CommandResult execute(CommandSender sender, Command parent, String label, String[] args)
	{
		if(args.length != 0)
		return CommandResult.ERROR_USAGE;

		if (!sender.hasPermission("shulker.about"))
			return CommandResult.ERROR_PERMISSION;

		sender.sendMessage(Shulker.getPrefixIG() + GREEN + " Reloading...");
		sender.sendMessage(Shulker.getPrefixIG() + GREEN + " Reloading " + GOLD + "configuration" + GREEN + "...");
		Shulker.getConfiguration().load();
		sender.sendMessage(Shulker.getPrefixIG() + GREEN + " Reloading " + GOLD + "symbols" + GREEN + "...");
		Shulker.getSymbolsManager().load();
		sender.sendMessage(Shulker.getPrefixIG() + GREEN + " Reload done!");
		return null;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command parent, String label, String[] args)
	{
		return new ArrayList<>();
	}

	@Override
	public @NotNull String getName()
	{
		return null;
	}
}