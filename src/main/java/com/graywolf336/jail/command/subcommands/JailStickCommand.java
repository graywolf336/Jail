package com.graywolf336.jail.command.subcommands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.enums.Settings;

@CommandInfo(
        maxArgs = 0,
        minimumArgs = 0,
        needsPlayer = true,
        pattern = "stick",
        permission = "jail.usercmd.jailstick",
        usage = "/jail stick"
        )
public class JailStickCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
        if(jm.getPlugin().getConfig().getBoolean(Settings.JAILSTICKENABLED.getPath())) {
            boolean using = jm.getPlugin().getJailStickManager().toggleUsingStick(((Player) sender).getUniqueId());

            if(using) {
                sender.sendMessage(Lang.JAILSTICKENABLED.get());
            }else {
                sender.sendMessage(Lang.JAILSTICKDISABLED.get());
            }
        }else {
            sender.sendMessage(Lang.JAILSTICKUSAGEDISABLED.get());
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        //Nothing to tab complete on jail stick
        return Collections.emptyList();
    }
}
