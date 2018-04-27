/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v112R1.wrappers;

import org.mcelytra.chat.ChatMessageType;
import org.shulker.core.wrappers.ChatMessageTypeWrapper;

public class ChatMessageTypeWrapperV112R1 extends ChatMessageTypeWrapper
{
	public static final ChatMessageTypeWrapperV112R1 INSTANCE = new ChatMessageTypeWrapperV112R1();

	@Override
	public Object fromShulker(ChatMessageType shulkerObject)
	{
		if (shulkerObject == null)
			return null;
		switch (shulkerObject)
		{
			case CHAT:
				return net.minecraft.server.v1_12_R1.ChatMessageType.CHAT;
			case SYSTEM:
				return net.minecraft.server.v1_12_R1.ChatMessageType.SYSTEM;
			case ACTION_BAR:
				return net.minecraft.server.v1_12_R1.ChatMessageType.GAME_INFO;
		}
		return null;
	}

	@Override
	public ChatMessageType toShulker(Object object)
	{
		if (!(object instanceof net.minecraft.server.v1_12_R1.ChatMessageType))
			return null;
		switch ((net.minecraft.server.v1_12_R1.ChatMessageType) object)
		{
			case CHAT:
				return ChatMessageType.CHAT;
			case SYSTEM:
				return ChatMessageType.SYSTEM;
			case GAME_INFO:
				return ChatMessageType.ACTION_BAR;
		}
		return null;
	}

	@Override
	public Class<?> getObjectClass()
	{
		return net.minecraft.server.v1_12_R1.ChatMessageType.class;
	}
}