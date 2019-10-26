package com.neutralplasma.holographicPlaceholders.commands;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
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

        if (sender.hasPermission("")) {
            if(sender instanceof Player) {
                handler.openMainGUI((Player) sender);
            }
        }else {
            sender.sendMessage(TextFormater.sFormatText("&cSorry but you do not have permission to execute this. &7(&e{0}&7).".replace("{0}", "simplecrops.command.editor")));
            return true;
        }

        return true;
    }


}
