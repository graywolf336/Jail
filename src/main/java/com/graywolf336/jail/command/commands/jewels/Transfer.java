package com.graywolf336.jail.command.commands.jewels;

import com.lexicalscope.jewel.cli.Option;

public interface Transfer {

    @Option(longName={"player", "pl"}, shortName="p", description = "the player's name")
    public String getPlayer();

    @Option(longName={"jail", "prison"}, shortName="j", description = "the jail")
    public String getJail();

    @Option(longName={"cell"}, shortName="c", description = "the cell")
    public String getCell();

    public boolean isPlayer();
    public boolean isJail();
    public boolean isCell();
}
