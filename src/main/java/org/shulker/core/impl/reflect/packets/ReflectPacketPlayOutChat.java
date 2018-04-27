/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.reflect.packets;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.aperlambda.lambdacommon.utils.Optional;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayOutChat;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class ReflectPacketPlayOutChat extends ShulkerPacketPlayOutChat<Object>
{
	private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
	private static final Optional<Field>                    MESSAGE_FIELD;
	private static final Optional<Field>                    COMPONENTS_FIELD;
	private static final Optional<Field>                    POSITION_FIELD;

	static
	{
		final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.getNmsClass("PacketPlayOutChat");
		Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketPlayOutChat: NMS class cannot be found!");
		CONSTRUCTOR = getConstructor(PACKET_CLASS);
		MESSAGE_FIELD = getFirstFieldOfType(PACKET_CLASS, getMCManager().getWrapperManager().getChatComponentWrapper().getObjectClass());
		COMPONENTS_FIELD = getFirstFieldOfType(PACKET_CLASS, BaseComponent[].class);
		POSITION_FIELD = getFirstFieldOfType(PACKET_CLASS, getMCManager().getWrapperManager().getChatMessageTypeWrapper().getObjectClass());
	}

	public ReflectPacketPlayOutChat()
	{
		this(newInstance(CONSTRUCTOR.get()));
		setMessage();
		setPosition(ChatMessageType.SYSTEM);
	}

	public ReflectPacketPlayOutChat(@NotNull BaseComponent... components)
	{
		this();
		setMessage(components);
	}

	public ReflectPacketPlayOutChat(Object packet)
	{
		super(packet);
	}

	@Override
	public BaseComponent[] getMessage()
	{
		return MESSAGE_FIELD.map(field -> getMCManager().getWrapperManager().getChatComponentWrapper().toShulker(getFieldValue(packet, field)))
				.getOrElse(COMPONENTS_FIELD.map(field -> getFieldValue(packet, field, new BaseComponent[0])).getOrElse(new BaseComponent[0]));
	}

	@Override
	public void setMessage(BaseComponent... components)
	{
		MESSAGE_FIELD.ifPresentOrElse(field -> setValue(packet, field, getMCManager().getWrapperManager().getChatComponentWrapper().fromShulker(components)),
									  () -> COMPONENTS_FIELD.ifPresent(field -> setValue(packet, field, components)));
	}

	@Override
	public String getMessageRaw()
	{
		return ComponentSerializer.toString(getMessage());
	}

	@Override
	public void setMessageRaw(String raw)
	{
		setMessage(ComponentSerializer.parse(raw));
	}

	@Override
	public ChatMessageType getPosition()
	{
		return getMCManager().getWrapperManager().getChatMessageTypeWrapper().toShulker(POSITION_FIELD.map(field -> getFieldValue(packet, field)).get());
	}

	@Override
	public void setPosition(ChatMessageType position)
	{
		POSITION_FIELD.ifPresent(field -> setValue(packet, field, getMCManager().getWrapperManager().getChatMessageTypeWrapper().fromShulker(position)));
	}
}