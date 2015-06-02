package com.graywolf336.jail.command.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 2,
        minimumArgs = 2,
        needsPlayer = false,
        pattern = "transferall|transall|ta",
        permission = "jail.command.jailtransferall",
        usage = "/jail transferall [current] [target]"
        )
public class JailTransferAllCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
        if(jm.getJails().isEmpty()) {
            sender.sendMessage(Lang.NOJAILS.get());
            return true;
        }

        jm.getPlugin().debug("Starting to transfer everyone from '" + args[1] + "' into '" + args[2] + "'.");

        //Check if the oldjail is not a valid jail
        if(!jm.isValidJail(args[1])) {
            sender.sendMessage(Lang.NOJAIL.get(args[1]));
            return true;
        }else if(!jm.isValidJail(args[2])) {
            //Check if the targetjail is a valid jail as well
            sender.sendMessage(Lang.NOJAIL.get(args[2]));
            return true;
        }

        jm.getPlugin().debug("Sending the transferring off, jail checks all came clean.");

        Jail old = jm.getJail(args[1]);
        HashSet<Prisoner> oldPrisoners = new HashSet<Prisoner>(old.getAllPrisoners().values());

        //Transfer all the prisoners
        for(Prisoner p : oldPrisoners) {
            jm.getPlugin().getPrisonerManager().transferPrisoner(old, old.getCellPrisonerIsIn(p.getUUID()), jm.getJail(args[2]), null, p);
        }

        //Send the messages to the sender when completed
        sender.sendMessage(Lang.TRANSFERALLCOMPLETE.get(new String[] { old.getName(), args[2] }));

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        switch(args.length) {
            case 2:
                return jm.getJailsByPrefix(args[1]);
            case 3:
                List<String> results = new ArrayList<String>();
                
                for(Jail j : jm.getJails())
                    if(!j.getName().equalsIgnoreCase(args[1]) && (args[2].isEmpty() || StringUtil.startsWithIgnoreCase(j.getName(), args[2])))
                        results.add(j.getName());
                
                Collections.sort(results);
                
                return results;
            default:
                return Collections.emptyList();
        }
    }
}
