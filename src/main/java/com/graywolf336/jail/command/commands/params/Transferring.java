package com.graywolf336.jail.command.commands.params;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class Transferring {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
	
	@Parameter(names = { "-player", "-p", "-prisoner" }, description = "The name of the player we are jailing.")
	private String player = "";
	
	@Parameter(names = { "-jail", "-j", "-prison" }, description = "The jail we are sending the player to.")
	private String jail = "";
	
	@Parameter(names = { "-cell", "-c"}, description = "The cell in the jail we are sending them to.")
	private String cell = "";
	
	/** Returns the parameters. */
	public List<String> parameters() {
		return parameters;
	}
	
	/** Returns the player parameter. */
	public String player() {
		return player;
	}
	
	/** Returns the jail parameter. */
	public String jail() {
		return jail;
	}
	
	/** Sets the jail parameter. */
	public void setJail(String jail) {
		this.jail = jail;
	}
	
	/** Returns the cell parameter. */
	public String cell() {
		return cell;
	}
}
