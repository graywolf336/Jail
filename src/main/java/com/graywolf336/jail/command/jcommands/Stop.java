package com.graywolf336.jail.command.jcommands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Stop the creations of anything.")
public class Stop {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
}
