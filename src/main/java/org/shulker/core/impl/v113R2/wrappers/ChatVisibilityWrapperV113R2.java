/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R2.wrappers;

import net.minecraft.server.v1_13_R2.EntityHuman;
import org.mcelytra.chat.ChatVisibility;
import org.shulker.core.wrappers.ChatVisibilityWrapper;

public class ChatVisibilityWrapperV113R2 extends ChatVisibilityWrapper
{
    public static final ChatVisibilityWrapperV113R2 INSTANCE = new ChatVisibilityWrapperV113R2();

    @Override
    public Object from_shulker(ChatVisibility shulker_object)
    {
        if (shulker_object == null)
            return null;
        switch (shulker_object) {
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
    public ChatVisibility to_shulker(Object object)
    {
        if (!(object instanceof EntityHuman.EnumChatVisibility))
            return null;
        switch ((EntityHuman.EnumChatVisibility) object) {
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
    public Class<?> get_object_class()
    {
        return EntityHuman.EnumChatVisibility.class;
    }
}
