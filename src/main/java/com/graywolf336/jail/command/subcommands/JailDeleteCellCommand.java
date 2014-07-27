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
        minimumArgs = 2,
        needsPlayer = false,
        pattern = "deletecell|dc",
        permission = "jail.command.jaildeletecell",
        usage = "/jail deletecell [jail] [cell]"
        )
public class JailDeleteCellCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
        if(jm.isConfirming(sender.getName())) {
            sender.sendMessage(Lang.ALREADY.get());
        }else {
            jm.addConfirming(sender.getName(), new ConfirmPlayer(sender.getName(), args, Confirmation.DELETECELL));
            sender.sendMessage(Lang.START.get());
        }

        return true;
    }
}
