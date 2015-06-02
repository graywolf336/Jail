package com.graywolf336.jail.command.subcommands;

import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 1,
        minimumArgs = 0,
        needsPlayer = false,
        pattern = "list|l",
        permission = "jail.command.jaillist",
        usage = "/jail list (jail)"
        )
public class JailListCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        sender.sendMessage(ChatColor.AQUA + "----------" + (args.length == 1 ? "Jails" : "Prisoners") + "----------");

        //Check if there are any jails
        if(jm.getJails().isEmpty()) {
            sender.sendMessage(" " + Lang.NOJAILS.get());
        }else {
            //Check if they have provided a jail to list or not
            if(args.length == 1) {
                //No jail provided, so give them a list of the jails
                for(Jail j : jm.getJails()) {
                    if(j.isEnabled()) sender.sendMessage(ChatColor.BLUE + "    " + j.getName() + " (" + j.getAllPrisoners().size() + ")");
                    else sender.sendMessage(ChatColor.RED + "    " + j.getName() + " (" + j.getAllPrisoners().size() + ") - WORLD UNLOADED");
                }
            }else {
                Jail j = jm.getJail(args[1]);

                if(j == null) {
                    //No jail was found
                    sender.sendMessage(" " + Lang.NOJAIL.get(args[1]));
                }else {
                    Collection<Prisoner> pris = j.getAllPrisoners().values();

                    if(pris.isEmpty()) {
                        //If there are no prisoners, then send that message
                        sender.sendMessage(" " + Lang.NOPRISONERS.get(j.getName()));
                    }else {
                        for(Prisoner p : pris) {
                            //graywolf663: Being gray's evil twin; CONSOLE (10)
                            //prisoner: reason; jailer (time in minutes)
                            sender.sendMessage(ChatColor.BLUE + " " + p.getLastKnownName() + ": " + p.getReason() + "; " + p.getJailer() + " (" + p.getRemainingTimeInMinutes() + " mins)");
                        }
                    }
                }
            }
        }

        sender.sendMessage(ChatColor.AQUA + "-------------------------");
        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        return jm.getJailsByPrefix(args.length == 2 ? args[1] : "");
    }
}
