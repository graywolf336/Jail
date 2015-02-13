package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.JailVoteManager;
import com.graywolf336.jail.beans.JailVote;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.enums.Settings;

@CommandInfo(
        maxArgs = 2,
        minimumArgs = 1,
        needsPlayer = true,
        pattern = "vote|v",
        permission = "jail.usercmd.jailvote",
        usage = "/jail vote [player] (yes|no)"
        )
public class JailVoteCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) {
        if(jm.getPlugin().getConfig().getBoolean(Settings.JAILVOTEENABLED.getPath()) && jm.getPlugin().getJailVoteManager() != null && !jm.getJails().isEmpty()) {
            Player p = (Player) sender;
            JailVoteManager jvm = jm.getPlugin().getJailVoteManager();
            
            switch(args.length) {
                case 2:
                    if(jvm.isVotedFor(args[1])) {
                        if(jvm.hasVotedAlready(args[1], p.getUniqueId())) {
                            sender.sendMessage(Lang.VOTEALREADYVOTEDFOR.get(args[1]));
                        }else {
                            if(jvm.addVote(args[1], p.getUniqueId(), true)) {
                                sender.sendMessage(Lang.VOTEYESSUCCESS.get(args[1]));
                            }else {
                                sender.sendMessage(Lang.VOTEUNSUCCESSFUL.get());
                            }
                        }
                    }else if(sender.hasPermission("jail.vote.start")) {
                        Player voteAgainst = jm.getPlugin().getServer().getPlayer(args[1]);  
                        if(voteAgainst == null) {
                            sender.sendMessage(Lang.PLAYERNOTONLINE.get());
                        }else {
                            if(voteAgainst.hasPermission("jail.cantbejailed")) {
                                sender.sendMessage(Lang.CANTBEJAILED.get());
                            }else {
                                jvm.addVote(new JailVote(voteAgainst.getName()));
                                jvm.addVote(voteAgainst.getName(), p.getUniqueId(), true);
                                
                                jm.getPlugin().getServer().broadcastMessage(Lang.VOTEBROADCASTHEADER.get());
                                jm.getPlugin().getServer().broadcastMessage(Lang.VOTEBROADCASTLINE1.get(new String[] { sender.getName(), args[1] }));
                                jm.getPlugin().getServer().broadcastMessage(Lang.VOTEBROADCASTLINE2.get(voteAgainst.getName()));
                                jm.getPlugin().getServer().broadcastMessage(Lang.VOTEBROADCASTLINE3.get(voteAgainst.getName()));
                                jm.getPlugin().getServer().broadcastMessage(Lang.VOTEBROADCASTLINE4.get(jvm.getTimerLengthDescription()));
                                jm.getPlugin().getServer().broadcastMessage(Lang.VOTEBROADCASTFOOTER.get());
                                
                                jvm.scheduleCalculating(args[1]);
                            }
                        }
                    }else {
                        jm.getPlugin().debug(sender.getName() + " tried to start a vote to jail someone but didn't have permission, jail.vote.start");
                        sender.sendMessage(Lang.VOTENOPERMISSIONTOSTART.get(args[1]));
                    }
                    break;
                case 3:
                    String name = args[1];
                    
                    if(jvm.isVotedFor(name)) {
                        if(jvm.hasVotedAlready(args[1], p.getUniqueId())) {
                            sender.sendMessage(Lang.VOTEALREADYVOTEDFOR.get(args[1]));
                        }else {
                            if(args[2].equalsIgnoreCase("yes")) {
                                jvm.addVote(args[1], p.getUniqueId(), true);
                                sender.sendMessage(Lang.VOTEYESSUCCESS.get(args[1]));
                            }else {
                                jvm.addVote(args[1], p.getUniqueId(), false);
                                sender.sendMessage(Lang.VOTENOSUCCESS.get(args[1]));
                            }
                        }
                    }else {
                        sender.sendMessage(Lang.VOTENOVOTEFORTHATPLAYER.get(name));
                    }
                    break;
                default:
                    return false;
            }
        }else {
            sender.sendMessage(Lang.VOTENOTENABLED.get());
            
            if(jm.getPlugin().getJailVoteManager() == null) {
                jm.getPlugin().getLogger().severe("Jail Vote Manager didn't load correctly, it is null.");
            }else if(jm.getJails().isEmpty()) {
                jm.getPlugin().getLogger().severe("There are no jails, Jail Vote needs a Jail to work.");
            }
        }
        
        return true;
    }
}
