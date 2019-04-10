/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.entity;

import net.md_5.bungee.api.chat.BaseComponent;
import org.mcelytra.chat.ChatMessageType;
import org.mcelytra.chat.ChatVisibility;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.DebugType;
import org.shulker.core.Shulker;
import org.shulker.core.packets.ShulkerPacket;
import org.shulker.core.packets.mc.play.ShulkerPacketTitle;

/**
 * ShulkerPlayer represents a Minecraft player.
 *
 * @param <T> The server object type.
 */
public interface ShulkerPlayer<T>
{
    /**
     * Sends a message to the player.
     * <p>NOTE: The default {@code ChatMessageType} here is {@code SYSTEM}. Please respect the use of the types.</p>
     *
     * @param message The message to send.
     * @see ShulkerPlayer#send_message(ChatMessageType, BaseComponent...)
     */
    default void send_message(BaseComponent... message)
    {
        send_message(ChatMessageType.SYSTEM, message);
    }

    /**
     * Sends a message to the player.
     * <p><b>NOTE:</b> Please use {@link #send_action_bar_message(BaseComponent...)} for {@link ChatMessageType#ACTION_BAR action bar type}.</p>
     *
     * @param type    The type of the message.
     * @param message The message to send.
     */
    void send_message(ChatMessageType type, BaseComponent... message);

    /**
     * Sends a message in the action bar to the player.
     *
     * @param message The component to send.
     */
    default void send_action_bar_message(BaseComponent... message)
    {
        send_packet(Shulker.get_mc().new_packet_title(ShulkerPacketTitle.TitleAction.ACTION_BAR, message));
    }

    /**
     * Sends a title to the player.
     *
     * @param title    The title.
     * @param subTitle The subtitle.
     * @param fadeIn   Ticks to spend fading in.
     * @param stay     Ticks to keep the title displayed.
     * @param fadeOut  Ticks to spend fading out.
     */
    default void send_title(BaseComponent[] title, BaseComponent[] subTitle, int fadeIn, int stay, int fadeOut)
    {
        // Note: here I don't use the Spigot Title API because there isn't any instance of Player available here.

        // Reset
        reset_title();
        // Title
        if (title != null)
            send_packet(Shulker.get_mc().new_packet_title(ShulkerPacketTitle.TitleAction.TITLE, title));
        // Subtitle
        if (subTitle != null)
            send_packet(Shulker.get_mc().new_packet_title(ShulkerPacketTitle.TitleAction.SUBTITLE, subTitle));

        send_packet(Shulker.get_mc().new_packet_title(ShulkerPacketTitle.TitleAction.TIMES, fadeIn, stay, fadeOut));
    }

    /**
     * Resets the title
     */
    default void reset_title()
    {
        send_packet(Shulker.get_mc().new_packet_title(ShulkerPacketTitle.TitleAction.RESET));
    }

    /**
     * Sends a Shulker's packet to the player.
     *
     * @param packet The packet to send.
     */
    default void send_packet(@NotNull ShulkerPacket<?> packet)
    {
        Shulker.log_debug(DebugType.PACKETS, Shulker.get_prefix(), "Packet send '" + packet.toString() + "'.");
        send_raw_packet(packet.get_handle());
    }

    /**
     * Sends a raw packet to the player.
     *
     * @param raw_packet The packet to send.
     */
    void send_raw_packet(Object raw_packet);

    /**
     * Gets the ping of the player in milliseconds.
     *
     * @return The ping of the player.
     */
    int get_ping();

    /**
     * Gets the locale used by the player.
     *
     * @return The locale as a string.
     */
    String get_locale();

    /**
     * Gets the chat visibility flag of the player.
     *
     * @return The chat visibility flag.
     */
    ChatVisibility get_chat_visibility();

    /**
     * Gets the player handle.
     *
     * @return The handle.
     */
    @NotNull T get_player_handle();
}
