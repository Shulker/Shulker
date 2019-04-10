/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.inventory;

public interface Windowable
{
    /**
     * Gets the id of the window.
     * <p>A unique id number for the window to be displayed. Notchian server implementation is a counter, starting at 1.</p>
     *
     * @return The id of the window.
     */
    int get_window_id();

    /**
     * Sets the id of the window.
     * <p>A unique id number for the window to be displayed. Notchian server implementation is a counter, starting at 1.</p>
     *
     * @param id The id of the window.
     */
    void set_window_id(int id);
}
