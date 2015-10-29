package com.graywolf336.jail;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.interfaces.IJailPayManager;

public class JailPayManager implements IJailPayManager {
    private Economy economy = null;
    private double minteCost, infiniteCost;
    private Material item;
    private boolean infinite, timed;

    protected JailPayManager(JailMain plugin) {
        this.item = Material.getMaterial(plugin.getConfig().getString(Settings.JAILPAYITEM.getPath()).toUpperCase());
        
        if(this.item == null) {
        	plugin.getLogger().warning("Pay item is null, setting to air");
        	this.item = Material.AIR;
        }

        this.minteCost = plugin.getConfig().getDouble(Settings.JAILPAYPRICEPERMINUTE.getPath());

        if(!this.usingItemsForPayment()) {
            if(!this.setupEconomy(plugin)) {
                plugin.getConfig().set(Settings.JAILPAYENABLED.getPath(), false);
            }
        }

        this.timed = plugin.getConfig().getDouble(Settings.JAILPAYPRICEPERMINUTE.getPath()) != 0;
        this.infinite = plugin.getConfig().getDouble(Settings.JAILPAYPRICEINFINITE.getPath()) != 0;
    }

    public boolean isInfiniteEnabled() {
        return this.infinite;
    }

    public boolean isTimedEnabled() {
        return this.timed;
    }

    public String getCostPerMinute() {
        return String.valueOf(this.minteCost);
    }

    public double calculateBill(Prisoner prisoner) {
        return prisoner.getRemainingTime() >= 0 ? prisoner.getRemainingTimeInMinutes() * this.minteCost : infiniteCost;
    }

    public long getMinutesPayingFor(double amount) {
        return (long) Math.floor(amount / this.minteCost);
    }

    public boolean usingItemsForPayment() {
        return this.item != Material.AIR;
    }

    public Material getItemItCost() {
        return this.item;
    }

    public boolean hasEnoughToPay(Player p, double amt) {
        if(this.usingItemsForPayment()) {
            return p.getInventory().contains(this.item, (int) Math.ceil(amt));
        }else {
            return this.economy.has(p, amt);
        }
    }

    public void pay(Player p, double amt) {
        if(this.usingItemsForPayment()) {
            int amtNeeded = (int) Math.ceil(amt);

            for (int i = 0; i < p.getInventory().getSize(); i++) {
                ItemStack it = p.getInventory().getItem(i);

                //The item is either air or we doesn't match out needs
                if(it == null || it.getType() != this.item) continue;

                //If the itemstack has more than or equal to the amount
                //that we need, remove it and subject from the amt needed
                if (amtNeeded >= it.getAmount()) {
                    amtNeeded -= it.getAmount();
                    p.getInventory().clear(i);
                } else {
                    //Otherwise, subject from the itemstack just the amount we need
                    it.setAmount(it.getAmount() - amtNeeded);
                    p.getInventory().setItem(i, it);
                    amtNeeded = 0;
                }

                if (amtNeeded == 0) break;
            }
        }else {
            this.economy.withdrawPlayer(p, amt);
        }
    }

    public String getCurrencyName() {
        if(this.usingItemsForPayment()) {
            String name = item.toString().replaceAll("_", " ");

            if(name.contains(" ")){
                String[] split = name.split(" ");
                for(int i=0; i < split.length; i++){
                    split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1).toLowerCase();
                }

                name = "";
                for(String s : split){
                    name += " " + s;
                }

                name = name.substring(1);
            } else {
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            }

            return name;
        }else {
            return this.economy.currencyNamePlural();
        }
    }

    public Economy getEconomy() {
        return this.economy;
    }

    private boolean setupEconomy(JailMain plugin) {
        if (economy != null) return true;

        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
