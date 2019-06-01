package org.shulker.core.impl.v113R2.wrappers;

import org.shulker.core.wrappers.TitleActionWrapper;

import static net.minecraft.server.v1_13_R2.PacketPlayOutTitle.*;
import static org.shulker.core.packets.mc.play.ShulkerPacketTitle.*;

public class TitleActionWrapperV113R2 extends TitleActionWrapper
{
    public static final TitleActionWrapperV113R2 INSTANCE = new TitleActionWrapperV113R2();

    @Override
    public Object from_shulker(TitleAction shulker_object)
    {
        if (shulker_object == null)
            return null;
        switch (shulker_object) {
            case TITLE:
                return EnumTitleAction.TITLE;
            case SUBTITLE:
                return EnumTitleAction.SUBTITLE;
            case ACTION_BAR:
                return EnumTitleAction.ACTIONBAR;
            case TIMES:
                return EnumTitleAction.TIMES;
            case HIDE:
                return EnumTitleAction.CLEAR;
            case RESET:
                return EnumTitleAction.RESET;
            default:
                return null;
        }
    }

    @Override
    public TitleAction to_shulker(Object object)
    {
        if (!(object instanceof EnumTitleAction))
            return null;
        switch ((EnumTitleAction) object) {
            case TITLE:
                return TitleAction.TITLE;
            case SUBTITLE:
                return TitleAction.SUBTITLE;
            case ACTIONBAR:
                return TitleAction.ACTION_BAR;
            case TIMES:
                return TitleAction.TIMES;
            case CLEAR:
                return TitleAction.HIDE;
            case RESET:
                return TitleAction.RESET;
            default:
                return null;
        }
    }

    @Override
    public Class<?> get_object_class()
    {
        return EnumTitleAction.class;
    }
}
