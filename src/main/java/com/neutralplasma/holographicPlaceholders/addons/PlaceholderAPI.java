package com.neutralplasma.holographicPlaceholders.addons;


import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderRegistry;
import com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderReplacer;
import com.neutralplasma.holographicPlaceholders.utils.PluginHook;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderAPI extends Addon {
    private HolographicPlaceholders holographicPlaceholders;
    private String placeholder;
    private PlaceholderRegistry placeholderRegistry;

    public PlaceholderAPI(HolographicPlaceholders holographicPlaceholders, PlaceholderRegistry placeholderRegistry){
        this.holographicPlaceholders = holographicPlaceholders;
        this.placeholderRegistry = placeholderRegistry;
        placeholders = cast(holographicPlaceholders.getConfig().getStringList("placeholderAPI.placeholders"));
        this.setHook(PluginHook.HOLOGRAPHICDISPLAYS);
    }

    private List<String> placeholders;

    @SuppressWarnings("unchecked")
    public static <T extends List<?>> T cast(Object obj) {
        return (T) obj;
    }


    @Override
    public void onEnable() {
        placeholders = cast(holographicPlaceholders.getConfig().getStringList("placeholderAPI.placeholders"));
        registerPapiPlaceholders();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        unregisterPlaceholders();
        super.onDisable();
    }

    public void registerPapiPlaceholders(){
        for(int index = 0; index < placeholders.size(); index++){
            String unformatedPlaceholder = placeholders.get(index);
            placeholder = placeholders.get(index);
            long delay = holographicPlaceholders.getConfig().getLong("placeholderAPI.delay");
            int i = index;

            placeholderRegistry.getRegister().registerPlaceholder(holographicPlaceholders, unformatedPlaceholder, delay, new PlaceholderReplacer() {
                @Override
                public String update() {
                    placeholder = placeholders.get(i);
                    placeholder = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(null, placeholder);
                    return placeholder;
                }
            });

        }
    }

    public void unregisterPlaceholders(){
        for(String placeholder : placeholders){
            placeholderRegistry.getRegister().unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
    }
}
