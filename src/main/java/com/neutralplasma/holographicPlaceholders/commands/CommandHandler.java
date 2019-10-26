package com.neutralplasma.holographicPlaceholders.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor {

    public CommandHandler(){

    }

    //This is where we will store the commands
    private static HashMap<String, CommandInterface> commands = new HashMap<String, CommandInterface>();

    //Register method. When we register commands in our onEnable() we will use this.
    public void register(String name, CommandInterface cmd) {
        //When we register the command, this is what actually will put the command in the hashmap.
        commands.put(name, cmd);
    }

    //This will be used to check if a string exists or not.
    public boolean exists(String name) {
        //To actually check if the string exists, we will return the hashmap
        return commands.containsKey(name);
    }

    //Getter method for the Executor.
    public CommandInterface getExecutor(String name) {
        //Returns a command in the hashmap of the same name.
        return commands.get(name);
    }

    //This will be a template. All commands will have this in common.
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if(args.length == 0) {
            getExecutor("hpe").onCommand(sender, cmd, commandLabel, args);
            return true;
        }

        if(exists(args[0])){

            //the command /example args, args is our args[0].
            getExecutor(args[0]).onCommand(sender, cmd, commandLabel, args);
            return true;
        } else {

            return true;
        }
    }
}
