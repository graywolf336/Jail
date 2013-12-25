package test.java.com.graywolf336.jail;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import com.graywolf336.jail.Util;

public class TestTimeParsing {
	
	@Test
	public void testSeconds() throws Exception {
		assertThat(1000L, is(Util.getTime("1s")));
		assertThat(1000L, is(Util.getTime("1second")));
		assertThat(1000L, is(Util.getTime("1seconds")));
	}
	
	@Test
	public void testMinutes() throws Exception {
		assertThat(60000L, is(Util.getTime("1m")));
		assertThat(60000L, is(Util.getTime("1minute")));
		assertThat(60000L, is(Util.getTime("1minutes")));
	}
	
	@Test
	public void testHours() throws Exception {
		assertThat(3600000L, is(Util.getTime("1h")));
		assertThat(3600000L, is(Util.getTime("1hours")));
	}
	
	@Test
	public void testDays() throws Exception {
		assertThat(86400000L, is(Util.getTime("1d")));
		assertThat(86400000L, is(Util.getTime("1days")));
	}
}
