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
import org.aperlambda.lambdacommon.utils.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.Shulker;
import org.shulker.core.packets.mc.play.ShulkerPacketTitle;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class ReflectPacketTitle extends ShulkerPacketTitle<Object>
{
	private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
	private static final Optional<Field>                    ACTION_FIELD;
	private static final Optional<Field>                    CHAT_FIELD;
	private static final Optional<Field>                    FADEIN_FIELD;
	private static final Optional<Field>                    STAY_FIELD;
	private static final Optional<Field>                    FADEOUT_FIELD;

	static
	{
		final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.getNmsClass("PacketPlayOutTitle");
		Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketTitle: NMS class cannot be found!");
		CONSTRUCTOR = getConstructor(PACKET_CLASS);
		ACTION_FIELD = getFirstFieldOfType(PACKET_CLASS, getMCManager().getWrapperManager().getTitleActionWrapper().getObjectClass());
		CHAT_FIELD = getFirstFieldOfType(PACKET_CLASS, getMCManager().getWrapperManager().getChatComponentWrapper().getObjectClass());
		FADEIN_FIELD = getFirstFieldOfType(PACKET_CLASS, int.class);
		STAY_FIELD = getField(PACKET_CLASS, "d", true);
		FADEOUT_FIELD = getLastFieldOfType(PACKET_CLASS, int.class);
	}

	public ReflectPacketTitle()
	{
		super(newInstance(CONSTRUCTOR.get()));
	}

	public ReflectPacketTitle(@NotNull TitleAction action)
	{
		this();
		setAction(action);
	}

	public ReflectPacketTitle(@NotNull TitleAction action, @NotNull BaseComponent[] chatValue)
	{
		this(action);
		setChatComponentValue(chatValue);
	}

	public ReflectPacketTitle(@NotNull TitleAction action, int fadeIn, int stay, int fadeOut)
	{
		this(action);
		setTimes(fadeIn, stay, fadeOut);
	}

	public ReflectPacketTitle(Object packet)
	{
		super(packet);
	}

	@Override
	public TitleAction getAction()
	{
		return ACTION_FIELD.map(field -> getFieldValue(packet, field))
				.map(o -> Shulker.getMCManager().getWrapperManager().getTitleActionWrapper().toShulker(o))
				.getOrElse(TitleAction.RESET);
	}

	@Override
	public void setAction(TitleAction action)
	{
		ACTION_FIELD.ifPresent(field -> setValue(packet, field, getMCManager().getWrapperManager().getTitleActionWrapper().fromShulker(action)));
	}

	@Override
	public @Nullable BaseComponent[] getChatComponentValue()
	{
		return CHAT_FIELD.map(field -> getFieldValue(packet, field))
				.map(o -> Shulker.getMCManager().getWrapperManager().getChatComponentWrapper().toShulker(o))
				.getOrElse(new BaseComponent[0]);
	}

	@Override
	public void setChatComponentValue(@Nullable BaseComponent... components)
	{
		CHAT_FIELD.ifPresent(field -> setValue(packet, field, getMCManager().getWrapperManager().getChatComponentWrapper().fromShulker(components)));
	}

	@Override
	public int getFadeIn()
	{
		return FADEIN_FIELD.map(field -> getFieldValue(packet, field, 0)).getOrElse(0);
	}

	@Override
	public void setFadeIn(int fadeIn)
	{
		FADEIN_FIELD.ifPresent(field -> setValue(packet, field, fadeIn));
	}

	@Override
	public int getStay()
	{
		return STAY_FIELD.map(field -> getFieldValue(packet, field, 0)).getOrElse(0);
	}

	@Override
	public void setStay(int stay)
	{
		STAY_FIELD.ifPresent(field -> setValue(packet, field, stay));
	}

	@Override
	public int getFadeOut()
	{
		return FADEOUT_FIELD.map(field -> getFieldValue(packet, field, 0)).getOrElse(0);
	}

	@Override
	public void setFadeOut(int fadeOut)
	{
		FADEOUT_FIELD.ifPresent(field -> setValue(packet, field, fadeOut));
	}
}