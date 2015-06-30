package com.graywolf336.jail.command.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.command.commands.jewels.Transfer;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.events.PrePrisonerTransferredEvent;
import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;

@CommandInfo(
        maxArgs = 6,
        minimumArgs = 2,
        needsPlayer = false,
        pattern = "transfer|trans",
        permission = "jail.command.jailtransfer",
        usage = "/jail transfer [-p player] (-j jail) (-c cell)"
        )
public class JailTransferCommand implements Command {
    private List<String> commands = Arrays.asList(new String[] { "p", "j", "c" });
    
    public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
        if(jm.getJails().isEmpty()) {
            sender.sendMessage(Lang.NOJAILS.get());
            return true;
        }

        //Convert to a List<String> so we can edit the list
        List<String> arguments = new LinkedList<String>(Arrays.asList(args));
        //remove the first argument of "transfer"
        arguments.remove(0);

        //Parse the command
        Transfer params = null;

        try {
            params = CliFactory.parseArguments(Transfer.class, arguments.toArray(new String[arguments.size()]));
        }catch(ArgumentValidationException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return true;
        }

        //Verify they gave us a player and if so check if they're jailed
        if(params.getPlayer() == null) {
            sender.sendMessage(Lang.PROVIDEAPLAYER.get(Lang.TRANSFERRING));
            return true;
        }else if(!jm.isPlayerJailedByLastKnownUsername(params.getPlayer())) {
            sender.sendMessage(Lang.NOTJAILED.get(params.getPlayer()));
            return true;
        }

        jm.getPlugin().debug("Checking everything before we transfer: " + params.getPlayer());

        //If they didn't provide a jail, tell them we need one
        if(params.getJail() == null) {
            sender.sendMessage(Lang.PROVIDEAJAIL.get(Lang.TRANSFERRING));
            return true;
        }else if(!jm.isValidJail(params.getJail())) {
            //Check if the jail they did provided is not a valid jail
            sender.sendMessage(Lang.NOJAIL.get(params.getJail()));
            return true;
        }

        jm.getPlugin().debug("They provided a valid jail, so let's check the target cell.");

        Jail target = jm.getJail(params.getJail());
        Cell targetCell = null;

        //Check if they provided a cell and if so does it exist
        if(params.getCell() != null) {
            if(target.getCell(params.getCell()) == null) {
                sender.sendMessage(Lang.NOCELL.get(new String[] { params.getCell(), params.getJail() }));
                return true;
            }else {
                //Store the cell for easy of access and also check if it already is full
                targetCell = target.getCell(params.getCell());
                if(targetCell.hasPrisoner()) {
                    //If the cell has a prisoner, don't allow jailing them to that particular cell
                    sender.sendMessage(Lang.CELLNOTEMPTY.get(params.getCell()));

                    //But suggest the first empty cell we find
                    Cell suggestedCell = jm.getJail(params.getCell()).getFirstEmptyCell();
                    if(suggestedCell != null) {
                        sender.sendMessage(Lang.SUGGESTEDCELL.get(new String[] { params.getCell(), suggestedCell.getName() }));
                    }else {
                        sender.sendMessage(Lang.NOEMPTYCELLS.get(params.getCell()));
                    }

                    return true;
                }
            }
        }

        jm.getPlugin().debug("Calling the PrePrisonerTransferredEvent, jail and cell check all came out clean.");
        Prisoner p = jm.getPrisonerByLastKnownName(params.getPlayer());

        //Throw the custom event before transferring them, allowing another plugin to cancel it.
        PrePrisonerTransferredEvent event = new PrePrisonerTransferredEvent(jm.getJailPlayerIsIn(p.getUUID()),
                jm.getJailPlayerIsIn(p.getUUID()).getCellPrisonerIsIn(p.getUUID()),
                target, targetCell, p, jm.getPlugin().getServer().getPlayer(p.getUUID()), sender.getName());
        jm.getPlugin().getServer().getPluginManager().callEvent(event);

        if(event.isCancelled()) {
            if(event.getCancelledMessage().isEmpty()) {
                //The plugin didn't provide a cancel message/reason so send the default one
                sender.sendMessage(Lang.TRANSFERCANCELLEDBYANOTHERPLUGIN.get(params.getPlayer()));
            }else {
                sender.sendMessage(event.getCancelledMessage());
            }

            return true;
        }

        //Start the transferring of the prisoner
        jm.getPlugin().getPrisonerManager().transferPrisoner(jm.getJailPlayerIsIn(p.getUUID()),
                jm.getJailPlayerIsIn(p.getUUID()).getCellPrisonerIsIn(p.getUUID()),
                target, targetCell, p);

        //Send the messages to the sender, if no cell then say that but if cell send that as well
        if(targetCell == null) {
            sender.sendMessage(Lang.TRANSFERCOMPLETENOCELL.get(new String[] { params.getPlayer(), target.getName() }));
        }else {
            sender.sendMessage(Lang.TRANSFERCOMPLETECELL.get(new String[] { params.getPlayer(), target.getName(), targetCell.getName() }));
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        //by the time it gets to this command it'll have at least two arguments
        String last = args[args.length - 1];
        
        if(last.isEmpty() || !commands.contains(last.replace("-", ""))) {
            //the current part is empty. Need to look at their previous
            //item and if it is a valid option, then provide them a valid tab complete option
            if(args.length - 2 > -1) {
                String previous = args[args.length - 2];
                jm.getPlugin().debug("args[args.length - 2]: " + previous);
                
                if(previous.equalsIgnoreCase("-p")) return getPlayers(jm, last);
                else if(previous.equalsIgnoreCase("-j")) return jm.getJailsByPrefix(last);
                else if(previous.equalsIgnoreCase("-c")) {
                    //Since we need to give them a list of the cells in a jail
                    //we need to get the jail they're giving
                    int jailIndex = ArrayUtils.indexOf(args, "-j");
                    if(jailIndex != -1) {
                        String jail = args[jailIndex + 1];
                        jm.getPlugin().debug("The jail is: " + jail);
                        if(jm.isValidJail(jail)) return getCells(jm, jail, last);
                    }
                }else if(!commands.contains(args[args.length - 2].replace("-", ""))) return Util.getUnusedItems(commands, args, true);
            }
        }else if(last.equalsIgnoreCase("-")) {
            //add some smart checking so that it only returns a list of what isn't already
            //in the command :)
            return Util.getUnusedItems(commands, args, true);
        }else {
            return getPlayers(jm, last);
        }
        
        return Collections.emptyList();
    }
    
    private List<String> getPlayers(JailManager jm, String first) {
        List<String> results = new ArrayList<String>();
        
        for(Player p : jm.getPlugin().getServer().getOnlinePlayers())
            if(first.isEmpty() || StringUtil.startsWithIgnoreCase(p.getName(), first))
                results.add(p.getName());
        
        Collections.sort(results);
        
        return results;
    }
    
    private List<String> getCells(JailManager jm, String jail, String cell) {
        List<String> results = new ArrayList<String>();
        
        for(Cell c : jm.getJail(jail).getCells())
            if(!c.hasPrisoner())
                if(cell.isEmpty() || StringUtil.startsWithIgnoreCase(c.getName(), cell))
                    results.add(c.getName());
        
        Collections.sort(results);
        
        return results;
    }
}
