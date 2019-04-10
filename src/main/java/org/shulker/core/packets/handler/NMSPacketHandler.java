/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.shulker.core.DebugType;
import org.shulker.core.Shulker;
import org.shulker.core.events.PacketEvent;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.get_mc;
import static org.shulker.core.Shulker.get_prefix;

public class NMSPacketHandler extends PacketHandler
{
    private static final Class<?> PLAYER_CONNECTION_CLASS;
    private static final Class<?> SERVER_CONNECTION_CLASS;
    private static final Class<?> MINECRAFT_SERVER_CLASS;

    private static final Optional<Field> PLAYER_CONNECTION_FIELD;
    private static final Optional<Field> PLAYER_NETWORK_FIELD;
    private static final Optional<Field> SERVER_CONNECTION_FIELD;
    private static final Optional<Field> SERVER_CONNECTION_CHANNEL_FUTURES_FIELD;
    private static final Optional<Field> SERVER_CONNECTION_LIST_FIELD;

    private static final Optional<Method> SERVER_GETSERVER_METHOD;

    private static final String SHULKER_HANDLER = "shulker_handler";

    static {
        PLAYER_CONNECTION_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.get_nms_class("PlayerConnection"), "Cannot initialize NMSPacketHandler without PlayerConnection class!");
        SERVER_CONNECTION_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.get_nms_class("ServerConnection"), "Cannot initialize NMSPacketHandler without ServerConnection class!");
        MINECRAFT_SERVER_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.get_nms_class("MinecraftServer"), "Cannot initialize NMSPacketHandler without MinecraftServer class!");

        PLAYER_CONNECTION_FIELD = get_first_field_of_type(get_mc().get_wrapper_manager().get_player_wrapper().get_object_class(), PLAYER_CONNECTION_CLASS);
        PLAYER_NETWORK_FIELD = get_field(PLAYER_CONNECTION_CLASS, "networkManager", true);
        SERVER_CONNECTION_FIELD = get_first_field_of_type(MINECRAFT_SERVER_CLASS, SERVER_CONNECTION_CLASS);
        SERVER_CONNECTION_CHANNEL_FUTURES_FIELD = get_first_field_of_type(SERVER_CONNECTION_CLASS, List.class);
        SERVER_CONNECTION_LIST_FIELD = get_last_field_of_type(SERVER_CONNECTION_CLASS, List.class);

        SERVER_GETSERVER_METHOD = get_method(Bukkit.getServer().getClass(), "getServer");
    }

    private ScheduledExecutorService thread_pool = Executors.newScheduledThreadPool(10, new ThreadFactoryBuilder().setNameFormat("Shulker NMSHandler #%d").build());

    private List<?>                      network_manager;
    // Injected channel handlers
    private List<Channel>                server_channels        = new ArrayList<>();
    private ChannelInboundHandlerAdapter server_channel_handler = new ChannelInboundHandlerAdapter()
    {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
        {
            Channel channel = (Channel) msg;
            channel.pipeline().addFirst(begin_init_protocol);
            ctx.fireChannelRead(msg);
        }
    };
    private ChannelInitializer<Channel>  begin_init_protocol    = new ChannelInitializer<>()
    {
        @Override
        protected void initChannel(Channel channel)
        {
            channel.pipeline().addLast(end_init_protocol);
        }
    };
    private ChannelInitializer<Channel>  end_init_protocol      = new ChannelInitializer<>()
    {
        @Override
        protected void initChannel(Channel channel)
        {
            try {
                synchronized (network_manager) {
                    if (run)
                        channel.eventLoop().submit(() -> channel.pipeline().addBefore("packet_handler",
                                SHULKER_HANDLER + "_ping", new ShulkerPingChannelHandler()));
                }
            } catch (Exception e) {
                Shulker.log_error(get_prefix(), "Cannot inject incoming channel " + channel);
                e.printStackTrace();
            }
        }
    };

    private static Channel get_channel(Object network)
    {
        if (network == null)
            return null;
        return get_first_field_of_type(network.getClass(), Channel.class).map(field -> get_field_value(network, field, (Channel) null)).orElse(null);
    }

    @Override
    public void add_handler(Player player)
    {
        final Object handle = get_mc().get_wrapper_manager().get_player_wrapper().from_shulker(player);
        try {
            final Object connection = PLAYER_CONNECTION_FIELD.map(field -> get_field_value(handle, field)).orElse(null);
            final var channel = get_channel(PLAYER_NETWORK_FIELD.map(field -> get_field_value(connection, field)).orElse(null));

            if (channel != null)
                thread_pool.execute(() -> {
                    try {
                        var handler = new ShulkerChannelHandler(player);
                        channel.pipeline().addBefore("packet_handler", SHULKER_HANDLER, handler);
                        Shulker.log_debug(DebugType.CONNECTIONS, Shulker.get_prefix(), handler.getClass().getName() + " added to " + player.getName() + ".");
                    } catch (Exception ignored) {
                        // Ignored error.
                    }
                });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove_handler(Player player)
    {
        final Object handle = get_mc().get_wrapper_manager().get_player_wrapper().from_shulker(player);
        try {
            final Object connection = PLAYER_CONNECTION_FIELD.map(field -> get_field_value(handle, field)).orElse(null);
            final var channel = get_channel(PLAYER_NETWORK_FIELD.map(field -> get_field_value(connection, field)).orElse(null));

            if (channel != null)
                thread_pool.execute(() -> {
                    try {
                        var handler = channel.pipeline().get(SHULKER_HANDLER);
                        channel.pipeline().remove(SHULKER_HANDLER);
                        Shulker.log_debug(DebugType.CONNECTIONS, Shulker.get_prefix(), handler.getClass().getName() + " removed to " + player.getName() + ".");
                    } catch (Exception ignored) {
                        // Ignored error.
                    }
                });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add_server_handler()
    {
        final var server = Bukkit.getServer();

        Object minecraft_server = SERVER_GETSERVER_METHOD.map(method -> invoke_method(server, method)).orElse(null);
        Object server_connection = SERVER_CONNECTION_FIELD.map(field -> get_field_value(minecraft_server, field)).orElse(null);

        network_manager = SERVER_CONNECTION_LIST_FIELD.map(field -> (List<?>) get_field_value(server_connection, field)).orElse(Collections.emptyList());

        List<?> futures = SERVER_CONNECTION_CHANNEL_FUTURES_FIELD.map(field -> (List<?>) get_field_value(server_connection, field)).orElse(Collections.emptyList());

        futures.forEach(future -> {
            if (!ChannelFuture.class.isInstance(future))
                return;
            Channel channel = ((ChannelFuture) future).channel();

            server_channels.add(channel);
            channel.pipeline().addFirst(server_channel_handler);
        });

        // For the old code, please see git history.
    }

    @Override
    public void remove_server_handler()
    {
        server_channels.forEach(channel -> {
            ChannelPipeline pipeline = channel.pipeline();
            channel.eventLoop().execute(() -> pipeline.remove(server_channel_handler));
        });

        // For the old code, please see the git history.
    }

    public class ShulkerChannelHandler extends ChannelDuplexHandler
    {
        private Player player;

        public ShulkerChannelHandler(Player player)
        {
            this.player = player;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
        {
            var ip = ctx.channel().remoteAddress();
            var packet = Shulker.get_mc().from_packet(msg);
            var packet_event = new PacketEvent(ip, player, packet);

            Shulker.get().fire_packet_event(packet_event, true);

            if (!packet_event.is_cancelled())
                super.channelRead(ctx, msg);
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
        {
            var ip = ctx.channel().remoteAddress();
            var packet = Shulker.get_mc().from_packet(msg);
            var packet_event = new PacketEvent(ip, player, packet);

            Shulker.get().fire_packet_event(packet_event, false);

            if (!packet_event.is_cancelled())
                super.write(ctx, msg, promise);
        }
    }

    public class ShulkerPingChannelHandler extends ChannelDuplexHandler
    {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
        {
            if (!msg.getClass().getSimpleName().startsWith("PacketStatus")) {
                super.write(ctx, msg, promise);
                return;
            }

            var ip = ctx.channel().remoteAddress();
            var packet = Shulker.get_mc().from_packet(msg);
            var packet_event = new PacketEvent(ip, packet);

            Shulker.get().fire_packet_event(packet_event, false);

            if (!packet_event.is_cancelled())
                super.write(ctx, msg, promise);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
        {
            // Logs connections
            if (msg.getClass().getSimpleName().equals("PacketHandshakingInSetProtocol"))
                Shulker.log_debug(DebugType.CONNECTIONS, Shulker.get_prefix(), "[" +
                        ctx.channel().remoteAddress().toString() + "] " + getClass().getSimpleName() +
                        " is connected.");

            if ((!msg.getClass().getSimpleName().startsWith("PacketStatus")) ||
                    (!msg.getClass().getSimpleName().equals("PacketHandshakingInSetProtocol"))) {
                super.channelRead(ctx, msg);
                return;
            }

            var ip = ctx.channel().remoteAddress();
            var packet = Shulker.get_mc().from_packet(msg);
            var packet_event = new PacketEvent(ip, packet);

            Shulker.get().fire_packet_event(packet_event, true);

            if (!packet_event.is_cancelled())
                super.channelRead(ctx, msg);
        }
    }
}
