package com.graywolf336.jail.command.jewel;

import java.util.List;

import com.lexicalscope.jewel.cli.Option;

public interface Jailing {
	@Option(shortName={"player", "pl", "p"})
	String getPlayer();
	
	@Option(shortName="c")
	String getCell();
	
	@Option(shortName="r")
	List<String> getReason();
}
