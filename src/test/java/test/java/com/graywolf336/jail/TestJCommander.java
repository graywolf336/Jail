package test.java.com.graywolf336.jail;

import junit.framework.Assert;

import org.junit.Test;

import com.beust.jcommander.JCommander;

import test.java.com.graywolf336.jail.util.JCommanderExample;

public class TestJCommander {
	@Test
	public void testIt() {
		JCommanderExample jce = new JCommanderExample();
		String[] args = { "-log", "2", "-groups", "unit" };
		new JCommander(jce, args);
		
		Assert.assertEquals(jce.verbose.intValue(), 2);
		Assert.assertEquals(jce.groups.toLowerCase(), "unit");
	}
}
