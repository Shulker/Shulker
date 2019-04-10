/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands.defaults.shulker;

import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandBuilder;
import org.aperlambda.kimiko.CommandContext;
import org.aperlambda.kimiko.CommandResult;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;
import org.shulker.core.commands.BukkitCommandExecutor;
import org.shulker.core.commands.BukkitCommandTabCompleter;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.util.ArrayList;
import java.util.List;

import static net.md_5.bungee.api.ChatColor.DARK_PURPLE;
import static net.md_5.bungee.api.ChatColor.LIGHT_PURPLE;

public class AboutCommand implements BukkitCommandExecutor, BukkitCommandTabCompleter
{
    private Command<CommandSender> command;

    public AboutCommand()
    {
        command = new CommandBuilder<CommandSender>(new ResourceName("shulker", "about"))
                .usage("<command>")
                .description("Displays information about Shulker.")
                .permission("shulker.commands.about")
                .aliases("info")
                .executor(this)
                .tab_completer(this)
                .build();
    }

    @Override
    public @NotNull CommandResult execute(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
    {
        if (args.length != 0)
            return CommandResult.ERROR_USAGE;

        context.send_message(DARK_PURPLE + "====== " + LIGHT_PURPLE + "About" + DARK_PURPLE + " ======");
        context.send_message(DARK_PURPLE + "Version: " + LIGHT_PURPLE + Shulker.get_version());
        context.send_message(DARK_PURPLE + "Authors: " + LIGHT_PURPLE + ((ShulkerSpigotPlugin) Shulker.get()).getDescription().getAuthors());
        context.send_message(DARK_PURPLE + "Java: " + LIGHT_PURPLE + System.getProperty("java.version"));
        context.send_message(DARK_PURPLE + "Internal Server Version: " + LIGHT_PURPLE + ShulkerSpigotPlugin.get_server_version());

        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> on_tab_complete(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
    {
        return new ArrayList<>();
    }

    public Command<CommandSender> get_result_command()
    {
        return command;
    }
}