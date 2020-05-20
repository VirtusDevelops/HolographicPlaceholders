package com.neutralplasma.holographicPlaceholders.gui.guis.balTop;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.baltop.BalTopAddon;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import com.neutralplasma.holographicPlaceholders.gui.Icon;
import com.neutralplasma.holographicPlaceholders.gui.InventoryCreator;
import com.neutralplasma.holographicPlaceholders.gui.actions.ClickAction;
import com.neutralplasma.holographicPlaceholders.gui.actions.InventoryCloseAction;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import com.neutralplasma.holographicPlaceholders.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class RegisteredUsers {
    private HolographicPlaceholders holographicPlaceholders;
    private Handler handler;

    private InventoryCreator ic = new InventoryCreator(45, "BaltopGui.");
    private List<Integer> items = new ArrayList<>();
    private List<ItemStack> users = new ArrayList<>();
    private HashMap<ItemStack, Integer> usersMap = new HashMap<>();
    private int currentpage = 1;

    public RegisteredUsers(HolographicPlaceholders holographicPlaceholders, Handler handler){
        this.holographicPlaceholders = holographicPlaceholders;
        this.handler = handler;
    }

    public void openGUI(Player player, int page){
        ic.clean();
        items.clear();
        users.clear();
        usersMap.clear();
        currentpage = page;
        registeredUsers();
        createUserIcons(page);
        prevPage();
        nextPage();
        setupBackground();
        ic.addCloseActions(new InventoryCloseAction() {
            @Override
            public void execute(Player player, Inventory inventory) {
                handler.openBalTopEdit(player);
            }
        });
        player.openInventory(ic.getInventory());
    }

    public void registeredUsers(){
        for(UUID uuid : BalTopAddon.getPlayers().keySet()){
            OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(uuid);
            if(oPlayer != null){
                ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwningPlayer(oPlayer);
                item.setItemMeta(meta);
                ItemMeta itemMeta = item.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(TextFormater.sFormatText("&7"));
                lore.add(TextFormater.sFormatText("&7Balance: &e" + holographicPlaceholders.getEconomy().getBalance(oPlayer)));
                itemMeta.setLore(lore);
                itemMeta.setDisplayName(TextFormater.sFormatText("&e" + oPlayer.getName()));
                item.setItemMeta(itemMeta);
                users.add(item);
            }
        }

        // Intializes pages for seeds
        int page = 1;

        for(int x = 0; x < users.size(); x++){
            if(x >= page*21){
                page++;
            }
            usersMap.put(users.get(x), page);
        }
    }

    public void createUserIcons(int page){
        int itempos = 10;
        for(ItemStack item : usersMap.keySet()){
            if(usersMap.get(item) == page){
                ItemStack seedItem = item;
                Icon seed = new Icon(seedItem);
                seed.addClickAction(new ClickAction() {
                    @Override
                    public void execute(Player player) {

                    }
                });
                items.add(itempos);
                ic.setIcon(itempos, seed);

                if(itempos == 16){
                    itempos = 19;
                }else if(itempos == 25){
                    itempos = 28;
                }else {
                    itempos++;
                }
            }
        }
    }

    public void nextPage(){
        ItemStack nextPageItem = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = nextPageItem.getItemMeta();
        itemMeta.setDisplayName(TextFormater.sFormatText("&eNext page."));
        nextPageItem.setItemMeta(itemMeta);
        Icon nextpage = new Icon(nextPageItem);
        nextpage.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                currentpage++;
                openGUI(player, currentpage);
            }
        });
        items.add(43);
        ic.setIcon(43, nextpage);
    }

    public void prevPage() {
        if (currentpage > 1 ) {
            ItemStack prevPage = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = prevPage.getItemMeta();
            itemMeta.setDisplayName(TextFormater.sFormatText("&ePrevious page."));
            prevPage.setItemMeta(itemMeta);
            Icon nextpage = new Icon(prevPage);
            nextpage.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    currentpage--;
                    openGUI(player, currentpage);
                }
            });
            items.add(37);
            ic.setIcon(37, nextpage);
        }
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
}
