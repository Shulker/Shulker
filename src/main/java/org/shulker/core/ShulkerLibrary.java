/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core;

import net.md_5.bungee.api.ChatColor;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static net.md_5.bungee.api.ChatColor.GREEN;
import static net.md_5.bungee.api.ChatColor.RED;

public class ShulkerLibrary implements Nameable
{
    private String           name;
    private File             file;
    private LibraryLoadState load_state;

    public ShulkerLibrary(String name, File file, LibraryLoadState load_state)
    {
        this.name = name;
        this.file = file;
        this.load_state = load_state;
    }

    @Override
    public @NotNull String get_name()
    {
        return this.name;
    }

    public File get_file()
    {
        return this.file;
    }

    public LibraryLoadState get_load_state()
    {
        return this.load_state;
    }

    public static enum LibraryLoadState
    {
        SUCCESS("Success", GREEN),
        FAILED("Failed", RED);

        private String    to_string;
        private ChatColor prefix_color;

        LibraryLoadState(String str, ChatColor prefix_color)
        {
            to_string = str;
            this.prefix_color = prefix_color;
        }

        @Override
        public String toString()
        {
            return to_string;
        }

        public ChatColor get_prefix_color()
        {
            return prefix_color;
        }
    }
}
