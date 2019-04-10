/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
import org.aperlambda.kimiko.Command;
import org.aperlambda.kimiko.CommandBuilder;
import org.aperlambda.kimiko.CommandContext;
import org.aperlambda.kimiko.CommandResult;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.DebugType;
import org.shulker.core.Shulker;
import org.shulker.core.commands.BukkitCommandExecutor;
import org.shulker.core.commands.BukkitCommandResult;
import org.shulker.core.commands.BukkitCommandTabCompleter;
import org.shulker.core.inventory.InventoryType;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.util.ArrayList;
import java.util.List;

import static net.md_5.bungee.api.ChatColor.*;
import static org.shulker.core.Shulker.get_mc;

public class TestCommand implements BukkitCommandExecutor, BukkitCommandTabCompleter
{
    private Command<CommandSender> command;
    private ShulkerSpigotPlugin    plugin;

    public TestCommand()
    {
        command = new CommandBuilder<CommandSender>(new ResourceName("Shulker", "test"))
                .usage("<command>")
                .description("OwO")
                .permission("shulker.commands.test")
                .executor(this)
                .tab_completer(this)
                .build();
        plugin = (ShulkerSpigotPlugin) Shulker.get();
    }

    @Override
    public @NotNull CommandResult execute(CommandContext<CommandSender> context, @Nullable Command<CommandSender> command, String label, String[] args)
    {
        if (!(context.get_sender() instanceof Player))
            return BukkitCommandResult.ERROR_ONLY_PLAYER;

        if (args.length != 0)
            return CommandResult.ERROR_USAGE;

        Shulker.log_debug(DebugType.BASE, Shulker.get_prefix(), "{message:\"Hello world\",int:52,null:null}");

        var player = (Player) context.get_sender();
        var shPlayer = get_mc().get_player(player);
        var components = new ComponentBuilder("Hello, it's a ").color(GREEN).append("test").color(DARK_AQUA).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Hover event!\nPing: ").append(
                "" +
                        shPlayer.get_ping()).color(GOLD).create())).append(" of the method send_message(BaseComponent)!", ComponentBuilder.FormatRetention.NONE).color(GREEN).create();
        var hello = new ComponentBuilder("Hello, world!").color(GREEN).create();
        var componentsOwO = new ComponentBuilder("OwO\n").append("Ping: ").color(GREEN).append(
                "" + shPlayer.get_ping()).color(GOLD).create();
        var owo = new BaseComponent[]{new TextComponent("OwO")};
        var emptyItem = new ItemStack(Material.AIR);

        shPlayer.send_message(components);

        shPlayer.send_packet(get_mc().new_packet_play_out_playerlist_header_footer(componentsOwO,
                new TextComponent[]{
                        new TextComponent("OwO")}));

        shPlayer.send_title(hello, owo, 20, 70, 10);
        shPlayer.send_action_bar_message(hello);

        player.sendMessage("Opening window with title 'owo'");

        shPlayer.send_packet(get_mc().new_packet_play_out_open_window(5, InventoryType.CONTAINER.toString(), owo, 9));
        shPlayer.send_packet(get_mc().new_packet_play_out_window_items(5, emptyItem, emptyItem, emptyItem, emptyItem, new ItemStack(Material.APPLE)));

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> shPlayer.send_packet(get_mc().new_packet_play_out_set_slot(5, 2, new ItemStack(Material.DIAMOND))), 25L);

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            player.sendMessage("Open your inventory!");
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> shPlayer.send_packet(get_mc().new_packet_play_out_window_items(0, new ItemStack(Material.DIAMOND), new ItemStack(Material.ANVIL), new ItemStack(Material.BEACON), new ItemStack(Material.CHORUS_PLANT))), 50L);
        }, 150L);
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