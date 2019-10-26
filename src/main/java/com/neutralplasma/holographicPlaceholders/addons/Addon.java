package com.neutralplasma.holographicPlaceholders.addons;

public abstract class Addon {

    private String name = "none";

    private boolean isEnabled = false;

    public boolean isEnabled(){
        return this.isEnabled;
    }

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
}
