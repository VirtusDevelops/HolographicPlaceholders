package com.neutralplasma.holographicPlaceholders.command;

import com.neutralplasma.holographicPlaceholders.HolographicPlaceholders;
import com.neutralplasma.holographicPlaceholders.gui.Handler;
import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MenuCommand extends AbstractCommand {

    private HolographicPlaceholders plugin;
    private Handler handler;

    public MenuCommand(HolographicPlaceholders plugin, Handler handler){
        super(CommandType.PLAYER_ONLY, false, "menu");
        this.plugin = plugin;
        this.handler = handler;
    }

    @Override
    protected ReturnType runCommand(CommandSender commandSender, String... strings) {
        if(commandSender instanceof Player) {
            handler.openMainGUI((Player) commandSender);
            return ReturnType.SUCCESS;
        }
        return ReturnType.FAILURE;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        return null;
    }


    @Override
    public String getPermissionNode() {
        return "hpe.command.menu";
    }

    @Override
    public String getSyntax() {
        return "/hpe menu";
    }

    @Override
    public String getDescription() {
        return "Opens main menu.";
    }
}
