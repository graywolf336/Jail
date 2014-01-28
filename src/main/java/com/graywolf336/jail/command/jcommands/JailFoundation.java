package com.graywolf336.jail.command.jcommands;

import com.beust.jcommander.Parameter;

public class JailFoundation {
	
	@Parameter(names = { "-v", "-verbose" })
	public boolean verbose = false;
}
