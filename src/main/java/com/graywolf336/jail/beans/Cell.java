package com.graywolf336.jail.beans;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

import com.graywolf336.jail.interfaces.ICell;

/** Represents a Cell inside of a {@link Jail}.
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.1.4
 */
public class Cell implements ICell {
    private String name;
    private Prisoner p;
    private HashSet<SimpleLocation> signs;
    private SimpleLocation teleport, chest;

    /** Creates a new Cell with the given name
     *
     * @param name The name of the cell.
     */
    public Cell(String name) {
        this.name = name;
        this.signs = new HashSet<SimpleLocation>();
    }

    public String getName() {
        return this.name;
    }

    public void setPrisoner(Prisoner prisoner) {
        this.p = prisoner;
    }

    public Prisoner getPrisoner() {
        return this.p;
    }

    public void removePrisoner() {
        this.p = null;
    }

    public boolean hasPrisoner() {
        return this.p != null; //Return true if prison is not null, as when it isn't null we have a prisoner in this cell
    }

    public void addAllSigns(HashSet<SimpleLocation> signs) {
        this.signs.addAll(signs);
    }

    public void addSign(SimpleLocation sign) {
        this.signs.add(sign);
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

    public void setTeleport(SimpleLocation location) {
        this.teleport = location;
    }

    public Location getTeleport() {
        return this.teleport.getLocation();
    }

    public void setChestLocation(SimpleLocation simpleLocation) {
        this.chest = simpleLocation;
    }

    public Location getChestLocation() {
        return this.chest == null ? null : this.chest.getLocation();
    }

    public Chest getChest() {
        if(this.chest == null) return null;
        if(this.chest.getLocation().getBlock() == null || this.chest.getLocation().getBlock().getType() != Material.CHEST) return null;

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
}
