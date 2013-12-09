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
	
	public SimpleLocation(String world, double x, double y, double z, float yaw, float pitch) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public SimpleLocation(Location location) {
		this.world = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}
	
	public World getWorld() {
		return Bukkit.getWorld(world);
	}
	
	public String getWorldName() {
		return this.world;
	}
	
	public Location getLocation() {
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}
	
	public String toString() {
		return world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;
	}
}
