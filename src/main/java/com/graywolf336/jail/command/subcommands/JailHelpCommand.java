package com.graywolf336.jail.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
        maxArgs = 1,
        minimumArgs = 0,
        needsPlayer = false,
        pattern = "help|h",
        permission = "jail.command.jailhelp",
        usage = "/jail help [page]"
        )
public class JailHelpCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
        sender.sendMessage(ChatColor.GREEN + "This command will be filled out shortly, use this link for now:");
        sender.sendMessage(ChatColor.GREEN + "https://github.com/graywolf336/Jail/wiki/Commands");
        return true;
    }
}
