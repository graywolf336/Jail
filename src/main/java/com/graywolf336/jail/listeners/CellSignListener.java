package com.graywolf336.jail.listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.events.JailPluginReloadedEvent;
import com.graywolf336.jail.events.OfflinePrisonerJailedEvent;
import com.graywolf336.jail.events.PrisonerJailedEvent;
import com.graywolf336.jail.events.PrisonerReleasedEvent;
import com.graywolf336.jail.events.PrisonerTimeChangeEvent;
import com.graywolf336.jail.events.PrisonerTransferredEvent;

public class CellSignListener implements Listener {
    private String lineOne = "", lineTwo = "", lineThree = "", lineFour = "";
    private JailMain pl;

    public CellSignListener(JailMain plugin) {
        pl = plugin;
        List<String> lines = pl.getConfig().getStringList(Settings.CELLSIGNLINES.getPath());

        if(lines.size() >= 1) lineOne = lines.get(0);
        if(lines.size() >= 2) lineTwo = lines.get(1);
        if(lines.size() >= 3) lineThree = lines.get(2);
        if(lines.size() >= 4) lineFour = lines.get(3);

        try {
            Util.updateSignLinesCache(new String[] { lineOne, lineTwo, lineThree, lineFour });
        }catch(Exception e) {
            //this should never happen
            pl.debug("Error on Cell Sign Listener constructor:" + e.getMessage());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void changeTheCellSigns(final PrisonerTimeChangeEvent event) {
        pl.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
            public void run() {
                if (event.hasCell() && event.getCell().hasSigns()) {
                    event.getCell().updateSigns();
                }
            }
        });
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void changeSignsOnOfflineJailing(OfflinePrisonerJailedEvent event) {
        if (event.hasCell() && event.getCell().hasSigns()) {
            event.getCell().updateSigns();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void changeCellSignsOnJail(PrisonerJailedEvent event) {
        if (event.hasCell() && event.getCell().hasSigns()) {
            event.getCell().updateSigns();
        }
    }

    @EventHandler
    public void clearTheCellSigns(PrisonerReleasedEvent event) {
        if (event.hasCell() && event.getCell().hasSigns()) {
            event.getCell().updateSigns();
        }
    }

    @EventHandler
    public void handleSignsOnTransfer(PrisonerTransferredEvent event) {
        if (event.hasOriginalCell() && event.getOriginalCell().hasSigns()) {
            event.getOriginalCell().updateSigns();
        }

        if (event.hasTargetCell() && event.getTargetCell().hasSigns()) {
            event.getTargetCell().updateSigns();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handleSignLineUpdates(JailPluginReloadedEvent event) throws Exception {
        List<String> lines = pl.getConfig().getStringList(Settings.CELLSIGNLINES.getPath());

        //Reset the lines to nothing
        lineOne = "";
        lineTwo = "";
        lineThree = "";
        lineFour = "";

        if(lines.size() >= 1) lineOne = lines.get(0);
        if(lines.size() >= 2) lineTwo = lines.get(1);
        if(lines.size() >= 3) lineThree = lines.get(2);
        if(lines.size() >= 4) lineFour = lines.get(3);

        Util.updateSignLinesCache(new String[] { lineOne, lineTwo, lineThree, lineFour });
    }
}
