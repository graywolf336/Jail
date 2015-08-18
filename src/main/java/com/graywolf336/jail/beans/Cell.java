package com.graywolf336.jail.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;

import com.graywolf336.jail.Util;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.interfaces.ICell;

/** Represents a Cell inside of a {@link Jail}.
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.2.0
 */
public class Cell implements ICell {
    private int databaseid;
    private String name;
    private Prisoner p;
    private HashSet<SimpleLocation> signs;
    private SimpleLocation teleport, chest;
    private boolean changed;

    /** Creates a new Cell with the given name
     *
     * @param name The name of the cell.
     */
    public Cell(String name) {
        this.databaseid = -1;
        this.name = name;
        this.signs = new HashSet<SimpleLocation>();
        this.changed = false;
    }
    
    /**
     * Creates a new Cell with the id in the database and the name.
     * 
     * @param id The id of the cell in the database.
     * @param name The name of the cell.
     */
    public Cell(int id, String name) {
        this.databaseid = id;
        this.name = name;
        this.signs = new HashSet<SimpleLocation>();
        this.changed = false;
    }
    
    public int getDatabaseID() {
        return this.databaseid;
    }

    public String getName() {
        return this.name;
    }

    public void setPrisoner(Prisoner prisoner) {
        this.p = prisoner;
        this.changed = true;
    }

    public Prisoner getPrisoner() {
        return this.p;
    }

    public void removePrisoner() {
        this.p = null;
        this.changed = true;
    }

    public boolean hasPrisoner() {
        return this.p != null; //Return true if prison is not null, as when it isn't null we have a prisoner in this cell
    }

    public void addAllSigns(HashSet<SimpleLocation> signs) {
        this.signs.addAll(signs);
        this.changed = true;
    }

    public void addSign(SimpleLocation sign) {
        this.signs.add(sign);
        this.changed = true;
    }

    public HashSet<SimpleLocation> getSigns() {
        return this.signs;
    }

    public boolean hasSigns() {
        return !this.signs.isEmpty();
    }

    public String getSignString() {
        String r = "";

        for(SimpleLocation s : signs) {
            if(r.isEmpty()) {
                r = s.toString();
            }else {
                r += ";" + s.toString();
            }
        }

        return r;
    }
    
    public List<String> getInvalidSigns() {
        List<String> invalid = new ArrayList<String>();

        for(SimpleLocation s : new HashSet<SimpleLocation>(signs))
            if (s.getLocation().getBlock().getState() instanceof Sign)
                continue;
           else
                invalid.add(s.toString());

        return invalid;
    }

    public List<String> cleanSigns() {
        List<String> cleaned = new ArrayList<String>();

        for(SimpleLocation s : new HashSet<SimpleLocation>(signs)) {
            if (s.getLocation().getBlock().getState() instanceof Sign) {
                continue;
            }else {
                changed = true;
                signs.remove(s);
                cleaned.add(s.toString());
            }
        }

        return cleaned;
    }

    public HashMap<String, List<String>> updateSigns() {
        List<String> removed = new ArrayList<String>();
        List<String> updated = new ArrayList<String>();

        for(SimpleLocation s : new HashSet<SimpleLocation>(signs)) {
            BlockState bs = s.getLocation().getBlock().getState();

            if (bs instanceof Sign) {
                Sign sign = (Sign) bs;

                if(hasPrisoner()) {
                    String[] lines = Util.replaceAllVariables(p, Util.getSignLines());

                    sign.setLine(0, lines[0]);
                    sign.setLine(1, lines[1]);
                    sign.setLine(2, lines[2]);
                    sign.setLine(3, lines[3]);
                    sign.update(true, false);
                }else {
                    sign.setLine(0, "");
                    sign.setLine(1, Lang.CELLEMPTYSIGN.get());
                    sign.setLine(2, "");
                    sign.setLine(3, "");
                    sign.update(true, false);
                }

                updated.add(s.toString());
            }else {
                changed = true;
                signs.remove(s);
                removed.add(s.toString());
            }
        }

        HashMap<String, List<String>> results = new HashMap<String, List<String>>();
        results.put("removed", removed);
        results.put("updated", updated);

        return results;
    }

    public void setTeleport(SimpleLocation location) {
        this.teleport = location;
        this.changed = true;
    }

    public Location getTeleport() {
        return this.teleport.getLocation();
    }

    public void setChestLocation(SimpleLocation simpleLocation) {
        this.chest = simpleLocation;
        this.changed = true;
    }

    public Location getChestLocation() {
        return this.chest == null ? null : this.chest.getLocation();
    }

    public Chest getChest() {
        if(this.chest == null) return null;
        if(this.chest.getLocation().getBlock() == null
                || (this.chest.getLocation().getBlock().getType() != Material.CHEST
                && this.chest.getLocation().getBlock().getType() != Material.TRAPPED_CHEST)) return null;

        return (Chest) this.chest.getLocation().getBlock().getState();
    }

    public boolean hasChest() {
        Chest c = getChest();
        if(c != null) {
            if(c.getInventory().getSize() >= 40)
                return true;
            else {
                Bukkit.getLogger().severe("The cell " + this.name + " has chest that isn't a double chest, please fix.");
                return false;
            }
        }else
            return false;
    }
    
    public boolean useChest() {
        return this.chest != null;
    }

    public boolean setChanged(boolean changed) {
        return this.changed = changed;
    }

    public boolean hasChanged() {
        return this.changed;
    }
}
