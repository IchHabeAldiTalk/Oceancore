package de.oceanblocks.oceancore.listener;

import lombok.SneakyThrows;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public class ListenerUtil {

    @SneakyThrows
    public void loadListeners(JavaPlugin plugin, String path) {
        Reflections reflections = new Reflections(path);
        reflections.getSubTypesOf(Listener.class).forEach(clazz -> {
            System.out.println("Try to register class with name " + clazz.getSimpleName());
            try {
                plugin.getServer().getPluginManager().registerEvents((Listener) clazz.newInstance(), plugin);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
