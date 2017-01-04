package com.graywolf336.jail.command.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 2,
        minimumArgs = 1,
        needsPlayer = false,
        pattern = "teleportout|teleout",
        permission = "jail.command.jailteleout",
        usage = "/jail teleout [jail] (name)"
        )
public class JailTeleOutCommand implements Command {
    @SuppressWarnings("deprecation")
    public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
        Jail j = jm.getJail(args[1]);

        //The jail doesn't exist
        if(j == null) {
            sender.sendMessage(Lang.NOJAIL.get(args[1]));
        }else {
            //The jail does exist
            //now let's check the size of the command
            //if it has two args then someone is sending someone else in
            //otherwise it is just the sender going in
            if(args.length == 3) {
                Player p = jm.getPlugin().getServer().getPlayer(args[2]);

                //If the player they're trying to send is offline, don't do anything
                if(p == null) {
                    sender.sendMessage(Lang.PLAYERNOTONLINE.get(args[2]));
                }else {
                    p.teleport(j.getTeleportFree());
                    sender.sendMessage(Lang.TELEOUT.get(new String[] { args[2], args[1] }));
                }
            }else {
                if(sender instanceof Player) {
                    ((Player) sender).teleport(j.getTeleportFree());
                    sender.sendMessage(Lang.TELEOUT.get(new String[] { sender.getName(), args[1] }));
                }else {
                    sender.sendMessage(Lang.PLAYERCONTEXTREQUIRED.get());
                }
            }
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        switch(args.length) {
            case 2:
                return jm.getJailsByPrefix(args[1]);
            case 3:
                List<String> results = new ArrayList<String>();
                
                for(Player p : jm.getPlugin().getServer().getOnlinePlayers())
                    if(args[2].isEmpty() || StringUtil.startsWithIgnoreCase(p.getName(), args[2]))
                        results.add(p.getName());
                
                Collections.sort(results);
                
                return results;
            default:
                return Collections.emptyList();
        }
    }
}
