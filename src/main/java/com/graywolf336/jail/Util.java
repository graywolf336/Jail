package com.graywolf336.jail;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class Util {
	private final static Pattern DURATION_PATTERN = Pattern.compile("^(\\d+)\\s*(m(?:inute)?s?|h(?:ours?)?|d(?:ays?)?|s(?:econd)?s?)?$", Pattern.CASE_INSENSITIVE);
	
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
    
    /**
     * Converts a string like '20minutes' into the appropriate amount of milliseconds.
     * 
     * @param time The string to convert.
     * @return The time in milliseconds that is converted.
     * @throws Exception if there are no matches
     */
    public static Long getTime(String time) throws Exception {
    	Long t = 10L;
    	Matcher match = DURATION_PATTERN.matcher(time);
    	
    	if (match.matches()) {
    		String units = match.group(2);
    		if ("seconds".equals(units) || "second".equals(units) || "s".equals(units))
    			t = TimeUnit.MILLISECONDS.convert(Long.valueOf(match.group(1)), TimeUnit.SECONDS);
    		else if ("minutes".equals(units) || "minute".equals(units) || "mins".equals(units) || "min".equals(units) || "m".equals(units))
                t = TimeUnit.MILLISECONDS.convert(Long.valueOf(match.group(1)), TimeUnit.MINUTES);
            else if ("hours".equals(units) || "hour".equals(units) || "h".equals(units))
            	t = TimeUnit.MILLISECONDS.convert(Long.valueOf(match.group(1)), TimeUnit.HOURS);
            else if ("days".equals(units) || "day".equals(units) || "d".equals(units))
            	t = TimeUnit.MILLISECONDS.convert(Long.valueOf(match.group(1)), TimeUnit.DAYS);
            else {
            	try {
        			t = TimeUnit.MILLISECONDS.convert(Long.parseLong(time), TimeUnit.MINUTES);
        		}catch(NumberFormatException e) {
        			throw new Exception("Invalid format.");
        		}
            }
    	}else {
    		throw new Exception("Invalid format.");
    	}
    	
    	return Long.valueOf(t);
    }
}
