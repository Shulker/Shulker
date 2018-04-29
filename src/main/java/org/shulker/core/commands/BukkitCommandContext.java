/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands;

import org.aperlambda.kimiko.CommandContext;
import org.bukkit.command.CommandSender;

public class BukkitCommandContext implements CommandContext<CommandSender>
{
	private CommandSender sender;

	public BukkitCommandContext(CommandSender sender)
	{
		this.sender = sender;
	}

	@Override
	public CommandSender getSender()
	{
		return sender;
	}

	@Override
	public String getSenderName()
	{
		return sender.getName();
	}

	@Override
	public void sendMessage(String message)
	{
		sender.sendMessage(message);
	}

	@Override
	public boolean hasPermission(String permission)
	{
		if (permission == null || permission.isEmpty())
			return true;
		return sender.hasPermission(permission);
	}
}