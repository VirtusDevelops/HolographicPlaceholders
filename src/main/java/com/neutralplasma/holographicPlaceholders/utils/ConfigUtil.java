package com.neutralplasma.holographicPlaceholders.utils;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtil {
    private HolographicPlaceholders holographicPlaceholders;

    public ConfigUtil(HolographicPlaceholders holographicPlaceholders){
        this.holographicPlaceholders = holographicPlaceholders;
    }


    public void setupConfig(){
        holographicPlaceholders.saveDefaultConfig();
        double version = holographicPlaceholders.getConfig().getDouble("config-version");
        double newestversion = 2.3;

        if(version < newestversion){
            recreateConfig();
        }
    }

    public void recreateConfig(){
        FileConfiguration config = holographicPlaceholders.getConfig();
        config.set("addons.BalTop", true);
        config.set("addons.ProtocolLib", true);
        config.set("addons.PlaceholderAPI", true);
        config.set("addons.PlayTime", true);

        config.set("placeholderAPI.delay", 1);
        List<String> placeholders = new ArrayList<>();
        placeholders.add("%server_name%");
        config.set("placeholderAPI.placeholders", placeholders);
        config.set("BalTop.delay", 200.0);
        config.set("BalTop.placeholder-delay", 10.0);
        config.set("BalTop.offline-delay", 800.0);
        config.set("BalTop.size", 10);
        config.set("BalTop.format", 1);
        config.set("PlayTime.update-delay", 4000.0);
        config.set("PlayTime.placeholder-delay", 200.0);
        config.set("PlayTime.size", 20);
        List<String> excludedusers = new ArrayList<>();
        excludedusers.add("name1");
        excludedusers.add("name2");
        excludedusers.add("name3");
        config.set("BalTop.excluded-users", excludedusers);
        config.set("config-version", 2.3);
        holographicPlaceholders.saveConfig();
    }
}
