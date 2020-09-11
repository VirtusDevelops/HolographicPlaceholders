package com.neutralplasma.holographicPlaceholders.placeholder;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Register {

    public void registerPlaceholder(JavaPlugin plugin, String placeholder, double delay, PlaceholderReplacer replacer){

    }

    public void unregisterPlaceholder(JavaPlugin plugin, String placeholder){}

}
