package com.graywolf336.jail.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.jail.JailMain;

/**
 * Event thrown after the plugin is reloaded, internal usage only.
 * 
 * <p />
 * 
 * This event is called right after the plugin and mostly everything has reloaded.
 * We listen to this event for updating various items in classes which are only
 * listeners so we don't store instances of them elsewhere.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class JailPluginReloadedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private JailMain plugin;
    
    public JailPluginReloadedEvent(JailMain main) {
        this.plugin = main;
    }
    
    public JailMain getPlugin() {
        return this.plugin;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
