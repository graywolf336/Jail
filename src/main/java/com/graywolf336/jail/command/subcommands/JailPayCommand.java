package com.graywolf336.jail.command.subcommands;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.interfaces.IJailPayManager;

@CommandInfo(
        maxArgs = 2,
        minimumArgs = 0,
        needsPlayer = true,
        pattern = "pay",
        permission = "jail.usercmd.jailpay",
        usage = "/jail pay (amount) (name)"
        )
public class JailPayCommand implements Command {
    public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
        if(jm.getPlugin().getConfig().getBoolean(Settings.JAILPAYENABLED.getPath())) {
            IJailPayManager pm = jm.getPlugin().getJailPayManager();

            switch(args.length) {
                case 1:
                    // `/jail pay`
                    //send how much it costs to get out
                    if(jm.isPlayerJailedByLastKnownUsername(sender.getName())) {
                        Prisoner p = jm.getPrisonerByLastKnownName(sender.getName());
                        String amt = "";

                        if(pm.usingItemsForPayment()) {
                            amt = String.valueOf((int) Math.ceil(pm.calculateBill(p)));
                        }else {
                            amt = String.valueOf(pm.calculateBill(p));
                        }

                        if(p.getRemainingTime() > 0) {
                            if(pm.isTimedEnabled()) {
                                sender.sendMessage(Lang.PAYCOST.get(new String[] { pm.getCostPerMinute(), pm.getCurrencyName(), amt }));
                            }else {
                                sender.sendMessage(Lang.PAYNOTENABLED.get());
                                jm.getPlugin().debug("Jail pay 'timed' paying is not enabled (config has 0 as the cost).");
                            }
                        }else {
                            if(pm.isInfiniteEnabled()) {
                                sender.sendMessage(Lang.PAYCOST.get(new String[] { amt, pm.getCurrencyName() }));
                            }else {
                                sender.sendMessage(Lang.PAYNOTENABLED.get());
                                jm.getPlugin().debug("Jail pay 'infinite' paying is not enabled (config has 0 as the cost).");
                            }
                        }
                    }else {
                        sender.sendMessage(Lang.YOUARENOTJAILED.get());
                    }

                    break;
                case 2:
                    // `/jail pay <amount>`
                    //They are trying to pay for their self
                    if(jm.isPlayerJailedByLastKnownUsername(sender.getName())) {
                        Prisoner p = jm.getPrisonerByLastKnownName(sender.getName());

                        if(p.getRemainingTime() > 0) {
                            if(!pm.isTimedEnabled()) {
                                sender.sendMessage(Lang.PAYNOTENABLED.get());
                                return true;
                            }
                        }else {
                            if(!pm.isInfiniteEnabled()) {
                                sender.sendMessage(Lang.PAYNOTENABLED.get());
                                return true;
                            }
                        }

                        if(args[1].startsWith("-")) {
                            sender.sendMessage(Lang.PAYNONEGATIVEAMOUNTS.get());
                        }else {
                            double amt = 0;

                            try {
                                amt = Double.parseDouble(args[1]);
                            }catch(NumberFormatException e) {
                                sender.sendMessage(ChatColor.RED + "<amount> must be a number.");
                                throw e;
                            }

                            if(pm.hasEnoughToPay((Player) sender, amt)) {
                                double bill = pm.calculateBill(p);

                                if(p.getRemainingTime() > 0) {
                                    //timed sentence
                                    if(amt >= bill) {
                                        pm.pay((Player) sender, bill);
                                        sender.sendMessage(Lang.PAYPAIDRELEASED.get(String.valueOf(bill)));
                                        jm.getPlugin().getPrisonerManager().schedulePrisonerRelease(p);
                                    }else {
                                        long minutes = pm.getMinutesPayingFor(amt);
                                        pm.pay((Player) sender, amt);
                                        long remain = p.subtractTime(TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES));
                                        sender.sendMessage(Lang.PAYPAIDLOWEREDTIME.get(new String[] { String.valueOf(amt), String.valueOf(TimeUnit.MINUTES.convert(remain, TimeUnit.MILLISECONDS)) }));
                                    }
                                }else {
                                    //infinite jailing
                                    if(amt >= bill) {
                                        pm.pay((Player) sender, bill);
                                        sender.sendMessage(Lang.PAYPAIDRELEASED.get(String.valueOf(bill)));
                                        jm.getPlugin().getPrisonerManager().schedulePrisonerRelease(p);
                                    }else {
                                        //You haven't provided enough money to get them out
                                        sender.sendMessage(Lang.PAYNOTENOUGHMONEYPROVIDED.get());
                                    }
                                }
                            }else {
                                sender.sendMessage(Lang.PAYNOTENOUGHMONEY.get());
                            }
                        }
                    }else {
                        sender.sendMessage(Lang.YOUARENOTJAILED.get());
                    }
                    break;
                case 3:
                    // `/jail pay <amount> <person>
                    //they are trying to pay for someone else
                    if(jm.isPlayerJailedByLastKnownUsername(sender.getName())) {
                        //When they are jailed they can not pay for someone else
                        sender.sendMessage(Lang.PAYCANTPAYWHILEJAILED.get());
                    }else {
                        if(jm.isPlayerJailedByLastKnownUsername(args[2])) {
                            Prisoner p = jm.getPrisonerByLastKnownName(args[2]);

                            if(p.getRemainingTime() > 0) {
                                if(!pm.isTimedEnabled()) {
                                    sender.sendMessage(Lang.PAYNOTENABLED.get());
                                    return true;
                                }
                            }else {
                                if(!pm.isInfiniteEnabled()) {
                                    sender.sendMessage(Lang.PAYNOTENABLED.get());
                                    return true;
                                }
                            }

                            if(args[1].startsWith("-")) {
                                sender.sendMessage(Lang.PAYNONEGATIVEAMOUNTS.get());
                            }else {
                                double amt = 0;

                                try {
                                    amt = Double.parseDouble(args[1]);
                                }catch(NumberFormatException e) {
                                    sender.sendMessage(ChatColor.RED + "<amount> must be a number.");
                                    throw e;
                                }


                                if(pm.hasEnoughToPay((Player) sender, amt)) {
                                    double bill = pm.calculateBill(p);

                                    if(p.getRemainingTime() > 0) {
                                        //timed sentence
                                        if(amt >= bill) {
                                            pm.pay((Player) sender, bill);
                                            sender.sendMessage(Lang.PAYPAIDRELEASEDELSE.get(new String[] { String.valueOf(bill), p.getLastKnownName() }));
                                            jm.getPlugin().getPrisonerManager().schedulePrisonerRelease(p);
                                        }else {
                                            long minutes = pm.getMinutesPayingFor(amt);
                                            pm.pay((Player) sender, amt);
                                            long remain = p.subtractTime(TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES));
                                            sender.sendMessage(Lang.PAYPAIDLOWEREDTIMEELSE.get(new String[] { String.valueOf(amt), p.getLastKnownName(), String.valueOf(TimeUnit.MINUTES.convert(remain, TimeUnit.MILLISECONDS)) }));
                                        }
                                    }else {
                                        //infinite jailing
                                        if(amt >= bill) {
                                            pm.pay((Player) sender, bill);
                                            sender.sendMessage(Lang.PAYPAIDRELEASEDELSE.get(new String[] { String.valueOf(bill), p.getLastKnownName() }));
                                            jm.getPlugin().getPrisonerManager().schedulePrisonerRelease(p);
                                        }else {
                                            //You haven't provided enough money to get them out
                                            sender.sendMessage(Lang.PAYNOTENOUGHMONEYPROVIDED.get());
                                        }
                                    }
                                }else {
                                    sender.sendMessage(Lang.PAYNOTENOUGHMONEY.get());
                                }
                            }
                        }else {
                            //Person they're trying to pay for is not jailed
                            sender.sendMessage(Lang.NOTJAILED.get(args[2]));
                        }
                    }
                    break;
                default:
                    return false;
            }
        }else {
            jm.getPlugin().debug("Jail pay not enabled.");
            sender.sendMessage(Lang.PAYNOTENABLED.get());
        }

        return true;
    }

    public List<String> provideTabCompletions(JailManager jm, CommandSender sender, String... args) throws Exception {
        //TODO implement
        return Collections.emptyList();
    }
}
