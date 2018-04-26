/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v112R1.wrappers;

import net.minecraft.server.v1_12_R1.EntityHuman;
import org.shulker.core.chat.ChatVisibility;
import org.shulker.core.wrappers.ChatVisibilityWrapper;

public class ChatVisibilityWrapperV112R1 extends ChatVisibilityWrapper
{
	public static final ChatVisibilityWrapperV112R1 INSTANCE = new ChatVisibilityWrapperV112R1();

	@Override
	public Object fromShulker(ChatVisibility shulkerObject)
	{
		if (shulkerObject == null)
			return null;
		switch (shulkerObject)
		{
			case FULL:
				return EntityHuman.EnumChatVisibility.FULL;
			case SYSTEM:
				return EntityHuman.EnumChatVisibility.SYSTEM;
			case HIDDEN:
				return EntityHuman.EnumChatVisibility.HIDDEN;
		}
		return null;
	}

	@Override
	public ChatVisibility toShulker(Object object)
	{
		if (!(object instanceof EntityHuman.EnumChatVisibility))
			return null;
		switch ((EntityHuman.EnumChatVisibility) object)
		{
			case FULL:
				return ChatVisibility.FULL;
			case SYSTEM:
				return ChatVisibility.SYSTEM;
			case HIDDEN:
				return ChatVisibility.HIDDEN;
		}
		return null;
	}

	@Override
	public Class<?> getObjectClass()
	{
		return EntityHuman.EnumChatVisibility.class;
	}
}