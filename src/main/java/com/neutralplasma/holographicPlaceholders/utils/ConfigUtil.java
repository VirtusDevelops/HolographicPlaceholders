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
        if(holographicPlaceholders.getConfig().contains("config-version")) {
            double version = holographicPlaceholders.getConfig().getDouble("config-version");
            double newestVersion = 2.7;
            if (version < newestVersion) {
                recreateConfig();
            }
        }else{
            recreateConfig();
        }
    }

    public void recreateConfig(){
        FileConfiguration config = holographicPlaceholders.getConfig();


        config.set("addons.BalTop", true);
        config.set("addons.BalTopV2", false);
        config.set("addons.ProtocolLib", true);
        config.set("addons.PlaceholderAPI", true);
        config.set("addons.PlayTime", false);
        config.set("addons.MultiPlaceholders", true);

        config.set("placeholder-addons.%statistic_deaths%.size", 10);
        config.set("placeholder-addons.%statistic_deaths%.interval", 200.0);
        config.set("placeholder-addons.%statistic_deaths%.value", "number");
        config.set("placeholder-addons.%statistic_deaths%.format", 1);
        config.set("placeholder-addons.%statistic_deaths%.signs", true);
        config.set("placeholder-addons.%statistic_deaths%.heads", true);

        config.set("placeholderAPI.delay", 1);
        List<String> placeholders = new ArrayList<>();
        placeholders.add("%server_name%");
        config.set("placeholderAPI.placeholders", placeholders);

        config.set("BalTop.delay", 200.0);
        config.set("BalTop.placeholder-delay", 10.0);
        config.set("BalTop.offline-delay", 800.0);
        config.set("BalTop.size", 10);
        config.set("BalTop.format", 1);

        config.set("BalTopV2.delay", 200.0);
        config.set("BalTopV2.placeholder-delay", 10.0);
        config.set("BalTopV2.offline-delay", 800.0);
        config.set("BalTopV2.size", 10);
        config.set("BalTopV2.format", 1);

        List<String> excludedUsers = new ArrayList<>();
        excludedUsers.add("name1");
        excludedUsers.add("name2");
        excludedUsers.add("name3");
        config.set("BalTop.excluded-users", excludedUsers);
        config.set("BalTopV2.excluded-users", new ArrayList<>(excludedUsers));

        config.set("PlayTime.update-delay", 4000.0);
        config.set("PlayTime.placeholder-delay", 200.0);
        config.set("PlayTime.size", 20);



        config.set("config-version", 2.7);
        holographicPlaceholders.saveConfig();
    }
}
