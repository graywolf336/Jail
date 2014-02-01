package com.graywolf336.jail.command.jcommands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Lists all the cells in a certain jail.")
public class ListCells {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
}
