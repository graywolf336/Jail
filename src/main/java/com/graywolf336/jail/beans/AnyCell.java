package com.graywolf336.jail.beans;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Chest;

import com.graywolf336.jail.interfaces.ICell;

public class AnyCell implements ICell {
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
