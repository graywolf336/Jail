package com.graywolf336.jail;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class Util {
	
	/** Checks if the first {@link Vector} is inside the other two. */
	public static boolean isInsideAB(Vector point, Vector first, Vector second) {
		boolean x = isInside(point.getBlockX(), first.getBlockX(), second.getBlockX());
		boolean y = isInside(point.getBlockY(), first.getBlockY(), second.getBlockY());
		boolean z = isInside(point.getBlockZ(), first.getBlockZ(), second.getBlockZ());
		
		return x && y && z;
	}
	
	/**
     * Checks if two numbers are inside a point, or something.
     * <p />
     * 
     * @param loc The location.
     * @param first The first point
     * @param second The second point
     * @return True if they are inside, false if not.
     */
    private static boolean isInside(int loc, int first, int second) {
        int point1 = 0;
        int point2 = 0;
        if (first < second) {
            point1 = first;
            point2 = second;
        } else {
            point2 = first;
            point1 = second;
        }

        return (point1 <= loc) && (loc <= point2);
    }
    
    /** Returns a colorful message from the color codes. */
    public static String getColorfulMessage(String message) {
		return message.replaceAll("(?i)&([0-9abcdefklmnor])", "\u00A7$1");
	}
    
    public static ItemStack getWand() {
    	ItemStack wand = new ItemStack(Material.WOOD_SWORD);
		ItemMeta meta = wand.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + "Jail Wand");
			LinkedList<String> lore = new LinkedList<String>();
			lore.add(ChatColor.BLUE + "The wand for creating");
			lore.add(ChatColor.BLUE + "a jail or cell.");
			meta.setLore(lore);
		wand.setItemMeta(meta);
		
		return wand;
    }
}
