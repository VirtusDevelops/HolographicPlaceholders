package com.neutralplasma.holographicPlaceholders.gui.guis;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.Addon;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import com.neutralplasma.holographicPlaceholders.gui.Icon;
import com.neutralplasma.holographicPlaceholders.gui.InventoryCreator;
import com.neutralplasma.holographicPlaceholders.gui.actions.ClickAction;
import com.neutralplasma.holographicPlaceholders.gui.actions.LeftClickAction;
import com.neutralplasma.holographicPlaceholders.gui.actions.RightClickAction;
import com.neutralplasma.holographicPlaceholders.utils.XMaterial;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainGUI {
    private HolographicPlaceholders holographicPlaceholders;
    private Handler handler;
    List<Integer> items = new ArrayList<>();

    public MainGUI(Handler handler, HolographicPlaceholders holographicPlaceholders){
        this.holographicPlaceholders = holographicPlaceholders;
        this.handler = handler;
    }

    InventoryCreator ic = new InventoryCreator(45, "Main GUI");

    public void openGui(Player player){
        items.clear();
        ic.clean();
        setupAddons();
        setupBackground();
        player.openInventory(ic.getInventory());
    }

    private void setupAddons(){
        int itempos = 10;
        for(Addon addon : holographicPlaceholders.getAddons().keySet()){
            ItemStack item = XMaterial.BOOK.parseItem();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(TextUtils.colorFormat("&e" + addon.getName()));
            List<String> lore = new ArrayList<>();
            lore.add(TextUtils.colorFormat("&7"));
            lore.add(TextUtils.colorFormat("&7R-Click to edit."));
            lore.add(TextUtils.colorFormat("&7L-Click to &aEnable&7/&cDisable."));
            lore.add(TextUtils.colorFormat("&7"));
            if(addon.isEnabled()) {
                lore.add(TextUtils.colorFormat("&7Currently: &aEnabled"));
            }else{
                lore.add(TextUtils.colorFormat("&7Currently: &cDisabled"));
            }
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            Icon icon = new Icon(item);
            icon.addLeftClickAction(new LeftClickAction() {
                @Override
                public void execute(Player player) {
                    holographicPlaceholders.getConfig().set("addons." + addon.getName(), !addon.isEnabled());
                    holographicPlaceholders.saveConfig();
                    if(addon.isEnabled()){
                        holographicPlaceholders.disableAddon(addon);
                    }else{
                        holographicPlaceholders.enableAddon(addon);
                    }
                    handler.openMainGUI(player);
                }
            });
            icon.addRightClickAction(new RightClickAction() {
                @Override
                public void execute(Player player) {
                    if(addon.isEnabled()){
                        handler.openAddonGUI(addon, player);
                    }
                }
            });
            items.add(itempos);
            ic.setIcon(itempos, icon);
            if(itempos == 16){
                itempos = 19;
            }else if(itempos == 25){
                itempos = 28;
            }else {
                itempos++;
            }

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
