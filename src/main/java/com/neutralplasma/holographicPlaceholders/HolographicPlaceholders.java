package com.neutralplasma.holographicPlaceholders;

import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.addons.baltop.BalTopAddon;

import com.neutralplasma.holographicPlaceholders.addons.PlaceholderAPI;
import com.neutralplasma.holographicPlaceholders.addons.PapiAddon;
import com.neutralplasma.holographicPlaceholders.addons.baltop.BalTopAddonV2;
import com.neutralplasma.holographicPlaceholders.addons.playTime.PlayTimeAddon;
import com.neutralplasma.holographicPlaceholders.addons.protocolLib.ProtocolHook;
import com.neutralplasma.holographicPlaceholders.addons.statistics.Modulator;
import com.neutralplasma.holographicPlaceholders.commands.CommandHandler;
import com.neutralplasma.holographicPlaceholders.commands.MainCommand;
import com.neutralplasma.holographicPlaceholders.commands.TabComplete;
import com.neutralplasma.holographicPlaceholders.commands.subCommands.*;
import com.neutralplasma.holographicPlaceholders.events.CloseInventoryEvent;
import com.neutralplasma.holographicPlaceholders.events.InventoryOpenEvent;
import com.neutralplasma.holographicPlaceholders.events.OnClickEvent;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import com.neutralplasma.holographicPlaceholders.storage.DataStorage;
import com.neutralplasma.holographicPlaceholders.utils.*;
import eu.virtusdevelops.virtuscore.managers.FileManager;
import eu.virtusdevelops.virtuscore.utils.FileLocation;
import eu.virtusdevelops.virtuscore.utils.TextUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;


public class HolographicPlaceholders extends JavaPlugin {

    private ConfigUtil configUtil;
    private DataStorage dataStorage;
    private FileManager fileManager;
    Metrics metrics;

    Handler handler;
    private Map<Addon, String> addons = new HashMap<>();
    private Modulator modulator;
    private static Economy econ = null;


    @Override
    public void onEnable() {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(TextUtil.colorFormat("&e==================[ &7Starting &e]=================="));
        long time = System.currentTimeMillis();
        metrics = new Metrics(this);
        configUtil = new ConfigUtil(this);
        this.fileManager = new FileManager(this, new LinkedHashSet<>(Arrays.asList(
                FileLocation.of("signs.yml", true, false))
        ));
        this.fileManager.loadFiles();

        this.dataStorage = new DataStorage(this);
        dataStorage.setup();
        setupConfig();
        setupEconomy();

        registerAddons();

        handler = new Handler(this);
        setupGui();
        registerCommands();
        time = (time - System.currentTimeMillis())*-1;
        this.getLogger().setLevel(Level.INFO);
        sender.sendMessage(TextUtil.colorFormat("&e=================[ &7Done: &e" + time + "&7ms &e]================="));
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
                Bukkit.getConsoleSender().sendMessage(TextUtil.colorFormat("&8[&eHPE&8] &7Disabled: &e" + addon.getName()));
                if(addon.getName().equals("ProtocolLib")) {
                    metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "False"));
                }
            }
        }
        addons.clear();
    }

    public void registerAddons(){
        //List<Addon> toRemove = new ArrayList<>();
        BalTopAddon baltopAddon = new BalTopAddon(this, "BalTop");
        BalTopAddonV2 balTopAddonV2 = new BalTopAddonV2(this, "BalTopV2");
        Addon protocolAddon = new ProtocolHook(this);
        Addon placeholderAddon = new PlaceholderAPI(this);
        PlayTimeAddon playTimeAddon = new PlayTimeAddon(this, dataStorage);
        modulator = new Modulator(this, dataStorage, fileManager);

        // OLD SYSTEM
        protocolAddon.setName("ProtocolLib");
        placeholderAddon.setName("PlaceholderAPI");
        playTimeAddon.setName("PlayTime");
        modulator.setName("MultiPlaceholders");


        addons.put(baltopAddon, baltopAddon.getName()); // reworked
        addons.put(balTopAddonV2, balTopAddonV2.getName()); // reworked

        addons.put(protocolAddon, protocolAddon.getName());
        addons.put(placeholderAddon, placeholderAddon.getName());
        addons.put(playTimeAddon, placeholderAddon.getName());
        addons.put(modulator, modulator.getName());

        for(Addon addon : addons.keySet()) {
            if (this.getConfig().getBoolean("addons." + addons.get(addon))) {
                if (!addon.isEnabled()) {
                    addon.onEnable();
                    Bukkit.getConsoleSender().sendMessage(TextUtil.colorFormat("&8[&eHPE&8] &7Enabled: &e" + addon.getName()));
                    if (addon.getName().equals("ProtocolLib")) {
                        metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "True"));
                    }
                }
            }
        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PapiAddon(this, baltopAddon, playTimeAddon).register();
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

    public void setupGui(){
        Handler handler = new Handler(this);
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new OnClickEvent(), this);
        pluginManager.registerEvents(new CloseInventoryEvent(this, handler), this);
        pluginManager.registerEvents(new InventoryOpenEvent(handler), this);
    }

    public long reload(){
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(TextUtil.colorFormat("&e==================[ &7Reload &e]=================="));
        long time = System.currentTimeMillis();
        reloadConfig();

        this.fileManager.clear();
        this.fileManager.loadFiles();

        unregisterAddons();
        registerAddons();
        time = (time - System.currentTimeMillis())*-1;
        sender.sendMessage(TextUtil.colorFormat("&e=================[ &7Done: &e" + time + "&7ms &e]================="));
        return time;
    }
    public void enableAddon(Addon addon){
        addon.onEnable();
        Bukkit.getConsoleSender().sendMessage(TextUtil.colorFormat("&8[&eHPE&8] &7Enabled: &e" + addon.getName()));
        if(addon.getName().equals("ProtocolLib")) {
            metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "True"));
        }
    }

    public void disableAddon(Addon addon){
        addon.onDisable();
        Bukkit.getConsoleSender().sendMessage(TextUtil.colorFormat("&8[&eHPE&8] &7Disabled: &e" + addon.getName()));
        if(addon.getName().equals("ProtocolLib")) {
            metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "False"));
        }
    }

    public void reload(Addon addon){
        addon.onDisable();
        addon.onEnable();
    }

    private void registerCommands() {

        CommandHandler handler = new CommandHandler();

        //Registers the command /example which has no arguments.
        handler.register("hpe", new MainCommand(this, this.handler));

        //Registers the command /example args based on args[0] (args)
        handler.register("reload", new ReloadCommand(this));
        handler.register("setsign", new SetSignCommand(this, modulator));
        handler.register("sethead", new SetHeadCommand(this, modulator));
        handler.register("removehead", new RemoveHeadCommand(this, modulator));
        handler.register("removesign", new RemoveSignCommand(this, modulator));

        getCommand("hpe").setExecutor(handler);
        getCommand("hpe").setTabCompleter(new TabComplete(this));
    }

    public Map<Addon, String> getAddons(){
        return addons;
    }
}
