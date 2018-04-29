package org.shulker.core.commands;

import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandManager;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.aperlambda.lambdacommon.utils.LambdaReflection;
import org.aperlambda.lambdacommon.utils.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandManager extends CommandManager<CommandSender>
{
	private final List<Command<CommandSender>> commands = new ArrayList<>();
	private       CommandMap                   cmdMap;

	public BukkitCommandManager()
	{
		hook();
	}

	public void hook()
	{
		var commandMap = LambdaReflection.getField(Bukkit.getServer().getClass(), "commandMap", true);
		if (!commandMap.isPresent())
			throw new RuntimeException("Cannot hook the Bukkit's command map.");
		cmdMap = (CommandMap) LambdaReflection.getFieldValue(Bukkit.getServer(), commandMap.get());
	}

	@Override
	public void register(Command<CommandSender> command)
	{
		var commandImpl = new CommandImpl(command);
		cmdMap.register(command.getResourceName().getDomain(), commandImpl);
		commands.add(command);
	}

	@Override
	public boolean hasCommand(ResourceName name)
	{
		return getCommand(name).isPresent();
	}

	@Override
	public Optional<Command<CommandSender>> getCommand(ResourceName name)
	{
		var bukkitCommand = cmdMap.getCommand(name.getDomain() + ":" + name.getName());
		if (bukkitCommand instanceof CommandImpl)
			return Optional.of(((CommandImpl) bukkitCommand).getKimikoCommand());
		return Optional.empty();
	}

	@Override
	public List<Command<CommandSender>> getCommands()
	{
		return new ArrayList<>(commands);
	}

	@Override
	public void clearCommands()
	{
		throw new UnsupportedOperationException("Sorry but clearCommands isn't supported on Bukkit.");
	}
}