/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands;

import org.aperlambda.kimiko.CommandResult;

public class BukkitCommandResult
{
    public static final CommandResult SUCCESS           = CommandResult.SUCCESS;
    public static final CommandResult ERROR_USAGE       = CommandResult.ERROR_USAGE;
    public static final CommandResult ERROR_PERMISSION  = CommandResult.ERROR_PERMISSION;
    public static final CommandResult ERROR_RUNTIME     = CommandResult.ERROR_RUNTIME;
    public static final CommandResult ERROR_ONLY_PLAYER = new CommandResult(() -> "translate:error_only_player");

    private BukkitCommandResult() throws IllegalAccessException
    {
        throw new IllegalAccessException("BukkitCommandResult is a full-static class.");
    }
}