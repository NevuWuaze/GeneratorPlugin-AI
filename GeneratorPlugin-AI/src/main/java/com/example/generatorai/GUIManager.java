package com.example.generatorai;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIManager implements Listener {

    private Main plugin;
    private CommandHandler commandHandler;

    public GUIManager(Main plugin, CommandHandler commandHandler) {
        this.plugin = plugin;
        this.commandHandler = commandHandler;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openMainMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "GeneratorPlugin-AI Menu");

        // Create plugin button
        ItemStack createItem = new ItemStack(Material.DIAMOND);
        ItemMeta createMeta = createItem.getItemMeta();
        createMeta.setDisplayName("Create Plugin");
        createItem.setItemMeta(createMeta);
        menu.setItem(11, createItem);

        // Reload button
        ItemStack reloadItem = new ItemStack(Material.REDSTONE);
        ItemMeta reloadMeta = reloadItem.getItemMeta();
        reloadMeta.setDisplayName("Reload Plugins");
        reloadItem.setItemMeta(reloadMeta);
        menu.setItem(13, reloadItem);

        // List button
        ItemStack listItem = new ItemStack(Material.BOOK);
        ItemMeta listMeta = listItem.getItemMeta();
        listMeta.setDisplayName("List Plugins");
        listItem.setItemMeta(listMeta);
        menu.setItem(15, listItem);

        player.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("GeneratorPlugin-AI Menu")) return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();

        switch (name) {
            case "Create Plugin":
                player.closeInventory();
                player.sendMessage("Use /ai createplugin <name> <description>");
                break;
            case "Reload Plugins":
                plugin.getPluginManager().reloadGeneratedPlugins();
                player.sendMessage("Plugins reloaded.");
                break;
            case "List Plugins":
                player.sendMessage("Generated plugins: " + String.join(", ", plugin.getPluginManager().getGeneratedPluginNames()));
                break;
        }
    }
}
