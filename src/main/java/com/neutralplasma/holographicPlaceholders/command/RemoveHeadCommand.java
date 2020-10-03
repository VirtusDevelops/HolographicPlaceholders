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
import java.util.List;

public class RemoveHeadCommand extends AbstractCommand {

    private HolographicPlaceholders plugin;
    private Modulator modulator;

    public RemoveHeadCommand(HolographicPlaceholders plugin, Modulator modulator){
        super(CommandType.PLAYER_ONLY, true, "removehead");
        this.plugin = plugin;
        this.modulator = modulator;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if(args.length > 0) {
            Player player = (Player) sender;
            Block block = player.getTargetBlock(null, 200);
            if (block.getState() instanceof Skull) {
                if(modulator.removeHead(args[0], new SignLocation(block.getLocation()))){
                    sender.sendMessage(TextUtils.colorFormat("&8[&6HPE&8] &aSuccessfully removed skull/head."));
                    return ReturnType.SUCCESS;
                }else{
                    sender.sendMessage(TextUtils.colorFormat("&8[&6HPE&8] &cCouldn't find any head that is set as leaderboard here."));
                    return ReturnType.SUCCESS;
                }
            }else{
                sender.sendMessage(TextUtils.colorFormat("&8[&6HPE&8] &cThis is not a skull/head."));
                return ReturnType.SUCCESS;
            }
        }else{
            return ReturnType.SYNTAX_ERROR;
        }
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
        return null;
    }


    @Override
    public String getPermissionNode() {
        return "hpe.command.removehead";
    }

    @Override
    public String getSyntax() {
        return "/hpe removehead <placeholder>";
    }

    @Override
    public String getDescription() {
        return "Removes leaderboard head.";
    }
}
