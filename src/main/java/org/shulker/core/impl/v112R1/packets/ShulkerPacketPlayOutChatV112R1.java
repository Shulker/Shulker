/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v112R1.packets;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.chat.ChatMessageType;
import org.shulker.core.packets.mc.play.ShulkerPacketPlayOutChat;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;
import static org.shulker.core.Shulker.getMCManager;

public class ShulkerPacketPlayOutChatV112R1 extends ShulkerPacketPlayOutChat<PacketPlayOutChat>
{
	private static final Optional<Field> mcComponentField = getField(PacketPlayOutChat.class, "a", true);
	private static final Optional<Field> positionField    = getLastFieldOfType(PacketPlayOutChat.class, net.minecraft.server.v1_12_R1.ChatMessageType.class);

	public ShulkerPacketPlayOutChatV112R1()
	{
		this(new PacketPlayOutChat());
		mcComponentField.ifPresentOrElse(field -> setValue(packet, field, new ChatComponentText("")), () -> packet.components = new BaseComponent[0]);
		positionField.ifPresent(field -> setValue(packet, field, net.minecraft.server.v1_12_R1.ChatMessageType.SYSTEM));
	}

	public ShulkerPacketPlayOutChatV112R1(@NotNull BaseComponent... components)
	{
		this(new PacketPlayOutChat((IChatBaseComponent) getMCManager().getWrapperManager().getChatComponentWrapper().fromShulker(components)));
	}

	public ShulkerPacketPlayOutChatV112R1(PacketPlayOutChat packet)
	{
		super(packet);
	}

	@Override
	public BaseComponent[] getMessage()
	{
		return mcComponentField.map(field -> getMCManager().getWrapperManager().getChatComponentWrapper().toShulker(getFieldValue(packet, field))).orElse(packet.components);
	}

	@Override
	public void setMessage(BaseComponent... components)
	{
		mcComponentField.ifPresentOrElse(field -> setValue(packet, field, getMCManager().getWrapperManager().getChatComponentWrapper().fromShulker(components)),
										 () -> packet.components = components);
	}

	@Override
	public String getMessageRaw()
	{
		return mcComponentField.map(field -> IChatBaseComponent.ChatSerializer.a((IChatBaseComponent) getFieldValue(packet, field))).orElse(ComponentSerializer.toString(packet.components));
	}

	@Override
	public void setMessageRaw(String raw)
	{
		mcComponentField.ifPresentOrElse(field -> setValue(packet, field, IChatBaseComponent.ChatSerializer.a(raw)),
										 () -> packet.components = ComponentSerializer.parse(raw));
	}

	@Override
	public ChatMessageType getPosition()
	{
		return getMCManager().getWrapperManager().getChatMessageTypeWrapper().toShulker(getFieldValue(packet, positionField.get()));
	}

	@Override
	public void setPosition(ChatMessageType position)
	{
		setValue(packet, positionField.get(), getMCManager().getWrapperManager().getChatMessageTypeWrapper().fromShulker(position));
	}
}