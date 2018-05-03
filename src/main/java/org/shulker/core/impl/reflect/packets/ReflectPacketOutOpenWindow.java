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
import org.shulker.core.Shulker;
import org.shulker.core.packets.mc.play.ShulkerPacketOutOpenWindow;
import org.shulker.spigot.ShulkerSpigotPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class ReflectPacketOutOpenWindow extends ShulkerPacketOutOpenWindow<Object>
{
	private static final Optional<? extends Constructor<?>> CONSTRUCTOR;
	private static final Optional<Field>                    WINDOW_ID_FIELD;
	private static final Optional<Field>                    WINDOW_TYPE_FIELD;
	private static final Optional<Field>                    TITLE_FIELD;
	private static final Optional<Field>                    SLOTS_FIELD;
	private static final Optional<Field>                    ENTITY_ID_FIELD;

	static
	{
		final Class<?> PACKET_CLASS = ShulkerSpigotPlugin.getNmsClass("PacketPlayOutOpenWindow");
		Objects.requireNonNull(PACKET_CLASS, "Cannot initialize ReflectPacketOutOpenWindow: NMS class cannot be found!");
		CONSTRUCTOR = getConstructor(PACKET_CLASS);
		WINDOW_ID_FIELD = getFirstFieldOfType(PACKET_CLASS, int.class);
		WINDOW_TYPE_FIELD = getFirstFieldOfType(PACKET_CLASS, String.class);
		TITLE_FIELD = getFirstFieldOfType(PACKET_CLASS, getMCManager().getWrapperManager().getChatComponentWrapper().getObjectClass());
		SLOTS_FIELD = getField(PACKET_CLASS, "d", true);
		ENTITY_ID_FIELD = getLastFieldOfType(PACKET_CLASS, int.class);
	}

	public ReflectPacketOutOpenWindow()
	{
		this(CONSTRUCTOR.map(constructor -> newInstance(constructor)).orElseThrow(() -> new RuntimeException("Cannot initialize ReflectPacketOutOpenWindow: constructor cannot be found!")));
	}

	public ReflectPacketOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots)
	{
		this();
		setWindowId(windowId);
		setWindowType(windowType);
		setTitle(title);
		setSlots(slots);
	}

	public ReflectPacketOutOpenWindow(int windowId, String windowType, BaseComponent[] title, int slots, int entityId)
	{
		this(windowId, windowType, title, slots);
		setEntityId(entityId);
	}

	public ReflectPacketOutOpenWindow(Object packet)
	{
		super(packet);
	}

	@Override
	public int getWindowId()
	{
		return WINDOW_ID_FIELD.map(field -> getFieldValue(packet, field, 0)).orElse(0);
	}

	@Override
	public void setWindowId(int id)
	{
		WINDOW_ID_FIELD.ifPresentOrElse(field -> setValue(packet, field, id), () -> {
			throw new RuntimeException("Cannot set window id (Field not found).");
		});
	}

	@Override
	public String getWindowType()
	{
		return WINDOW_TYPE_FIELD.map(field -> getFieldValue(packet, field, "")).orElse("");
	}

	@Override
	public void setWindowType(String windowType)
	{
		WINDOW_TYPE_FIELD.ifPresentOrElse(field -> setValue(packet, field, windowType),
										  () -> {
											  throw new RuntimeException("Cannot set window type (Field not found).");
										  });
	}

	@Override
	public BaseComponent[] getTitle()
	{
		return TITLE_FIELD.map(field -> getFieldValue(packet, field))
				.map(o -> Shulker.getMCManager().getWrapperManager().getChatComponentWrapper().toShulker(o))
				.orElse(new BaseComponent[0]);
	}

	@Override
	public void setTitle(BaseComponent... title)
	{
		TITLE_FIELD.ifPresentOrElse(field -> setValue(packet, field, Shulker.getMCManager().getWrapperManager().getChatComponentWrapper().fromShulker(title)),
									() -> {
										throw new RuntimeException("Cannot set window type (Field not found).");
									});
	}

	@Override
	public int getSlots()
	{
		return SLOTS_FIELD.map(field -> getFieldValue(packet, field, 0)).orElse(0);
	}

	@Override
	public void setSlots(int slots)
	{
		SLOTS_FIELD.ifPresentOrElse(field -> setValue(packet, field, slots),
									() -> {
										throw new RuntimeException("Cannot set window slots (Field not found).");
									});
	}

	@Override
	public int getEntityId()
	{
		return ENTITY_ID_FIELD.map(field -> getFieldValue(packet, field, 0)).orElse(0);
	}

	@Override
	public void setEntityId(int entityId)
	{
		ENTITY_ID_FIELD.ifPresentOrElse(field -> setValue(packet, field, entityId), () -> {
			throw new RuntimeException("Cannot set window entity id (Field not found).");
		});
	}
}
