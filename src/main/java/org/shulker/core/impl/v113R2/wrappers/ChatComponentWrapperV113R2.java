/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R2.wrappers;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import org.shulker.core.wrappers.ChatComponentWrapper;

public class ChatComponentWrapperV113R2 extends ChatComponentWrapper
{
    public static final ChatComponentWrapperV113R2 INSTANCE = new ChatComponentWrapperV113R2();

    @Override
    public Object from_shulker(BaseComponent... shulker_object)
    {
        if (shulker_object == null)
            return null;
        return IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(shulker_object));
    }

    @Override
    public BaseComponent[] to_shulker(Object object)
    {
        if (!(object instanceof IChatBaseComponent))
            return null;
        return ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a((IChatBaseComponent) object));
    }

    @Override
    public Class<?> get_object_class()
    {
        return IChatBaseComponent.class;
    }
}
