package com.neutralplasma.holographicPlaceholders.events;


import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import com.neutralplasma.holographicPlaceholders.gui.InventoryCreator;
import com.neutralplasma.holographicPlaceholders.gui.actions.InventoryCloseAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CloseInventoryEvent implements Listener {

    private HolographicPlaceholders holographicPlaceholders;
    private Handler handler;

    public CloseInventoryEvent(HolographicPlaceholders holographicPlaceholders, Handler handler){
        this.handler = handler;
        this.holographicPlaceholders = holographicPlaceholders;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event){
        // remove player
        handler.removeFromList(event.getPlayer().getUniqueId());

        //Get our CustomHolder
        InventoryCreator customHolder = null;
        try {
            customHolder = (InventoryCreator) event.getView().getTopInventory().getHolder();
        }catch (Exception error){

        }
        if(customHolder != null){
            for(InventoryCloseAction closeAction : customHolder.getCloseActions()){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Player player = (Player) event.getPlayer();
                        if(!handler.hasOpened(event.getPlayer().getUniqueId())) {
                            closeAction.execute((Player) event.getPlayer(), event.getInventory());
                        }
                    }
                }.runTaskLater(holographicPlaceholders, 2L);
            }
        }
    }
}
