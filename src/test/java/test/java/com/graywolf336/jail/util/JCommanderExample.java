package test.java.com.graywolf336.jail.util;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class JCommanderExample {
	@Parameter
	public List<String> parameters = new ArrayList<String>();
	
	@Parameter(names = { "-log", "-verbose" }, description = "Level of verbosity")
	public Integer verbose = 1;

	@Parameter(names = "-groups", description = "Comma-separated list of group names to be run")
	public String groups;
	
	@Parameter(names = "-debug", description = "Debug mode")
	public boolean debug = false;
}
