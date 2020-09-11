package com.neutralplasma.holographicPlaceholders.storage;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.animations.AnimationReplacer;
import eu.virtusdevelops.virtuscore.VirtusCore;
import eu.virtusdevelops.virtuscore.managers.FileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class AnimationLoader {
    private FileManager fileManager;
    private HolographicPlaceholders holographicPlaceholders;

    public AnimationLoader(HolographicPlaceholders holographicPlaceholders, FileManager fileManager){
        this.fileManager = fileManager;
        this.holographicPlaceholders = holographicPlaceholders;
    }

    public void reload(){
        loadAnimations();
    }

    private void loadAnimations() {
        FileConfiguration config = fileManager.getConfiguration("animations");
        for (String animationName : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(animationName);

            List<String> frames = section.getStringList("frames");
            double speed = section.getDouble("speed");
            frames = AnimationReplacer.setAnimations(frames);

            AnimationConfig animation = new AnimationConfig(animationName, speed, frames);

            holographicPlaceholders.getPlaceholderRegistery().registerPlaceholder(animation);
            VirtusCore.console().sendMessage("Loading: " + animationName);
        }
    }



}
