/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.shulker.core.plugin.js;

import org.aperlambda.lambdacommon.utils.LambdaReflection;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.shulker.core.Shulker;
import org.shulker.core.events.PacketEvent;
import org.shulker.core.events.PacketListener;
import org.shulker.core.plugin.EventsManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSEventsManager implements EventsManager
{
    private static final HashMap<String, Class<? extends Event>> events_name = new HashMap<>();
    private              JSPlugin                                plugin;

    public JSEventsManager(JSPlugin plugin)
    {
        this.plugin = plugin;
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void on(Class<T> event, Consumer<T> executor, EventPriority priority)
    {
        Bukkit.getPluginManager().registerEvent(event, new Listener()
        {
        }, priority, ((listener, event1) -> executor.accept((T) event1)), plugin);
    }

    public void on_packet_receive(Consumer<PacketEvent> executor)
    {
        Shulker.register_packet_listener(new PacketListener()
        {
            @Override
            public void on_packet_receive(PacketEvent event)
            {
                executor.accept(event);
            }

            @Override
            public void on_packet_send(PacketEvent event)
            {

            }
        });
    }

    public void on_packet_send(Consumer<PacketEvent> executor)
    {
        Shulker.register_packet_listener(new PacketListener()
        {
            @Override
            public void on_packet_receive(PacketEvent event)
            {

            }

            @Override
            public void on_packet_send(PacketEvent event)
            {
                executor.accept(event);
            }
        });
    }

    @Override
    public void call(@NotNull Event event)
    {
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public Class<? extends Event> get_event(String name)
    {
        return events_name.get(name);
    }

    @SuppressWarnings("unchecked")
    public static void register()
    {
        String bukkit_location = Bukkit.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        JarFile file;
        try {
            file = new JarFile(bukkit_location);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var entries = file.stream().map(entry -> {
            var name = entry.getName();
            if (name.startsWith("/"))
                name = name.substring(1);
            return name;
        }).collect(Collectors.toList());
        var stream = entries.stream().filter(entry -> entry.startsWith("org/bukkit/event/")).collect(Collectors.toList());
        // Block
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/block")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));
        // Enchantment
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/enchantment")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));
        // Entity
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/entity")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));
        // Hanging
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/hanging")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));
        // Inventory
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/inventory")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));
        // Player
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/player")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));
        // Server
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/server")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));
        // Vehicle
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/vehicle")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));
        // Weather
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/weather")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));
        // World
        filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/bukkit/event/world")))
                .forEach(clazz -> register((Class<? extends Event>) clazz));

        if (entries.stream().anyMatch(entry -> entry.startsWith("org/spigotmc/event"))) {
            stream = entries.stream().filter(entry -> entry.startsWith("org/spigotmc/event/")).collect(Collectors.toList());
            // Spigot entity
            filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/spigotmc/event/entity")))
                    .forEach(clazz -> register((Class<? extends Event>) clazz));
            // Spigot player
            filter_and_map(stream.stream().filter(entry -> entry.startsWith("org/spigotmc/event/player")))
                    .forEach(clazz -> register((Class<? extends Event>) clazz));
        }
    }

    private static Stream<? extends Class<?>> filter_and_map(Stream<String> stream)
    {
        return stream.map(entry -> LambdaReflection.get_class(entry.replace('/', '.').replace(".class", "")))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(JSEventsManager::extends_of_event);
    }

    private static void register(Class<? extends Event> event)
    {
        events_name.put(event.getSimpleName(), event);
    }

    private static boolean extends_of_event(Class<?> clazz)
    {
        if (clazz.isAnnotation() || clazz.isEnum())
            return false;
        Class<?> super_class = clazz.getSuperclass();
        while (super_class != null) {
            if (super_class == Event.class)
                return Arrays.stream(clazz.getAnnotations()).map(Annotation::annotationType).noneMatch(annotation -> annotation == Deprecated.class);
            super_class = super_class.getSuperclass();
        }
        return false;
    }
}
