/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands.defaults;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandBuilder;
import org.aperlambda.kimiko.CommandContext;
import org.aperlambda.kimiko.CommandResult;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;
import org.shulker.core.commands.BukkitCommandExecutor;
import org.shulker.core.commands.BukkitCommandResult;
import org.shulker.core.commands.BukkitCommandTabCompleter;

import java.util.ArrayList;
import java.util.List;

import static net.md_5.bungee.api.ChatColor.*;

public class TestCommand implements BukkitCommandExecutor, BukkitCommandTabCompleter
{
	private Command<CommandSender> command;

	public TestCommand()
	{
		command = new CommandBuilder<CommandSender>(new ResourceName("shulker", "test"))
				.usage("<command>")
				.description("OwO")
				.permission("shulker.commands.test")
				.executor(this)
				.tabCompleter(this)
				.build();
	}

	@Override
	public @NotNull CommandResult execute(CommandContext<CommandSender> context, @Nullable Command<CommandSender> command, String label, String[] args)
	{
		if (!(context.getSender() instanceof Player))
			return BukkitCommandResult.ERROR_ONLY_PLAYER;

		if (args.length != 0)
			return CommandResult.ERROR_USAGE;

		var player = (Player) context.getSender();
		var shPlayer = Shulker.getMCManager().getPlayer(player);
		var components = new ComponentBuilder("Hello, it's a ").color(GREEN).append("test").color(DARK_AQUA).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Hover event!\nPing: ").append(
				"" +
						shPlayer.getPing()).color(GOLD).create())).append(" of the method sendMessage(BaseComponent)!", ComponentBuilder.FormatRetention.NONE).color(GREEN).create();
		var hello = new ComponentBuilder("Hello world!").color(GREEN).create();
		var componentsOwO = new ComponentBuilder("OwO\n").append("Ping: ").color(GREEN).append(
				"" + shPlayer.getPing()).color(GOLD).create();
		shPlayer.sendMessage(components);

		shPlayer.sendPacket(Shulker.getMCManager().newPacketPlayOutPlayerListHeaderFooter(componentsOwO,
																						  new TextComponent[]{
																								  new TextComponent("OwO")}));

		shPlayer.sendTitle(hello, new BaseComponent[]{new TextComponent("OwO")}, 20, 70, 10);
		//shPlayer.sendActionBar(hello);

		return CommandResult.SUCCESS;
	}

	@Override
	public List<String> onTabComplete(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
	{
		return new ArrayList<>();
	}

	public Command<CommandSender> getResultCommand()
	{
		return command;
	}
}