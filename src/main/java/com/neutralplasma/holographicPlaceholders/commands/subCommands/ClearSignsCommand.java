package com.neutralplasma.holographicPlaceholders.commands.subCommands;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.statistics.Modulator;
import com.neutralplasma.holographicPlaceholders.commands.CommandInterface;
import com.neutralplasma.holographicPlaceholders.storage.SignLocation;
import eu.virtusdevelops.virtuscore.utils.TextUtil;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearSignsCommand implements CommandInterface {
    private HolographicPlaceholders holographicPlaceholders;
    private Modulator modulator;

    public ClearSignsCommand(HolographicPlaceholders holographicPlaceholders, Modulator modulator){
        this.holographicPlaceholders = holographicPlaceholders;
        this.modulator = modulator;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(sender instanceof Player){
            if(sender.hasPermission("holographicplaceholders.command.setsign")){
                if(args.length > 2) {
                    Player player = (Player) sender;


                }
            }
        }else{
            sender.sendMessage(TextUtil.colorFormat("&cERROR: This is player only command!"));
        }
        return false;
    }
}
