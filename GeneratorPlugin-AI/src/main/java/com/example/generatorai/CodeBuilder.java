package com.example.generatorai;

public class CodeBuilder {

    public String buildBasicPlugin(String event, String action) {
        // Similar to AIInterpreter's generateCode, but more modular
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
        code.append("        // ").append(event).append("\n");
        code.append("        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DIAMOND_BLOCK) {\n");
        code.append("            // ").append(action).append("\n");
        code.append("            player.getInventory().addItem(new ItemStack(Material.EMERALD, 5));\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        return code.toString();
    }
}
