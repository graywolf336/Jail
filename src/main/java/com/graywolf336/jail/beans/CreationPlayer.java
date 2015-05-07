package com.graywolf336.jail.beans;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Represents an instance of a player creating something, whether it be a jail or cell.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.1.0
 *
 */
public class CreationPlayer {
    private String jailName, cellName;
    private int step;
    private int x1, y1, z1, x2, y2, z2;
    private String inWorld, freeWorld;
    private double inX, inY, inZ, freeX, freeY, freeZ;
    private float inPitch, inYaw, freePitch, freeYaw;
    private HashSet<SimpleLocation> signs;
    private SimpleLocation chest;

    /**
     * Create a new instance of a CreationPlayer, given the name of the jail.
     * 
     * @param jailName The name of the jail.
     */
    public CreationPlayer(String jailName) {
        this.jailName = jailName;
        this.step = 1; //Set the default to 1 when creating this.
    }

    /**
     * Creates a new instance of a CreationPlayer, give the name of the jail and cell.
     * 
     * @param jailName The name of the jail.
     * @param cellName The name of the cell.
     */
    public CreationPlayer(String jailName, String cellName) {
        this.jailName = jailName;
        this.cellName = cellName;
        this.signs = new HashSet<SimpleLocation>();
        this.step = 1;
    }

    /** Gets the name of the jail. */
    public String getJailName() {
        return this.jailName;
    }

    /** Gets the name of the cell. */
    public String getCellName() {
        return this.cellName;
    }

    /**
     * Returns the step the creation is in.
     * 
     * <p>
     * 
     * If it is a <strong>Jail</strong>, then when these numbers are returned it means the following:
     * <ol>
     * 	<li>Creating the first block of the Jail region.</li>
     * 	<li>Creating the second block of the Jail region.</li>
     * 	<li>Creating the teleport in location.</li>
     * 	<li>Creating the teleport out location.</li>
     * </ol>
     * 
     * If it is a <strong>Cell</strong>, then when these numbers are returned it means the following:
     * <ol>
     * 	<li>Setting the teleport in location.</li>
     * 	<li>Setting all the signs.</li>
     * 	<li>Setting the double chest.</li>
     * </ol>
     * 
     * @return The step of the Jail/Cell Creation, as an integer.
     */
    public int getStep() {
        return this.step;
    }

    /**
     * Sets the step of the creation.
     * 
     * @param step The state of the creation, see {@link #getStep() getStep} for more information.
     */
    public void setStep(int step) {
        this.step = step;
    }

    /**
     * Increments the current step up one.
     * 
     * <p>
     * 
     * <em>Notice:</em> Using this method can cause the step to go above four (three for cell),
     * which might cause errors later on. Only use when you know that it won't
     * be used again or you know for a fact that the next step is not above four (three for cell).
     * 
     */
    public void nextStep() {
        this.step++;
    }

    /** Sets the first corner with the given location. */
    public void setCornerOne(Location loc) {
        this.x1 = loc.getBlockX();
        this.y1 = loc.getBlockY();
        this.z1 = loc.getBlockZ();
    }

    /** Sets the first corner with the given x, y, and z. */
    public void setCornerOne(int x, int y, int z) {
        this.x1 = x;
        this.y1 = y;
        this.z1 = z;
    }

    /** Returns the <strong>first corner</strong> coords an array of int. <strong>0 = x</strong>, <strong>1 = y</strong>, <strong>2 = z</strong> */
    public int[] getCornerOne() {
        int[] t = {x1, y1, z1};
        return t;
    }

    /** Sets the second corner with the given location. */
    public void setCornerTwo(Location loc) {
        this.x2 = loc.getBlockX();
        this.y2 = loc.getBlockY();
        this.z2 = loc.getBlockZ();
    }

    /** Sets the second corner with the given x, y, and z. */
    public void setCornerTwo(int x, int y, int z) {
        this.x2 = x;
        this.y2 = y;
        this.z2 = z;
    }

    /** Returns the <strong>second corner</strong> coords an array of int. <strong>0 = x</strong>, <strong>1 = y</strong>, <strong>2 = z</strong> */
    public int[] getCornerTwo() {
        int[] t = {x2, y2, z2};
        return t;
    }

    /** Sets the teleport in coords from the given location. */
    public void setTeleportIn(Location location) {
        this.inWorld = location.getWorld().getName();
        this.inX = location.getX();
        this.inY = location.getY();
        this.inZ = location.getZ();
        this.inYaw = location.getYaw();
        this.inPitch = location.getPitch();
    }

    /** Sets the teleport in coords from the given params. */
    public void setTeleportIn(String world, double x, double y, double z, float yaw, float pitch) {
        this.inWorld = world;
        this.inX = x;
        this.inY = y;
        this.inZ = z;
        this.inYaw = yaw;
        this.inPitch = pitch;
    }

    /** Gets the teleport in location in a {@link Location}. */
    public Location getTeleportIn() {
        return new Location(Bukkit.getWorld(inWorld), inX, inY, inZ, inYaw, inPitch);
    }

    /** Gets the teleport in location in a {@link SimpleLocation}. */
    public SimpleLocation getTeleportInSL() {
        return new SimpleLocation(inWorld, inX, inY, inZ, inYaw, inPitch);
    }

    /** Sets the teleport free coords from the given location. */
    public void setTeleportFree(Location location) {
        this.freeWorld = location.getWorld().getName();
        this.freeX = location.getX();
        this.freeY = location.getY();
        this.freeZ = location.getZ();
        this.freeYaw = location.getYaw();
        this.freePitch = location.getPitch();
    }

    /** Sets the teleport in coords from the given params. */
    public void setTeleportFree(String world, double x, double y, double z, float yaw, float pitch) {
        this.freeWorld = world;
        this.freeX = x;
        this.freeY = y;
        this.freeZ = z;
        this.freeYaw = yaw;
        this.freePitch = pitch;
    }

    /** Gets the teleport free location in a {@link Location}. */
    public Location getTeleportFree() {
        return new Location(Bukkit.getWorld(freeWorld), freeX, freeY, freeZ, freeYaw, freePitch);
    }

    /** Gets the teleport free location in a {@link SimpleLocation}. */
    public SimpleLocation getTeleportFreeSL() {
        return new SimpleLocation(freeWorld, freeX, freeY, freeZ, freeYaw, freePitch);
    }

    /** Adds a sign to this cell. */
    public void addSign(SimpleLocation sign) {
        this.signs.add(sign);
    }

    /** Returns all the signs, null if none (usually null when a jail is being created). */
    public HashSet<SimpleLocation> getSigns() {
        return this.signs == null ? null : new HashSet<SimpleLocation>(this.signs);
    }

    /** Sets the chest's location, used mainly for cells. */
    public void setChestLocation(SimpleLocation loc) {
        this.chest = loc;
    }

    /** Gets the chest's location. */
    public SimpleLocation getChestLocation() {
        return this.chest;
    }
}
