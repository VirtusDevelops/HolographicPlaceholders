package com.neutralplasma.holographicPlaceholders.commands.subCommands;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.statistics.Modulator;
import com.neutralplasma.holographicPlaceholders.commands.CommandInterface;
import com.neutralplasma.holographicPlaceholders.storage.SignLocation;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import eu.virtusdevelops.virtuscore.utils.TextUtil;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHeadCommand implements CommandInterface {
    private HolographicPlaceholders holographicPlaceholders;
    private Modulator modulator;

    public SetHeadCommand(HolographicPlaceholders holographicPlaceholders, Modulator modulator){
        this.holographicPlaceholders = holographicPlaceholders;
        this.modulator = modulator;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(sender instanceof Player){
            if(sender.hasPermission("holographicplaceholders.command.sethead")){
                if(args.length > 2) {
                    Player player = (Player) sender;
                    Block block = player.getTargetBlock(null, 200);
                    if (block.getState() instanceof Skull) {
                        if(modulator.addHead(args[1], new SignLocation(block.getLocation()), Integer.parseInt(args[2]))){
                            sender.sendMessage(TextUtil.colorFormat("&8[&6HPE&8] &aSuccessfully set skull/head."));
                        }else{
                            sender.sendMessage(TextUtil.colorFormat("&8[&6HPE&8] &cThis skull/head is already set as leaderboard skull/head."));
                        }
                    }else{
                        sender.sendMessage(TextUtil.colorFormat("&8[&6HPE&8] &cThis is not a skull/head."));
                    }
                }
            }
        }else{
            sender.sendMessage(TextUtil.colorFormat("&cERROR: This is player only command!"));
        }
        return false;
    }
}
