package com.graywolf336.jail.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.enums.Settings;

@CommandInfo(
        maxArgs = 1,
        minimumArgs = 1,
        needsPlayer = false,
        pattern = "unjailforce|ujf",
        permission = "jail.command.unjailforce",
        usage = "/unjailforce [player]"
        )
public class UnJailForceCommand implements Command {

    public boolean execute(JailManager jm, CommandSender sender, String... args) {
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
}
