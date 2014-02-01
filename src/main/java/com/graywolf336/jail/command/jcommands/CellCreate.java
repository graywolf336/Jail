package com.graywolf336.jail.command.jcommands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Starts the creation of cells in the specified jail.")
public class CellCreate {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
}
