package com.graywolf336.jail.listeners;

import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;

public class ProtectionListener implements Listener {
	private JailMain pl;
	
	public ProtectionListener(JailMain plugin) {
		this.pl = plugin;
	}
	
	@EventHandler(ignoreCancelled=true)
	public void protectionBlockBreaking(BlockBreakEvent event) {
		//Before we check if the player is jailed, let's save a
		//tiny bit of resources and check if this protection is enabled
		if(pl.getConfig().getBoolean(Settings.BLOCKBREAKPROTECTION.getPath())) {
			if(pl.getJailManager().isPlayerJailed(event.getPlayer().getName())) {
				try {
					long add = Util.getTime(pl.getConfig().getString(Settings.BLOCKBREAKPENALTY.getPath()));
					pl.getJailManager().getPrisoner(event.getPlayer().getName()).addTime(add);
					
					String msg = pl.getJailIO().getLanguageString(LangString.PROTECTIONMESSAGE,
							new String[] { String.valueOf(TimeUnit.MINUTES.convert(add, TimeUnit.MILLISECONDS)),
							pl.getJailIO().getLanguageString(LangString.BLOCKBREAKING) });
					
					event.getPlayer().sendMessage(msg);
				}catch (Exception e) {
					pl.getLogger().severe("Block break penalty's time is in the wrong format, please fix.");
				}
				
			}
		}
	}
}
