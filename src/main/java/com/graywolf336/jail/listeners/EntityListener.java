package com.graywolf336.jail.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.enums.Settings;

public class EntityListener implements Listener {
    private JailMain pl;

    public EntityListener(JailMain plugin) {
        this.pl = plugin;
    }

    @EventHandler(ignoreCancelled=true)
    public void onEntityExplode(EntityExplodeEvent event) {
        //Only do the checking if plugin has it enabled
        //otherwise let's not go through all the blocks
        if(pl.getConfig().getBoolean(Settings.EXPLOSIONPROTECTION.getPath())) {
            //Loop through the blocklist and do stuff
            for(Block b : event.blockList()) {
                //Check the current block and if it is inside a jail,
                //then let's do something else
                if(pl.getJailManager().getJailFromLocation(b.getLocation()) != null) {
                    //Clear the blocklist, this way the explosion effect still happens
                    event.blockList().clear();
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void protectFromEndermen(EntityChangeBlockEvent event) {
        //If we are protecting the jails from endermen protection
        if(pl.getConfig().getBoolean(Settings.ENDERMENPROTECTION.getPath())) {
            //Check if there are any jails where the block's location is
            if(pl.getJailManager().getJailFromLocation(event.getBlock().getLocation()) != null) {
                //Let's cancel the event so it doesn't happen
                event.setCancelled(true);
            }
        }
    }
}
