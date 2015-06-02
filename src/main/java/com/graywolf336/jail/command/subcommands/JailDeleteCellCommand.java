package com.graywolf336.jail.command.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.ConfirmPlayer;
import com.graywolf336.jail.beans.Jail;
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

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        List<String> results = new ArrayList<String>();
        
        switch(args.length) {
            case 1:
                results.addAll(jm.getJailNamesAsList());
                break;
            case 2:
                for(Jail j : jm.getJails())
                    if(StringUtil.startsWithIgnoreCase(j.getName(), args[1]))
                        results.add(j.getName());
                break;
            case 3:
                if(jm.isValidJail(args[1])) {
                    Jail j = jm.getJail(args[1]);
                    
                    for(Cell c : j.getCells())
                        if(StringUtil.startsWithIgnoreCase(c.getName(), args[2]))
                            results.add(c.getName());
                }
                break;
        }
        
        Collections.sort(results);
        
        return results;
    }
}
