/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
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
     * @param shulker_object The Shulker object to wrap.
     * @return The wrapped object.
     */
    Object from_shulker(X shulker_object);

    /**
     * Gets the Shulker object.
     *
     * @param object The wrapped object.
     * @return The Shulker object.
     */
    X to_shulker(Object object);

    /**
     * Gets the server object type.
     *
     * @return The server object class.
     */
    Class<?> get_object_class();
}
