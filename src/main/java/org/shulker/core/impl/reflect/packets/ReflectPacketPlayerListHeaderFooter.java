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
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayerListHeaderFooter;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class ReflectPacketPlayerListHeaderFooter extends ShulkerPacketPlayerListHeaderFooter<Object>
{
	private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
	private static final Optional<Field>                    HEADER_FIELD;
	private static final Optional<Field>                    FOOTER_FIELD;

	static
	{
		final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.getNmsClass("PacketPlayOutPlayerListHeaderFooter");
		Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketPlayerListHeaderFooter: NMS class cannot be found!");
		CONSTRUCTOR = getConstructor(PACKET_CLASS);
		HEADER_FIELD = getFirstFieldOfType(PACKET_CLASS, getMCManager().getWrapperManager().getChatComponentWrapper().getObjectClass());
		FOOTER_FIELD = getLastFieldOfType(PACKET_CLASS, getMCManager().getWrapperManager().getChatComponentWrapper().getObjectClass());
	}

	public ReflectPacketPlayerListHeaderFooter()
	{
		this(new BaseComponent[0], new BaseComponent[0]);
	}

	public ReflectPacketPlayerListHeaderFooter(@NotNull BaseComponent[] header, @NotNull BaseComponent[] footer)
	{
		this(newInstance(CONSTRUCTOR.get()));
		setHeader(header);
		setFooter(footer);
	}

	public ReflectPacketPlayerListHeaderFooter(Object packet)
	{
		super(packet);
	}

	@Override
	public BaseComponent[] getHeader()
	{
		return HEADER_FIELD.map(field -> getFieldValue(packet, field))
				.map(o -> Shulker.getMCManager().getWrapperManager().getChatComponentWrapper().toShulker(o))
				.orElse(new BaseComponent[0]);
	}

	@Override
	public void setHeader(BaseComponent... header)
	{
		HEADER_FIELD.ifPresent(field -> setValue(packet, field, Shulker.getMCManager().getWrapperManager().getChatComponentWrapper().fromShulker(header)));
	}

	@Override
	public BaseComponent[] getFooter()
	{
		return FOOTER_FIELD.map(field -> getFieldValue(packet, field))
				.map(o -> Shulker.getMCManager().getWrapperManager().getChatComponentWrapper().toShulker(o))
				.orElse(new BaseComponent[0]);
	}

	@Override
	public void setFooter(BaseComponent... footer)
	{
		FOOTER_FIELD.ifPresent(field -> setValue(packet, field, Shulker.getMCManager().getWrapperManager().getChatComponentWrapper().fromShulker(footer)));
	}
}