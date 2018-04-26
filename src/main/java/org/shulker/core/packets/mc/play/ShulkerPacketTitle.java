/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
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
 * ShulkerPacketTitle represents the packet which manages the titles.
 * <p>
 * {@link TitleAction#HIDE} makes the title disappear, but if you run times again the same title will appear. {@link TitleAction#RESET} erases the text.
 * The title is visible on screen for Fade In + Stay + Fade Out ticks.
 *
 * @param <T> The server object type.
 */
public abstract class ShulkerPacketTitle<T> extends ShulkerPacket<T>
{
	public ShulkerPacketTitle()
	{
		super();
	}

	public ShulkerPacketTitle(@NotNull TitleAction action)
	{
		this();
	}

	public ShulkerPacketTitle(@NotNull TitleAction action, @NotNull BaseComponent[] chatValue)
	{
		this();
	}

	public ShulkerPacketTitle(@NotNull TitleAction action, int fadeIn, int stay, int fadeOut)
	{
		this();
	}

	public ShulkerPacketTitle(T packet)
	{
		super(packet);
	}

	/**
	 * Gets the action to execute of the title.
	 *
	 * @return The action to performe.
	 */
	public abstract TitleAction getAction();

	/**
	 * Sets the action to execute of the title.
	 *
	 * @param action The action to perform.
	 */
	public abstract void setAction(TitleAction action);

	/**
	 * Gets the ChatComponent value.
	 * It's defined for some actions:
	 * <ul>
	 * <li>{@link TitleAction#TITLE TITLE}</li>
	 * <li>{@link TitleAction#SUBTITLE SUBTITLE}</li>
	 * <li>{@link TitleAction#ACTIONBAR ACTIONBAR}</li>
	 * </ul>
	 *
	 * @return The ChatComponent value.
	 */
	public abstract @Nullable BaseComponent[] getChatComponentValue();

	/**
	 * Sets the ChatComponent value.
	 * The value can be null, only define it for the actions: {@code TITLE}, {@code SUBTITLE}, {@code ACTIONBAR}.
	 *
	 * @param components The ChatComponent value, mays be null.
	 */
	public abstract void setChatComponentValue(@Nullable BaseComponent... components);

	/**
	 * Gets the fade in of the title.
	 * <p>Defined if the action is {@link TitleAction#TIMES TIMES}</p>
	 *
	 * @return Ticks to spend fading in.
	 */
	public abstract int getFadeIn();

	/**
	 * Sets the fade in of the title.
	 * <p>Use it with {@link TitleAction#TIMES TIMES action}</p>
	 *
	 * @param fadeIn Ticks to spend fading in.
	 */
	public abstract void setFadeIn(int fadeIn);

	/**
	 * Gets the stay time of the title.
	 * <p>Defined if the action is {@link TitleAction#TIMES TIMES}</p>
	 *
	 * @return Ticks to keep the title displayed.
	 */
	public abstract int getStay();

	/**
	 * Sets the stay time of the title.
	 * <p>Use it with {@link TitleAction#TIMES TIMES action}</p>
	 *
	 * @param stay Ticks to keep the title displayed.
	 */
	public abstract void setStay(int stay);

	/**
	 * Gets the fade out of the title.
	 * <p>Defined if the action is {@link TitleAction#TIMES TIMES}</p>
	 *
	 * @return Ticks to spend fading out.
	 */
	public abstract int getFadeOut();

	/**
	 * Sets the fade out of the title.
	 * <p>Use it with {@link TitleAction#TIMES TIMES action}</p>
	 *
	 * @param fadeOut Ticks to spend fading out, not when to start fading out.
	 */
	public abstract void setFadeOut(int fadeOut);

	/**
	 * Sets the times and display of the title.
	 * <p>Use it with {@link TitleAction#TIMES TIMES action}</p>
	 *
	 * @param fadeIn  Ticks to spend fading in.
	 * @param stay    Ticks to keep the title displayed.
	 * @param fadeOut Ticks to spend fading out, not when to start fading out.
	 * @see ShulkerPacketTitle#setFadeIn(int)
	 * @see ShulkerPacketTitle#setStay(int)
	 * @see ShulkerPacketTitle#setFadeOut(int)
	 */
	public void setTimes(int fadeIn, int stay, int fadeOut)
	{
		setFadeIn(fadeIn);
		setStay(stay);
		setFadeOut(fadeOut);
	}

	@Override
	public void reset()
	{
		setAction(null);
		setTimes(0, 0, 0);
		setChatComponentValue();
	}

	@Override
	public String toString()
	{
		return "ShulkerPacketTitle{action:" + getAction().getName() + ",chat:" +
				Arrays.toString(getChatComponentValue()) + ",fadeIn:" + getFadeIn() + ",stay:" + getStay() +
				",fadeOut:" + getFadeOut() + "}";
	}

	/**
	 * Represents the available actions to manage titles.
	 */
	public static enum TitleAction implements Nameable
	{
		TITLE("Set title", "TITLE"),
		SUBTITLE("Set subtitle", "SUBTITLE"),
		ACTIONBAR("Set action bar", "ACTIONBAR"),
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
		public @NotNull String getName()
		{
			return name;
		}

		public String getNmsEquivalent()
		{
			return nms;
		}
	}
}