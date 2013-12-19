package com.graywolf336.jail.command.parameters;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

/**
 * Contains all the parameters from the jail command.
 * 
 * @author graywolf336
 * @version 1.0.1
 * @since 3.0.0
 */
public class JailParameters {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
	
	@Parameter(names = { "-p", "-player", "-prisoner" }, description = "The name of the player we are jailing.")
	private String player = "";
	
	@Parameter(names = { "-t", "-time", "-length" }, description = "The length of the jailing sentence.")
	private String time = "";
	
	@Parameter(names = { "-j", "-jail", "-prison" }, description = "The jail we are sending the player to.")
	private String jail = "";
	
	@Parameter(names = { "-c", "-cell" }, description = "The cell in the jail we are sending them to.")
	private String cell = "";
	
	@Parameter(names = { "-m", "-muted" }, description = "Whether they can talk or not.")
	private boolean muted = false;
	
	@Parameter(names = { "-r", "-reason" }, description = "The reason this player is being jailed for.", variableArity = true)
	private List<String> reason = new ArrayList<String>();
	
	/** Returns the parameters. */
	public List<String> parameters() {
		return parameters;
	}
	
	/** Returns the player parameter. */
	public String player() {
		return player;
	}
	
	/** Returns the time parameter. */
	public String time() {
		return time;
	}
	
	/** Returns the jail parameter. */
	public String jail() {
		return jail;
	}
	
	/** Returns the cell parameter. */
	public String cell() {
		return cell;
	}
	
	/** Returns the muted parameter. */
	public boolean muted() {
		return muted;
	}
	
	/** Returns the reason compressed into one string. */
	public String reason() {
		String r = "";
		
		for(String s : reason) {
			if(r.isEmpty()) {
				r = s;
			}else {
				r += " " + s;
			}
		}
		
		return r;
	}
}
