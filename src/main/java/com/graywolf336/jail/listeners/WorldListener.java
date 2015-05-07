package com.graywolf336.jail.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.beans.Jail;

public class WorldListener implements Listener {
    private JailMain pl;

    public WorldListener(JailMain plugin) {
        this.pl = plugin;
    }

    @EventHandler(ignoreCancelled=true, priority = EventPriority.LOW)
    public void worldLoaded(WorldLoadEvent event) {
        for(Jail j : pl.getJailManager().getJails())
            if(j.getWorldName().equalsIgnoreCase(event.getWorld().getName())) j.setEnabled(true);
    }

    @EventHandler(ignoreCancelled=true, priority = EventPriority.LOW)
    public void worldUnload(WorldUnloadEvent event) {
        for(Jail j : pl.getJailManager().getJails())
            if(j.getWorldName().equalsIgnoreCase(event.getWorld().getName())) j.setEnabled(false);
    }
}
