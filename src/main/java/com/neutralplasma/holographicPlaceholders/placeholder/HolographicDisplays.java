package com.neutralplasma.holographicPlaceholders.placeholder;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class HolographicDisplays extends Register{

    @Override
    public void registerPlaceholder(JavaPlugin plugin, String placeholder, double delay, PlaceholderReplacer replacer) {
        HologramsAPI.registerPlaceholder(plugin, placeholder, delay, new com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer() {
            @Override
            public String update() {
               return replacer.update();
            }
        });

        super.registerPlaceholder(plugin, placeholder, delay, replacer);
    }

    @Override
    public void unregisterPlaceholder(JavaPlugin plugin, String placeholder) {
        HologramsAPI.unregisterPlaceholder(plugin, placeholder);
        super.unregisterPlaceholder(plugin, placeholder);
    }
}
