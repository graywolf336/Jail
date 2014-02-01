package com.graywolf336.jail.command.jcommands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Unjails all the prisoners from all the jails or the specified jail.")
public class Clear {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
}
