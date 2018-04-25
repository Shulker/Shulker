/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v112R1.wrappers;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.shulker.core.wrappers.ChatComponentWrapper;

public class ChatComponentWrapperV112R1 extends ChatComponentWrapper
{
	public static final ChatComponentWrapperV112R1 INSTANCE = new ChatComponentWrapperV112R1();

	@Override
	public Object fromShulker(BaseComponent ... shulkerObject)
	{
		return IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(shulkerObject));
	}

	@Override
	public BaseComponent[] toShulker(Object object)
	{
		return ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a((IChatBaseComponent) object));
	}
}