package com.graywolf336.jail.command.subcommands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 2,
        minimumArgs = 1,
        needsPlayer = false,
        pattern = "record|r",
        permission = "jail.command.jailrecord",
        usage = "/jail record [name] (display)"
        )
public class JailRecordCommand implements Command {
    @SuppressWarnings("deprecation")
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        if(args.length == 2) {
            // /jail record <username>
            List<String> entries = jm.getPlugin().getJailIO().getRecordEntries(args[1]);

            sender.sendMessage(Lang.RECORDTIMESJAILED.get(new String[] { args[1], String.valueOf(entries.size()) }));
        }else if(args.length == 3) {
            // /jail record <username> something
            List<String> entries = jm.getPlugin().getJailIO().getRecordEntries(args[1]);

            //Send all the record entries
            for(String s : entries) {
                sender.sendMessage(s);
            }

            sender.sendMessage(Lang.RECORDTIMESJAILED.get(new String[] { args[1], String.valueOf(entries.size()) }));
        }else {
            //They didn't do the command right
            //send them back to get the usage
            return false;
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        //We won't be implementing tab complete on this for now (unless requested)
        return Collections.emptyList();
    }
}
