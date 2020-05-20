package com.neutralplasma.holographicPlaceholders.addons;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.baltop.BalTopAddon;
import com.neutralplasma.holographicPlaceholders.addons.playTime.PlayTimeAddon;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PapiAddon extends PlaceholderExpansion {

    private HolographicPlaceholders holographicPlaceholders;
    private BalTopAddon balTopAddon;
    private PlayTimeAddon playTimeAddon;

    public PapiAddon(HolographicPlaceholders holographicPlaceholders, BalTopAddon balTopAddon,
                     PlayTimeAddon playTimeAddon){
        this.holographicPlaceholders = holographicPlaceholders;
        this.balTopAddon = balTopAddon;
        this.playTimeAddon = playTimeAddon;
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

        if(identifier.equals("playtime")){
            return TextFormater.formatTime(playTimeAddon.getPlayTime(player));
        }

        if(identifier.equals("playtime_raw")){
            return String.valueOf(playTimeAddon.getPlayTime(player));
        }

        if(identifier.equals("playtime_position")){
            return String.valueOf(playTimeAddon.getPosition(player));
        }


        // %someplugin_money%
        /*if(identifier.equals("placeholder2")){
            return holographicPlaceholders.getConfig().getString("placeholder2", "value doesnt exist");
        }*/
        return null;
    }
}
