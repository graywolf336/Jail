package com.graywolf336.jail.steps;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.CreationPlayer;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.SimpleLocation;

/**
 * Class for stepping a player through the Cell creation process, instance is stored in {@link JailManager}.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.1
 */
public class CellCreationSteps {
	
	/** Sends the Cell Creation message for starting out. */
	public void startStepping(Player player){
		player.sendMessage(ChatColor.AQUA + "---------- Jail Cell Creation ----------");
		player.sendMessage(ChatColor.GREEN + "First, you must select a teleport point for the cell! Move to the teleport point and then click anywhere with your wooden sword to set it.");
		player.sendMessage(ChatColor.AQUA + "----------------------------------------");
		
		ItemStack wand = Util.getWand();
		if(player.getInventory().contains(wand)) {
			int i = player.getInventory().first(wand);
			if(i != -1) {
				player.getInventory().setItem(i, player.getItemInHand());
				player.setItemInHand(wand);
			}
		}else {
			player.getInventory().addItem(wand);
		}
	}
	
	/**
	 * Applies the next step in the Cell Creation process that involves a location, null if no location is needed.
	 * 
	 * @param jm The {@link JailManager} instance.
	 * @param player The player who is doing the creating.
	 * @param cp The {@link CreationPlayer} instance
	 * @param location The location, null if none, being set.
	 */
	public void step(JailManager jm, Player player, CreationPlayer cp, Location location) {
		switch(cp.getStep()) {
			case 1:
				firstStep(jm, cp, player);
				break;
			case 2:
				secondStep(cp, player, location.getBlock());
				break;
			case 3:
				thirdStep(jm, cp, player, location.getBlock());
				break;
			default:
				player.sendMessage(ChatColor.RED + "Something went wrong with the creation of the Jail, please start over");
				jm.removeJailCreationPlayer(player.getName());
				break;
		}
	}

	/** Applies the first step, which is setting the teleport in location. */
	private void firstStep(JailManager jm, CreationPlayer cp, Player player) {
		Vector v1 = jm.getJail(cp.getJailName()).getMinPoint().toVector().clone();
		Vector v2 = jm.getJail(cp.getJailName()).getMaxPoint().toVector().clone();
		Vector point = player.getLocation().toVector().clone();
		
		if(Util.isInsideAB(point, v1, v2)) {
			player.sendMessage(ChatColor.AQUA + "---------- Jail Cell Creation ----------");
			player.sendMessage(ChatColor.GREEN + "Teleport point selected. Now select signs associated with this cell. You may select multiple signs. After you are done with the sign selection, right click on any non-sign block.");
			player.sendMessage(ChatColor.AQUA + "----------------------------------------");
			
			cp.setTeleportIn(player.getLocation());
			cp.nextStep();
		}else {
			player.sendMessage(ChatColor.RED + "---------- Jail Cell Creation ----------");
			player.sendMessage(ChatColor.RED + "Teleport point NOT selected. Please make sure that you are setting the teleport point inside the Jail's corners.");
			player.sendMessage(ChatColor.RED + "----------------------------------------");
		}
	}
	
	/** Applies the second step, which is adding signs to the cell. */
	private void secondStep(CreationPlayer cp, Player player, Block block) {
		if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
			cp.addSign(new SimpleLocation(block.getLocation()));
			player.sendMessage(ChatColor.GREEN + "Sign added, if you want to select another go ahead otherwise right click on any non-sign block.");
		}else {
			player.sendMessage(ChatColor.AQUA + "---------- Jail Cell Creation ----------");
			player.sendMessage(ChatColor.GREEN + "Sign selection completed. Now select a double chest associated with this cell. If there is no chest click on any non-chest block. (Please note that having no chest may result in players items being lost.)");
			player.sendMessage(ChatColor.AQUA + "----------------------------------------");
			cp.nextStep();
		}
	}
	
	/** Applies the third step, which is adding a chest or select not to have a chest. */
	private void thirdStep(JailManager jm, CreationPlayer cp, Player player, Block block) {
		Material bpos1 = block.getLocation().add(-1, 0, 0).getBlock().getType();
	    Material bpos2 = block.getLocation().add(+1, 0, 0).getBlock().getType();
	    Material bpos3 = block.getLocation().add(0, 0, -1).getBlock().getType();
	    Material bpos4 = block.getLocation().add(0, 0, +1).getBlock().getType();
	    
	    if (block.getType() == Material.CHEST) {
	    	if(bpos1 == Material.CHEST || bpos2 == Material.CHEST || bpos3 == Material.CHEST || bpos4 == Material.CHEST) {
	    		cp.setChestLocation(block.getLocation());
	    		player.sendMessage(ChatColor.AQUA + "---------- Jail Cell Creation ----------");
	    	    player.sendMessage(ChatColor.GREEN + "Chest selected.");
	    	    player.sendMessage(ChatColor.AQUA + "----------------------------------------");
	    	}else {
	    		player.sendMessage(ChatColor.RED + "---------- Jail Cell Creation ----------");
	    		player.sendMessage(ChatColor.RED + "Chest must be a double chest, chest not selected");
	    		player.sendMessage(ChatColor.RED + "----------------------------------------");
	    		return;
	    	}
	    }else {
	    	player.sendMessage(ChatColor.AQUA + "---------- Jail Cell Creation ----------");
		    player.sendMessage(ChatColor.RED + "No chest selected.");
		    player.sendMessage(ChatColor.AQUA + "----------------------------------------");
		    return;
	    }
	    
	    finalStep(jm, cp, player);
	}

	private void finalStep(JailManager jm, CreationPlayer cp, Player player) {
		Jail j = jm.getJail(cp.getJailName());
		Cell c = new Cell(cp.getCellName());
		
		c.addAllSigns(cp.getSigns());
		c.setTeleport(cp.getTeleportInSL());
		c.setChestLocation(cp.getChestLocation());
		
		j.addCell(c, true);
		
		jm.removeCellCreationPlayer(player.getName());
		jm.addCreatingCell(player.getName(), j.getName(), "cell_n" + (j.getCellCount() + 1));
		
		player.sendMessage(ChatColor.AQUA + "---------- Jail Cell Creation ----------");
		player.sendMessage(ChatColor.GREEN + "Cell created. Now select the teleport point of the next cell, which is going to be named '" + jm.getCellCreationPlayer(player.getName()).getCellName() + "'. If you wish to stop creating cells, type /jailstop.");
		player.sendMessage(ChatColor.AQUA + "----------------------------------------");
	}
}
