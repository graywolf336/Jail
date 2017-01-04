package com.graywolf336.jail.command.commands;

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
        pattern = "handcuff|hc",
        permission = "jail.command.handcuff",
        usage = "/handcuff [player]"
        )
public class HandCuffCommand implements Command {
    @SuppressWarnings("deprecation")
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        Player player = jm.getPlugin().getServer().getPlayer(args[0]);

        if(player == null) {
            sender.sendMessage(Lang.PLAYERNOTONLINE.get());
        }else if(player.hasPermission("jail.cantbehandcuffed")) {
            sender.sendMessage(Lang.CANTBEHANDCUFFED.get(player.getName()));
        }else if(jm.isPlayerJailed(player.getUniqueId())) {
            sender.sendMessage(Lang.CURRENTLYJAILEDHANDCUFF.get(player.getName()));
        }else if(jm.getPlugin().getHandCuffManager().isHandCuffed(player.getUniqueId())) {
            sender.sendMessage(Lang.HANDCUFFSRELEASED.get(player.getName()));
            jm.getPlugin().getHandCuffManager().removeHandCuffs(player.getUniqueId());
            player.sendMessage(Lang.UNHANDCUFFED.get());
        }else {
            jm.getPlugin().getHandCuffManager().addHandCuffs(player.getUniqueId(), player.getLocation());
            sender.sendMessage(Lang.HANDCUFFSON.get(player.getName()));
            player.sendMessage(Lang.HANDCUFFED.get());
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        List<String> results = new ArrayList<String>();
        
        for(Player p : jm.getPlugin().getServer().getOnlinePlayers())
            if(!jm.getPlugin().getHandCuffManager().isHandCuffed(p.getUniqueId())) //don't send someone who is already handcuffed
                if(!p.hasPermission("jail.cantbehandcuffed")) //don't send someone who can't be handcuffed
                    if(args[0].isEmpty() || StringUtil.startsWithIgnoreCase(p.getName(), args[0]))
                        results.add(p.getName());
        
        Collections.sort(results);
        
        return results;
    }
}
