package com.graywolf336.jail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.enums.Lang;

/**
 * Provides a variety of methods, static, that are used throughout the plugin.
 *
 * @author graywolf336
 * @since 2.x.x
 * @version 3.1.0
 */
public class Util {
    private final static Pattern DURATION_PATTERN = Pattern.compile("^(\\d+)\\s*(m(?:inute)?s?|h(?:ours?)?|d(?:ays?)?|s(?:econd)?s?)?$", Pattern.CASE_INSENSITIVE);
    private static String[] signLines = new String[] { "", "", "", "" };

    /**
     * Checks if the first {@link Vector} is inside this region.
     *
     * @param point The point to check
     * @param first point of the region
     * @param second second point of the region
     * @return True if all the coords of the first vector are in the entire region.
     */
    public static boolean isInsideAB(Vector point, Vector first, Vector second) {
        boolean x = isInside(point.getBlockX(), first.getBlockX(), second.getBlockX());
        boolean y = isInside(point.getBlockY(), first.getBlockY(), second.getBlockY());
        boolean z = isInside(point.getBlockZ(), first.getBlockZ(), second.getBlockZ());

        return x && y && z;
    }

    /**
     * Checks if two numbers are inside a point, or something.
     *
     * <p>
     *
     * @param loc The location.
     * @param first The first point
     * @param second The second point
     * @return true if they are inside, false if not.
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

        return point1 <= loc && loc <= point2;
    }

    /**
     * Checks if the given string is inside the array, ignoring the casing.
     *
     * <p />
     *
     * @param value to check
     * @param array of strings to check
     * @return true if the array contains the provided value, false if it doesn't
     */
    public static boolean isStringInsideArray(String value, String... array) {
        for(String s : array)
            if(s.equalsIgnoreCase(value))
                return true;

        return false;
    }

    /**
     * Checks if the given string is inside the list, ignoring the casing.
     *
     * <p />
     *
     * @param value to check
     * @param list of strings to check
     * @return true if the list contains the provided value, false if it doesn't
     */
    public static boolean isStringInsideList(String value, List<String> list) {
        for(String s : list)
            if(s.equalsIgnoreCase(value))
                return true;

        return false;
    }

    /**
     * Gets a single string from an array of strings, separated by the separator.
     *
     * @param separator The item to separate the items
     * @param array The array of strings to combine
     * @return the resulting combined string
     */
    public static String getStringFromArray(String separator, String... array) {
        StringBuilder result = new StringBuilder();

        for(String s : array) {
            if(result.length() != 0) result.append(separator);
            result.append(s);
        }

        return result.toString();
    }

    /**
     * Gets a single string from a list of strings, separated by the separator.
     *
     * @param separator The item to separate the items
     * @param list The list of strings to combine
     * @return the resulting combined string
     */
    public static String getStringFromList(String separator, List<String> list) {
        StringBuilder result = new StringBuilder();

        for(String s : list) {
            if(result.length() != 0) result.append(separator);
            result.append(s);
        }

        return result.toString();
    }

    /**
     * Returns a colorful message from the color codes.
     * 
     * @param message the message to colorize
     * @return the colorized message
     */
    public static String getColorfulMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Returns a message with all the possible variables replaced.
     * 
     * @param p the {@link Prisoner} data
     * @param msg the message to replace everything in
     * @return The message with everything replaced and colorized.
     */
    public static String replaceAllVariables(Prisoner p, String msg) {
        msg = msg.replace("%player%", p.getLastKnownName())
                .replace("%uuid%", p.getUUID().toString())
                .replace("%reason%", p.getReason())
                .replace("%jailer", p.getJailer())
                .replace("%afktime%", Util.getDurationBreakdown(p.getAFKTime()));

        if(p.getRemainingTime() >= 0) {
            msg = msg.replace("%timeinminutes%", String.valueOf(p.getRemainingTimeInMinutes()));
            msg = msg.replace("%prettytime%", Util.getDurationBreakdown(p.getRemainingTime()));
        }else {
            msg = msg.replace("%timeinminutes%", Lang.JAILEDFOREVERSIGN.get());
            msg = msg.replace("%prettytime%", Lang.JAILEDFOREVERSIGN.get());
        }

        return getColorfulMessage(msg);
    }

    /**
     * Replaces all the variables in the messages with their possible values.
     * 
     * @param p the {@link Prisoner} data.
     * @param msgs the messages
     * @return the messages but variables replaced and colorized
     */
    public static String[] replaceAllVariables(Prisoner p, String... msgs) {
        String[] results = new String[msgs.length];
        
        for(int i = 0; i < msgs.length; i++)
            results[i] = replaceAllVariables(p, msgs[i]);

        return results;
    }

