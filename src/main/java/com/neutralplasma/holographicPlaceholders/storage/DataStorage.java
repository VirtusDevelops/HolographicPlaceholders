package com.neutralplasma.holographicPlaceholders.storage;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;

public class DataStorage {

    private FileConfiguration dataConfiguration;
    private File dataFile;
    private HolographicPlaceholders plugin;

    public DataStorage(HolographicPlaceholders plugin){
        this.plugin = plugin;
    }

    public void setup(){

        //creates plugin folder
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        //---------------------

        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if(!dataFile.exists()){
            try{
                dataFile.createNewFile();
                dataConfiguration = YamlConfiguration.loadConfiguration(dataFile);
                plugin.saveResource("data.yml", true);
                Bukkit.getConsoleSender().sendMessage(TextFormater.sFormatText("&aSuccessfully created data.yml file!"));


            }catch (IOException e){
                Bukkit.getConsoleSender().sendMessage(TextFormater.sFormatText("&cFailed to create data.yml file, Error: &f" + e.getMessage()));

            }

        }
        dataConfiguration = YamlConfiguration.loadConfiguration(dataFile);
    }

    public FileConfiguration getData() {
        return dataConfiguration;
    }


    public void saveData(){
        try{
            dataConfiguration.save(dataFile);
            //Bukkit.getConsoleSender().sendMessage(TextUtil.colorFormat("&aSuccessfully saved data.yml file."));
        }catch(IOException e){
            Bukkit.getConsoleSender().sendMessage(TextFormater.sFormatText("&cFailed to save data.yml file, Error: &f" + e.getMessage()));
        }
    }

    public void reloadData() {
        dataConfiguration = YamlConfiguration.loadConfiguration(dataFile);
        Bukkit.getConsoleSender().sendMessage(TextFormater.sFormatText("&aReloaded data.yml file."));
    }
}