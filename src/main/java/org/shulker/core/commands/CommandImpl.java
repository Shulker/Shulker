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
import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandResult;
import org.aperlambda.lambdacommon.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.shulker.core.Shulker;
import org.shulker.core.plugin.UnknownPlugin;

import java.util.List;
import java.util.Optional;

public class CommandImpl extends org.bukkit.command.Command implements PluginIdentifiableCommand
{
    protected Command<CommandSender> command;

    protected CommandImpl(Command<CommandSender> command)
    {
        super(command.get_name());
        this.command = command;
        setUsage(command.get_usage());
        setDescription(command.get_description());
        setAliases(command.get_aliases());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args)
    {
        BukkitCommandContext context = new BukkitCommandContext(sender);
        Pair<CommandResult, String> result = command.handle_execution(context, label, args);

        String a = result.get_key().call();

        switch (a) {
            case "":
                break;
            case "translate:error.runtime":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.get_configuration().get_error_runtime_message()));
                break;
            case "translate:error.usage":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.get_configuration().get_error_usage_message().replace("${command.usage}", result.get_value())));
                break;
            case "translate:error.permission":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.get_configuration().get_error_permission_message()));
                break;
            case "translate:error_only_player":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Shulker.get_configuration().get_error_only_player_message()));
                break;
            default:
                sender.sendMessage(a);
                break;
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException
    {
        BukkitCommandContext context = new BukkitCommandContext(sender);
        var result = command.on_tab_complete(context, alias, args);
        if (result == null)
            return super.tabComplete(sender, alias, args);
        else return result;
    }

    /**
     * Gets the kimiko command.
     *
     * @return The kimiko command.
     */
    public Command<CommandSender> get_kimiko_command()
    {
        return command;
    }

    @Override
    public Plugin getPlugin()
    {
        return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(command.get_resource_name().get_domain())).orElseGet(() -> {
            Shulker.get().log_error("[CommandManager]", "WARNING: command '" + command.get_resource_name() +
                    "' has an invalid ResourceName's domain. (Domain must correspond to a plugin).");
            return UnknownPlugin.INSTANCE;
        });
    }
}