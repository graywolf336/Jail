package com.graywolf336.jail.command.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 1,
        minimumArgs = 1,
        needsPlayer = false,
        pattern = "unhandcuff|uhc",
        permission = "jail.command.handcuff",
        usage = "/unhandcuff [player]"
        )
public class UnHandCuffCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        Player player = jm.getPlugin().getServer().getPlayerExact(args[0]);

        if(player == null) {
            sender.sendMessage(Lang.PLAYERNOTONLINE.get());
        }else if(jm.getPlugin().getHandCuffManager().isHandCuffed(player.getUniqueId())) {
            sender.sendMessage(Lang.HANDCUFFSRELEASED.get(player.getName()));
            jm.getPlugin().getHandCuffManager().removeHandCuffs(player.getUniqueId());
            player.sendMessage(Lang.UNHANDCUFFED.get());
        }else {
            sender.sendMessage(Lang.NOTHANDCUFFED.get(player.getName()));
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        //TODO implement
        return Collections.emptyList();
    }
}
