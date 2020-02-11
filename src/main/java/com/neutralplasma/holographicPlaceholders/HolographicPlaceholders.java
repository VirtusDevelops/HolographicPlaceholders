package com.neutralplasma.holographicPlaceholders;

import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.addons.BalTopAddon;

import com.neutralplasma.holographicPlaceholders.addons.PlaceholderAPI;
import com.neutralplasma.holographicPlaceholders.addons.protocolLib.ProtocolHook;
import com.neutralplasma.holographicPlaceholders.commands.CommandHandler;
import com.neutralplasma.holographicPlaceholders.commands.MainCommand;
import com.neutralplasma.holographicPlaceholders.commands.TabComplete;
import com.neutralplasma.holographicPlaceholders.commands.subCommands.ReloadCommand;
import com.neutralplasma.holographicPlaceholders.events.CloseInventoryEvent;
import com.neutralplasma.holographicPlaceholders.events.InventoryOpenEvent;
import com.neutralplasma.holographicPlaceholders.events.OnClickEvent;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import com.neutralplasma.holographicPlaceholders.utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HolographicPlaceholders extends JavaPlugin {

    private BalanceFormater balanceFormater;
    private ConfigUtil configUtil;
    Metrics metrics;

    Handler handler;
    private Map<Addon, String> addons = new HashMap<>();
    private static Economy econ = null;

    @Override
    public void onEnable() {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(TextFormater.sFormatText("&e==================[ &7Starting &e]=================="));
        long time = System.currentTimeMillis();
        metrics = new Metrics(this);
        configUtil = new ConfigUtil(this);
        setupConfig();
        setupEconomy();
        balanceFormater = new BalanceFormater();
        registerAddons();
        handler = new Handler(this);
        setupGui();
        registerCommands();
        time = (time - System.currentTimeMillis())*-1;
        sender.sendMessage(TextFormater.sFormatText("&e=================[ &7Done: &e" + time + "&7ms &e]================="));
    }

    @Override
    public void onDisable() {
        unregisterAddons();
        super.onDisable();
    }

    public void unregisterAddons(){
        for(Addon addon : addons.keySet()){
            if(addon.isEnabled()) {
                addon.onDisable();
                Bukkit.getConsoleSender().sendMessage(TextFormater.sFormatText("&8[&eHPE&8] &7Disabled: &e" + addon.getName()));
                if(addon.getName().equals("ProtocolLib")) {
                    metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "False"));
                }
            }
        }
        addons.clear();
    }

    public void registerAddons(){
        List<Addon> toRemove = new ArrayList<>();
        Addon baltopAddon = new BalTopAddon(this, balanceFormater);
        Addon protocolAddon = new ProtocolHook(this);
        Addon placeholderAddon = new PlaceholderAPI(this);

        baltopAddon.setName("BalTop");
        protocolAddon.setName("ProtocolLib");
        placeholderAddon.setName("PlaceholderAPI");

        addons.put(baltopAddon, baltopAddon.getName());
        addons.put(protocolAddon, protocolAddon.getName());
        addons.put(placeholderAddon, placeholderAddon.getName());

        for(Addon addon : addons.keySet()) {
            if (this.getConfig().getBoolean("addons." + addons.get(addon))) {
                if (!addon.isEnabled()) {
                    addon.onEnable();
                    Bukkit.getConsoleSender().sendMessage(TextFormater.sFormatText("&8[&eHPE&8] &7Enabled: &e" + addon.getName()));
                    if (addon.getName().equals("ProtocolLib")) {
                        metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "True"));
                    }
                }
            }
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
        sender.sendMessage(TextFormater.sFormatText("&e==================[ &7Reload &e]=================="));
        long time = System.currentTimeMillis();
        reloadConfig();
        unregisterAddons();
        registerAddons();
        time = (time - System.currentTimeMillis())*-1;
        sender.sendMessage(TextFormater.sFormatText("&e=================[ &7Done: &e" + time + "&7ms &e]================="));
        return time;
    }
    public void enableAddon(Addon addon){
        addon.onEnable();
        Bukkit.getConsoleSender().sendMessage(TextFormater.sFormatText("&8[&eHPE&8] &7Enabled: &e" + addon.getName()));
        if(addon.getName().equals("ProtocolLib")) {
            metrics.addCustomChart(new Metrics.SimplePie("protocollib_enabled", () -> "True"));
        }
    }

    public void disableAddon(Addon addon){
        addon.onDisable();
        Bukkit.getConsoleSender().sendMessage(TextFormater.sFormatText("&8[&eHPE&8] &7Disabled: &e" + addon.getName()));
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

        getCommand("hpe").setExecutor(handler);
        getCommand("hpe").setTabCompleter(new TabComplete(this));
    }

    public Map<Addon, String> getAddons(){
        return addons;
    }
}
