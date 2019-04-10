/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.languages;

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
    private final String     lang_id;
    private       JsonConfig config;

    public Language(ResourceName name)
    {
        this(get_lang_id(name).toLowerCase(), new File(Shulker.get_configuration_dir(), name.get_domain() + "/" + name.get_name() + ".json"));
    }

    public Language(String lang_id, File file)
    {
        this.lang_id = lang_id;
        config = new JsonConfig(file);
    }

    public void load()
    {
        config.load();
    }

    public String get_lang_id()
    {
        return lang_id;
    }

    public String get_lang_name()
    {
        return get_nullable("lang");
    }

    public @NotNull Optional<String> get_translation(@NotNull String path)
    {
        String str = get_nullable(path);
        return (str == null || str.isEmpty()) ? Optional.empty() : Optional.of(str);
    }

    public String get_nullable(@NotNull String path)
    {
        return config.at(path);
    }

    private static String get_lang_id(ResourceName name)
    {
        var path = name.get_name().replace("\\", "/").split("/");
        return path[path.length - 1];
    }
}
