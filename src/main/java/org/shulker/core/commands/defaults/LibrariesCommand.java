/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands.defaults;

import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandContext;
import org.aperlambda.kimiko.CommandResult;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;
import org.shulker.core.ShulkerLibrary;
import org.shulker.core.commands.BukkitCommandExecutor;
import org.shulker.core.commands.BukkitCommandTabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatColor.*;

public class LibrariesCommand implements BukkitCommandExecutor, BukkitCommandTabCompleter
{
    @NotNull
    public CommandResult execute(CommandSender sender)
    {
        var libraries = Shulker.get().get_libraries();

        if (libraries.size() == 0)
            sender.sendMessage("No libraries installed.");
        else {

            var string = new StringBuilder();
            string.append("Libraries (").append(libraries.size()).append("): ");

            for (var library : libraries)
                string.append(library.get_load_state().get_prefix_color()).append(library.get_name()).append(RESET).append(", ");

            sender.sendMessage(string.substring(0, string.length() - 4));
        }

        return CommandResult.SUCCESS;
    }

    @NotNull
    public CommandResult execute(CommandSender sender, String[] args)
    {
        if (args.length > 1)
            return CommandResult.ERROR_USAGE;

        var libraries = Shulker.get().get_libraries();
        Optional<ShulkerLibrary> o = libraries.stream().filter(l -> l.get_name().equalsIgnoreCase(args[0])).findFirst();
        if (o.isPresent()) {
            sender.sendMessage(new String[]{"Library " + GOLD + o.get().get_name() + RESET + ":",
                    "File: " + GOLD + o.get().get_file().getPath() + RESET,
                    "Load state: " + o.get().get_load_state().get_prefix_color() +
                            o.get().get_load_state()});
        } else
            sender.sendMessage(RED + "Cannot find the library '" + args[0] + RED + "'.");
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull CommandResult execute(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
    {
        if (args.length == 0)
            return execute(context.get_sender());
        return execute(context.get_sender(), args);
    }

    @Override
    public List<String> on_tab_complete(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
    {
        if (context.has_permission("shulker.commands.libraries") && args.length == 1)
            return Shulker.get().get_libraries().stream()
                    .map(ShulkerLibrary::get_name)
                    .filter(l -> l.startsWith(args[0]))
                    .sorted().collect(Collectors.toList());
        else
            return new ArrayList<>();
    }
}