package com.neutralplasma.holographicPlaceholders.addons.protocolLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.Addon;

import java.util.Arrays;
import java.util.List;

public class ProtocolHook extends Addon {
    private HolographicPlaceholders holographicPlaceholders;

    public ProtocolHook(HolographicPlaceholders holographicPlaceholders){
        this.holographicPlaceholders = holographicPlaceholders;
    }


    @Override
    public void onEnable() {
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
        HologramsAPI.registerPlaceholder(holographicPlaceholders, "{fast}", 0.1, new TimePlaceholdersUpdater(changingTextImitation));
        HologramsAPI.registerPlaceholder(holographicPlaceholders, "{medium}", 1, new TimePlaceholdersUpdater(changingTextImitation));
        HologramsAPI.registerPlaceholder(holographicPlaceholders, "{slow}", 10, new TimePlaceholdersUpdater(changingTextImitation));
    }

    public void unregisterFastPlaceholders(){
        HologramsAPI.unregisterPlaceholder(holographicPlaceholders, "{fast}");
        HologramsAPI.unregisterPlaceholder(holographicPlaceholders, "{medium}");
        HologramsAPI.unregisterPlaceholder(holographicPlaceholders, "{slow}");
    }
}
