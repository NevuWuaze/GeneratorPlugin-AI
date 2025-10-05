package com.example.generatorai;

import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class SecurityManager {

    private static final Pattern FORBIDDEN_PATTERNS = Pattern.compile(
        "(?i)(system\\.exit|runtime\\.exec|file\\.delete|processbuilder|classloader|reflection)"
    );

    public boolean validateCode(String code, Player player) {
        // Check for forbidden operations
        if (FORBIDDEN_PATTERNS.matcher(code).find()) {
            player.sendMessage("Code contains forbidden operations.");
            return false;
        }

        // Check code size
        int maxSize = 500 * 1024; // 500KB
        if (code.length() > maxSize) {
            player.sendMessage("Code exceeds maximum size limit.");
            return false;
        }

        // Additional validations can be added
        return true;
    }

    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }
}
