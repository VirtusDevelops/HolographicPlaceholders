package com.neutralplasma.holographicPlaceholders.addons;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.baltop.BalTopAddon;
import com.neutralplasma.holographicPlaceholders.addons.baltop.BalTopAddonV2;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PapiAddon extends PlaceholderExpansion {

    private HolographicPlaceholders holographicPlaceholders;
    private BalTopAddon balTopAddon;
    private BalTopAddonV2 balTopAddonV2;

    public PapiAddon(HolographicPlaceholders holographicPlaceholders, BalTopAddon balTopAddon,
                     BalTopAddonV2 balTopAddonV2){
        this.holographicPlaceholders = holographicPlaceholders;
        this.balTopAddon = balTopAddon;
        this.balTopAddonV2 = balTopAddonV2;
    }

    @Override
    public boolean persist(){
        return true;
    }
    @Override
    public boolean canRegister(){
        return true;
    }
    @Override
    public String getAuthor(){
        return holographicPlaceholders.getDescription().getAuthors().toString();
    }
    @Override
    public String getIdentifier(){
        return "holographicplaceholders";
    }
    @Override
    public String getVersion(){
        return holographicPlaceholders.getDescription().getVersion();
    }
    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        // %holographicplaceholders_baltopposition%
        if(identifier.equals("baltopposition")){
            return String.valueOf(balTopAddon.getPlayerPosition(player));
        }
        if(identifier.equals("baltopV2position")){
            return String.valueOf(balTopAddonV2.getPlayerPosition(player));
        }




        // %someplugin_money%
        /*if(identifier.equals("placeholder2")){
            return holographicPlaceholders.getConfig().getString("placeholder2", "value doesnt exist");
        }*/
        return null;
    }
}
