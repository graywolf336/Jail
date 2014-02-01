package com.graywolf336.jail.command.jcommands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Checks if a player is jailed and if so gives the sender information about the jailing.")
public class Check {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
}
