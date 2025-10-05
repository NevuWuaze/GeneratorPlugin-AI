package com.example.generatorai;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private CommandHandler commandHandler;
    private PluginManager pluginManager;
    private ScriptManager scriptManager;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();

        // Initialize managers
        pluginManager = new PluginManager(this);
        scriptManager = new ScriptManager(this);
        commandHandler = new CommandHandler(this, pluginManager, scriptManager);
        guiManager = new GUIManager(this, commandHandler);

        // Register commands
        getCommand("ai").setExecutor(commandHandler);
        getCommand("gen").setExecutor(commandHandler);

        getLogger().info("GeneratorPlugin-AI has been enabled!");
    }

    @Override
    public void onDisable() {
        // Cleanup
        if (pluginManager != null) {
            pluginManager.unloadAllGeneratedPlugins();
        }
        getLogger().info("GeneratorPlugin-AI has been disabled!");
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public GUIManager getGUIManager() {
        return guiManager;
    }
}
