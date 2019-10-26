package com.neutralplasma.holographicPlaceholders.events;

import com.neutralplasma.holographicPlaceholders.gui.Handler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryOpenEvent implements Listener {
    private Handler handler;

    public InventoryOpenEvent(Handler handler){
        this.handler = handler;
    }

    @EventHandler
    public void onInvOpen(org.bukkit.event.inventory.InventoryOpenEvent event){
        handler.addToList(event.getPlayer().getUniqueId());
    }
}
