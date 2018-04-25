/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.wrappers;

/**
 * Represents a wrapper template.
 *
 * @param <X> The Shulker object to wrap.
 */
public interface Wrapper<X>
{
	/**
	 * Gets the wrapped object.
	 *
	 * @param shulkerObject The Shulker object to wrap.
	 * @return The wrapped object.
	 */
	Object fromShulker(X shulkerObject);

	/**
	 * Gets the Shulker object.
	 *
	 * @param object The wrapped object.
	 * @return The Shulker object.
	 */
	X toShulker(Object object);

	/**
	 * Gets the server object type.
	 *
	 * @return The server object class.
	 */
	Class<?> getObjectClass();
}