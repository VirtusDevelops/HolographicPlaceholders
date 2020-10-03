package com.neutralplasma.holographicPlaceholders.command;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends AbstractCommand {

    private HolographicPlaceholders plugin;

    public ReloadCommand(HolographicPlaceholders plugin){
        super(CommandType.BOTH, false, "reload");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender commandSender, String... strings) {
        long time = plugin.reload();
        commandSender.sendMessage(TextUtils.colorFormat("&7Successfully reloaded! Took: &e" + time + "ms&7."));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        return null;
    }


    @Override
    public String getPermissionNode() {
        return "hpe.command.reload";
    }

    @Override
    public String getSyntax() {
        return "/hpe reload";
    }

    @Override
    public String getDescription() {
        return "Reloads configuration";
    }
}
