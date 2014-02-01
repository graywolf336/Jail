package com.graywolf336.jail.command.jcommands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Starts the creation process of creating a jail.")
public class ClearForce {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
}
