package com.neutralplasma.holographicPlaceholders.commands;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {
    private HolographicPlaceholders holographicPlaceholders;

    public TabComplete(HolographicPlaceholders holographicPlaceholders){
        this.holographicPlaceholders = holographicPlaceholders;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("hpe")){
            List<String> players = new ArrayList<>();
            //get online players
            for(Player player : Bukkit.getOnlinePlayers()){
                players.add(player.getName());
            }
            List<String> listedPlayers = new ArrayList<>();
            // player stuff.
            if(args.length > 1){

            }



            else{ // subcommand stuff.
                List<String> possible = new ArrayList<>();
                List<String> has = new ArrayList<>();
                if(commandSender.hasPermission("holographicplaceholders.command.reload")){
                    possible.add("reload");
                }

                for(String entered : possible){
                    String lentered = entered.toLowerCase();
                    if(entered.contains(args[0]) || lentered.contains(args[0])){
                        has.add(entered);
                    }
                }
                return has;
            }
        }
        return null;
    }
}
