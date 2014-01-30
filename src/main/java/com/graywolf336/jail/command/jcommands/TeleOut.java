package com.graywolf336.jail.command.jcommands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Teleports the player to the teleport out location.")
public class TeleOut {
	@Parameter
	private List<String> parameters = new ArrayList<String>();
}
