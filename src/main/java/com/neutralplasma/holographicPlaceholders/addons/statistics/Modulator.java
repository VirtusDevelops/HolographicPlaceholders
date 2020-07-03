package com.neutralplasma.holographicPlaceholders.addons.statistics;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.storage.DataStorage;
import com.neutralplasma.holographicPlaceholders.storage.SignLocation;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import eu.virtusdevelops.virtuscore.managers.FileManager;
import eu.virtusdevelops.virtuscore.utils.TextUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Modulator extends Addon {
    private List<Module> modules = new ArrayList<>();
    private HolographicPlaceholders holographicPlaceholders;
    private DataStorage dataStorage;
    private FileManager fileManager;

    public Modulator(HolographicPlaceholders holographicPlaceholders, DataStorage dataStorage, FileManager fileManager){
        this.holographicPlaceholders = holographicPlaceholders;
        this.dataStorage = dataStorage;
        this.fileManager = fileManager;
    }

    @Override
    public void onEnable() {
        startModules();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        stopModules();
        super.onDisable();
    }

    public void stopModules(){
        for(Module module : modules){
            module.unRegisterPlaceholders(holographicPlaceholders);
            List<String> data = new ArrayList<>();
            for(SignLocation signLocation : module.getSigns().keySet()){
                data.add(signLocation.toString() + ":" + module.getSigns().get(signLocation));
            }
            dataStorage.getData().set("addon-data." + module.getPlaceholder() + ".signs", data);

            List<String> data2 = new ArrayList<>();
            for(SignLocation signLocation : module.getHeads().keySet()){
                data2.add(signLocation.toString() + ":" + module.getHeads().get(signLocation));
            }
            dataStorage.getData().set("addon-data." + module.getPlaceholder() + ".heads", data2);
        }
    }

    public boolean addSign(String placeholder, SignLocation signLocation, int position){
        for(Module module : modules){
            if(module.getPlaceholder().equalsIgnoreCase(placeholder)){
                return module.addSign(signLocation, position);
            }
        }
        return false;
    }

    public boolean addHead(String placeholder, SignLocation signLocation, int position){
        for(Module module : modules){
            if(module.getPlaceholder().equalsIgnoreCase(placeholder)){
                return module.addHead(signLocation, position);
            }
        }
        return false;
    }
    public boolean removeSign(String placeholder, SignLocation signLocation){
        for(Module module : modules){
            if(module.getPlaceholder().equalsIgnoreCase(placeholder)){
                return module.removeSign(signLocation);
            }
        }
        return false;
    }
    public boolean removeHead(String placeholder, SignLocation signLocation){
        for(Module module : modules){
            if(module.getPlaceholder().equalsIgnoreCase(placeholder)){
                return module.removeHead(signLocation);
            }
        }
        return false;
    }

    public void startModules(){
        ConfigurationSection configurationSection = holographicPlaceholders.getConfig().getConfigurationSection("placeholder-addons");

        for(String placeholder : configurationSection.getKeys(false)){
            if(PlaceholderAPI.containsPlaceholders(placeholder)){

                long interval = holographicPlaceholders.getConfig().getLong("placeholder-addons." + placeholder + ".interval");
                int size = holographicPlaceholders.getConfig().getInt("placeholder-addons." + placeholder + ".size");
                String type = holographicPlaceholders.getConfig().getString("placeholder-addons." + placeholder + ".value");
                int format = holographicPlaceholders.getConfig().getInt("placeholder-addons." + placeholder + ".format");
                int placeholderdelay = holographicPlaceholders.getConfig().getInt("placeholder-addons." + placeholder + ".placeholder-delay");
                boolean doSigns = holographicPlaceholders.getConfig().getBoolean("placeholder-addons." + placeholder + ".signs");
                boolean doHeads = holographicPlaceholders.getConfig().getBoolean("placeholder-addons." + placeholder + ".heads");
                // ADDING SIGNS W.I.P
                List<String> signs = dataStorage.getData().getStringList("addon-data." + placeholder + ".signs");
                HashMap<SignLocation, Integer> signsdata = new HashMap<>();

                for(String loc : signs){
                    String[] splited = loc.split(":");

                    SignLocation location = new SignLocation(Integer.valueOf(splited[1]),
                            Integer.valueOf(splited[2]), Integer.valueOf(splited[3]),
                                    splited[0]);
                    signsdata.put(location, Integer.valueOf(splited[4]));
                }
                //

                List<String> heads = dataStorage.getData().getStringList("addon-data." + placeholder + ".heads");
                HashMap<SignLocation, Integer> headsdata = new HashMap<>();

                for(String loc : heads){
                    String[] splited = loc.split(":");

                    SignLocation location = new SignLocation(Integer.valueOf(splited[1]),
                            Integer.valueOf(splited[2]), Integer.valueOf(splited[3]),
                            splited[0]);
                    headsdata.put(location, Integer.valueOf(splited[4]));
                }

                Module module = new Module(placeholder, interval, size, type, format, placeholderdelay, doSigns,
                        doHeads, dataStorage, holographicPlaceholders, fileManager);
                module.addSigns(signsdata);
                module.addHeads(headsdata);
                modules.add(module);

                module.run();
            }else{
                Bukkit.getConsoleSender().sendMessage(TextUtil.colorFormat("&8[&6HPE&8]&7Failed loading: " + placeholder + " (Placeholder doesn't exist or module is not installed!)"));
            }
        }
    }
}
