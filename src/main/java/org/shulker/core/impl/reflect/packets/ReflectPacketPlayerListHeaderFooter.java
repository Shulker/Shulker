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
import org.shulker.core.Shulker;
import org.shulker.core.impl.reflect.ReflectMinecraftManager;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayerListHeaderFooter;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;

public class ReflectPacketPlayerListHeaderFooter extends ShulkerPacketPlayerListHeaderFooter<Object>
{
	private static final Class<?>                           PACKET_CLASS = ShulkerSpigotPlugin.getNmsClass("PacketPlayOutPlayerListHeaderFooter");
	private static final Optional<? extends Constructor<?>> CONSTRUCTOR  = getConstructor(PACKET_CLASS);
	private static final Optional<Field>                    HEADER_FIELD = getFirstFieldOfType(PACKET_CLASS, ReflectMinecraftManager.ICHATBASECOMPONENT_CLASS);
	private static final Optional<Field>                    FOOTER_FIELD = getLastFieldOfType(PACKET_CLASS, ReflectMinecraftManager.ICHATBASECOMPONENT_CLASS);

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
				.getOrElse(new BaseComponent[0]);
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
				.getOrElse(new BaseComponent[0]);
	}

	@Override
	public void setFooter(BaseComponent... footer)
	{
		FOOTER_FIELD.ifPresent(field -> setValue(packet, field, Shulker.getMCManager().getWrapperManager().getChatComponentWrapper().fromShulker(footer)));
	}
}