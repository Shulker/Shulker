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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;
import org.shulker.core.commands.Command;
import org.shulker.core.commands.CommandResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.md_5.bungee.api.ChatColor.*;

public class TestCommand extends Command
{
	public TestCommand()
	{
		super("test");
	}

	@Override
	public @NotNull String getUsage()
	{
		return "<command>";
	}

	@Override
	public @NotNull String getDescription()
	{
		return "OwO";
	}

	@Override
	public @NotNull List<String> getAliases()
	{
		return Collections.singletonList("shulker_test");
	}

	@Override
	public @NotNull CommandResult execute(CommandSender sender, @Nullable Command parent, String label, String[] args)
	{
		if (!(sender instanceof Player))
			return CommandResult.ERROR_ONLY_PLAYER;

		if (args.length != 0)
			return CommandResult.ERROR_USAGE;

		var player = (Player) sender;
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
		shPlayer.sendActionBar(hello);

		return CommandResult.SUCCESS;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		return new ArrayList<>();
	}
}