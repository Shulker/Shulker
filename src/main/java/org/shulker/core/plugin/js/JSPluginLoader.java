/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.plugin.js;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.*;
import org.shulker.core.Shulker;
import org.shulker.core.commands.BukkitCommandResult;
import org.yaml.snakeyaml.error.YAMLException;

import javax.script.ScriptException;
import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JSPluginLoader implements PluginLoader
{
	private final Pattern[] fileFilters = new Pattern[]{Pattern.compile("\\.jsar$")};
	private       Server    server;

	public JSPluginLoader(Server server)
	{
		this.server = server;
	}

	@Override
	public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException
	{
		Validate.notNull(file, "File cannot be null");
		if (!file.exists())
			throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));

		PluginDescriptionFile description;
		try
		{
			description = this.getPluginDescription(file);
		}
		catch (InvalidDescriptionException e)
		{
			throw new InvalidPluginException(e);
		}

		var configDir = Shulker.getConfigurationDirectory();
		var dataFolder = new File(configDir, description.getName());

		if (dataFolder.exists() && !dataFolder.isDirectory())
			throw new InvalidPluginException(String.format("Projected datafolder: `%s' for %s (%s) exists and is not a directory", dataFolder, description.getFullName(), file));
		else
		{
			for (var pluginName : description.getDepend())
			{
				Plugin current = Bukkit.getPluginManager().getPlugin(pluginName);
				if (current == null)
					throw new UnknownDependencyException(pluginName);
			}

			String script;

			try
			{
				ZipFile jsar = new ZipFile(file);
				ZipEntry mainEntry = jsar.getEntry(description.getMain());
				BufferedReader br = new BufferedReader(new InputStreamReader(jsar.getInputStream(mainEntry)));
				StringBuilder sb = new StringBuilder();
				String line;

				sb.append("load(\"nashorn:mozilla_compat.js\");\n");
				sb.append("importPackage(org.aperlambda.kimiko);\n");
				sb.append("importPackage(org.shulker.core);\n");
				sb.append("importPackage(org.shulker.core.commands);\n");
				sb.append("importPackage(org.shulker.core.config);\n");
				sb.append("importPackage(org.bukkit);\n");
				sb.append("\n");

				while ((line = br.readLine()) != null)
					sb.append(line).append('\n');

				script = sb.toString();

				br.close();
			}
			catch (IOException e)
			{
				throw new InvalidPluginException(e);
			}

			JSPlugin plugin;
			NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
			NashornScriptEngine jsEngine = (NashornScriptEngine) factory.getScriptEngine();
			try
			{
				jsEngine.put("server", Bukkit.getServer());
				jsEngine.put("description", description);
				jsEngine.put("dataFolder", dataFolder);
				jsEngine.put("__SHULKER__", Shulker.getShulker());

				JSUtils.loadClass(jsEngine, "CommandResult", BukkitCommandResult.class);
				JSUtils.loadClass(jsEngine, "Bukkit", Bukkit.class);
				JSUtils.loadClass(jsEngine, "Listener", Listener.class);
				JSUtils.loadClass(jsEngine, "Optional", Optional.class);
				JSUtils.loadClass(jsEngine, "ChatColor", ChatColor.class);

				jsEngine.eval(script);
			}
			catch (ScriptException e)
			{
				throw new InvalidPluginException(e);
			}
			plugin = new JSPlugin();
			plugin.init(this, server, description, dataFolder, file, jsEngine);
			plugin.onLoad();
			return plugin;
		}
	}

	@Override
	public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException
	{
		Validate.notNull(file, "File cannot be null");
		InputStream descrStream = null;
		ZipFile jsar;
		PluginDescriptionFile pdf;
		if (file.isFile())
		{
			try
			{
				jsar = new ZipFile(file);
				ZipEntry entry = jsar.getEntry("plugin.yml");
				if (entry == null)
					throw new InvalidDescriptionException(new FileNotFoundException("JS archive does not contain plugin.yml"));

				descrStream = jsar.getInputStream(entry);
				pdf = new PluginDescriptionFile(descrStream);
			}
			catch (IOException | YAMLException e)
			{
				throw new InvalidDescriptionException(e);
			}
			finally
			{
				if (descrStream != null)
				{
					try
					{
						descrStream.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		else
			throw new InvalidDescriptionException("Plugin file is a directory.");
		return pdf;
	}

	@Override
	public Pattern[] getPluginFileFilters()
	{
		return fileFilters;
	}

	@Override
	public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin)
	{
		return null;
	}

	@Override
	public void enablePlugin(Plugin plugin)
	{
		Validate.isTrue(plugin instanceof JSPlugin, "Plugin is not associated with this PluginLoader");
		if (!plugin.isEnabled())
		{
			plugin.getLogger().info("Enabling " + plugin.getDescription().getFullName());
			JSPlugin jPlugin = (JSPlugin) plugin;

			try
			{
				jPlugin.setEnabled(true);
			}
			catch (Throwable var5)
			{
				server.getLogger().log(Level.SEVERE,
									   "Error occurred while enabling " + plugin.getDescription().getFullName() +
											   " (Is it up to date?)", var5);
			}

			server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
		}
	}

	@Override
	public void disablePlugin(Plugin plugin)
	{
		Validate.isTrue(plugin instanceof JSPlugin, "Plugin is not associated with this PluginLoader");
		if (plugin.isEnabled())
		{
			String message = String.format("Disabling %s", plugin.getDescription().getFullName());
			plugin.getLogger().info(message);
			server.getPluginManager().callEvent(new PluginDisableEvent(plugin));
			JSPlugin jsPlugin = (JSPlugin) plugin;

			try
			{
				jsPlugin.setEnabled(false);
			}
			catch (Throwable var9)
			{
				this.server.getLogger().log(Level.SEVERE,
											"Error occurred while disabling " + plugin.getDescription().getFullName() +
													" (Is it up to date?)", var9);
			}
		}
	}
}