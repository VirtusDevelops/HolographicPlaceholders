package com.neutralplasma.holographicPlaceholders.command;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.statistics.Modulator;
import com.neutralplasma.holographicPlaceholders.storage.SignLocation;
import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import eu.virtusdevelops.virtuscore.utils.TextUtil;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetSignCommand extends AbstractCommand {

    private HolographicPlaceholders plugin;
    private Modulator modulator;

    public SetSignCommand(HolographicPlaceholders plugin, Modulator modulator){
        super(CommandType.PLAYER_ONLY, true, "setsign");
        this.plugin = plugin;
        this.modulator = modulator;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if(args.length > 1) {
            Player player = (Player) sender;
            Block block = player.getTargetBlock(null, 200);
            if (block.getState() instanceof Sign) {
                if(modulator.addSign(args[0], new SignLocation(block.getLocation()), Integer.parseInt(args[1]))){
                    sender.sendMessage(TextUtil.colorFormat("&8[&6HPE&8] &aSuccessfully set sign."));
                }else{
                    sender.sendMessage(TextUtil.colorFormat("&8[&6HPE&8] &cThis sign is already set as leaderboard sign."));
                }
                return ReturnType.SUCCESS;
            }else{
                sender.sendMessage(TextUtil.colorFormat("&8[&6HPE&8] &cThis is not a sign."));
                return ReturnType.SUCCESS;
            }
        }
        return ReturnType.SYNTAX_ERROR;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if(args.length == 1){
            List<String> placeholders = new ArrayList<>();
            ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("placeholder-addons");
            for (String entered : configurationSection.getKeys(false)) {
                String lEntered;
                lEntered = entered.toLowerCase();
                if (entered.contains(args[0]) || lEntered.contains(args[0])) {
                    placeholders.add(entered);
                }
            }
            return placeholders;
        }
        else if(args.length == 2){
            return Arrays.asList("1","2","3","4","5","6","7","8","9","10");
        }
        return null;
    }


    @Override
    public String getPermissionNode() {
        return "hpe.command.setsign";
    }

    @Override
    public String getSyntax() {
        return "/hpe setsign <placeholder> <position>";
    }

    @Override
    public String getDescription() {
        return "Adds leaderboard sign.";
    }
}
