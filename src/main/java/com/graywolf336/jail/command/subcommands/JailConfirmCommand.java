package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

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
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.EXPIRED));
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
					default:
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTHING));
						jm.removeConfirming(sender.getName());
						break;
				}
			}
		}else {
			//They aren't confirming anything right now.
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTHING));
		}
		
		return true;
	}

}
