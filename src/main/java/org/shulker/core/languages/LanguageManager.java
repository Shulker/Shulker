/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.languages;

import org.aperlambda.lambdacommon.resources.ResourceName;
import org.bukkit.plugin.Plugin;
import org.shulker.core.Shulker;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Represents a manager for languages.
 *
 * @author lambdaurora
 * @version 1.0.0
 * @since 1.0.0
 */
public class LanguageManager
{
    private final HashMap<String, Language> languages = new HashMap<>();
    private final Plugin                    plugin;
    private final ResourceName              languages_dir;
    private final String                    res_path;
    private final File                      langs_folder;

    public LanguageManager(Plugin plugin, ResourceName languages_dir)
    {
        this.plugin = plugin;
        this.languages_dir = languages_dir;
        String after = "";
        if (!languages_dir.get_name().isEmpty())
            after = "/" + languages_dir.get_name() + "/";
        res_path = after;
        langs_folder = new File(Shulker.get_configuration_dir(), languages_dir.get_domain() + after);
    }

    /**
     * Initializes the language system.
     */
    public void init()
    {
        if (!langs_folder.exists())
            if (!langs_folder.mkdirs())
                throw new RuntimeException("Cannot create directory \"" + langs_folder.getAbsolutePath() + "\".");

        var files = langs_folder.listFiles();
        if (files != null)
            for (var file : files)
                if (file.getName().endsWith(".json")) {
                    var langId = file.getName().replace(".json", "").toLowerCase();
                    Language lang = languages.get(langId);
                    if (lang == null) {
                        lang = new Language(langId, file);
                        languages.put(langId, lang);
                    }
                }
    }

    /**
     * Updates the languages files.
     */
    public void update()
    {
        init();
        languages.forEach((id, lang) -> lang.load());
    }

    /**
     * Save a default language. The default language file must be in the JAR with the path {@code /langs/${langId}.json}
     *
     * @param lang_id The language ID.
     */
    public void save_default(String lang_id)
    {
        var path = "langs/" + lang_id + ".json";
        InputStream stream = plugin.getResource(path);
        if (stream == null)
            return;

        Shulker.get_configs().save_resource(stream, languages_dir.sub(lang_id), "json", false);
    }

    /**
     * Translates with the specified path
     *
     * @param lang_id The translated language.
     * @param path   The path of the translation.
     * @return The translation or an error message if not present.
     */
    public String translate(String lang_id, String path)
    {
        Language lang = languages.get(lang_id.toLowerCase());
        if (lang == null)
            lang = languages.get("en_us");

        if (lang == null)
            return "";

        String error = "${translate:" + path + "}";
        var translation = lang.get_translation(path);
        if (!translation.isPresent() && lang.get_lang_id().equalsIgnoreCase("en_us"))
            return error;
        else
            return translation.orElseGet(() -> {
                var defLang = languages.get("en_us");
                if (defLang == null)
                    return error;
                else
                    return defLang.get_translation(path).orElse(error);
            });
    }
}
