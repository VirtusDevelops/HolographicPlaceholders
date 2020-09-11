package com.neutralplasma.holographicPlaceholders.placeholder;


import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.utils.PluginHook;

public class PlaceholderRegistry {

    private Register register;

    public PlaceholderRegistry(HolographicPlaceholders holographicPlaceholders){
        if(holographicPlaceholders.getPluginHook() == PluginHook.HOLOGRAPHICDISPLAYS){
            register = new HolographicDisplays();
        }else if(holographicPlaceholders.getPluginHook() == PluginHook.SIMPLEHOLOGRAMS){
            register = new SimpleHolograms();
        }
    }

    public Register getRegister() {
        return register;
    }
}
