package test.java.com.graywolf336.jail;

import junit.framework.Assert;

import org.junit.Test;

import com.graywolf336.jail.command.commands.jewels.Jailing;
import com.graywolf336.jail.command.commands.jewels.Transfer;
import com.lexicalscope.jewel.cli.CliFactory;

public class TestJewelCommands {
	@Test
	public void testJailJewel() {
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
	
	@Test
	public void testTransferForJailAndCell() {
		String[] args = { "-p", "graywolf336", "-j", "hardcore", "-c", "cell_n01" };
		Transfer t = CliFactory.parseArguments(Transfer.class, args);
		
		Assert.assertEquals("The player parsed is not what we expected.", "graywolf336", t.getPlayer());
		Assert.assertEquals("The jail parsed is not what we expected.", "hardcore", t.getJail());
		Assert.assertEquals("The cell parsed is not what we expected.", "cell_n01", t.getCell());
	}
	
	@Test
	public void testTransferForNoCell() {
		String[] args = { "-p", "graywolf336", "-j", "hardcore" };
		Transfer t = CliFactory.parseArguments(Transfer.class, args);
		
		Assert.assertEquals("The player parsed is not what we expected.", "graywolf336", t.getPlayer());
		Assert.assertEquals("The jail parsed is not what we expected.", "hardcore", t.getJail());
		Assert.assertNull("The cell is not null?", t.getCell());
	}
}