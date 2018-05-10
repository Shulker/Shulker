/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.languages;

import org.aperlambda.lambdacommon.config.FileConfig;
import org.aperlambda.lambdacommon.config.json.JsonConfig;
import org.aperlambda.lambdacommon.resources.ResourceName;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;

import java.io.File;
import java.util.Optional;

/**
 * Represents a language.
 *
 * @author lambdaurora
 * @version 1.0.0
 * @since 1.0.0
 */
public class Language
{
	private final String     langId;
	private       JsonConfig config;

	public Language(ResourceName name)
	{
		this(getLangId(name).toLowerCase(), new File(Shulker.getConfigurationDirectory(),
													 name.getDomain() + "/" + name.getName() + ".json"));
	}

	public Language(String langId, File file)
	{
		this.langId = langId;
		config = new JsonConfig(file);
	}

	public void load()
	{
		config.load();
	}

	public String getLangId()
	{
		return langId;
	}

	public String getLangName()
	{
		return getNullable("lang");
	}

	public @NotNull Optional<String> getTranslation(@NotNull String path)
	{
		String str = getNullable(path);
		return (str == null || str.isEmpty()) ? Optional.empty() : Optional.of(str);
	}

	public String getNullable(@NotNull String path)
	{
		return config.at(path);
	}

	private static String getLangId(ResourceName name)
	{
		var path = name.getName().replace("\\", "/").split("/");
		return path[path.length - 1];
	}
}