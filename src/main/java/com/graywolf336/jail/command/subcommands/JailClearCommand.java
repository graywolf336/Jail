package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.ConfirmPlayer;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Confirmation;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 2,
        minimumArgs = 0,
        needsPlayer = false,
        pattern = "clear|clearforce",
        permission = "jail.command.jailclear",
        usage = "/jail clear (-f) (jail)"
        )
public class JailClearCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        boolean force = false;

        //Check if we need to forcefully clear something
        for(String s : args)
            if(s.equalsIgnoreCase("-f") || s.equalsIgnoreCase("-force"))
                force = true;

        if(jm.isConfirming(sender.getName())) {
            sender.sendMessage(Lang.ALREADY.get());
        }else if(force && sender.hasPermission("jail.command.jailclearforce")) {
            jm.addConfirming(sender.getName(), new ConfirmPlayer(sender.getName(), args, Confirmation.CLEARFORCE));
            sender.sendMessage(Lang.START.get());
        }else {
            jm.addConfirming(sender.getName(), new ConfirmPlayer(sender.getName(), args, Confirmation.CLEAR));
            sender.sendMessage(Lang.START.get());
        }

        return true;
    }
}
