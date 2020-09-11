package com.neutralplasma.holographicPlaceholders.addons.protocolLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderRegistry;
import com.neutralplasma.holographicPlaceholders.utils.PluginHook;

import java.util.Arrays;
import java.util.List;

public class ProtocolHook extends Addon {
    private HolographicPlaceholders holographicPlaceholders;
    private PlaceholderRegistry placeholderRegistry;

    public ProtocolHook(HolographicPlaceholders holographicPlaceholders, PlaceholderRegistry placeholderRegistry){
        this.holographicPlaceholders = holographicPlaceholders;
        this.placeholderRegistry = placeholderRegistry;
        this.setHook(PluginHook.HOLOGRAPHICDISPLAYS);
    }


    @Override
    public void onEnable() {
        if(holographicPlaceholders.getPluginHook() == PluginHook.SIMPLEHOLOGRAMS){
            holographicPlaceholders.getLogger().info("Not enabling this expansion.. its built into holograms plugin");
            super.onEnable();
            return;
        }

        hook(holographicPlaceholders);
        registerFastPlaceholder();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        unHook(holographicPlaceholders);
        unregisterFastPlaceholders();
        super.onDisable();
    }

    public static void hook(HolographicPlaceholders holographicPlaceholders){
        final PacketAdapter.AdapterParameteters params = PacketAdapter.params().plugin(holographicPlaceholders).types(PacketType.Play.Server.ENTITY_METADATA);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketEditor(params));
    }
    public static void unHook(HolographicPlaceholders holographicPlaceholders){
        ProtocolLibrary.getProtocolManager().removePacketListeners(holographicPlaceholders);
    }

    public void registerFastPlaceholder(){
        final List<String> changingTextImitation = Arrays.asList("&8 &r", "&7 &r");

        placeholderRegistry.getRegister().registerPlaceholder(holographicPlaceholders, "{fast}", 0.1, new TimePlaceholdersUpdater(changingTextImitation));
        placeholderRegistry.getRegister().registerPlaceholder(holographicPlaceholders, "{medium}", 1, new TimePlaceholdersUpdater(changingTextImitation));
        placeholderRegistry.getRegister().registerPlaceholder(holographicPlaceholders, "{slow}", 10, new TimePlaceholdersUpdater(changingTextImitation));

    }

    public void unregisterFastPlaceholders(){

        placeholderRegistry.getRegister().unregisterPlaceholder(holographicPlaceholders, "{fast}");
        placeholderRegistry.getRegister().unregisterPlaceholder(holographicPlaceholders, "{medium}");
        placeholderRegistry.getRegister().unregisterPlaceholder(holographicPlaceholders, "{slow}");

    }
}
