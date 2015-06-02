package com.graywolf336.jail.command.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.graywolf336.jail.JailManager;
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
    @Override
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        sender.sendMessage(ChatColor.AQUA + "----------Cells----------");

        if(!jm.getJails().isEmpty()) {
            if(jm.getJail(args[1]) != null) {
                Jail j = jm.getJail(args[1]);

                String message = "";
                for(Cell c : j.getCells()) {
                    if(message.isEmpty()) {
                        message = c.getName() + (c.getPrisoner() == null ? "" : " (" + c.getPrisoner().getLastKnownName() + ")");
                    }else {
                        message += ", " + c.getName() + (c.getPrisoner() == null ? "" : " (" + c.getPrisoner().getLastKnownName() + ")");
                    }
                }

                if(message.isEmpty()) {
                    sender.sendMessage(Lang.NOCELLS.get(j.getName()));
                }else {
                    sender.sendMessage(ChatColor.GREEN + message);
                }
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
        List<String> results = new ArrayList<String>();
        
        for(Jail j : jm.getJails()) {
            if(args.length == 2 && StringUtil.startsWithIgnoreCase(j.getName(), args[1])) {
                results.add(j.getName());
            }else {
                results.add(j.getName());
            }
        }
        
        Collections.sort(results);
        
        return results;
    }
}
