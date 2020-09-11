package com.neutralplasma.holographicPlaceholders.placeholder;

import eu.virtusdevelops.simplehologram.API.SimpleHologramsAPI;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class SimpleHolograms extends Register{

    @Override
    public void unregisterPlaceholder(JavaPlugin plugin, String placeholder) {
        SimpleHologramsAPI.unregisterPlaceholder(plugin, placeholder);
        super.unregisterPlaceholder(plugin, placeholder);
    }

    @Override
    public void registerPlaceholder(JavaPlugin plugin, String placeholder, double delay, PlaceholderReplacer replacer) {

        SimpleHologramsAPI.registerPlaceholder(plugin, delay, new eu.virtusdevelops.simpleholograms.API.PlaceholderReplacer() {

            @Override
            public String update() {
                return replacer.update();
            }
        }, placeholder);

        super.registerPlaceholder(plugin, placeholder, delay, replacer);
    }
}
