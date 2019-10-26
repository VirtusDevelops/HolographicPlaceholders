package com.neutralplasma.holographicPlaceholders.utils;
import org.bukkit.ChatColor;

public class TextFormater {
    public String formatText(String message){
        String formated = ChatColor.translateAlternateColorCodes('&', message);
        return formated;
    }

    public static String sFormatText(String message){
        String formated = ChatColor.translateAlternateColorCodes('&', message);
        return formated;
    }
}

