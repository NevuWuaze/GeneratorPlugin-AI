package com.example.generatorai;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private Main plugin;
    private PluginManager pluginManager;
    private ScriptManager scriptManager;
    private AIInterpreter aiInterpreter;
    private SecurityManager securityManager;

    public CommandHandler(Main plugin, PluginManager pluginManager, ScriptManager scriptManager) {
        this.plugin = plugin;
        this.pluginManager = pluginManager;
        this.scriptManager = scriptManager;
        this.aiInterpreter = new AIInterpreter();
        this.securityManager = new SecurityManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("generator.create")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usage: /" + label + " <createplugin|reload|list>");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "createplugin":
                if (args.length < 2) {
                    player.sendMessage("Usage: /" + label + " createplugin <name> [description]");
                    return true;
                }
                String pluginName = args[1];
                String description = args.length > 2 ? String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)) : "";
                handleCreatePlugin(player, pluginName, description);
                break;
            case "reload":
                pluginManager.reloadGeneratedPlugins();
                player.sendMessage("Generated plugins reloaded.");
                break;
            case "list":
                // List generated plugins
                player.sendMessage("Generated plugins: " + String.join(", ", pluginManager.getGeneratedPluginNames()));
                break;
            case "gui":
                // Open GUI menu
                plugin.getGUIManager().openMainMenu(player);
                break;
            case "runscript":
                if (args.length < 3) {
                    player.sendMessage("Usage: /" + label + " runscript <language> <script>");
                    return true;
                }
                String language = args[1];
                String script = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
                handleRunScript(player, language, script);
                break;
            case "export":
                if (args.length < 2) {
                    player.sendMessage("Usage: /" + label + " export <pluginname>");
                    return true;
                }
                String exportName = args[1];
                handleExport(player, exportName);
                break;
            default:
                player.sendMessage("Unknown subcommand. Use /" + label + " <createplugin|reload|list|gui|runscript|export>");
                break;
        }

        return true;
    }

    private void handleCreatePlugin(Player player, String pluginName, String description) {
        if (description.isEmpty()) {
            player.sendMessage("Please provide a description for the plugin.");
            return;
        }

        try {
            String generatedCode = aiInterpreter.interpret(description);
            if (!securityManager.validateCode(generatedCode, player)) {
                return; // Validation failed, message already sent
            }
            pluginManager.compileAndLoadPlugin(pluginName, generatedCode, player);
            aiInterpreter.learnPattern(description, generatedCode); // Learn for future
            player.sendMessage("Plugin '" + pluginName + "' created successfully!");
        } catch (Exception e) {
            player.sendMessage("Error creating plugin: " + e.getMessage());
            plugin.getLogger().severe("Error creating plugin: " + e.getMessage());
        }
    }

    private void handleRunScript(Player player, String language, String script) {
        try {
            Object result = scriptManager.executeScript(script, language);
            player.sendMessage("Script executed successfully. Result: " + result);
        } catch (Exception e) {
            player.sendMessage("Error executing script: " + e.getMessage());
            plugin.getLogger().severe("Error executing script: " + e.getMessage());
        }
    }

    private void handleExport(Player player, String pluginName) {
        try {
            pluginManager.exportPlugin(pluginName, player);
            player.sendMessage("Plugin '" + pluginName + "' exported successfully!");
        } catch (Exception e) {
            player.sendMessage("Error exporting plugin: " + e.getMessage());
            plugin.getLogger().severe("Error exporting plugin: " + e.getMessage());
        }
    }
}
