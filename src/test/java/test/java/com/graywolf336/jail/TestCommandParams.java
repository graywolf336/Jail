package test.java.com.graywolf336.jail;

import junit.framework.Assert;

import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.graywolf336.jail.command.parameters.JailParameters;

public class TestCommandParams {
	
	@Test
	public void TestJailCommand() {
		JailParameters jail = new JailParameters();
		//"/jail [-p name] (-t time) (-j JailName) (-c CellName) (-m Muted) (-r A reason for jailing)"
		String[] params = { "-p", "graywolf336", "-t", "30", "-j", "den", "-c", "cell_01", "-m", "true", "-r", "He", "was", "a", "very", "bad", "boy." };
		new JCommander(jail, params);
		
		Assert.assertEquals("The player is not the one we provided.", jail.player(), "graywolf336");
		Assert.assertEquals("The time doesn't match what we gave.", jail.time(), "30");
		Assert.assertEquals("The jail is not the one we specified.", jail.jail(), "den");
		Assert.assertEquals("The cell doesn't match up.", jail.cell(), "cell_01");
		Assert.assertEquals("The muted is not true.", jail.muted(), true);
		Assert.assertEquals("Jailed reason didn't match up.", jail.reason(), "He was a very bad boy.");
	}
}
