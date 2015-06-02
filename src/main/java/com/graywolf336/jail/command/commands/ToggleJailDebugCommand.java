package com.graywolf336.jail.command.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
        maxArgs = 0,
        minimumArgs = 0,
        needsPlayer = false,
        pattern = "togglejaildebug|tjd",
        permission = "jail.command.toggledebug",
        usage = "/togglejaildebug"
        )
public class ToggleJailDebugCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        boolean debug = jm.getPlugin().setDebugging(!jm.getPlugin().inDebug());
        sender.sendMessage("Jail debugging is now: " + (debug ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        //No tab completion required for toggling debug command
        return Collections.emptyList();
    }
}
