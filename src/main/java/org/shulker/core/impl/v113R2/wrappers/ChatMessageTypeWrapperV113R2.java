/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v113R2.wrappers;

import org.mcelytra.chat.ChatMessageType;
import org.shulker.core.wrappers.ChatMessageTypeWrapper;

public class ChatMessageTypeWrapperV113R2 extends ChatMessageTypeWrapper
{
    public static final ChatMessageTypeWrapperV113R2 INSTANCE = new ChatMessageTypeWrapperV113R2();

    @Override
    public Object from_shulker(ChatMessageType shulker_object)
    {
        if (shulker_object == null)
            return null;
        switch (shulker_object) {
            case CHAT:
                return net.minecraft.server.v1_13_R2.ChatMessageType.CHAT;
            case SYSTEM:
                return net.minecraft.server.v1_13_R2.ChatMessageType.SYSTEM;
            case ACTION_BAR:
                return net.minecraft.server.v1_13_R2.ChatMessageType.GAME_INFO;
        }
        return null;
    }

    @Override
    public ChatMessageType to_shulker(Object object)
    {
        if (!(object instanceof net.minecraft.server.v1_13_R2.ChatMessageType))
            return null;
        switch ((net.minecraft.server.v1_13_R2.ChatMessageType) object) {
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
    public Class<?> get_object_class()
    {
        return net.minecraft.server.v1_13_R2.ChatMessageType.class;
    }
}
