package com.neutralplasma.holographicPlaceholders.gui;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.gui.guis.MainGUI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Handler {

    private HolographicPlaceholders holographicPlaceholders;

    private List<UUID> openedInv = new ArrayList<>();

    private MainGUI mainGUI;

    public Handler(HolographicPlaceholders holographicPlaceholders){
        this.holographicPlaceholders = holographicPlaceholders;
        mainGUI = new MainGUI(this, holographicPlaceholders);
    }
    public void openMainGUI(Player player){
        mainGUI.openGui(player);
    }

    public void openAddonGUI(String addon, Player player){

    }

    public void addToList(UUID uuid){
        openedInv.add(uuid);
    }
    public void removeFromList(UUID uuid){
        openedInv.remove(uuid);
    }
    public boolean hasOpened(UUID uuid) {
        return openedInv.contains(uuid);
    }
}
