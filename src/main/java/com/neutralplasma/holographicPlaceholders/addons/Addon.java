package com.neutralplasma.holographicPlaceholders.addons;

import com.neutralplasma.holographicPlaceholders.storage.SignLocation;
import com.neutralplasma.holographicPlaceholders.utils.PluginHook;

import java.util.HashMap;

public abstract class Addon {

    public String name = "none";

    private boolean isEnabled = false;

    private PluginHook hook = PluginHook.BOTH;

    public boolean isEnabled(){
        return this.isEnabled;
    }

    public PluginHook getHook() { return this.hook; }

    public void onEnable(){
        this.isEnabled = true;
    }

    public void onDisable(){
        this.isEnabled = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHook(PluginHook hook) {
        this.hook = hook;
    }
}
