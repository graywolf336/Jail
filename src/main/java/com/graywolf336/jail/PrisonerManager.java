package com.graywolf336.jail;

public class PrisonerManager {
	private JailMain pl;
	
	public PrisonerManager(JailMain plugin) {
		this.pl = plugin;
	}
	
	public void prepareSomething() {
		pl.getLogger().info("Preparing something.");
	}
}
