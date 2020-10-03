package com.neutralplasma.holographicPlaceholders;

import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.addons.baltop.BalTopAddon;

import com.neutralplasma.holographicPlaceholders.addons.*;
import com.neutralplasma.holographicPlaceholders.addons.baltop.BalTopAddonV2;
import com.neutralplasma.holographicPlaceholders.addons.protocolLib.ProtocolHook;
import com.neutralplasma.holographicPlaceholders.addons.statistics.Modulator;
import com.neutralplasma.holographicPlaceholders.command.*;
import com.neutralplasma.holographicPlaceholders.events.CloseInventoryEvent;
import com.neutralplasma.holographicPlaceholders.events.InventoryOpenEvent;
import com.neutralplasma.holographicPlaceholders.events.OnClickEvent;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderRegistry;
import com.neutralplasma.holographicPlaceholders.storage.DataStorage;
import com.neutralplasma.holographicPlaceholders.utils.*;
import eu.virtusdevelops.virtuscore.command.CommandManager;
import eu.virtusdevelops.virtuscore.managers.FileManager;
import eu.virtusdevelops.virtuscore.utils.FileLocation;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public class HolographicPlaceholders extends JavaPlugin {

    private ConfigUtil configUtil;
    private DataStorage dataStorage;
    private FileManager fileManager;
    private CommandManager commandManager;
    private PluginHook pluginHook;
    private PlaceholderRegistry placeholderRegistry;
    Metrics metrics;

    Handler handler;
    private Map<Addon, String> addons = new HashMap<>();
    private Modulator modulator;
    private static Economy econ = null;


    @Override
    public void onEnable() {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(TextUtils.colorFormat("&e==================[ &7Starting &e]=================="));
        long time = System.currentTimeMillis();
        PluginManager pm = Bukkit.getPluginManager();
        // Check if HologramsPlugin is present.
        if(pm.isPluginEnabled("SimpleHolograms")){
            pluginHook = PluginHook.SIMPLEHOLOGRAMS;
        }else if(pm.isPluginEnabled("HolographicDisplays")){
            pluginHook = PluginHook.HOLOGRAPHICDISPLAYS;
        }else{
            sender.sendMessage("Could not find any compatible holograms plugin, disabling plugin...");
            pm.disablePlugin(this);
            return;
        }
        // Regsisters placeholderRegistry
        this.placeholderRegistry = new PlaceholderRegistry(this);


        metrics = new Metrics(this); // Metrics for bstats
        // Loads configuration files.
        configUtil = new ConfigUtil(this);
        this.fileManager = new FileManager(this, new LinkedHashSet<>(Arrays.asList(
                FileLocation.of("signs.yml", true, false)
        )));
        this.fileManager.loadFiles();

        // Configures commandManager
        this.commandManager = new CommandManager(this);

        // DataStorage
        this.dataStorage = new DataStorage(this);
        dataStorage.setup();
        setupConfig();
        setupEconomy();

        registerAddons();


        // Gui stuff that needs to be recoded.
        handler = new Handler(this);
        setupGui();

        // Register commands.
        registerCommands();
        time = (time - System.currentTimeMillis())*-1;
        //this.getLogger().setLevel(Level.INFO);
        sender.sendMessage(TextUtils.colorFormat("&e=================[ &7Done: &e" + time + "&7ms &e]================="));
    }

    @Override
    public void onDisable() {
        unregisterAddons();
        dataStorage.saveData();
        super.onDisable();
    }

    public void unregisterAddons(){
        for(Addon addon : addons.keySet()){
            if(addon.isEnabled()) {
                addon.onDisable();
                Bukkit.getConsoleSender().sendMessage(TextUtils.colorFormat("&8[&eHPE&8] &7Disabled: &e" + addon.getName()));
                if(addon.getName().equals("ProtocolLib")) {
                    metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "False"));
                }
            }
        }
        addons.clear();
    }

    public void registerAddons(){
        //List<Addon> toRemove = new ArrayList<>();
        BalTopAddon balTopAddon = new BalTopAddon(this, fileManager, dataStorage,"BalTop", placeholderRegistry);
        BalTopAddonV2 balTopAddonV2 = new BalTopAddonV2(this, "BalTopV2", placeholderRegistry);
        Addon placeholderAddon = new PlaceholderAPI(this, placeholderRegistry);
        //Addon protocolAddon = new ProtocolHook(this, placeholderRegistry);

        modulator = new Modulator(this, dataStorage, fileManager, placeholderRegistry);

        // OLD SYSTEM
        //protocolAddon.setName("ProtocolLib");
        placeholderAddon.setName("PlaceholderAPI");

        if(Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")){
            Addon protocolAddon = new ProtocolHook(this, placeholderRegistry);
            protocolAddon.setName("ProtocolLib");
            addons.put(modulator, modulator.getName());
        }


        modulator.setName("MultiPlaceholders");


        addons.put(balTopAddon, balTopAddon.getName()); // reworked
        addons.put(balTopAddonV2, balTopAddonV2.getName()); // reworked

        //addons.put(protocolAddon, protocolAddon.getName());
        addons.put(placeholderAddon, placeholderAddon.getName());
        addons.put(modulator, modulator.getName());

        for(Addon addon : addons.keySet()) {
            if (this.getConfig().getBoolean("addons." + addons.get(addon))) {
                if (!addon.isEnabled() && addon.getHook().shouldEnable(pluginHook)) {
                    addon.onEnable();
                    Bukkit.getConsoleSender().sendMessage(TextUtils.colorFormat("&8[&eHPE&8] &7Enabled: &e" + addon.getName()));
                    if (addon.getName().equals("ProtocolLib")) {
                        metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "True"));
                    }
                }else{
                    getLogger().info("Not enabling " + addon.getName() + " due to it's not compatible with current holograms plugin.");
                }
            }
        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PapiAddon(this, balTopAddon, balTopAddonV2).register();
        }

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    public void setupConfig(){
        configUtil.setupConfig();
    }

    // TODO: Redo entire GUI handler (its already in VirtusCore)
    public void setupGui(){
        Handler handler = new Handler(this);
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new OnClickEvent(), this);
        pluginManager.registerEvents(new CloseInventoryEvent(this, handler), this);
        pluginManager.registerEvents(new InventoryOpenEvent(handler), this);
    }

    // Reload
    public long reload(){
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(TextUtils.colorFormat("&e==================[ &7Reload &e]=================="));
        long time = System.currentTimeMillis();
        reloadConfig();

        this.fileManager.clear();
        this.fileManager.loadFiles();





        unregisterAddons();
        registerAddons();

        time = (time - System.currentTimeMillis())*-1;
        sender.sendMessage(TextUtils.colorFormat("&e=================[ &7Done: &e" + time + "&7ms &e]================="));
        return time;
    }

    public void enableAddon(Addon addon){
        if(addon.getHook().shouldEnable(pluginHook)) {
            addon.onEnable();
            Bukkit.getConsoleSender().sendMessage(TextUtils.colorFormat("&8[&eHPE&8] &7Enabled: &e" + addon.getName()));
            if (addon.getName().equals("ProtocolLib")) {
                metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "True"));
            }
        }else{

            getLogger().info("Not enabling " + addon.getName() + " due to it's not compatible with current holograms plugin " + addon.getHook().toString() + ":" + pluginHook.toString());
        }
    }

    public void disableAddon(Addon addon){
        addon.onDisable();
        Bukkit.getConsoleSender().sendMessage(TextUtils.colorFormat("&8[&eHPE&8] &7Disabled: &e" + addon.getName()));
        if(addon.getName().equals("ProtocolLib")) {
            metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "False"));
        }
    }


    private void registerCommands() {

        commandManager.addMainCommand("hpe").addSubCommands(
                new ReloadCommand(this),
                new MenuCommand(this, handler),
                new RemoveHeadCommand(this, modulator),
                new RemoveSignCommand(this, modulator),
                new SetHeadCommand(this, modulator),
                new SetSignCommand(this, modulator)
        );
    }

    public PluginHook getPluginHook(){
        return this.pluginHook;
    }

    public Map<Addon, String> getAddons(){
        return addons;
    }
}
