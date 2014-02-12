package test.java.com.graywolf336.jail;

import junit.framework.Assert;

import org.junit.Test;

import com.graywolf336.jail.command.commands.jewels.Jailing;
import com.lexicalscope.jewel.cli.CliFactory;

public class JewelCommands {
	@Test
	public void testJewel() {
		String[] args = { "--player", "graywolf336", "-c", "testing", "-r", "This", "is", "a", "reason" };
		Jailing j = CliFactory.parseArguments(Jailing.class, args);
		
		Assert.assertEquals("graywolf336", j.getPlayer());
		Assert.assertEquals("testing", j.getCell());
		
		StringBuilder sb = new StringBuilder();
		for(String s : j.getReason()) {
			sb.append(s).append(' ');
		}
		
		sb.deleteCharAt(sb.length() - 1);
		
		Assert.assertEquals("This is a reason", sb.toString());
	}
}