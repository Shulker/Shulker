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
import org.jetbrains.annotations.NotNull;
import org.shulker.core.DebugType;
import org.shulker.core.Shulker;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class NMSPacketHandler extends PacketHandler
{
	private static final Class<?> PLAYER_CONNECTION_CLASS;
	private static final Class<?> SERVER_CONNECTION_CLASS;
	private static final Class<?> MINECRAFT_SERVER_CLASS;

	private static final Optional<Field> PLAYER_CONNECTION_FIELD;
	private static final Optional<Field> PLAYER_NETWORK_FIELD;
	private static final Optional<Field> SERVER_CONNECTION_FIELD;
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
		SERVER_CONNECTION_LIST_FIELD = getLastFieldOfType(SERVER_CONNECTION_CLASS, List.class);

		SERVER_GETSERVER_METHOD = getMethod(Bukkit.getServer().getClass(), "getServer");
	}

	private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10, new ThreadFactoryBuilder().setNameFormat("Shulker NMSHandler #%d").build());

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

		var currentList = SERVER_CONNECTION_LIST_FIELD.map(field -> (List<?>) getFieldValue(serverConnection, field)).orElse(Collections.emptyList());

		if (currentList instanceof PacketPingListenerList)
			return;

		/*Optional<Field> field1 = getField(currentList.getClass().getSuperclass(), "list", true);
		if (field1.isPresent())
		{
			Object obj = getFieldValue(currentList, field1.get());
			if (obj != null && obj.getClass().equals(PacketPingListenerList.class))
				return;
		}*/

		// New list to listen packets.
		Shulker.logDebug(DebugType.CONNECTIONS, Shulker.getPrefix(), "Injecting server handler...");
		List<?> newList = new PacketPingListenerList<>(currentList);
		SERVER_CONNECTION_LIST_FIELD.ifPresent(field -> setValue(serverConnection, field, newList));
	}

	@Override
	public void removeServerHandler()
	{
		final var server = Bukkit.getServer();

		Object minecraftServer = SERVER_GETSERVER_METHOD.map(method -> invokeMethod(server, method)).orElse(null);
		Object serverConnection = SERVER_CONNECTION_FIELD.map(field -> getFieldValue(minecraftServer, field)).orElse(null);

		var currentList = SERVER_CONNECTION_LIST_FIELD.map(field -> (List<?>) getFieldValue(serverConnection, field)).orElse(Collections.emptyList());

		if (!(currentList instanceof PacketPingListenerList))
			return;

		// Just put the old list.
		Shulker.logDebug(DebugType.CONNECTIONS, Shulker.getPrefix(), "Removing server handler...");
		((PacketPingListenerList<?>) currentList).unprocessAll();
		List oldList = ((PacketPingListenerList<?>) currentList).getDelegate();
		SERVER_CONNECTION_LIST_FIELD.ifPresent(field -> setValue(serverConnection, field, oldList));
	}

	public class PacketPingListenerList<E> implements List<E>
	{
		private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(15, new ThreadFactoryBuilder().setNameFormat("Shulker Ping Listener #%d").build());
		private List<E>                  delegate;

		public PacketPingListenerList(List<E> old)
		{
			delegate = old;
			processAll();
		}

		protected void processElement(E element)
		{
			threadPool.execute(() -> {
				Channel channel = null;
				while (channel == null)
					channel = getChannel(element);
				channel.pipeline().addBefore("packet_handler",
											 SHULKER_HANDLER + "_ping", new ShulkerPingChannelHandler());
			});
		}

		protected void unprocessElement(E element)
		{
			threadPool.execute(() -> {
				Channel channel = null;
				while (channel == null)
					channel = getChannel(element);
				channel.pipeline().remove(SHULKER_HANDLER + "_ping");
			});
		}

		protected void processAll()
		{
			delegate.forEach(this::processElement);
		}

		protected void unprocessAll()
		{
			delegate.forEach(this::unprocessElement);
		}

		public List<E> getDelegate()
		{
			return delegate;
		}

		@Override
		public synchronized boolean add(E element)
		{
			processElement(element);
			return delegate.add(element);
		}

		@Override
		public synchronized boolean addAll(@NotNull Collection<? extends E> collection)
		{
			collection.forEach(this::processElement);
			return delegate.addAll(collection);
		}

		@Override
		public synchronized E set(int index, E element)
		{
			E old = delegate.set(index, element);

			if (old != element)
			{
				unprocessElement(old);
				processElement(element);
			}
			return old;
		}

		@SuppressWarnings("unchecked")
		@Override
		public synchronized boolean remove(Object o)
		{
			unprocessElement((E) o);
			return delegate.remove(o);
		}

		@SuppressWarnings("unchecked")
		@Override
		public synchronized boolean removeAll(@NotNull Collection<?> c)
		{
			c.forEach(o -> unprocessElement((E) o));
			return delegate.removeAll(c);
		}

		@Override
		public synchronized void clear()
		{
			unprocessAll();
			delegate.clear();
		}

		// Boiler plate
		@Override
		public synchronized int size()
		{
			return delegate.size();
		}

		@Override
		public synchronized boolean isEmpty()
		{
			return delegate.isEmpty();
		}

		@Override
		public boolean contains(Object o)
		{
			return delegate.contains(o);
		}

		@NotNull
		@Override
		public synchronized Iterator<E> iterator()
		{
			return delegate.iterator();
		}

		@NotNull
		@Override
		public synchronized Object[] toArray()
		{
			return delegate.toArray();
		}

		@NotNull
		@Override
		public synchronized <T> T[] toArray(@NotNull T[] a)
		{
			return delegate.toArray(a);
		}

		@Override
		public synchronized boolean containsAll(@NotNull Collection<?> c)
		{
			return delegate.containsAll(c);
		}

		@Override
		public synchronized boolean addAll(int index, @NotNull Collection<? extends E> c)
		{
			return delegate.addAll(index, c);
		}

		@Override
		public synchronized boolean retainAll(@NotNull Collection<?> c)
		{
			return delegate.retainAll(c);
		}

		@Override
		public synchronized E get(int index)
		{
			return delegate.get(index);
		}

		@Override
		public synchronized void add(int index, E element)
		{
			delegate.add(index, element);
		}

		@Override
		public synchronized E remove(int index)
		{
			return delegate.remove(index);
		}

		@Override
		public synchronized int indexOf(Object o)
		{
			return delegate.indexOf(o);
		}

		@Override
		public synchronized int lastIndexOf(Object o)
		{
			return delegate.lastIndexOf(o);
		}

		@NotNull
		@Override
		public synchronized ListIterator<E> listIterator()
		{
			return delegate.listIterator();
		}

		@NotNull
		@Override
		public synchronized ListIterator<E> listIterator(int index)
		{
			return delegate.listIterator(index);
		}

		@NotNull
		@Override
		public synchronized List<E> subList(int fromIndex, int toIndex)
		{
			return delegate.subList(fromIndex, toIndex);
		}
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
			super.channelRead(ctx, msg);
		}

		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
		{
			super.write(ctx, msg, promise);
		}
	}

	public class ShulkerPingChannelHandler extends ChannelDuplexHandler
	{
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
		{
			if (!msg.getClass().getSimpleName().startsWith("PacketStatus"))
			{
				super.write(ctx, msg, promise);
				return;
			}
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

			super.channelRead(ctx, msg);
		}
	}
}