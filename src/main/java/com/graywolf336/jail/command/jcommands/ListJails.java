package com.graywolf336.jail.command.jcommands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Lists all the jails in the system.")
public class ListJails {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
}
