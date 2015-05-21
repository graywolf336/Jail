package com.graywolf336.jail.interfaces;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.graywolf336.jail.beans.Prisoner;

public interface IJailPayManager {
	
	/** Checks if paying for infinite is enabled. */
    public boolean isInfiniteEnabled();
    
    /** Checks if paying for timed is enabled. */
    public boolean isTimedEnabled();
    
    /** Gets how much it cost per minute in string format. */
    public String getCostPerMinute();
    
    /**
     * Calculates how much players have to pay to get completely free.
     * 
     * @param prisoner data of who we're calculating
     * @return The economy cost the prisoner will need to pay to get completely free.
     */
    public double calculateBill(Prisoner prisoner);
    
    /** Gets how many minutes someone is paying for (rounds to the lowest number). */
    public long getMinutesPayingFor(double amount);
    
    /** Returns if we are using items for payment instead of economy. */
    public boolean usingItemsForPayment();
    
    /**
     * Gets the {@link Material} it costs for jail pay, will be air if using economy.
     * 
     * @return The item type it costs, air if using virtual economy.
     */
    public Material getItemItCost();
    
    /**
     * Checks if the player has enough money/items to pay what they have said they want to.
     * 
     * @param player The player who is doing the paying.
     * @param amount The amount to check they if they have.
     * @return true if they have enough, false if not.
     */
    public boolean hasEnoughToPay(Player player, double amount);
    
    /**
     * Pays the required fees from the given player, removing items or money from economy.
     * 
     * @param player The player who is paying.
     * @param amount The amount of items or money to withdraw from the player.
     */
    public void pay(Player player, double amount);
    
    /** Gets the name of the item in nice capitals. */
    public String getCurrencyName();
    
    /** Returns the economy provider to do transaction with. */
    public Economy getEconomy();
}
