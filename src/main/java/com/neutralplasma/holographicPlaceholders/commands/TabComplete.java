package com.neutralplasma.holographicPlaceholders.commands;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
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
            // yes stuff
            if(args.length > 1){
                // ADD SETHEAD AND SETSIGN HANDLER TO SHOW POSSIBLE PLACEHOLDERS
                if(args[0].equalsIgnoreCase("sethead") || args[0].equalsIgnoreCase("setsign")){
                    if(args.length == 2) {
                        List<String> placeholders = new ArrayList<>();
                        ConfigurationSection configurationSection = holographicPlaceholders.getConfig().getConfigurationSection("placeholder-addons");
                        for (String entered : configurationSection.getKeys(false)) {
                            String lEntered;
                            lEntered = entered.toLowerCase();
                            if (entered.contains(args[1]) || lEntered.contains(args[1])) {
                                placeholders.add(entered);
                            }
                        }
                        return placeholders;
                    }else if(args.length == 3){
                        return Arrays.asList("1","2","3","4","5","6","7","8","9","10");
                    }
                }

                if(args[0].equalsIgnoreCase("removehead") || args[0].equalsIgnoreCase("removesign")){
                    if(args.length == 2){
                        List<String> placeholders = new ArrayList<>();
                        ConfigurationSection configurationSection = holographicPlaceholders.getConfig().getConfigurationSection("placeholder-addons");
                        for (String entered : configurationSection.getKeys(false)) {
                            String lEntered;
                            lEntered = entered.toLowerCase();
                            if (entered.contains(args[1]) || lEntered.contains(args[1])) {
                                placeholders.add(entered);
                            }
                        }
                        return placeholders;
                    }
                }

            }else{ // subcommand stuff.
                List<String> possible = new ArrayList<>();
                List<String> has = new ArrayList<>();
                if(commandSender.hasPermission("holographicplaceholders.command.reload")){
                    possible.add("reload");
                }
                if(commandSender.hasPermission("holographicplaceholders.command.setsign")){
                    possible.add("setsign");
                }
                if(commandSender.hasPermission("holographicplaceholders.command.sethead")){
                    possible.add("sethead");
                }
                if(commandSender.hasPermission("holographicplaceholders.command.removehead")){
                    possible.add("removehead");
                }
                if(commandSender.hasPermission("holographicplaceholders.command.removesign")){
                    possible.add("removesign");
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
