package com.neutralplasma.holographicPlaceholders.gui;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.gui.guis.MainGUI;
import com.neutralplasma.holographicPlaceholders.gui.guis.balTop.BalTopEdit;
import com.neutralplasma.holographicPlaceholders.gui.guis.balTop.RegisteredUsers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Handler {

    private HolographicPlaceholders holographicPlaceholders;

    private List<UUID> openedInv = new ArrayList<>();
    private List<UUID> hardFix = new ArrayList<>();

    private MainGUI mainGUI;
    private RegisteredUsers mainBaltop;
    private BalTopEdit balTopEdit;

    public Handler(HolographicPlaceholders holographicPlaceholders){
        this.holographicPlaceholders = holographicPlaceholders;
        mainGUI = new MainGUI(this, holographicPlaceholders);
        mainBaltop = new RegisteredUsers(holographicPlaceholders, this);
        balTopEdit = new BalTopEdit(holographicPlaceholders, this);
    }

    public void openAddonGUI(Addon addon, Player player){
        if(addon.getName().equalsIgnoreCase("baltop")){
            openBalTopEdit(player);
        }
    }

    public void openMainGUI(Player player){
        mainGUI.openGui(player);
    }

    public void openRegisteredUsers(Player player){
        mainBaltop.openGUI(player, 1);
    }

    public void openBalTopEdit(Player player){
        balTopEdit.openGUI(player);
    }

    public void addToList(UUID uuid){
        openedInv.add(uuid);
    }
    public void removeFromList(UUID uuid){
        openedInv.remove(uuid);
    }
    public boolean hasOpened(UUID uuid) {
        return openedInv.contains(uuid) || hardFix.contains(uuid);
    }
}
