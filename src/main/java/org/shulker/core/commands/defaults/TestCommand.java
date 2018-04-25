/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands.defaults;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;
import org.shulker.core.commands.CommandResult;
import org.shulker.core.commands.ShulkerCommandExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.md_5.bungee.api.ChatColor.*;

public class TestCommand extends ShulkerCommandExecutor
{
	@Override
	public @NotNull CommandResult execute(CommandSender sender, Command command, String label)
	{
		if (!(sender instanceof Player))
			return CommandResult.ERROR_ONLY_PLAYER;

		var player = (Player) sender;
		var shPlayer = Shulker.getMCManager().getPlayer(player);
		var components = new ComponentBuilder("Hello, it's a ").color(GREEN).append("test").color(DARK_AQUA).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Hover event!\nPing: ").append(
				"" +
						shPlayer.getPing()).color(GOLD).create())).append(" of the method sendMessage(BaseComponent)!", ComponentBuilder.FormatRetention.NONE).color(GREEN).create();
		var componentsOwO = new ComponentBuilder("OwO\n").append("Ping: ").color(GREEN).append(
				"" + shPlayer.getPing()).color(GOLD).create();
		shPlayer.sendMessage(components);
		var packetChat = Shulker.getMCManager().newPacketPlayOutChat(components);
		packetChat.setPosition(ChatMessageType.SYSTEM);
		shPlayer.sendPacket(packetChat);
		player.sendMessage("Ping: " + shPlayer.getPing());
		player.sendMessage("Locale: " + shPlayer.getLocale());

		sender.sendMessage("Sending packet 'PacketPlayOutPlayerListHeaderFooter'");
		var packet = Shulker.getMCManager().newPacketPlayOutPlayerListHeaderFooter(componentsOwO,
																				   new TextComponent[]{
																						   new TextComponent("OwO")});
		sender.sendMessage(packet.getHandle().toString());
		shPlayer.sendPacket(Shulker.getMCManager().newPacketPlayOutPlayerListHeaderFooter(componentsOwO,
																						  new TextComponent[]{
																								  new TextComponent("OwO")}));

		return CommandResult.SUCCESS;
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