    /**
     * Returns the wand used throughout the different creation steps.
     * 
     * @return The {@link ItemStack} to use for creation
     */
    public static ItemStack getWand() {
        ItemStack wand = new ItemStack(Material.CLAY_BRICK);
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
     * Converts a string like '20minutes' into the appropriate amount of the given unit.
     *
     * @param time in a string to convert.
     * @param unit which to convert to.
     * @return The time in the unit given that is converted.
     * @throws Exception if there are no matches
     */
    public static Long getTime(String time, TimeUnit unit) throws Exception {
        return unit.convert(getTime(time), TimeUnit.MILLISECONDS);
    }

    /**
     * Converts a string like '20minutes' into the appropriate amount of milliseconds.
     *
     * @param time in a string to convert.
     * @return The time in milliseconds that is converted.
     * @throws Exception if there are no matches
     */
    public static Long getTime(String time) throws Exception {
        if(time.equalsIgnoreCase("-1")) return -1L;

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

        return t;
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "XdYhZAs".
     */
    public static String getDurationBreakdown(long millis) {
        if(millis < 0) {
            return Lang.JAILEDFOREVERSIGN.get();
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if(days > 0) {
            sb.append(days);
            sb.append("d");
        }

        if(days > 0 || hours > 0) {
            sb.append(hours);
            sb.append("h");
        }

        if(days > 0 || hours > 0 || minutes > 0) {
            sb.append(minutes);
            sb.append("m");
        }

        sb.append(seconds);
        sb.append("s");

        return sb.toString();
    }

    /**
     * Updates the local cache of the lines which go on signs.
     *
     * @param lines array of string which go on signs, must contain exactly four.
     * @throws Exception Throws an exception if there aren't exactly four lines.
     */
    public static void updateSignLinesCache(String[] lines) throws Exception {
        if(lines.length != 4) throw new Exception("Exactly four lines are required for the signs.");
        signLines = lines;
    }

    /**
     * Gets all the lines which go on the cell signs.
     * 
     * @return the strings for the signs
     */
    public static String[] getSignLines() {
        return signLines;
    }
    
    public static List<String> getUnusedItems(List<String> items, String[] args, boolean useP) {
        List<String> used = new ArrayList<String>();
        for(String s : args)
            if(s.contains("-"))
                used.add(s.replace("-", ""));
        
        List<String> unused = new ArrayList<String>();
        for(String t : items)
            if(!used.contains(t)) //don't add it if it is already used
                if(!t.equalsIgnoreCase("p") || (useP && t.equalsIgnoreCase("p")))//don't add -p unless otherwise stated
                        unused.add("-" + t);
        
        Collections.sort(unused);
        
        return unused;
    }

    /**
     * Converts the player inventory to a String array of Base64 strings. First string is the content and second string is the armor.
     *
     * @param playerInventory to turn into an array of strings.
     * @return Array of strings: [ main content, armor content ]
     * @throws IllegalStateException if any of the {@link ItemStack}s couldn't be parsed
     */
    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        //get the main content part, this doesn't return the armor
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[] { content, armor };
    }

    /**
     *
     * A method to serialize an {@link ItemStack} array to Base64 String.
     *
     * <p>
     *
     * Based off of {@link #toBase64(Inventory)}.
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     * @throws IllegalStateException if any of the {@link ItemStack}s couldn't be parsed
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * A method to serialize an inventory to Base64 string.
     *
     * <p>
     *
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub. <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param inventory to serialize
     * @return Base64 string of the provided inventory
     * @throws IllegalStateException if any of the {@link ItemStack}s couldn't be parsed
     */
    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     *
     * A method to get an {@link Inventory} from an encoded, Base64, string.
     *
     * <p>
     *
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param data Base64 string of data containing an inventory.
     * @return Inventory created from the Base64 string.
     * @throws IOException if we were unable to parse the base64 string
     */
    public static Inventory fromBase64(String data) throws IOException {
        if(data.isEmpty()) return Bukkit.getServer().createInventory(null, 0);

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    /**
     * Gets an array of ItemStacks from Base64 string.
     *
     * <p>
     *
     * Base off of {@link #fromBase64(String)}.
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException if we was unable to parse the base64 string
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        if(data.isEmpty()) return new ItemStack[] {};

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static void restoreInventory(Player player, Prisoner prisoner) {
        try {
            Inventory content = Util.fromBase64(prisoner.getInventory());
            ItemStack[] armor = Util.itemStackArrayFromBase64(prisoner.getArmor());

            for(ItemStack item : armor) {
                if(item == null)
                    continue;
                else if(item.getType().toString().toLowerCase().contains("helmet"))
                    player.getInventory().setHelmet(item);
                else if(item.getType().toString().toLowerCase().contains("chestplate"))
                    player.getInventory().setChestplate(item);
                else if(item.getType().toString().toLowerCase().contains("leg"))
                    player.getInventory().setLeggings(item);
                else if(item.getType().toString().toLowerCase().contains("boots"))
                    player.getInventory().setBoots(item);
                else if (player.getInventory().firstEmpty() == -1)
                    player.getWorld().dropItem(player.getLocation(), item);
                else
                    player.getInventory().addItem(item);
            }

            for(ItemStack item : content.getContents()) {
                if(item == null) continue;
                else if(player.getInventory().firstEmpty() == -1)
                    player.getWorld().dropItem(player.getLocation(), item);
                else
                    player.getInventory().addItem(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Unable to restore " + player.getName() + "'s inventory.");
        }
    }
}
