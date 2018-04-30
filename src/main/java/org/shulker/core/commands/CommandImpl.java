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
import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandResult;
import org.aperlambda.lambdacommon.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.shulker.core.Shulker;
import org.shulker.core.plugin.UnknownPlugin;

import java.util.List;
import java.util.Optional;

public class CommandImpl extends org.bukkit.command.Command implements PluginIdentifiableCommand
{
	protected Command<CommandSender> command;

	protected CommandImpl(Command<CommandSender> command)
	{
		super(command.getName());
		this.command = command;
		setUsage(command.getUsage());
		setDescription(command.getDescription());
		setAliases(command.getAliases());
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		BukkitCommandContext context = new BukkitCommandContext(sender);
		Pair<CommandResult, String> result = command.handleExecution(context, label, args);

		String a = result.getKey().call();

		switch (a)
		{
			case "":
				break;
			case "translate:error.runtime":
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorRuntimeMessage()));
				break;
			case "translate:error.usage":
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorUsageMessage().replace("${command.usage}", result.getValue().get())));
				break;
			case "translate:error.permission":
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorPermissionMessage()));
				break;
			case "translate:error_only_player":
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.getConfiguration().getErrorOnlyPlayerMessage()));
				break;
			default:
				sender.sendMessage(a);
				break;
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException
	{
		BukkitCommandContext context = new BukkitCommandContext(sender);
		var result = command.onTabComplete(context, alias, args);
		if (result == null)
			return super.tabComplete(sender, alias, args);
		else return result;
	}

	/**
	 * Gets the kimiko command.
	 *
	 * @return The kimiko command.
	 */
	public Command<CommandSender> getKimikoCommand()
	{
		return command;
	}

	@Override
	public Plugin getPlugin()
	{
		return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(command.getResourceName().getDomain())).orElseGet(() -> {
			Shulker.getShulker().logError("[CommandManager]", "WARNING: command '" + command.getResourceName() +
					"' has an invalid ResourceName's domain. (Domain must correspond to a plugin).");
			return UnknownPlugin.INSTANCE;
		});
	}
}