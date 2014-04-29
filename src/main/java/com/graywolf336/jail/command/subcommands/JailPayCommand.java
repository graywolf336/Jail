package com.graywolf336.jail.command.subcommands;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.JailPayManager;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;

@CommandInfo(
		maxArgs = 2,
		minimumArgs = 0,
		needsPlayer = true,
		pattern = "pay",
		permission = "jail.usercmd.jailpay",
		usage = "/jail pay <amount> <player>"
	)
public class JailPayCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		if(jm.getPlugin().getConfig().getBoolean(Settings.JAILPAYENABLED.getPath())) {
			JailPayManager pm = jm.getPlugin().getJailPayManager();
			
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
								sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYCOST, new String[] { pm.getCostPerMinute(), pm.getCurrencyName(), amt }));
							}else {
								sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENABLED));
								jm.getPlugin().debug("Jail pay 'timed' paying is not enabled (config has 0 as the cost).");
							}
						}else {
							if(pm.isInfiniteEnabled()) {
								sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYCOST, new String[] { amt, pm.getCurrencyName() }));
							}else {
								sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENABLED));
								jm.getPlugin().debug("Jail pay 'infinite' paying is not enabled (config has 0 as the cost).");
							}
						}
					}else {
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.YOUARENOTJAILED));
					}
					
					break;
				case 2:
					// `/jail pay <amount>`
					//They are trying to pay for their self
					if(jm.isPlayerJailedByLastKnownUsername(sender.getName())) {
						Prisoner p = jm.getPrisonerByLastKnownName(sender.getName());
						
						if(p.getRemainingTime() > 0) {
							if(!pm.isTimedEnabled()) {
								sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENABLED));
								return true;
							}
						}else {
							if(!pm.isInfiniteEnabled()) {
								sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENABLED));
								return true;
							}
						}
						
						if(args[1].startsWith("-")) {
							sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNONEGATIVEAMOUNTS));
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
										sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYPAIDRELEASED, String.valueOf(bill)));
										jm.getPlugin().getPrisonerManager().releasePrisoner((Player) sender, p);
									}else {
										long minutes = pm.getMinutesPayingFor(amt);
										pm.pay((Player) sender, amt);
										long remain = p.subtractTime(TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES));
										sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYPAIDLOWEREDTIME, 
												new String[] { String.valueOf(amt), String.valueOf(TimeUnit.MINUTES.convert(remain, TimeUnit.MILLISECONDS)) }));
									}
								}else {
									//infinite jailing
									if(amt >= bill) {
										pm.pay((Player) sender, bill);
										sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYPAIDRELEASED, String.valueOf(bill)));
										jm.getPlugin().getPrisonerManager().releasePrisoner((Player) sender, p);
									}else {
										//You haven't provided enough money to get them out
										sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENOUGHMONEYPROVIDED));
									}
								}
							}else {
								sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENOUGHMONEY));
							}
						}
					}else {
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.YOUARENOTJAILED));
					}
					break;
				case 3:
					// `/jail pay <amount> <person>
					//they are trying to pay for someone else
					if(jm.isPlayerJailedByLastKnownUsername(sender.getName())) {
						//When they are jailed they can not pay for someone else
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYCANTPAYWHILEJAILED));
					}else {
						if(jm.isPlayerJailedByLastKnownUsername(args[2])) {
							Prisoner p = jm.getPrisonerByLastKnownName(args[2]);
							
							if(p.getRemainingTime() > 0) {
								if(!pm.isTimedEnabled()) {
									sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENABLED));
									return true;
								}
							}else {
								if(!pm.isInfiniteEnabled()) {
									sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENABLED));
									return true;
								}
							}
							
							if(args[1].startsWith("-")) {
								sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNONEGATIVEAMOUNTS));
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
											sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYPAIDRELEASEDELSE, new String[] { String.valueOf(bill), p.getLastKnownName() }));
											jm.getPlugin().getPrisonerManager().releasePrisoner(jm.getPlugin().getServer().getPlayer(p.getUUID()), p);
										}else {
											long minutes = pm.getMinutesPayingFor(amt);
											pm.pay((Player) sender, amt);
											long remain = p.subtractTime(TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES));
											sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYPAIDLOWEREDTIMEELSE, 
													new String[] { String.valueOf(amt), p.getLastKnownName(), String.valueOf(TimeUnit.MINUTES.convert(remain, TimeUnit.MILLISECONDS)) }));
										}
									}else {
										//infinite jailing
										if(amt >= bill) {
											pm.pay((Player) sender, bill);
											sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYPAIDRELEASEDELSE, new String[] { String.valueOf(bill), p.getLastKnownName() }));
											jm.getPlugin().getPrisonerManager().releasePrisoner(jm.getPlugin().getServer().getPlayer(p.getUUID()), p);
										}else {
											//You haven't provided enough money to get them out
											sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENOUGHMONEYPROVIDED));
										}
									}
								}else {
									sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENOUGHMONEY));
								}
							}
						}else {
							//Person they're trying to pay for is not jailed
							sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTJAILED, args[2]));
						}
					}
					break;
				default:
					return false;
			}
		}else {
			jm.getPlugin().debug("Jail pay not enabled.");
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PAYNOTENABLED));
		}
		
		return true;
	}
}

/*
 * 
Messages:
  MessageJailPayAmountForever: To get out of this mess, you will have to pay <Amount>.
  JailPayCannotPay: Sorry, money won't help you this time.
  JailPayCannotPayHim: Sorry, money won't help him this time.
  JailPayNotEnoughMoney: You don't have that much money!
  JailPayCost: 1 minute of your sentence will cost you <MinutePrice>. That means that cost for releasing you out of the jail is <WholePrice>.
  JailPayPaidReleased: You have just payed <Amount> and saved yourself from the jail!
  JailPayPaidReleasedHim: You have just payed <Amount> and saved <Prisoner> from the jail!
  JailPayLoweredTime: You have just payed <Amount> and lowered your sentence to <NewTime> minutes!
  JailPayLoweredTimeHim: You have just payed <Amount> and lowered <Prisoner>'s sentence to <NewTime> minutes!
JailPay:
  PricePerMinute: 10
  PriceForInfiniteJail: 9999
  Currency: 0
 */