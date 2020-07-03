package com.neutralplasma.holographicPlaceholders.commands.subCommands;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.statistics.Modulator;
import com.neutralplasma.holographicPlaceholders.commands.CommandInterface;
import com.neutralplasma.holographicPlaceholders.storage.SignLocation;
import com.neutralplasma.holographicPlaceholders.utils.TextFormater;
import eu.virtusdevelops.virtuscore.utils.TextUtil;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.plaf.TextUI;

public class SetSignCommand implements CommandInterface {
    private HolographicPlaceholders holographicPlaceholders;
    private Modulator modulator;

    public SetSignCommand(HolographicPlaceholders holographicPlaceholders, Modulator modulator){
        this.holographicPlaceholders = holographicPlaceholders;
        this.modulator = modulator;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(sender instanceof Player){
            if(sender.hasPermission("holographicplaceholders.command.setsign")){
                if(args.length > 2) {
                    Player player = (Player) sender;
                    Block block = player.getTargetBlock(null, 200);
                    if (block.getState() instanceof Sign) {
                        if(modulator.addSign(args[1], new SignLocation(block.getLocation()), Integer.parseInt(args[2]))){
                            sender.sendMessage(TextUtil.colorFormat("&8[&6HPE&8] &aSuccessfully set sign."));
                        }else{
                            sender.sendMessage(TextUtil.colorFormat("&8[&6HPE&8] &cThis sign is already set as leaderboard sign."));
                        }
                    }else{
                        sender.sendMessage(TextUtil.colorFormat("&8[&6HPE&8] &cThis is not a sign."));
                    }
                }
            }
        }else{
            sender.sendMessage(TextUtil.colorFormat("&cERROR: This is player only command!"));
        }
        return false;
    }
}
