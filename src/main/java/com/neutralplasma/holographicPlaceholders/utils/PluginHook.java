package com.neutralplasma.holographicPlaceholders.utils;

public enum PluginHook {
    SIMPLEHOLOGRAMS(),
    HOLOGRAPHICDISPLAYS(),
    BOTH();

    public boolean shouldEnable(PluginHook hook){
        if(hook == this){
            return true;
        }
        return this == BOTH;
    }
}
