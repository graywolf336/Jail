package com.graywolf336.jail.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Chest;

import com.graywolf336.jail.interfaces.ICell;

/**
 * Pass this an instance of this class into the jailing of a player and they go to any open cell.
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 *
 */
public class AnyCell implements ICell {
    public int getDatabaseID() {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    public void setPrisoner(Prisoner prisoner) {
        throw new UnsupportedOperationException();
    }

    public Prisoner getPrisoner() {
        throw new UnsupportedOperationException();
    }

    public void removePrisoner() {
        throw new UnsupportedOperationException();
    }

    public boolean hasPrisoner() {
        throw new UnsupportedOperationException();
    }

    public void addAllSigns(HashSet<SimpleLocation> signs) {
        throw new UnsupportedOperationException();
    }

    public void addSign(SimpleLocation sign) {
        throw new UnsupportedOperationException();
    }

    public HashSet<SimpleLocation> getSigns() {
        throw new UnsupportedOperationException();
    }

    public boolean hasSigns() {
        throw new UnsupportedOperationException();
    }

    public String getSignString() {
        throw new UnsupportedOperationException();
    }

    public List<String> getInvalidSigns() {
        throw new UnsupportedOperationException();
    }

    public List<String> cleanSigns() {
        throw new UnsupportedOperationException();
    }

    public HashMap<String, List<String>> updateSigns() {
        throw new UnsupportedOperationException();
    }

    public void setTeleport(SimpleLocation location) {
        throw new UnsupportedOperationException();
    }

    public Location getTeleport() {
        throw new UnsupportedOperationException();
    }

    public void setChestLocation(SimpleLocation simpleLocation) {
        throw new UnsupportedOperationException();
    }

    public Location getChestLocation() {
        throw new UnsupportedOperationException();
    }

    public Chest getChest() {
        throw new UnsupportedOperationException();
    }

    public boolean hasChest() {
        throw new UnsupportedOperationException();
    }

    public boolean setChanged(boolean changed) {
        throw new UnsupportedOperationException();
    }

    public boolean hasChanged() {
        throw new UnsupportedOperationException();
    }
}
