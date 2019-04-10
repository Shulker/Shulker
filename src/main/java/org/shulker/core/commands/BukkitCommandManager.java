/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands;

import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandManager;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.aperlambda.lambdacommon.utils.LambdaReflection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BukkitCommandManager extends CommandManager<CommandSender>
{
    private final List<Command<CommandSender>> commands = new ArrayList<>();
    private       CommandMap                   cmd_map;

    public BukkitCommandManager()
    {
        hook();
    }

    public void hook()
    {
        var command_map = LambdaReflection.get_field(Bukkit.getServer().getClass(), "commandMap", true);
        if (!command_map.isPresent())
            throw new RuntimeException("Cannot hook the Bukkit's command map.");
        cmd_map = (CommandMap) LambdaReflection.get_field_value(Bukkit.getServer(), command_map.get());
    }

    @Override
    public void register(Command<CommandSender> command)
    {
        var command_impl = new CommandImpl(command);
        cmd_map.register(command.get_resource_name().get_domain(), command_impl);
        commands.add(command);
    }

    @Override
    public boolean has_command(ResourceName name)
    {
        return get_command(name).isPresent();
    }

    @Override
    public Optional<Command<CommandSender>> get_command(ResourceName name)
    {
        var bukkit_command = cmd_map.getCommand(name.get_domain() + ":" + name.get_name());
        if (bukkit_command instanceof CommandImpl)
            return Optional.of(((CommandImpl) bukkit_command).get_kimiko_command());
        return Optional.empty();
    }

    @Override
    public List<Command<CommandSender>> get_commands()
    {
        return new ArrayList<>(commands);
    }

    @Override
    public void clear_commands()
    {
        throw new UnsupportedOperationException("Sorry but clearCommands isn't supported on Bukkit.");
    }
}