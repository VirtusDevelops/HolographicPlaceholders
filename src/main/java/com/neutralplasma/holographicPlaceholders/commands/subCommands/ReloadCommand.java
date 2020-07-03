package com.neutralplasma.holographicPlaceholders.commands.subCommands;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.commands.CommandInterface;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import eu.virtusdevelops.virtuscore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandInterface {

    private HolographicPlaceholders holographicPlaceholders;

    public ReloadCommand(HolographicPlaceholders holographicPlaceholders){
        this.holographicPlaceholders = holographicPlaceholders;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(sender.hasPermission("holographicplaceholders.command.reload")){
            long time = holographicPlaceholders.reload();
            sender.sendMessage(TextUtil.colorFormat("&7Successfully reloaded! Took: &e" + time + "ms&7."));
            return true;
        }else{
            sender.sendMessage(TextUtil.colorFormat("&cSorry but you do not have permission to execute this command."));
            return true;
        }
    }
}
