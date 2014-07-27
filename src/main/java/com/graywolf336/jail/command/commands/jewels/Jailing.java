package com.graywolf336.jail.command.commands.jewels;

import java.util.List;

import com.lexicalscope.jewel.cli.Option;

public interface Jailing {

    @Option(longName={"player", "pl"}, shortName="p", description = "the player's name")
    public String getPlayer();

    @Option(longName={"time", "length"}, shortName="t", description = "the amount of time")
    public String getTime();

    @Option(longName={"jail", "prison"}, shortName="j", description = "the jail")
    public String getJail();

    @Option(longName={"cell"}, shortName="c", description = "the cell")
    public String getCell();

    @Option(longName={"anycell"}, shortName="a", description = "decides whether the plugin will pick any open cell")
    public boolean getAnyCell();

    @Option(longName={"muted", "canttalk"}, shortName="m", description = "whether the prisoner is muted or not")
    public boolean getMuted();

    @Option(longName={"reason"}, shortName="r", description = "the reason this player is being jailed")
    public List<String> getReason();

    public boolean isTime();
    public boolean isJail();
    public boolean isCell();
    public boolean isAnyCell();
    public boolean isMuted();
    public boolean isReason();
}
