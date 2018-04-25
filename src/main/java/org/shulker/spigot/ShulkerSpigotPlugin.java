/*
 * Copyright © 2018 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.spigot;

import com.google.common.collect.ImmutableList;
import org.aperlambda.lambdacommon.utils.LambdaReflection;
import org.aperlambda.lambdacommon.utils.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shulker.core.MinecraftManager;
import org.shulker.core.Shulker;
import org.shulker.core.ShulkerLibrary;
import org.shulker.core.ShulkerPlugin;
import org.shulker.core.commands.ShulkerCommandExecutor;
import org.shulker.core.commands.defaults.LibrariesCommand;
import org.shulker.core.commands.defaults.TestCommand;
import org.shulker.core.config.ShulkerConfiguration;
import org.shulker.core.config.ShulkerSymbols;
import org.shulker.core.impl.reflect.ReflectMinecraftManager;
import org.shulker.core.impl.v112R1.MinecraftManagerV112R1;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.ansi;

public class ShulkerSpigotPlugin extends JavaPlugin implements ShulkerPlugin
{
	private static Optional<Method> addUrl = LambdaReflection.getMethod(URLClassLoader.class, "addURL", true, URL.class);

	private File pluginsDir = getDataFolder().getParentFile();
	private File baseDir    = pluginsDir.getParentFile();

	private ShulkerConfiguration config  = new ShulkerConfiguration();
	private ShulkerSymbols       symbols = new ShulkerSymbols();

	/**
	 * Minecraft manager
	 */
	private MinecraftManager mcManager;

	private List<ShulkerLibrary> libraries = new ArrayList<>();

	@Override
	public void onLoad()
	{
		super.onLoad();
		Shulker.init(this);

		var file = new File(baseDir, "addons/libs");
		file.mkdirs();
		File[] libs = file.listFiles();
		if (libs != null)
			for (var lib : libs)
			{
				if (!addUrl.isPresent())
				{
					logError(getPrefix(), "Cannot load libraries. (Cannot access to method addURL in URLClassLoader)");
					break;
				}
				if (lib.getName().endsWith(".jar"))
				{
					ShulkerLibrary library;
					try
					{
						var url = lib.toURI().toURL();
						var classLoader = (URLClassLoader) getClassLoader();

						boolean next = false;
						for (URL it : classLoader.getURLs())
							if (it.equals(url))
								next = true;

						if (next)
							continue;

						library = new ShulkerLibrary(lib.getName().replace(".jar", ""), lib, ShulkerLibrary.LibraryLoadState.SUCCESS);

						logInfo(getPrefix(), "Loading library '" +
								ansi().fg(Color.YELLOW).a(lib.getName().replace(".jar", "")).fg(Color.WHITE).toString() +
								"'...");

						LambdaReflection.invokeMethod(classLoader, addUrl.get(), url);
					}
					catch (MalformedURLException e)
					{
						library = new ShulkerLibrary(lib.getName().replace(".jar", ""), lib, ShulkerLibrary.LibraryLoadState.FAILED);
					}
					libraries.add(library);
				}
			}
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		config.load();
		symbols.load();

		switch (getServerVersion())
		{
			case "v1_12_R1":
				mcManager = new MinecraftManagerV112R1();
				break;
			default:
				mcManager = new ReflectMinecraftManager();
				logInfo(getPrefix(), ansi().fg(Color.YELLOW).a("WARNING: ").fgBright(Color.MAGENTA).a("Shulker ").fg(Color.YELLOW).a("uses ReflectMinecraftManager due to unsupported Minecraft version. Please contact the developer.").reset().toString());
				break;
		}
		logInfo(getPrefix(), ansi().a("Using ").fg(Color.CYAN).a(mcManager.getName()).fgBright(Color.WHITE).a(" with ").fg(Color.CYAN).a(mcManager.getWrapperManager().getName()).fgBright(Color.WHITE).a("...").reset().toString());
		Bukkit.getPluginManager().registerEvents(new ShulkerListener(), this);
		Bukkit.getOnlinePlayers().forEach(p -> mcManager.addPlayer(p));

		setupCommand("libraries", new LibrariesCommand());
		setupCommand("test", new TestCommand());
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		libraries.clear();
	}

	@Override
	public String getVersion()
	{
		return getDescription().getVersion();
	}

	@Override
	public String getPrefix()
	{
		return ansi().fg(Color.WHITE).a("[").fgBright(Color.MAGENTA).a("Shulker").fg(Color.WHITE).a("]").reset().toString();
	}

	@Override
	public void logInfo(@Nullable String prefix, @NotNull String message)
	{
		String p = "";
		if (prefix != null)
			p = prefix + " ";
		System.out.println(p + ansi().fg(Color.WHITE).a(message).reset().toString());
	}

	@Override
	public void logError(@Nullable String prefix, @NotNull String message)
	{
		String p = "";
		if (prefix != null)
			p = prefix + "";
		p += ansi().fg(Color.RED).a("[Error] ").reset().toString();
		System.out.println(p + ansi().fgBright(Color.RED).a(message).reset().toString());
	}

	@Override
	public File getBaseDirectory()
	{
		return baseDir;
	}

	@Override
	public File getPluginsDirectory()
	{
		return pluginsDir;
	}

	@Override
	public ShulkerSymbols getSymbolsManager()
	{
		return symbols;
	}

	@Override
	public ShulkerConfiguration getConfiguration()
	{
		return config;
	}

	@Override
	public MinecraftManager getMinecraftManager()
	{
		return mcManager;
	}

	@Override
	public List<ShulkerLibrary> getLibraries()
	{
		return ImmutableList.copyOf(libraries);
	}

	public void setupCommand(String command, ShulkerCommandExecutor executor)
	{
		PluginCommand cmd = getCommand(command);
		cmd.setExecutor(executor);
		cmd.setTabCompleter(executor);
	}

	/**
	 * Gets the version of your server by the packages
	 *
	 * @return The server version
	 */
	public static String getServerVersion()
	{
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);

	}

	public static Class<?> getNmsClass(String name)
	{
		try
		{
			return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}