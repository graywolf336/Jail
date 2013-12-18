package test.java.com.graywolf336.jail;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class TestJCommander {
	@Parameter
	public List<String> parameters = new ArrayList<String>();
	
	@Parameter(names = { "-log", "-verbose" }, description = "Level of verbosity")
	public Integer verbose = 1;

	@Parameter(names = "-groups", description = "Comma-separated list of group names to be run")
	public String groups;
	
	@Parameter(names = "-debug", description = "Debug mode")
	public boolean debug = false;
	
	@Test
	public void testJCommander() {
		String[] args = { "-log", "2", "-groups", "unit" };
		new JCommander(this, args);
		
		Assert.assertEquals(this.verbose.intValue(), 2);
		Assert.assertEquals(this.groups.toLowerCase(), "unit");
	}
}
