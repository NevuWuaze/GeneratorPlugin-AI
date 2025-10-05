package com.example.generatorai;

import org.bukkit.plugin.Plugin;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptManager {

    private Plugin plugin;
    private ScriptEngineManager engineManager;

    public ScriptManager(Plugin plugin) {
        this.plugin = plugin;
        this.engineManager = new ScriptEngineManager();
    }

    public Object executeScript(String script, String language) throws ScriptException {
        ScriptEngine engine = engineManager.getEngineByName(language);
        if (engine == null) {
            throw new ScriptException("Unsupported script language: " + language);
        }
        // Set context if needed
        return engine.eval(script);
    }

    public void loadAndExecuteScript(String scriptName, String language) {
        // Load script from file, for now assume script is provided
        // In full impl, read from /scripts/
    }
}
