package test.java.com.graywolf336.jail;

import junit.framework.Assert;

import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.graywolf336.jail.command.commands.params.Jailing;

public class TestCommandParams {
	
	@Test
	public void TestJailCommand() {
		Jailing jail = new Jailing();
		//"/jail [-p name] (-t time) (-j JailName) (-c CellName) (-m Muted) (-r A reason for jailing)"
		String[] params = { "-p", "graywolf336", "-t", "30", "-j", "den", "-c", "cell_01", "-m", "-r", "He", "was", "a", "very", "bad", "boy." };
		new JCommander(jail, params);
		
		Assert.assertEquals("The player is not the one we provided.", "graywolf336", jail.player());
		Assert.assertEquals("The time doesn't match what we gave.", "30", jail.time());
		Assert.assertEquals("The jail is not the one we specified.", "den", jail.jail());
		Assert.assertEquals("The cell doesn't match up.", "cell_01", jail.cell());
		Assert.assertEquals("The muted is false.", true, jail.muted());
		Assert.assertEquals("Jailed reason didn't match up.", "He was a very bad boy.", jail.reason());
	}
	
	@Test(expected=ParameterException.class)
	public void TestFailedJailCommand() {
		Jailing jail = new Jailing();
		String[] params = { "-t", "30", "-j", "den", "-c", "cell_01", "-m true", "-r", "He", "was", "a", "very", "bad", "boy." };
		
		new JCommander(jail, params);
	}
}
