/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
    public CommandSender get_sender()
    {
        return sender;
    }

    @Override
    public String get_sender_name()
    {
        return sender.getName();
    }

    @Override
    public void send_message(String message)
    {
        sender.sendMessage(message);
    }

    @Override
    public boolean has_permission(String permission)
    {
        if (permission == null || permission.isEmpty())
            return true;
        return sender.hasPermission(permission);
    }
}