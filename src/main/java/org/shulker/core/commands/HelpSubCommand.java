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
import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandContext;
import org.aperlambda.kimiko.CommandResult;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.aperlambda.lambdacommon.utils.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatColor.GRAY;

public class HelpSubCommand implements BukkitCommandExecutor, BukkitCommandTabCompleter
{
	private String              title;
	private ChatColor           mainColor;
	private ChatColor           secondaryColor;
	private Optional<ChatColor> commandColor = Optional.empty();

	private Command<CommandSender> command;

	public HelpSubCommand(String permission, ChatColor mainColor, ChatColor secondaryColor)
	{
		this.mainColor = mainColor;
		this.secondaryColor = secondaryColor;
		this.command = new Command<>(new ResourceName("", "help"));
		command.setUsage("<command> [subcommand]");
		command.setDescription("Displays the help message.");
		command.setPermissionRequired(permission);
		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	public HelpSubCommand(String permission, ChatColor mainColor, ChatColor secondaryColor, @Nullable ChatColor commandColor)
	{
		this(permission, mainColor, secondaryColor);
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

	/**
	 * Gets the command created by {@code HelpSubCommand}.
	 *
	 * @return The new command.
	 */
	public Command<CommandSender> getResultCommand()
	{
		return command;
	}

	@Override
	public @NotNull CommandResult execute(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
	{
		var parent = command.getParent();
		if (parent == null)
			return CommandResult.ERROR_RUNTIME;

		if (args.length > 1)
			return CommandResult.ERROR_USAGE;

		if (args.length == 1)
		{
			var sc = parent.getSubCommand(args[0].toLowerCase());
			if (!sc.isPresent())
				return CommandResult.ERROR_RUNTIME;
			context.sendMessage(mainColor + "====== " + secondaryColor + sc.get().getName() + mainColor + " ======");
			context.sendMessage(mainColor + "Usage: " + commandColor.getOrElse(secondaryColor) +
										sc.get().getUsage().replace("<command>", sc.get().getName()));
			context.sendMessage(mainColor + "Description: " + GRAY + sc.get().getDescription());
			context.sendMessage(mainColor + "Aliases: " + commandColor.getOrElse(secondaryColor) +
										String.join(",", sc.get().getAliases()));
			context.sendMessage(mainColor + "Permission required: " + secondaryColor +
										(sc.get().getPermissionRequired() == null ? "none" :
										 sc.get().getPermissionRequired()));
			return CommandResult.SUCCESS;
		}

		String baseCmd = "/" + parent.getName() + " ";
		context.sendMessage(mainColor + "====== " + secondaryColor + title + mainColor + " ======");
		context.sendMessage(commandColor.getOrElse(secondaryColor) + baseCmd);
		if (context.getSender() instanceof Player)
		{
			var player = Shulker.getMCManager().getPlayer((Player) context.getSender());
			var hoverMessage = new ComponentBuilder("Click on the command to insert it in the chat.").color(GRAY).create();
			parent.getSubCommands().forEach(subCommand -> player.sendMessage(new ComponentBuilder(
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
			parent.getSubCommands().forEach(subCommand -> context.sendMessage(
					commandColor.getOrElse(secondaryColor) + "├─ " +
							subCommand.getUsage().replace("<command>", subCommand.getName()) + GRAY + " - " +
							subCommand.getDescription()));
		context.sendMessage(mainColor + "====== " + secondaryColor + title + mainColor + " ======");

		return CommandResult.SUCCESS;
	}

	@Override
	public List<String> onTabComplete(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
	{
		if (args.length == 1)
			return Optional.ofNullable(command.getParent()).map(parent -> parent.getSubCommands().stream()
					.filter(sc -> sc.getPermissionRequired() == null ||
							context.hasPermission(sc.getPermissionRequired()))
					.map(Nameable::getName)
					.sorted().collect(Collectors.toList())).getOrElse(new ArrayList<>());
		return new ArrayList<>();
	}
}