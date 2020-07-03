package com.neutralplasma.holographicPlaceholders.commands;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import eu.virtusdevelops.virtuscore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandInterface{

    private HolographicPlaceholders HolographicPlaceholders;
    private Handler handler;

    public MainCommand(HolographicPlaceholders HolographicPlaceholders, Handler handler){
        this.HolographicPlaceholders = HolographicPlaceholders;
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {


        if(sender instanceof Player) {
            handler.openMainGUI((Player) sender);
        }


        return true;
    }


}
