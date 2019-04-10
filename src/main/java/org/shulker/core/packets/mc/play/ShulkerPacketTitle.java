/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets.mc.play;

import net.md_5.bungee.api.chat.BaseComponent;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.packets.ShulkerPacket;

import java.util.Arrays;

/**
 * Represents the packet which manages the titles.
 * <p>
 * {@link TitleAction#HIDE} makes the title disappear, but if you run times again the same title will appear. {@link TitleAction#RESET} erases the text.
 * The title is visible on screen for Fade In + Stay + Fade Out ticks.
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketTitle<T> extends ShulkerPacket<T>
{
    public ShulkerPacketTitle(T packet)
    {
        super(packet);
    }

    /**
     * Gets the action to execute of the title.
     *
     * @return The action to performe.
     */
    public abstract TitleAction get_action();

    /**
     * Sets the action to execute of the title.
     *
     * @param action The action to perform.
     */
    public abstract void set_action(TitleAction action);

    /**
     * Gets the ChatComponent value.
     * It's defined for some actions:
     * <ul>
     * <li>{@link TitleAction#TITLE TITLE}</li>
     * <li>{@link TitleAction#SUBTITLE SUBTITLE}</li>
     * <li>{@link TitleAction#ACTION_BAR ACTION_BAR}</li>
     * </ul>
     *
     * @return The ChatComponent value.
     */
    public abstract @Nullable BaseComponent[] get_chat_component_value();

    /**
     * Sets the ChatComponent value.
     * The value can be null, only define it for the actions: {@code TITLE}, {@code SUBTITLE}, {@code ACTION_BAR}.
     *
     * @param components The ChatComponent value, mays be null.
     */
    public abstract void set_chat_component_value(@Nullable BaseComponent... components);

    /**
     * Gets the fade in of the title.
     * <p>Defined if the action is {@link TitleAction#TIMES TIMES}</p>
     *
     * @return Ticks to spend fading in.
     */
    public abstract int get_fade_in();

    /**
     * Sets the fade in of the title.
     * <p>Use it with {@link TitleAction#TIMES TIMES action}</p>
     *
     * @param fade_in Ticks to spend fading in.
     */
    public abstract void set_fade_in(int fade_in);

    /**
     * Gets the stay time of the title.
     * <p>Defined if the action is {@link TitleAction#TIMES TIMES}</p>
     *
     * @return Ticks to keep the title displayed.
     */
    public abstract int get_stay();

    /**
     * Sets the stay time of the title.
     * <p>Use it with {@link TitleAction#TIMES TIMES action}</p>
     *
     * @param stay Ticks to keep the title displayed.
     */
    public abstract void set_stay(int stay);

    /**
     * Gets the fade out of the title.
     * <p>Defined if the action is {@link TitleAction#TIMES TIMES}</p>
     *
     * @return Ticks to spend fading out.
     */
    public abstract int get_fade_out();

    /**
     * Sets the fade out of the title.
     * <p>Use it with {@link TitleAction#TIMES TIMES action}</p>
     *
     * @param fade_out Ticks to spend fading out, not when to start fading out.
     */
    public abstract void set_fade_out(int fade_out);

    /**
     * Sets the times and display of the title.
     * <p>Use it with {@link TitleAction#TIMES TIMES action}</p>
     *
     * @param fade_in  Ticks to spend fading in.
     * @param stay     Ticks to keep the title displayed.
     * @param fade_out Ticks to spend fading out, not when to start fading out.
     * @see ShulkerPacketTitle#set_fade_in(int)
     * @see ShulkerPacketTitle#set_stay(int)
     * @see ShulkerPacketTitle#set_fade_out(int)
     */
    public void set_times(int fade_in, int stay, int fade_out)
    {
        set_fade_in(fade_in);
        set_stay(stay);
        set_fade_out(fade_out);
    }

    @Override
    public void reset()
    {
        set_action(null);
        set_times(0, 0, 0);
        set_chat_component_value((BaseComponent[]) null);
    }

    @Override
    public String toString()
    {
        return "ShulkerPacketTitle{action:" + get_action().get_name() + ",chat:" +
                Arrays.toString(get_chat_component_value()) + ",fadeIn:" + get_fade_in() + ",stay:" + get_stay() +
                ",fadeOut:" + get_fade_out() + "}";
    }

    /**
     * Represents the available actions to manage titles.
     */
    public static enum TitleAction implements Nameable
    {
        TITLE("Set title", "TITLE"),
        SUBTITLE("Set subtitle", "SUBTITLE"),
        ACTION_BAR("Set action bar", "ACTION_BAR"),
        TIMES("Set times and display", "TIMES"),
        HIDE("Hide", "CLEAR"),
        RESET("Reset", "RESET");

        private String name;
        private String nms;

        TitleAction(String name, String nms)
        {
            this.name = name;
            this.nms = nms;
        }

        @Override
        public @NotNull String get_name()
        {
            return name;
        }

        public String getNmsEquivalent()
        {
            return nms;
        }
    }
}
