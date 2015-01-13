package com.graywolf336.jail.listeners;

import java.util.HashSet;
import java.util.List;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.SimpleLocation;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.events.PrisonerJailedEvent;
import com.graywolf336.jail.events.PrisonerReleasedEvent;
import com.graywolf336.jail.events.PrisonerTimeChangeEvent;
import com.graywolf336.jail.events.PrisonerTransferredEvent;

public class CellSignListener implements Listener {
    private String lineOne, lineTwo, lineThree, lineFour;
    private JailMain pl;

    public CellSignListener(JailMain plugin) {
        pl = plugin;
        List<String> lines = pl.getConfig().getStringList(Settings.CELLSIGNLINES.getPath());
        lineOne = lines.get(0);
        lineTwo = lines.get(1);
        lineThree = lines.get(2);
        lineFour = lines.get(3);
    }

    @EventHandler(ignoreCancelled=true, priority = EventPriority.MONITOR)
    public void changeTheCellSigns(final PrisonerTimeChangeEvent event) {
        pl.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
            public void run() {
                if (event.hasCell() && event.getCell().hasSigns()) {
                    HashSet<SimpleLocation> signs = event.getCell().getSigns();
                    String s1 = Util.replaceAllVariables(event.getPrisoner(), lineOne);
                    String s2 = Util.replaceAllVariables(event.getPrisoner(), lineTwo);
                    String s3 = Util.replaceAllVariables(event.getPrisoner(), lineThree);
                    String s4 = Util.replaceAllVariables(event.getPrisoner(), lineFour);

                    for (SimpleLocation s : signs) {
                        if (s.getLocation().getBlock().getState() instanceof Sign) {
                            Sign sign = (Sign) s.getLocation().getBlock().getState();
                            sign.setLine(0, s1);
                            sign.setLine(1, s2);
                            sign.setLine(2, s3);
                            sign.setLine(3, s4);
                            sign.update();
                        } else {
                            // Remove the sign from the cell since it isn't
                            // a valid sign
                            event.getCell().getSigns().remove(s);
                        }
                    }
                }
            }
        });
    }
    
    @EventHandler
    public void changeCellSignsOnJail(PrisonerJailedEvent event) {
        if (event.hasCell() && event.getCell().hasSigns()) {
            HashSet<SimpleLocation> signs = event.getCell().getSigns();
            String s1 = Util.replaceAllVariables(event.getPrisoner(), lineOne);
            String s2 = Util.replaceAllVariables(event.getPrisoner(), lineTwo);
            String s3 = Util.replaceAllVariables(event.getPrisoner(), lineThree);
            String s4 = Util.replaceAllVariables(event.getPrisoner(), lineFour);

            for (SimpleLocation s : signs) {
                if (s.getLocation().getBlock().getState() instanceof Sign) {
                    Sign sign = (Sign) s.getLocation().getBlock().getState();
                    sign.setLine(0, s1);
                    sign.setLine(1, s2);
                    sign.setLine(2, s3);
                    sign.setLine(3, s4);
                    sign.update();
                } else {
                    // Remove the sign from the cell since it isn't
                    // a valid sign
                    event.getCell().getSigns().remove(s);
                }
            }
        }
    }

    @EventHandler
    public void clearTheCellSigns(PrisonerReleasedEvent event) {
        if (event.hasCell() && event.getCell().hasSigns()) {
            HashSet<SimpleLocation> signs = event.getCell().getSigns();

            for (SimpleLocation s : signs) {
                if (s.getLocation().getBlock().getState() instanceof Sign) {
                    Sign sign = (Sign) s.getLocation().getBlock().getState();
                    sign.setLine(0, "");
                    sign.setLine(1, Lang.CELLEMPTYSIGN.get());
                    sign.setLine(2, "");
                    sign.setLine(3, "");
                    sign.update();
                } else {
                    // Remove the sign from the cell since it isn't
                    // a valid sign
                    event.getCell().getSigns().remove(s);
                }
            }
        }
    }

    @EventHandler
    public void handleSignsOnTransfer(PrisonerTransferredEvent event) {
        if (event.hasOriginalCell() && event.getOriginalCell().hasSigns()) {
            HashSet<SimpleLocation> signs = event.getOriginalCell().getSigns();

            for (SimpleLocation s : signs) {
                if (s.getLocation().getBlock().getState() instanceof Sign) {
                    Sign sign = (Sign) s.getLocation().getBlock().getState();
                    sign.setLine(0, "");
                    sign.setLine(1, Lang.CELLEMPTYSIGN.get());
                    sign.setLine(2, "");
                    sign.setLine(3, "");
                    sign.update();
                } else {
                    // Remove the sign from the cell since it isn't
                    // a valid sign
                    event.getOriginalCell().getSigns().remove(s);
                }
            }
        }

        if (event.hasTargetCell() && event.getTargetCell().hasSigns()) {
            HashSet<SimpleLocation> signs = event.getTargetCell().getSigns();
            String s1 = Util.replaceAllVariables(event.getPrisoner(), lineOne);
            String s2 = Util.replaceAllVariables(event.getPrisoner(), lineTwo);
            String s3 = Util.replaceAllVariables(event.getPrisoner(), lineThree);
            String s4 = Util.replaceAllVariables(event.getPrisoner(), lineFour);

            for (SimpleLocation s : signs) {
                if (s.getLocation().getBlock().getState() instanceof Sign) {
                    Sign sign = (Sign) s.getLocation().getBlock().getState();
                    sign.setLine(0, s1);
                    sign.setLine(1, s2);
                    sign.setLine(2, s3);
                    sign.setLine(3, s4);
                    sign.update();
                } else {
                    // Remove the sign from the cell since it isn't
                    // a valid sign
                    event.getTargetCell().getSigns().remove(s);
                }
            }
        }
    }
}
