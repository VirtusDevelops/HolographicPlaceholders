package com.neutralplasma.holographicPlaceholders.utils;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import org.bukkit.Bukkit;
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
        double newestversion = 2.0;

        if(version < newestversion){
            if(Bukkit.getPluginManager().isPluginEnabled("Stats")) {
                statsAddon();
                killsAddon();
            }
            recreateConfig();
        }
    }

    public void recreateConfig(){
        FileConfiguration config = holographicPlaceholders.getConfig();
        config.set("addons.BalTop", true);
        config.set("addons.ProtocolLib", true);
        config.set("addons.PlaceholderAPI", true);
        config.set("placeholderAPI.delay", 1);
        List<String> placeholders = new ArrayList<>();
        placeholders.add("%server_name%");
        config.set("placeholderAPI.placeholders", placeholders);
        config.set("BalTop.delay", 200.0);
        config.set("BalTop.placeholder-delay", 10.0);
        config.set("BalTop.offline-delay", 800.0);
        config.set("BalTop.size", 10);
        config.set("BalTop.format", 1);
        List<String> excludedusers = new ArrayList<>();
        excludedusers.add("name1");
        excludedusers.add("name2");
        excludedusers.add("name3");
        config.set("BalTop.excluded-users", excludedusers);
        config.set("config-version", 2.1);
        holographicPlaceholders.saveConfig();
    }

    public void statsAddon(){
        FileConfiguration config = holographicPlaceholders.getConfig();
        config.set("stats.enabled", false);
        config.set("stats.mysql.connection", "localhost:3306");
        config.set("stats.mysql.user", "root");
        config.set("stats.mysql.password", "someComplicatedPassword");
        config.set("stats.mysql.database", "database");
        config.set("stats.mysql.timeout", 3600);
        config.set("stats.mysql.poolsize", 10);
        holographicPlaceholders.saveConfig();
    }

    public void killsAddon(){
        FileConfiguration config = holographicPlaceholders.getConfig();
        config.set("addons.Kills", false);
        config.set("Kills.delay", 200.0);
        config.set("Kills.placeholder-delay", 10.0);
        config.set("Kills.size", 10);
        holographicPlaceholders.saveConfig();
    }
}
