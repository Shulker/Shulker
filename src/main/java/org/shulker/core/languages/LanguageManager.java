/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
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
	private final ResourceName              languagesDirectory;
	private final String                    resPath;
	private final File                      langsFolder;

	public LanguageManager(Plugin plugin, ResourceName languagesDirectory)
	{
		this.plugin = plugin;
		this.languagesDirectory = languagesDirectory;
		String after = "";
		if (!languagesDirectory.getName().isEmpty())
			after = "/" + languagesDirectory.getName() + "/";
		resPath = after;
		langsFolder = new File(Shulker.getConfigurationDirectory(), languagesDirectory.getDomain() + after);
	}

	/**
	 * Initializes the language system.
	 */
	public void init()
	{
		if (!langsFolder.exists())
			if (!langsFolder.mkdirs())
				throw new RuntimeException("Cannot create directory \"" + langsFolder.getAbsolutePath() + "\".");

		var files = langsFolder.listFiles();
		if (files != null)
			for (var file : files)
				if (file.getName().endsWith(".json"))
				{
					var langId = file.getName().replace(".json", "").toLowerCase();
					Language lang = languages.get(langId);
					if (lang == null)
					{
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
	 * @param langId The language ID.
	 */
	public void saveDefault(String langId)
	{
		var path = "langs/" + langId + ".json";
		InputStream stream = plugin.getResource(path);
		if (stream == null)
			return;

		Shulker.getConfigManager().saveResource(stream, languagesDirectory.sub(langId), "json", false);
	}

	/**
	 * Translates with the specified path
	 *
	 * @param langId The translated language.
	 * @param path   The path of the translation.
	 * @return The translation or an error message if not present.
	 */
	public String translate(String langId, String path)
	{
		Language lang = languages.get(langId.toLowerCase());
		if (lang == null)
			lang = languages.get("en_us");

		if (lang == null)
			return "";

		String error = "${translate:" + path + "}";
		var translation = lang.getTranslation(path);
		if (!translation.isPresent() && lang.getLangId().equalsIgnoreCase("en_us"))
			return error;
		else
			return translation.orElseGet(() -> {
				var defLang = languages.get("en_us");
				if (defLang == null)
					return error;
				else
					return defLang.getTranslation(path).orElse(error);
			});
	}
}