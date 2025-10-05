package com.example.generatorai;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AIInterpreter {

    private CodeBuilder codeBuilder;
    private java.util.Map<String, String> learnedPatterns;

    public AIInterpreter() {
        this.codeBuilder = new CodeBuilder();
        this.learnedPatterns = new java.util.HashMap<>();
    }

    public String interpret(String input) {
        // Enhanced parser for natural language
        // Examples:
        // "when player uses diamond block, give 5 emeralds"
        // "if player says boom in chat, explode tnt"
        // "every 30 minutes, give food to all players"

        input = input.toLowerCase();

        // Pattern for event-action
        Pattern eventActionPattern = Pattern.compile("(when|if)\\s+(.+?),\\s+(.+)", Pattern.CASE_INSENSITIVE);
        Matcher eventActionMatcher = eventActionPattern.matcher(input);

        if (eventActionMatcher.find()) {
            String event = eventActionMatcher.group(2).trim();
            String action = eventActionMatcher.group(3).trim();
            return generateCode(event, action);
        }

        // Pattern for timed actions
        Pattern timedPattern = Pattern.compile("every\\s+(\\d+)\\s+minutes?,\\s+(.+)", Pattern.CASE_INSENSITIVE);
        Matcher timedMatcher = timedPattern.matcher(input);

        if (timedMatcher.find()) {
            int minutes = Integer.parseInt(timedMatcher.group(1));
            String action = timedMatcher.group(2).trim();
            return generateTimedCode(minutes, action);
        }

        throw new IllegalArgumentException("Unable to parse the instruction. Supported formats: 'when/if [event], [action]' or 'every X minutes, [action]'");
    }

    private String generateCode(String event, String action) {
        // Generate basic Java code for event and action
        StringBuilder code = new StringBuilder();
        code.append("package generated;\n\n");
        code.append("import org.bukkit.event.Listener;\n");
        code.append("import org.bukkit.event.EventHandler;\n");
        code.append("import org.bukkit.event.player.PlayerInteractEvent;\n");
        code.append("import org.bukkit.Material;\n");
        code.append("import org.bukkit.inventory.ItemStack;\n");
        code.append("import org.bukkit.entity.Player;\n\n");
        code.append("public class GeneratedPlugin implements Listener {\n\n");
        code.append("    @EventHandler\n");
        code.append("    public void onPlayerInteract(PlayerInteractEvent event) {\n");
        code.append("        Player player = event.getPlayer();\n");

        // Simple event check: if event contains "diamond block"
        if (event.toLowerCase().contains("diamond block")) {
            code.append("        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DIAMOND_BLOCK) {\n");
        } else {
            code.append("        // Event condition not recognized\n");
            code.append("        if (true) {\n");
        }

        // Simple action: if action contains "give 5 emeralds"
        if (action.toLowerCase().contains("give") && action.toLowerCase().contains("emerald")) {
            Pattern numPattern = Pattern.compile("give\\s+(\\d+)\\s+emerald");
            Matcher numMatcher = numPattern.matcher(action.toLowerCase());
            int amount = 5; // default
            if (numMatcher.find()) {
                amount = Integer.parseInt(numMatcher.group(1));
            }
            code.append("            player.getInventory().addItem(new ItemStack(Material.EMERALD, ").append(amount).append("));\n");
        } else {
            code.append("            // Action not recognized\n");
        }

        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    private String generateTimedCode(int minutes, String action) {
        StringBuilder code = new StringBuilder();
        code.append("package generated;\n\n");
        code.append("import org.bukkit.plugin.java.JavaPlugin;\n");
        code.append("import org.bukkit.Bukkit;\n");
        code.append("import org.bukkit.Material;\n");
        code.append("import org.bukkit.inventory.ItemStack;\n");
        code.append("import org.bukkit.entity.Player;\n\n");
        code.append("public class GeneratedPlugin extends JavaPlugin {\n\n");
        code.append("    @Override\n");
        code.append("    public void onEnable() {\n");
        code.append("        getServer().getScheduler().runTaskTimer(this, () -> {\n");

        if (action.contains("give food")) {
            code.append("            for (Player player : Bukkit.getOnlinePlayers()) {\n");
            code.append("                player.getInventory().addItem(new ItemStack(Material.BREAD, 1));\n");
            code.append("            }\n");
        } else {
            code.append("            // Action not recognized\n");
        }

        code.append("        }, 0L, ").append(minutes * 60 * 20).append("L); // ").append(minutes).append(" minutes in ticks\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    public void learnPattern(String input, String code) {
        learnedPatterns.put(input.toLowerCase(), code);
    }

    public String getLearnedCode(String input) {
        return learnedPatterns.get(input.toLowerCase());
    }
}
