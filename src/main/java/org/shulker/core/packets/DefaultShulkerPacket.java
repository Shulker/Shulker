/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.packets;

import org.aperlambda.lambdacommon.utils.LambdaReflection;

import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;

/**
 * Represents the default packet handler.
 */
public class DefaultShulkerPacket extends ShulkerPacket<Object>
{
	public DefaultShulkerPacket(Object packet)
	{
		super(packet);
	}

	/**
	 * Gets the value from a field of the packet.
	 *
	 * @param fieldName The field name.
	 * @return The optional value of the packet.
	 */
	public Optional<Object> getValue(String fieldName)
	{
		return getField(packet.getClass(), fieldName, true).map(field -> getFieldValue(packet, field));
	}

	/**
	 * Sets the value of the specified packet field.
	 *
	 * @param fieldName The name of the field.
	 * @param object    The object to put.
	 */
	public void setValue(String fieldName, Object object)
	{
		getField(packet.getClass(), fieldName, true).ifPresent(field -> LambdaReflection.setValue(packet, field, object));
	}

	@Override
	public void reset()
	{
		throw new UnsupportedOperationException("Cannot reset a packet with the default packet handler.");
	}
}