package com.graywolf336.jail.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.events.PrePrisonerJailedByJailStickEvent;
import com.graywolf336.jail.events.PrePrisonerJailedEvent;
import com.graywolf336.jail.events.PrisonerJailedEvent;

public class JailingListener implements Listener {
	private JailMain pl;
	private DateFormat dateFormat;
	
	public JailingListener(JailMain plugin) {
		this.pl = plugin;
		this.dateFormat = new SimpleDateFormat(Lang.TIMEFORMAT.get());
	}
	
	@EventHandler(ignoreCancelled=true)
	public void preJailingListener(PrePrisonerJailedEvent event) {
		if(pl.getConfig().getBoolean(Settings.LOGJAILINGTOPROFILE.getPath())) {
			pl.getJailIO().addRecordEntry(event.getPrisoner().getUUID().toString(),
					event.getPrisoner().getLastKnownName(),
					event.getPrisoner().getJailer(), dateFormat.format(new Date()),
					event.getPrisoner().getRemainingTimeInMinutes(), event.getPrisoner().getReason());
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void preJailingListener(PrePrisonerJailedByJailStickEvent event) {
		if(pl.getConfig().getBoolean(Settings.LOGJAILINGTOPROFILE.getPath())) {
			pl.getJailIO().addRecordEntry(event.getPrisoner().getUUID().toString(),
					event.getPrisoner().getLastKnownName(),
					event.getPrisoner().getJailer(), dateFormat.format(new Date()),
					event.getPrisoner().getRemainingTimeInMinutes(), event.getPrisoner().getReason());
		}
	}
	
	@EventHandler
	public void setInmatesClothing(PrisonerJailedEvent event) {
		if(pl.getConfig().getBoolean(Settings.CLOTHINGENABLED.getPath())) {
			String[] helmet = pl.getConfig().getString(Settings.CLOTHINGHELMET.getPath()).toUpperCase().split("~");
			switch(helmet.length) {
				case 1:
					event.getPlayer().getInventory().setHelmet(new ItemStack(Material.valueOf(helmet[0])));
					break;
				case 2:
					ItemStack item = new ItemStack(Material.valueOf(helmet[0]));
					LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
					String[] colors = helmet[1].split(",");
					meta.setColor(Color.fromBGR(Integer.parseInt(colors[2]), Integer.parseInt(colors[1]), Integer.parseInt(colors[0])));
					item.setItemMeta(meta);
					event.getPlayer().getInventory().setHelmet(item);
				default:
					break;
			}
			
			String[] chest = pl.getConfig().getString(Settings.CLOTHINGCHEST.getPath()).toUpperCase().split("~");
			switch(chest.length) {
				case 1:
					event.getPlayer().getInventory().setChestplate(new ItemStack(Material.valueOf(chest[0])));
					break;
				case 2:
					ItemStack item = new ItemStack(Material.valueOf(chest[0]));
					LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
					String[] colors = chest[1].split(",");
					meta.setColor(Color.fromBGR(Integer.parseInt(colors[2]), Integer.parseInt(colors[1]), Integer.parseInt(colors[0])));
					item.setItemMeta(meta);
					event.getPlayer().getInventory().setChestplate(item);
				default:
					break;
			}
			
			String[] legs = pl.getConfig().getString(Settings.CLOTHINGLEGS.getPath()).toUpperCase().split("~");
			switch(legs.length) {
				case 1:
					event.getPlayer().getInventory().setLeggings(new ItemStack(Material.valueOf(legs[0])));
					break;
				case 2:
					ItemStack item = new ItemStack(Material.valueOf(legs[0]));
					LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
					String[] colors = legs[1].split(",");
					meta.setColor(Color.fromBGR(Integer.parseInt(colors[2]), Integer.parseInt(colors[1]), Integer.parseInt(colors[0])));
					item.setItemMeta(meta);
					event.getPlayer().getInventory().setLeggings(item);
				default:
					break;
			}
			
			String[] boots = pl.getConfig().getString(Settings.CLOTHINGBOOTS.getPath()).toUpperCase().split("~");
			switch(boots.length) {
				case 1:
					event.getPlayer().getInventory().setBoots(new ItemStack(Material.valueOf(boots[0])));
					break;
				case 2:
					ItemStack item = new ItemStack(Material.valueOf(boots[0]));
					LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
					String[] colors = boots[1].split(",");
					meta.setColor(Color.fromBGR(Integer.parseInt(colors[2]), Integer.parseInt(colors[1]), Integer.parseInt(colors[0])));
					item.setItemMeta(meta);
					event.getPlayer().getInventory().setBoots(item);
				default:
					break;
			}
		}
	}
}
