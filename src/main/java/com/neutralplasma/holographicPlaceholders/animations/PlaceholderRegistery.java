package com.neutralplasma.holographicPlaceholders.animations;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderRegistry;
import com.neutralplasma.holographicPlaceholders.storage.AnimationConfig;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderRegistery {
    private final HolographicPlaceholders holographicPlaceholders;
    private final List<AnimationConfig> placeholderList;
    private final PlaceholderRegistry placeholderRegistry;
    private static final String PLACEHOLDER_PREFIX = "hpe:";

    public PlaceholderRegistery(HolographicPlaceholders holographicPlaceholders, PlaceholderRegistry placeholderRegistry) {
        placeholderList = new ArrayList<>();
        this.holographicPlaceholders = holographicPlaceholders;
        this.placeholderRegistry = placeholderRegistry;
    }

    public void registerPlaceholder(AnimationConfig animation) {
        placeholderList.add(animation);

        //Bukkit.getConsoleSender().sendMessage("§e[HPE] §7Registering placeholder '" + animation.getName() + "' from config.");
        String placeholder = "{" + PLACEHOLDER_PREFIX + animation.getName() + "}";
        placeholderRegistry.getRegister().registerPlaceholder(holographicPlaceholders, placeholder, animation.getRefreshRate(), new PlaceholderReplacer(animation.getFrames()));
    }

    public void unregisterPlaceholders(){
        for (AnimationConfig animation : placeholderList) {
            String placeholder = "{" + PLACEHOLDER_PREFIX + animation.getName() + "}";
            placeholderRegistry.getRegister().unregisterPlaceholder(holographicPlaceholders, placeholder);
            //HologramsAPI.unregisterPlaceholder(holographicPlaceholders, placeholder);
        }
    }

    public List<AnimationConfig> getRegisteredPlaceholders() {
        return placeholderList;
    }

    public AnimationConfig getPlaceholderFromName(String name) throws Exception {
        for (AnimationConfig animation : placeholderList) {
            if (animation.getName().equalsIgnoreCase(name)) return animation;
        }
        throw new Exception();
    }
}