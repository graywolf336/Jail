package com.graywolf336.jail.command.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 1,
        minimumArgs = 1,
        needsPlayer = false,
        pattern = "listcells|lc",
        permission = "jail.command.jaillistcells",
        usage = "/jail listcells [jail]"
        )
public class JailListCellsCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        sender.sendMessage(ChatColor.AQUA + "----------Cells----------");

        if(!jm.getJails().isEmpty()) {
            if(jm.getJail(args[1]) != null) {
                Jail j = jm.getJail(args[1]);

                List<String> cells = new ArrayList<String>();
                
                for(Cell c : j.getCells()) {
                    cells.add(c.getName() + (c.getPrisoner() == null ? "" : " (" + c.getPrisoner().getLastKnownName() + ")"));
                }
                
                Collections.sort(cells);
                
                sender.sendMessage(cells.size() == 0 ? Lang.NOCELLS.get(j.getName()) : ChatColor.GREEN + Util.getStringFromList(", ", cells));
            }else {
                sender.sendMessage(Lang.NOJAIL.get(args[1]));
            }
        }else {
            sender.sendMessage(Lang.NOJAILS.get());
        }

        sender.sendMessage(ChatColor.AQUA + "-------------------------");
        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        return jm.getJailsByPrefix(args.length == 2 ? args[1] : "");
    }
}
