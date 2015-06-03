package com.graywolf336.jail.command.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 1,
        minimumArgs = 1,
        needsPlayer = false,
        pattern = "check",
        permission = "jail.command.jailcheck",
        usage = "/jail check [name]"
        )
public class JailCheckCommand implements Command {

    // Checks the status of the specified prisoner
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        //Otherwise let's check the first argument
        if(jm.isPlayerJailedByLastKnownUsername(args[1])) {
            Prisoner p = jm.getPrisonerByLastKnownName(args[1]);

            //graywolf663: Being gray's evil twin; CONSOLE (10)
            //prisoner: reason; jailer (time in minutes)
            sender.sendMessage(ChatColor.BLUE + " " + p.getLastKnownName() + ": " + p.getReason() + "; " + p.getJailer() + " (" + Util.getDurationBreakdown(p.getRemainingTime()) + " )");
        }else {
            sender.sendMessage(Lang.NOTJAILED.get(args[1]));
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        List<String> results = new ArrayList<String>();
        
        for(Prisoner p : jm.getAllPrisoners().values()) {
            if(args.length == 2 && StringUtil.startsWithIgnoreCase(p.getLastKnownName(), args[1].toLowerCase())) {
                results.add(p.getLastKnownName());
            }else {
                results.add(p.getLastKnownName());
            }
        }
        
        Collections.sort(results);
        
        if(args.length == 1)
            for(Player p : jm.getPlugin().getServer().getOnlinePlayers())
                if(!results.contains(p.getName()))
                    results.add(p.getName());
        
        return results;
    }
}
