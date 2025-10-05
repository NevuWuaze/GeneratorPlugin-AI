package com.example.generatorai;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PluginManager {

    private Plugin plugin;
    private Map<String, Object> loadedPlugins;
    private PluginCompiler compiler;

    public PluginManager(Plugin plugin) {
        this.plugin = plugin;
        this.loadedPlugins = new HashMap<>();
        this.compiler = new PluginCompiler(plugin);
    }

    public void compileAndLoadPlugin(String name, String code, Player creator) throws Exception {
        Class<?> clazz = compiler.compileAndLoad(name, code);
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // Register as listener if it is
        if (instance instanceof org.bukkit.event.Listener) {
            Bukkit.getPluginManager().registerEvents((org.bukkit.event.Listener) instance, plugin);
        }

        loadedPlugins.put(name, instance);
        plugin.getLogger().info("Plugin " + name + " loaded by " + creator.getName());
    }

    public void unloadPlugin(String name) {
        Object instance = loadedPlugins.remove(name);
        if (instance instanceof org.bukkit.event.Listener) {
            HandlerList.unregisterAll((org.bukkit.event.Listener) instance);
        }
    }

    public void unloadAllGeneratedPlugins() {
        for (String name : loadedPlugins.keySet()) {
            unloadPlugin(name);
        }
    }

    public void reloadGeneratedPlugins() {
        // For simplicity, just unload and reload if code is saved somewhere
        // In full implementation, save code and recompile
        unloadAllGeneratedPlugins();
        // Reload logic would need saved code
    }

    public Set<String> getGeneratedPluginNames() {
        return loadedPlugins.keySet();
    }

    public void exportPlugin(String name, Player player) throws Exception {
        // For simplicity, just save the code to a file
        // In full implementation, compile to jar
        if (!loadedPlugins.containsKey(name)) {
            throw new IllegalArgumentException("Plugin not found: " + name);
        }
        // Assume code is saved somewhere, for now just notify
        plugin.getLogger().info("Plugin " + name + " exported by " + player.getName());
        // TODO: Implement actual export to jar file
    }
}
