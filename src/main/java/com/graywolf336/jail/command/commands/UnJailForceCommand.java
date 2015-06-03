package com.graywolf336.jail.command.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.exceptions.JailRequiredException;
import com.graywolf336.jail.exceptions.PrisonerRequiredException;

@CommandInfo(
        maxArgs = 1,
        minimumArgs = 1,
        needsPlayer = false,
        pattern = "unjailforce|ujf",
        permission = "jail.command.unjailforce",
        usage = "/unjailforce [player]"
        )
public class UnJailForceCommand implements Command {

    public boolean execute(JailManager jm, CommandSender sender, String... args) throws JailRequiredException, PrisonerRequiredException {
        //Check if the player is jailed
        if(jm.isPlayerJailedByLastKnownUsername(args[0])) {
            jm.getPlugin().getPrisonerManager().forceRelease(jm.getPrisonerByLastKnownName(args[0]), sender);

            if(jm.getPlugin().getConfig().getBoolean(Settings.LOGJAILINGTOCONSOLE.getPath())) {
                jm.getPlugin().getLogger().info(ChatColor.stripColor(Lang.BROADCASTUNJAILING.get(new String[] { args[0], sender.getName() })));
            }
        }else {
            //The player is not currently jailed
            sender.sendMessage(Lang.NOTJAILED.get(args[0]));
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        List<String> results = new ArrayList<String>();
        
        for(Prisoner p : jm.getAllPrisoners().values())
            if(args[0].isEmpty() || StringUtil.startsWithIgnoreCase(p.getLastKnownName(), args[0]))
                results.add(p.getLastKnownName());
        
        Collections.sort(results);
        
        return results;
    }
}
