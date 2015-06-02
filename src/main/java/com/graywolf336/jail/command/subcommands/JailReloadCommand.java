package com.graywolf336.jail.command.subcommands;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 0,
        minimumArgs = 0,
        needsPlayer = false,
        pattern = "reload",
        permission = "jail.command.jailreload",
        usage = "/jail reload"
        )
public class JailReloadCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        try {
            jm.getPlugin().reloadEverything();
            sender.sendMessage(Lang.PLUGINRELOADED.get());
        }catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Failed to reload due to (see the console): " + e.getMessage());
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        //Reloading the plugin doesn't require tab completions
        return Collections.emptyList();
    }
}
