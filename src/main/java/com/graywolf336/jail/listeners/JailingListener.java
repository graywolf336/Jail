package com.graywolf336.jail.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.events.PrePrisonerJailedByJailStickEvent;
import com.graywolf336.jail.events.PrePrisonerJailedEvent;

public class JailingListener implements Listener {
	private JailMain pl;
	private DateFormat dateFormat;
	
	public JailingListener(JailMain plugin) {
		this.pl = plugin;
		this.dateFormat = new SimpleDateFormat(pl.getJailIO().getLanguageString(LangString.TIMEFORMAT));
	}
	
	@EventHandler(ignoreCancelled=true)
	public void preJailingListener(PrePrisonerJailedEvent event) {
		pl.getJailIO().addRecordEntry(event.getPrisoner().getName(),
				event.getPrisoner().getJailer(), dateFormat.format(new Date()),
				event.getPrisoner().getRemainingTimeInMinutes(), event.getPrisoner().getReason());
	}
	
	@EventHandler(ignoreCancelled=true)
	public void preJailingListener(PrePrisonerJailedByJailStickEvent event) {
		pl.getJailIO().addRecordEntry(event.getPrisoner().getName(),
				event.getPrisoner().getJailer(), dateFormat.format(new Date()),
				event.getPrisoner().getRemainingTimeInMinutes(), event.getPrisoner().getReason());
	}
}
