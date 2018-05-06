/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
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
import static org.shulker.core.Shulker.getMCManager;
import static org.shulker.core.Shulker.getPrefix;

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

	static
	{
		PLAYER_CONNECTION_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.getNmsClass("PlayerConnection"), "Cannot initialize NMSPacketHandler without PlayerConnection class!");
		SERVER_CONNECTION_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.getNmsClass("ServerConnection"), "Cannot initialize NMSPacketHandler without ServerConnection class!");
		MINECRAFT_SERVER_CLASS = Objects.requireNonNull(ShulkerSpigotPlugin.getNmsClass("MinecraftServer"), "Cannot initialize NMSPacketHandler without MinecraftServer class!");

		PLAYER_CONNECTION_FIELD = getFirstFieldOfType(getMCManager().getWrapperManager().getPlayerWrapper().getObjectClass(), PLAYER_CONNECTION_CLASS);
		PLAYER_NETWORK_FIELD = getField(PLAYER_CONNECTION_CLASS, "networkManager", true);
		SERVER_CONNECTION_FIELD = getFirstFieldOfType(MINECRAFT_SERVER_CLASS, SERVER_CONNECTION_CLASS);
		SERVER_CONNECTION_CHANNEL_FUTURES_FIELD = getFirstFieldOfType(SERVER_CONNECTION_CLASS, List.class);
		SERVER_CONNECTION_LIST_FIELD = getLastFieldOfType(SERVER_CONNECTION_CLASS, List.class);

		SERVER_GETSERVER_METHOD = getMethod(Bukkit.getServer().getClass(), "getServer");
	}

	private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10, new ThreadFactoryBuilder().setNameFormat("Shulker NMSHandler #%d").build());

	private List<?>                      networkManagers;
	// Injected channel handlers
	private List<Channel>                serverChannels       = new ArrayList<>();
	private ChannelInboundHandlerAdapter serverChannelHandler = new ChannelInboundHandlerAdapter()
	{
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
		{
			Channel channel = (Channel) msg;
			channel.pipeline().addFirst(beginInitProtocol);
			ctx.fireChannelRead(msg);
		}
	};
	private ChannelInitializer<Channel>  beginInitProtocol    = new ChannelInitializer<>()
	{
		@Override
		protected void initChannel(Channel channel)
		{
			channel.pipeline().addLast(endInitProtocol);
		}
	};
	private ChannelInitializer<Channel>  endInitProtocol      = new ChannelInitializer<>()
	{
		@Override
		protected void initChannel(Channel channel)
		{
			try
			{
				synchronized (networkManagers)
				{
					if (run)
						channel.eventLoop().submit(() -> channel.pipeline().addBefore("packet_handler",
																					  SHULKER_HANDLER +
																							  "_ping", new ShulkerPingChannelHandler()));
				}
			}
			catch (Exception e)
			{
				Shulker.logError(getPrefix(), "Cannot inject incoming channel " + channel);
				e.printStackTrace();
			}
		}
	};

	private static Channel getChannel(Object network)
	{
		if (network == null)
			return null;
		return getFirstFieldOfType(network.getClass(), Channel.class).map(field -> getFieldValue(network, field, (Channel) null)).orElse(null);
	}

	@Override
	public void addHandler(Player player)
	{
		final Object handle = getMCManager().getWrapperManager().getPlayerWrapper().fromShulker(player);
		try
		{
			final Object connection = PLAYER_CONNECTION_FIELD.map(field -> getFieldValue(handle, field)).orElse(null);
			final var channel = getChannel(PLAYER_NETWORK_FIELD.map(field -> getFieldValue(connection, field)).orElse(null));

			if (channel != null)
				threadPool.execute(() -> {
					try
					{
						var handler = new ShulkerChannelHandler(player);
						channel.pipeline().addBefore("packet_handler", SHULKER_HANDLER, handler);
						Shulker.logDebug(DebugType.CONNECTIONS, Shulker.getPrefix(),
										 handler.getClass().getName() + " added to " + player.getName() + ".");
					}
					catch (Exception ignored)
					{
						// Ignored error.
					}
				});
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void removeHandler(Player player)
	{
		final Object handle = getMCManager().getWrapperManager().getPlayerWrapper().fromShulker(player);
		try
		{
			final Object connection = PLAYER_CONNECTION_FIELD.map(field -> getFieldValue(handle, field)).orElse(null);
			final var channel = getChannel(PLAYER_NETWORK_FIELD.map(field -> getFieldValue(connection, field)).orElse(null));

			if (channel != null)
				threadPool.execute(() -> {
					try
					{
						var handler = channel.pipeline().get(SHULKER_HANDLER);
						channel.pipeline().remove(SHULKER_HANDLER);
						Shulker.logDebug(DebugType.CONNECTIONS, Shulker.getPrefix(),
										 handler.getClass().getName() + " removed to " + player.getName() + ".");
					}
					catch (Exception ignored)
					{
						// Ignored error.
					}
				});
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void addServerHandler()
	{
		final var server = Bukkit.getServer();

		Object minecraftServer = SERVER_GETSERVER_METHOD.map(method -> invokeMethod(server, method)).orElse(null);
		Object serverConnection = SERVER_CONNECTION_FIELD.map(field -> getFieldValue(minecraftServer, field)).orElse(null);

		networkManagers = SERVER_CONNECTION_LIST_FIELD.map(field -> (List<?>) getFieldValue(serverConnection, field)).orElse(Collections.emptyList());

		List<?> futures = SERVER_CONNECTION_CHANNEL_FUTURES_FIELD.map(field -> (List<?>) getFieldValue(serverConnection, field)).orElse(Collections.emptyList());

		futures.forEach(future -> {
			if (!ChannelFuture.class.isInstance(future))
				return;
			Channel channel = ((ChannelFuture) future).channel();

			serverChannels.add(channel);
			channel.pipeline().addFirst(serverChannelHandler);
		});

		// For the old code, please see git history.
	}

	@Override
	public void removeServerHandler()
	{
		final var server = Bukkit.getServer();

		Object minecraftServer = SERVER_GETSERVER_METHOD.map(method -> invokeMethod(server, method)).orElse(null);
		Object serverConnection = SERVER_CONNECTION_FIELD.map(field -> getFieldValue(minecraftServer, field)).orElse(null);

		serverChannels.forEach(channel -> {
			ChannelPipeline pipeline = channel.pipeline();
			channel.eventLoop().execute(() -> pipeline.remove(serverChannelHandler));
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
			var packet = Shulker.getMCManager().fromPacket(msg);
			var packetEvent = new PacketEvent(ip, player, packet);

			Shulker.getShulker().firePacketEvent(packetEvent, true);

			if (!packetEvent.isCancelled())
				super.channelRead(ctx, msg);
		}

		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
		{
			var ip = ctx.channel().remoteAddress();
			var packet = Shulker.getMCManager().fromPacket(msg);
			var packetEvent = new PacketEvent(ip, player, packet);

			Shulker.getShulker().firePacketEvent(packetEvent, false);

			if (!packetEvent.isCancelled())
				super.write(ctx, msg, promise);
		}
	}

	public class ShulkerPingChannelHandler extends ChannelDuplexHandler
	{
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
		{
			System.out.println(msg.getClass().getSimpleName());
			if (!msg.getClass().getSimpleName().startsWith("PacketStatus"))
			{
				super.write(ctx, msg, promise);
				return;
			}

			var ip = ctx.channel().remoteAddress();
			var packet = Shulker.getMCManager().fromPacket(msg);
			var packetEvent = new PacketEvent(ip, packet);

			Shulker.getShulker().firePacketEvent(packetEvent, false);

			if (!packetEvent.isCancelled())
				super.write(ctx, msg, promise);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
		{
			// Logs connections
			if (msg.getClass().getSimpleName().equals("PacketHandshakingInSetProtocol"))
				Shulker.logDebug(DebugType.CONNECTIONS, Shulker.getPrefix(), "[" +
						ctx.channel().remoteAddress().toString() + "] " + getClass().getSimpleName() +
						" is connected.");

			if ((!msg.getClass().getSimpleName().startsWith("PacketStatus")) ||
					(!msg.getClass().getSimpleName().equals("PacketHandshakingInSetProtocol")))
			{
				super.channelRead(ctx, msg);
				return;
			}

			var ip = ctx.channel().remoteAddress();
			var packet = Shulker.getMCManager().fromPacket(msg);
			var packetEvent = new PacketEvent(ip, packet);

			Shulker.getShulker().firePacketEvent(packetEvent, true);

			if (!packetEvent.isCancelled())
				super.channelRead(ctx, msg);
		}
	}
}