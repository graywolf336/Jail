package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
        maxArgs = 0,
        minimumArgs = 0,
        needsPlayer = false,
        pattern = "confirm|con",
        permission = "",
        usage = "/jail confirm"
        )
public class JailConfirmCommand implements Command{

    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        //Check if the sender is actually confirming something.
        if(jm.isConfirming(sender.getName())) {
            if(jm.confirmingHasExpired(sender.getName())) {
                //Their confirmation time frame has closed
                sender.sendMessage(Lang.EXPIRED.get());
            }else {
                switch(jm.getWhatIsConfirming(sender.getName())) {
                    case CLEAR:
                        //Copy the original arguments for easy access
                        String[] cArgs = jm.getOriginalArgs(sender.getName());
                        //Clear a jail if the args length is two, else send null
                        String msg = jm.clearJailOfPrisoners(cArgs.length == 2 ? cArgs[1] : null);
                        //Send the message we got back
                        sender.sendMessage(msg);
                        //Remove them from confirming so they can't do it again
                        jm.removeConfirming(sender.getName());
                        break;
                    case CLEARFORCE:
                        //Copy the original arguments for easy access
                        String[] cArgs2 = jm.getOriginalArgs(sender.getName());
                        //Forcefully clear a jail if the args length is two, else send null to clear all
                        String msg2 = jm.forcefullyClearJailOrJails(cArgs2.length == 2 ? cArgs2[1] : null);
                        //Send the message we got back
                        sender.sendMessage(msg2);
                        jm.removeConfirming(sender.getName());
                        break;
                    case DELETECELL:
                        //Copy the original arguments for easy access
                        String[] cArgs3 = jm.getOriginalArgs(sender.getName());
                        //delete a cell from a jail with the given arguments
                        String msg3 = jm.deleteJailCell(cArgs3[1], cArgs3[2]);
                        //Send the message we got back
                        sender.sendMessage(msg3);
                        jm.removeConfirming(sender.getName());
                        break;
                    case DELETECELLS:
                        //Copy the original arguments for easy access
                        String[] cArgs4 = jm.getOriginalArgs(sender.getName());
                        //delete a cell from a jail with the given arguments
                        String[] msgs4 = jm.deleteAllJailCells(cArgs4[1]);
                        //Send the messages we got back
                        for(String s : msgs4) {
                            sender.sendMessage(s);
                        }

                        jm.removeConfirming(sender.getName());
                        break;
                    case DELETE:
                        //Copy the original arguments for easy access
                        String[] cArgs5 = jm.getOriginalArgs(sender.getName());
                        //delete a cell from a jail with the given arguments
                        String msg5 = jm.deleteJail(cArgs5[1]);
                        //Send the message we got back
                        sender.sendMessage(msg5);
                        jm.removeConfirming(sender.getName());
                        break;
                    default:
                        sender.sendMessage(Lang.NOTHING.get());
                        jm.removeConfirming(sender.getName());
                        break;
                }
            }
        }else {
            //They aren't confirming anything right now.
            sender.sendMessage(Lang.NOTHING.get());
        }

        return true;
    }

}
