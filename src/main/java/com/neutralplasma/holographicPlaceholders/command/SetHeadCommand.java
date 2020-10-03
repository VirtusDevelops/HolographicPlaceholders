package com.neutralplasma.holographicPlaceholders.command;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.addons.statistics.Modulator;
import com.neutralplasma.holographicPlaceholders.storage.SignLocation;
import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetHeadCommand extends AbstractCommand {

    private HolographicPlaceholders plugin;
    private Modulator modulator;

    public SetHeadCommand(HolographicPlaceholders plugin, Modulator modulator){
        super(CommandType.PLAYER_ONLY, true, "sethead");
        this.plugin = plugin;
        this.modulator = modulator;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if(args.length > 1) {
            Player player = (Player) sender;
            Block block = player.getTargetBlock(null, 200);
            if (block.getState() instanceof Skull) {
                if(modulator.addHead(args[0], new SignLocation(block.getLocation()), Integer.parseInt(args[1]))){
                    sender.sendMessage(TextUtils.colorFormat("&8[&6HPE&8] &aSuccessfully set skull/head."));
                }else{
                    sender.sendMessage(TextUtils.colorFormat("&8[&6HPE&8] &cThis skull/head is already set as leaderboard skull/head."));
                }
                return ReturnType.SUCCESS;
            }else{
                sender.sendMessage(TextUtils.colorFormat("&8[&6HPE&8] &cThis is not a skull/head."));
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
        return "hpe.command.sethead";
    }

    @Override
    public String getSyntax() {
        return "/hpe sethead <placeholder> <position>";
    }

    @Override
    public String getDescription() {
        return "Adds leaderboard head.";
    }
}
