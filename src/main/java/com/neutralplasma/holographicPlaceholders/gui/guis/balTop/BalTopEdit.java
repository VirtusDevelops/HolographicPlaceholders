package com.neutralplasma.holographicPlaceholders.gui.guis.balTop;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import com.neutralplasma.holographicPlaceholders.gui.Icon;
import com.neutralplasma.holographicPlaceholders.gui.InventoryCreator;
import com.neutralplasma.holographicPlaceholders.gui.actions.ClickAction;
import com.neutralplasma.holographicPlaceholders.gui.actions.InventoryCloseAction;
import com.neutralplasma.holographicPlaceholders.utils.AbstractChatUtil;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import com.neutralplasma.holographicPlaceholders.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BalTopEdit {

    private HolographicPlaceholders holographicPlaceholders;
    private Handler handler;

    private InventoryCreator ic = new InventoryCreator(45, "BaltopGui.");
    private List<Integer> items = new ArrayList<>();

    public BalTopEdit(HolographicPlaceholders holographicPlaceholders, Handler handler){
        this.holographicPlaceholders = holographicPlaceholders;
        this.handler = handler;
    }


    public void openGUI(Player player){
        items.clear();

        registeredUsers();
        sizeButton();
        onlineRefreshButton();
        offlineRefreshButton();
        placeholderRefreshButton();

        setupBackground();

        setupCloseAction();
        player.openInventory(ic.getInventory());
    }

    public void registeredUsers(){
        ItemStack item = XMaterial.CHEST.parseItem();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(TextFormater.sFormatText("&eRegistered Users"));
        List<String> lore = new ArrayList<>();
        lore.add(TextFormater.sFormatText("&7"));
        lore.add(TextFormater.sFormatText("&7Click to see."));
        lore.add(TextFormater.sFormatText("&7"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        Icon icon = new Icon(item);
        icon.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                handler.openRegisteredUsers(player);
            }
        });
        items.add(11);
        ic.setIcon(11, icon);
    }

    public void sizeButton(){
        ItemStack item = XMaterial.WRITABLE_BOOK.parseItem();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(TextFormater.sFormatText("&eBaltop Size"));
        int currentsize = holographicPlaceholders.getConfig().getInt("BalTop.size");
        List<String> lore = new ArrayList<>();
        lore.add(TextFormater.sFormatText("&7"));
        lore.add(TextFormater.sFormatText("&7Click to modify."));
        lore.add(TextFormater.sFormatText("&7"));
        lore.add(TextFormater.sFormatText("&7Current size: &e{0}&7.").replace("{0}", String.valueOf(currentsize)));
        lore.add(TextFormater.sFormatText("&7"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        Icon icon = new Icon(item);
        icon.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                player.sendMessage(TextFormater.sFormatText("&7"));
                player.sendMessage(TextFormater.sFormatText("&7Enter a valid size number."));
                player.sendMessage(TextFormater.sFormatText("&7"));
                ic.clearCloseActions();
                AbstractChatUtil chat = new AbstractChatUtil(player, event -> {
                    String enteredinfo = event.getMessage();
                    int entered;

                    try {
                        entered = Integer.valueOf(enteredinfo);
                    } catch (Exception error) {
                        entered = 10;
                        player.sendMessage(TextFormater.sFormatText("&cPlease enter a valid number!"));
                    }

                    holographicPlaceholders.getConfig().set("BalTop.size", entered);
                    holographicPlaceholders.saveConfig();

                }, holographicPlaceholders);
                chat.setOnClose(() -> {
                    handler.removeFromList(player.getUniqueId());
                    openGUI(player);
                });
            }
        });
        items.add(13);
        ic.setIcon(13, icon);
    }

    public void onlineRefreshButton(){
        ItemStack item = XMaterial.WRITABLE_BOOK.parseItem();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(TextFormater.sFormatText("&eRefresh interval &7(&6Online&7)"));
        double currentsize = holographicPlaceholders.getConfig().getDouble("BalTop.delay");
        List<String> lore = new ArrayList<>();
        lore.add(TextFormater.sFormatText("&7"));
        lore.add(TextFormater.sFormatText("&7Click to modify."));
        lore.add(TextFormater.sFormatText("&7"));
        lore.add(TextFormater.sFormatText("&7Current: &e{0}&7.").replace("{0}", String.valueOf(currentsize)));
        lore.add(TextFormater.sFormatText("&7"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        Icon icon = new Icon(item);
        icon.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                player.sendMessage(TextFormater.sFormatText("&7"));
                player.sendMessage(TextFormater.sFormatText("&7Enter a valid refresh number."));
                player.sendMessage(TextFormater.sFormatText("&7"));
                ic.clearCloseActions();
                AbstractChatUtil chat = new AbstractChatUtil(player, event -> {
                    String enteredinfo = event.getMessage();
                    double entered;

                    try {
                        entered = Double.valueOf(enteredinfo);
                    } catch (Exception error) {
                        entered = 10;
                        player.sendMessage(TextFormater.sFormatText("&cPlease enter a valid number!"));
                    }

                    holographicPlaceholders.getConfig().set("BalTop.delay", entered);
                    holographicPlaceholders.saveConfig();

                }, holographicPlaceholders);
                chat.setOnClose(() -> {
                    handler.removeFromList(player.getUniqueId());
                    openGUI(player);
                });
            }
        });
        items.add(15);
        ic.setIcon(15, icon);
    }
    public void offlineRefreshButton(){
        ItemStack item = XMaterial.WRITABLE_BOOK.parseItem();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(TextFormater.sFormatText("&eRefresh interval &7(&6Offline&7)"));
        double currentsize = holographicPlaceholders.getConfig().getDouble("BalTop.offline-delay");
        List<String> lore = new ArrayList<>();
        lore.add(TextFormater.sFormatText("&7"));
        lore.add(TextFormater.sFormatText("&7Click to modify."));
        lore.add(TextFormater.sFormatText("&7"));
        lore.add(TextFormater.sFormatText("&7Current: &e{0}&7.").replace("{0}", String.valueOf(currentsize)));
        lore.add(TextFormater.sFormatText("&7"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        Icon icon = new Icon(item);
        icon.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                player.sendMessage(TextFormater.sFormatText("&7"));
                player.sendMessage(TextFormater.sFormatText("&7Enter a valid refresh number."));
                player.sendMessage(TextFormater.sFormatText("&7"));
                ic.clearCloseActions();
                AbstractChatUtil chat = new AbstractChatUtil(player, event -> {
                    String enteredinfo = event.getMessage();
                    double entered;

                    try {
                        entered = Double.valueOf(enteredinfo);
                    } catch (Exception error) {
                        entered = 10.0;
                        player.sendMessage(TextFormater.sFormatText("&cPlease enter a valid number!"));
                    }

                    holographicPlaceholders.getConfig().set("BalTop.offline-delay", entered);
                    holographicPlaceholders.saveConfig();

                }, holographicPlaceholders);
                chat.setOnClose(() -> {
                    handler.removeFromList(player.getUniqueId());
                    openGUI(player);
                });
            }
        });
        items.add(30);
        ic.setIcon(30, icon);
    }
    public void placeholderRefreshButton(){
        ItemStack item = XMaterial.PAPER.parseItem();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(TextFormater.sFormatText("&eRefresh interval &7(&6Placeholders&7)"));
        double currentsize = holographicPlaceholders.getConfig().getDouble("BalTop.placeholder-delay");
        List<String> lore = new ArrayList<>();
        lore.add(TextFormater.sFormatText("&7"));
        lore.add(TextFormater.sFormatText("&7Click to modify."));
        lore.add(TextFormater.sFormatText("&7"));
        lore.add(TextFormater.sFormatText("&7Current: &e{0}&7.").replace("{0}", String.valueOf(currentsize)));
        lore.add(TextFormater.sFormatText("&7"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        Icon icon = new Icon(item);
        icon.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                player.sendMessage(TextFormater.sFormatText("&7"));
                player.sendMessage(TextFormater.sFormatText("&7Enter a valid refresh number."));
                player.sendMessage(TextFormater.sFormatText("&7"));
                ic.clearCloseActions();
                AbstractChatUtil chat = new AbstractChatUtil(player, event -> {
                    String enteredinfo = event.getMessage();
                    double entered;

                    try {
                        entered = Double.valueOf(enteredinfo);
                    } catch (Exception error) {
                        entered = 10.0;
                        player.sendMessage(TextFormater.sFormatText("&cPlease enter a valid number!"));
                    }

                    holographicPlaceholders.getConfig().set("BalTop.placeholder-delay", entered);
                    holographicPlaceholders.saveConfig();

                }, holographicPlaceholders);
                chat.setOnClose(() -> {
                    handler.removeFromList(player.getUniqueId());
                    openGUI(player);
                });
            }
        });
        items.add(32);
        ic.setIcon(32, icon);
    }

    private Icon getBackGround(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("");
        item.setItemMeta(meta);
        Icon background = new Icon(item);
        background.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
            }
        });
        return background;
    }

    private void setupBackground(){
        ItemStack purple = XMaterial.MAGENTA_STAINED_GLASS_PANE.parseItem();
        ic.setIcon(0, getBackGround(purple));
        ic.setIcon(1, getBackGround(purple));
        ic.setIcon(7, getBackGround(purple));
        ic.setIcon(8, getBackGround(purple));
        ic.setIcon(9, getBackGround(purple));
        ic.setIcon(17, getBackGround(purple));
        ic.setIcon(27, getBackGround(purple));
        ic.setIcon(35, getBackGround(purple));
        ic.setIcon(36, getBackGround(purple));
        ic.setIcon(44, getBackGround(purple));
        items.add(0);
        items.add(1);
        items.add(7);
        items.add(8);
        items.add(9);
        items.add(17);
        items.add(27);
        items.add(35);
        items.add(36);
        items.add(44);

        Icon background = getBackGround(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem());
        for(int x = 0 ; x < 45; x++){
            if(!items.contains(x)) {
                ic.setIcon(x, background);
            }
        }
    }

    public void setupCloseAction(){
        ic.addCloseActions(new InventoryCloseAction() {
            @Override
            public void execute(Player player, Inventory inventory) {
                holographicPlaceholders.saveConfig();
                holographicPlaceholders.reload();
                Bukkit.dispatchCommand(player, "hd reload");
                handler.openMainGUI(player);
            }
        });
    }
}
