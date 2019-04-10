/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatColor.GRAY;

public class HelpSubCommand implements BukkitCommandExecutor, BukkitCommandTabCompleter
{
    private String              title;
    private ChatColor           main_color;
    private ChatColor           secondary_color;
    private Optional<ChatColor> command_color = Optional.empty();

    private Command<CommandSender> command;

    public HelpSubCommand(String permission, ChatColor main_color, ChatColor secondary_color)
    {
        this.main_color = main_color;
        this.secondary_color = secondary_color;
        this.command = new Command<>(new ResourceName("", "help"));
        command.set_usage("<command> [subcommand]");
        command.set_description("Displays the help message.");
        command.set_required_permission(permission);
        command.set_executor(this);
        command.set_tab_completer(this);
    }

    public HelpSubCommand(String permission, ChatColor main_color, ChatColor secondary_color, @Nullable ChatColor command_color)
    {
        this(permission, main_color, secondary_color);
        this.command_color = Optional.ofNullable(command_color);
    }

    /**
     * Gets the title of the help message.
     *
     * @return The title of the help message.
     */
    public String get_title()
    {
        return title;
    }

    /**
     * Sets the title of the help message.
     *
     * @param title The title of the help message.
     */
    public void set_title(String title)
    {
        this.title = title;
    }

    public ChatColor get_main_color()
    {
        return main_color;
    }

    public void set_main_color(ChatColor main_color)
    {
        this.main_color = main_color;
    }

    public ChatColor get_secondary_color()
    {
        return secondary_color;
    }

    public void set_secondary_color(ChatColor secondary_color)
    {
        this.secondary_color = secondary_color;
    }

    public @NotNull Optional<ChatColor> get_command_color()
    {
        return command_color;
    }

    public void set_command_color(@Nullable ChatColor command_color)
    {
        this.command_color = Optional.ofNullable(command_color);
    }

    /**
     * Gets the command created by {@code HelpSubCommand}.
     *
     * @return The new command.
     */
    public Command<CommandSender> get_result_command()
    {
        return command;
    }

    @Override
    public @NotNull CommandResult execute(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
    {
        var parent = command.get_parent();
        if (parent == null)
            return CommandResult.ERROR_RUNTIME;

        if (args.length > 1)
            return CommandResult.ERROR_USAGE;

        if (args.length == 1) {
            var sc = parent.get_sub_command(args[0].toLowerCase());
            if (!sc.isPresent())
                return CommandResult.ERROR_RUNTIME;
            context.send_message(main_color + "====== " + secondary_color + sc.get().get_name() + main_color + " ======");
            context.send_message(main_color + "Usage: " + command_color.orElse(secondary_color)
                    + sc.get().get_usage(context.get_sender()).replace("<command>", sc.get().get_name()));
            context.send_message(main_color + "Description: " + GRAY + sc.get().get_description(context.get_sender()));
            context.send_message(main_color + "Aliases: " + command_color.orElse(secondary_color) + String.join(",", sc.get().get_aliases()));
            context.send_message(main_color + "Permission required: " + secondary_color +
                    (sc.get().get_required_permission() == null ? "none" : sc.get().get_required_permission()));
            return CommandResult.SUCCESS;
        }

        String base_cmd = "/" + parent.get_name() + " ";
        context.send_message(main_color + "====== " + secondary_color + title + main_color + " ======");
        context.send_message(command_color.orElse(secondary_color) + base_cmd);
        if (context.get_sender() instanceof Player) {
            var player = Shulker.get_mc().get_player((Player) context.get_sender());
            var hover_message = new ComponentBuilder("Click on the command to insert it in the chat.").color(GRAY).create();
            parent.get_sub_commands().forEach(sub_command -> player.send_message(new ComponentBuilder(
                    "├─ " + sub_command.get_usage(context.get_sender()).replace("<command>", sub_command.get_name()))
                    .color(command_color.orElse(secondary_color))
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, base_cmd + sub_command.get_name()))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover_message))
                    .append(" - " + sub_command.get_description(context.get_sender()), ComponentBuilder.FormatRetention.NONE)
                    .color(GRAY)
                    .create()));
        } else
            parent.get_sub_commands().forEach(sub_command -> context.send_message(
                    command_color.orElse(secondary_color) + "├─ " +
                            sub_command.get_usage().replace("<command>", sub_command.get_name()) + GRAY + " - " + sub_command.get_description()));
        context.send_message(main_color + "====== " + secondary_color + title + main_color + " ======");

        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> on_tab_complete(CommandContext<CommandSender> context, @NotNull Command<CommandSender> command, String label, String[] args)
    {
        if (args.length == 1)
            return Optional.ofNullable(command.get_parent()).map(parent -> parent.get_sub_commands().stream()
                    .filter(sc -> sc.get_required_permission() == null || context.has_permission(sc.get_required_permission()))
                    .map(Nameable::get_name)
                    .sorted().collect(Collectors.toList())).orElse(new ArrayList<>());
        return new ArrayList<>();
    }
}