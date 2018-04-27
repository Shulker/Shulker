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
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.aperlambda.lambdacommon.utils.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatColor.GRAY;

public class HelpSubCommand implements SubCommand
{
	private ShulkerCommandExecutor parent;
	private String                 title;
	private ChatColor              mainColor;
	private ChatColor              secondaryColor;
	private Optional<ChatColor>    commandColor = Optional.empty();
	private String                 permission;

	public HelpSubCommand(ShulkerCommandExecutor parent, String permission, ChatColor mainColor, ChatColor secondaryColor)
	{
		this.parent = parent;
		this.permission = permission;
		this.mainColor = mainColor;
		this.secondaryColor = secondaryColor;
	}

	public HelpSubCommand(ShulkerCommandExecutor parent, String permission, ChatColor mainColor, ChatColor secondaryColor, @Nullable ChatColor commandColor)
	{
		this(parent, permission, mainColor, secondaryColor);
		this.commandColor = Optional.ofNullable(commandColor);
	}

	/**
	 * Gets the title of the help message.
	 *
	 * @return The title of the help message.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets the title of the help message.
	 *
	 * @param title The title of the help message.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	public ChatColor getMainColor()
	{
		return mainColor;
	}

	public void setMainColor(ChatColor mainColor)
	{
		this.mainColor = mainColor;
	}

	public ChatColor getSecondaryColor()
	{
		return secondaryColor;
	}

	public void setSecondaryColor(ChatColor secondaryColor)
	{
		this.secondaryColor = secondaryColor;
	}

	public @NotNull Optional<ChatColor> getCommandColor()
	{
		return commandColor;
	}

	public void setCommandColor(@Nullable ChatColor commandColor)
	{
		this.commandColor = Optional.ofNullable(commandColor);
	}

	@Override
	public @NotNull String getUsage()
	{
		return "<command> [command]";
	}

	@Override
	public @NotNull String getDescription()
	{
		// @TODO: Add the Lang Manager.
		return "Displays help message.";
	}

	@Override
	public @NotNull List<String> getAliases()
	{
		return new ArrayList<>();
	}

	@Override
	public @Nullable String getPermissionRequired()
	{
		return permission;
	}

	@Override
	public @NotNull CommandResult execute(CommandSender sender, Command parent, String label, String[] args)
	{
		if (args.length > 1)
			return CommandResult.ERROR_USAGE;

		if (args.length == 1)
		{
			var sc = this.parent.getSubCommand(args[0].toLowerCase());
			if (!sc.isPresent())
				return CommandResult.ERROR_RUNTIME;
			sender.sendMessage(mainColor + "====== " + secondaryColor + sc.get().getName() + mainColor + " ======");
			sender.sendMessage(mainColor + "Usage: " + commandColor +
									   sc.get().getUsage().replace("<command>", sc.get().getName()));
			sender.sendMessage(mainColor + "Description: " + GRAY + sc.get().getDescription());
			sender.sendMessage(mainColor + "Aliases: " + commandColor + String.join(",", sc.get().getAliases()));
			sender.sendMessage(mainColor + "Permission required: " + secondaryColor +
									   (sc.get().getPermissionRequired() == null ? "none" :
										sc.get().getPermissionRequired()));
			return CommandResult.SUCCESS;
		}

		String baseCmd = "/" + parent.getName() + " ";
		sender.sendMessage(mainColor + "====== " + secondaryColor + title + mainColor + " ======");
		sender.sendMessage(commandColor.getOrElse(secondaryColor) + baseCmd);
		if (sender instanceof Player)
		{
			var player = Shulker.getMCManager().getPlayer((Player) sender);
			var hoverMessage = new ComponentBuilder("Click on the command to insert it in the chat.").color(GRAY).create();
			this.parent.getSubCommands().forEach(subCommand -> player.sendMessage(new ComponentBuilder(
					"├─ " +
							subCommand.getUsage().replace("<command>", subCommand.getName()))
																						  .color(commandColor.getOrElse(secondaryColor))
																						  .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
																												baseCmd +
																														subCommand.getName()))
																						  .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMessage))
																						  .append(" - " +
																										  subCommand.getDescription(), ComponentBuilder.FormatRetention.NONE)
																						  .color(GRAY)
																						  .create()));
		}
		else
			this.parent.getSubCommands().forEach(subCommand -> sender.sendMessage(
					commandColor.getOrElse(secondaryColor) + "├─ " +
							subCommand.getUsage().replace("<command>", subCommand.getName()) + GRAY + " - " +
							subCommand.getDescription()));
		sender.sendMessage(mainColor + "====== " + secondaryColor + title + mainColor + " ======");

		return CommandResult.SUCCESS;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command parent, String label, String[] args)
	{
		if (args.length == 1)
			return this.parent.getSubCommands().stream()
					.filter(sc -> sc.getPermissionRequired() == null ||
							sender.hasPermission(sc.getPermissionRequired()))
					.map(Nameable::getName)
					.sorted().collect(Collectors.toList());
		return new ArrayList<>();
	}

	@Override
	public @NotNull String getName()
	{
		return "help";
	}
}