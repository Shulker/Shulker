/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.commands;

/**
 * CommandResult represents the result of a command.
 *
 * @author lambdaurora
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public enum CommandResult
{
	SUCCESS,
	ERROR_RUNTIME,
	ERROR_USAGE,
	ERROR_PERMISSION,
	ERROR_OBJECT_NOT_FOUND,
	ERROR_ONLY_PLAYER
}