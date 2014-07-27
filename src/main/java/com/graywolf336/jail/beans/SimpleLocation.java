package com.graywolf336.jail.beans;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Simple location class which doesn't store any instances of {@link World worlds} or {@link org.bukkit.block.Block blocks}, just uses strings, floats, and doubles.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.1.1
 */
public class SimpleLocation {
    private String world;
    private double x, y, z;
    private float yaw, pitch;

    /**
     * Creates a new SimpleLocation with each detail provided separately.
     * 
     * @param world as a string
     * @param x coordinate as a double
     * @param y coordinate as a double
     * @param z coordinate as a double
     * @param yaw as a float
     * @param pitch as a float
     */
    public SimpleLocation(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Creates a new SimpleLocation with all the detail provided from {@link Location}.
     * 
     * @param location to convert to a SimpleLocation
     */
    public SimpleLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    /**
     * Creates a new Simple Location with all the inputs being in string.
     * 
     * @param world the name of the world
     * @param x coordinate as a string
     * @param y coordinate as a string
     * @param z coordinate as a string
     */
    public SimpleLocation(String world, String x, String y, String z) {
        this.world = world;
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
        this.z = Double.valueOf(z);
        this.yaw = 0;
        this.pitch = 0;
    }

    /**
     * Creates a new SimpleLocation with each detail provided separately.
     * 
     * @param world as a string
     * @param x coordinate as a double
     * @param y coordinate as a double
     * @param z coordinate as a double
     */
    public SimpleLocation(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** Returns the instance from Bukkit of the world this location is in. */
    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    /** Returns the name of the world this location is in. */
    public String getWorldName() {
        return this.world;
    }

    /** Returns a new {@link Location} from this SimpleLocation. */
    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;
    }
}
