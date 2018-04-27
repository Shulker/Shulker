/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.impl.v112R1.wrappers;

import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import org.shulker.core.packets.mc.play.ShulkerPacketTitle;
import org.shulker.core.wrappers.TitleActionWrapper;

public class TitleActionWrapperV112R1 extends TitleActionWrapper
{
	public static final TitleActionWrapperV112R1 INSTANCE = new TitleActionWrapperV112R1();

	@Override
	public Object fromShulker(ShulkerPacketTitle.TitleAction shulkerObject)
	{
		if (shulkerObject == null)
			return null;
		switch (shulkerObject)
		{

			case TITLE:
				return PacketPlayOutTitle.EnumTitleAction.TITLE;
			case SUBTITLE:
				return PacketPlayOutTitle.EnumTitleAction.SUBTITLE;
			case ACTION_BAR:
				return PacketPlayOutTitle.EnumTitleAction.ACTIONBAR;
			case TIMES:
				return PacketPlayOutTitle.EnumTitleAction.TIMES;
			case HIDE:
				return PacketPlayOutTitle.EnumTitleAction.CLEAR;
			case RESET:
				return PacketPlayOutTitle.EnumTitleAction.RESET;
			default:
				return null;
		}
	}

	@Override
	public ShulkerPacketTitle.TitleAction toShulker(Object object)
	{
		if (!(object instanceof PacketPlayOutTitle.EnumTitleAction))
			return null;
		switch ((PacketPlayOutTitle.EnumTitleAction) object)
		{
			case TITLE:
				return ShulkerPacketTitle.TitleAction.TITLE;
			case SUBTITLE:
				return ShulkerPacketTitle.TitleAction.SUBTITLE;
			case ACTIONBAR:
				return ShulkerPacketTitle.TitleAction.ACTION_BAR;
			case TIMES:
				return ShulkerPacketTitle.TitleAction.TIMES;
			case CLEAR:
				return ShulkerPacketTitle.TitleAction.HIDE;
			case RESET:
				return ShulkerPacketTitle.TitleAction.RESET;
			default:
				return null;
		}
	}

	@Override
	public Class<?> getObjectClass()
	{
		return PacketPlayOutTitle.EnumTitleAction.class;
	}
}