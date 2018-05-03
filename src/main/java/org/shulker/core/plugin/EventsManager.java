/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface EventsManager
{
	<T extends Event> void on(Class<T> eventClass, Consumer<T> executor, EventPriority priority);

	default <T extends Event> void on(Class<T> eventClass, Consumer<T> executor)
	{
		on(eventClass, executor, EventPriority.NORMAL);
	}

	default <T extends Event> void on(String eventName, Consumer<T> executor)
	{
		on(eventName, executor, EventPriority.NORMAL);
	}

	@SuppressWarnings("unchecked")
	default <T extends Event> void on(String eventName, Consumer<T> executor, EventPriority priority)
	{
		on((Class<T>) getEvent(eventName), executor, priority);
	}

	void call(@NotNull Event event);

	Class<? extends Event> getEvent(String name);
}