package com.graywolf336.jail.command.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = -1,
        minimumArgs = 2,
        needsPlayer = false,
        pattern = "signs",
        permission = "jail.command.jailsigns",
        usage = "/jail signs [clean|refresh|update|verify] [jail] (cell)..."
        )
public class JailSignsCommand implements Command {
    private static final String[] options =  new String[] { "clean", "refresh", "update", "verify" };

    public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
        if(!jm.isValidJail(args[2])) {
            sender.sendMessage(" " + Lang.NOJAIL.get(args[1]));
            return true;
        }

        Jail j = jm.getJail(args[2]);

        HashMap<String, List<String>> items = new HashMap<String, List<String>>();

        if(args[1].equalsIgnoreCase("clean")) {
            //if they type clean, we can remove all signs which are no longer signs
            //then provide them a list of signs which got cleaned up

            for(Cell c : j.getCells()) {
                if(Util.isStringInsideArray(c.getName(), args) || args.length == 3) {
                    if(c.hasSigns()) {
                        List<String> cleaned = c.cleanSigns();
                        if(!cleaned.isEmpty()) {
                            items.put(c.getName(), cleaned);
                        }
                    }
                }
            }

            if(items.isEmpty()) {
                sender.sendMessage(Lang.NOINVALIDSIGNS.get());
            }else {
                sender.sendMessage(Lang.CLEANEDSIGNS.get());
                for(Entry<String, List<String>> e : items.entrySet()) {
                    sender.sendMessage("  " + e.getKey());
                    for(String s : e.getValue())
                        sender.sendMessage("    Sign: " + s);
                }
            }

            return true;
        }else if(args[1].equalsIgnoreCase("refresh") || args[1].equalsIgnoreCase("update")) {
            //if they type refresh, we will go through all the signs and update them to
            //display the correct message on the sign whether empty or time

            int cells = 0;
            int updated = 0;
            int removed = 0;

            for(Cell c : j.getCells()) {
                if(Util.isStringInsideArray(c.getName(), args) || args.length == 3) {
                    HashMap<String, List<String>> results = c.updateSigns();
                    updated += results.get("updated").size();
                    removed += results.get("removed").size();
                    cells++;
                }
            }

            sender.sendMessage(Lang.SIGNSREFRESHED.get(new String[] { String.valueOf(updated), String.valueOf(removed), String.valueOf(cells) }));
            return true;
        }else if(args[1].equalsIgnoreCase("verify")) {
            //if they type verify, we will go through all the signs and then provide them
            //as a list of cells which have signs that aren't actually signs and provide
            //the location where the signs are

            for(Cell c : j.getCells()) {
                if(Util.isStringInsideArray(c.getName(), args) || args.length == 3) {
                    List<String> invalids = c.getInvalidSigns();
                    if(!invalids.isEmpty()) {
                        items.put(c.getName(), invalids);
                    }
                }
            }

            if(items.isEmpty()) {
                sender.sendMessage(Lang.NOINVALIDSIGNS.get());
            }else {
                sender.sendMessage(Lang.INVALIDSIGNS.get());
                for(Entry<String, List<String>> e : items.entrySet()) {
                    sender.sendMessage("  " + e.getKey());
                    for(String s : e.getValue())
                        sender.sendMessage("    " + Lang.SIGN.get() + ": " + s);
                }
            }
            return true;
        }else
            return false;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        List<String> results = new ArrayList<String>();

        switch(args.length) {
            case 2:
                for(String s : options)
                    if(args[1].isEmpty() || StringUtil.startsWithIgnoreCase(s, args[1]))
                        results.add(s);
                break;
            case 3:
                results.addAll(jm.getJailsByPrefix(args[2]));
                break;
            default:
                if(jm.isValidJail(args[2]))
                    for(Cell c : jm.getJail(args[2]).getCells())
                        if(!Util.isStringInsideArray(c.getName(), args))
                            results.add(c.getName());
                break;
        }

        Collections.sort(results);

        return results;
    }
}
