package com.graywolf336.jail.command.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 1,
        minimumArgs = 1,
        needsPlayer = false,
        pattern = "mute|m",
        permission = "jail.command.jailmute",
        usage = "/jail mute [name]"
        )
public class JailMuteCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
        //Let's check if the player they're sending us is jailed
        if(jm.isPlayerJailedByLastKnownUsername(args[1])) {
            //They are, so let's toggle whether they are muted or not
            boolean muted = !jm.getPrisonerByLastKnownName(args[1]).isMuted();
            jm.getPrisonerByLastKnownName(args[1]).setMuted(muted);

            //Send the message to the sender based upon whether they are muted or unmuted
            if(muted)
                sender.sendMessage(Lang.NOWMUTED.get(args[1]));
            else
                sender.sendMessage(Lang.NOWUNMUTED.get(args[1]));
        }else {
            //The player provided is not jailed, so let's tell the sender that
            sender.sendMessage(Lang.NOTJAILED.get(args[1]));
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        switch(args.length) {
            case 2:
                List<String> results = new ArrayList<String>();
                for(Player p : jm.getPlugin().getServer().getOnlinePlayers())
                    if(StringUtil.startsWithIgnoreCase(p.getName(), args[1].toLowerCase()))
                        results.add(p.getName());
                
                Collections.sort(results);
                return results;
            default:
                return Collections.emptyList();
        }
    }
}
