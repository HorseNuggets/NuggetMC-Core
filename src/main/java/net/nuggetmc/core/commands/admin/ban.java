package net.nuggetmc.core.commands.admin;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerPreLoginEvent;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.Checks;
import net.nuggetmc.core.util.TimeConverter;

public class ban implements CommandExecutor {
	
	private Main plugin;
	
	public ban(Main plugin) {
		this.plugin = plugin;
	}
	
	/*
	 * https://bukkit.org/threads/how-to-temp-ban-ban-a-player-using-just-one-line.432451/
	 * do banlist with custom dates and everything
	 * include ipban as a separate command
	 * 
	 * FIX nullpointerexception when player is offline
	 * report bans in staffchat
	 * also keep logs of them
	 * report pardons in staffchat as well
	 * 
	 */
	
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label.toLowerCase()) {
		case "ban":
			if (args.length >= 1) {
				
				String reason = "No reason specified";
				int bantime = TimeConverter.stringToInt("30d");
				
				if (args.length >= 2) {
					
					String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
					String[] ultraSubArgs = Arrays.copyOfRange(args, 1, args.length);
					
					String timeResult = "";
					String reasonResult = "";
					
					for (int i = 0; i < subArgs.length; i++) {
						char end = subArgs[i].charAt(subArgs[i].length() - 1);
						if (end == 's' || end == 'm' || end == 'h' || end == 'd') {
							String numtest = subArgs[i].substring(0, subArgs[i].length() - 1);
							
							if (isInteger(numtest)) {
								timeResult = timeResult + subArgs[i] + " ";
								continue;
							}
						}
						else {
							ultraSubArgs = Arrays.copyOfRange(subArgs, i, subArgs.length);
							break;
						}
					}
					
					for (int i = 0; i < ultraSubArgs.length; i++) {
						reasonResult = reasonResult + ultraSubArgs[i] + " ";
					}
					
					if (timeResult.endsWith(" ")) {
						timeResult = timeResult.substring(0, timeResult.length() - 1);
					}
					if (reasonResult.endsWith(" ")) {
						reasonResult = reasonResult.substring(0, reasonResult.length() - 1);
					}
					if (!timeResult.equals("")) {
						bantime = TimeConverter.stringToInt(timeResult);
					}
					if (!reasonResult.equals("")) {
						reason = reasonResult;
					}
				}
				
				BanList list = Bukkit.getBanList(BanList.Type.NAME);
				Date date = new Date(System.currentTimeMillis());
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.SECOND, bantime);
				Date expiration = calendar.getTime();
				
				list.addBan(args[0], reason, expiration, sender.getName());
				
				Player bannedPlayer = Bukkit.getPlayer(args[0]);
				String bannedPlayerName = args[0];
				
				if (bannedPlayer != null) {
					bannedPlayerName = bannedPlayer.getName();
					if (bannedPlayer.isOnline()) {
						bannedPlayer.kickPlayer(banmsg(reason, expiration));
					}
				}
				else {
					OfflinePlayer bannedOfflinePlayer = Bukkit.getOfflinePlayer(args[0]);
					if (bannedOfflinePlayer != null) {
						bannedPlayerName = bannedOfflinePlayer.getName();
					}
				}
				
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (Checks.checkStaff(all)) {
						all.sendMessage(sender.getName() + " banned player " + bannedPlayerName + ChatColor.WHITE + " with reason [" + ChatColor.YELLOW
								+ reason + ChatColor.WHITE + "] and time" + ChatColor.YELLOW + TimeConverter.intToString(bantime) + ChatColor.WHITE + ".");
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "Usage: /ban <player> <time> <reason>");
			}
			break;
			
		case "banlist":
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				BanList list = Bukkit.getBanList(BanList.Type.NAME);
				Set<BanEntry> entries = list.getBanEntries();
				sender.sendMessage("There are " + entries.size() + " total banned players:");
				
				for (BanEntry entry : entries) {
					String name = entry.getTarget();
					String reason = entry.getReason();
					String by = entry.getSource();
					Date expiration = entry.getExpiration();
					
					int bantime = (int) ((expiration.getTime() - System.currentTimeMillis()) / 1000);
					String bantimestr = TimeConverter.intToString(bantime);
					if (bantimestr.startsWith(" ")) {
						bantimestr = bantimestr.substring(1);
					}
					
					sender.sendMessage(name + " (by " + ChatColor.YELLOW + by + ChatColor.RESET + ") [" + ChatColor.YELLOW + reason
							+ ChatColor.RESET + "] (" + ChatColor.YELLOW + bantimestr + ChatColor.RESET + ")");
				}
			});
			break;
			
		case "ban-ip":
			if (args.length >= 1) {
				
				String reason = "No reason specified";
				int bantime = TimeConverter.stringToInt("30d");
				
				if (args.length >= 2) {
					
					String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
					String[] ultraSubArgs = Arrays.copyOfRange(args, 1, args.length);
					
					String timeResult = "";
					String reasonResult = "";
					
					for (int i = 0; i < subArgs.length; i++) {
						char end = subArgs[i].charAt(subArgs[i].length() - 1);
						if (end == 's' || end == 'm' || end == 'h' || end == 'd') {
							String numtest = subArgs[i].substring(0, subArgs[i].length() - 1);
							
							if (isInteger(numtest)) {
								timeResult = timeResult + subArgs[i] + " ";
								continue;
							}
						}
						else {
							ultraSubArgs = Arrays.copyOfRange(subArgs, i, subArgs.length);
							break;
						}
					}
					
					for (int i = 0; i < ultraSubArgs.length; i++) {
						reasonResult = reasonResult + ultraSubArgs[i] + " ";
					}
					
					if (timeResult.endsWith(" ")) {
						timeResult = timeResult.substring(0, timeResult.length() - 1);
					}
					if (reasonResult.endsWith(" ")) {
						reasonResult = reasonResult.substring(0, reasonResult.length() - 1);
					}
					if (!timeResult.equals("")) {
						bantime = TimeConverter.stringToInt(timeResult);
					}
					if (!reasonResult.equals("")) {
						reason = reasonResult;
					}
				}
				
				BanList list = Bukkit.getBanList(BanList.Type.IP);
				Date date = new Date(System.currentTimeMillis());
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.SECOND, bantime);
				Date expiration = calendar.getTime();
				
				list.addBan(args[0], reason, expiration, sender.getName());
				
				Player bannedPlayer = Bukkit.getPlayer(args[0]);
				String bannedPlayerName = args[0];
				
				if (bannedPlayer != null) {
					bannedPlayerName = bannedPlayer.getName();
					if (bannedPlayer.isOnline()) {
						bannedPlayer.kickPlayer(banmsg(reason, expiration));
					}
				}
				else {
					OfflinePlayer bannedOfflinePlayer = Bukkit.getOfflinePlayer(args[0]);
					if (bannedOfflinePlayer != null) {
						bannedPlayerName = bannedOfflinePlayer.getName();
					}
				}
				
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (Checks.checkStaff(all)) {
						all.sendMessage(sender.getName() + " IP-banned " + bannedPlayerName + ChatColor.WHITE + " with reason [" + ChatColor.YELLOW
								+ reason + ChatColor.WHITE + "] and time" + ChatColor.YELLOW + TimeConverter.intToString(bantime) + ChatColor.WHITE + ".");
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "Usage: /ban-ip <player> <time> <reason>");
			}
			break;
			
		case "ipbanlist":
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				BanList list = Bukkit.getBanList(BanList.Type.IP);
				Set<BanEntry> entries = list.getBanEntries();
				sender.sendMessage("There are " + entries.size() + " total IP-banned players:");
				
				for (BanEntry entry : entries) {
					String name = entry.getTarget();
					String reason = entry.getReason();
					String by = entry.getSource();
					Date expiration = entry.getExpiration();
					
					int bantime = (int) ((expiration.getTime() - System.currentTimeMillis()) / 1000);
					String bantimestr = TimeConverter.intToString(bantime);
					if (bantimestr.startsWith(" ")) {
						bantimestr = bantimestr.substring(1);
					}
					
					sender.sendMessage(name + " (by " + ChatColor.YELLOW + by + ChatColor.RESET + ") [" + ChatColor.YELLOW + reason
							+ ChatColor.RESET + "] (" + ChatColor.YELLOW + bantimestr + ChatColor.RESET + ")");
				}
			});
			break;
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private static String banmsg(String reason, Date expiration) {
		int bantime = (int) ((expiration.getTime() - System.currentTimeMillis()) / 1000);
		
		String result = ChatColor.RED + "You have been temporarily banned from the network."
				+ "\n\n" + ChatColor.WHITE + "Reason: " + ChatColor.YELLOW + reason
				+ "\n\n" + ChatColor.WHITE + "You will be unbanned on " + ChatColor.YELLOW + expiration.toLocaleString() + ChatColor.WHITE + ","
				+ "\n" + ChatColor.WHITE + "which is in" + ChatColor.YELLOW + TimeConverter.intToStringElongated(bantime) + ChatColor.WHITE + ".";
		
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static void onConnect(AsyncPlayerPreLoginEvent event) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(event.getName());
		if (player.isBanned()) {
			BanList list = Bukkit.getBanList(BanList.Type.NAME);
			BanEntry entry = list.getBanEntry(player.getName());
			
			String reason = entry.getReason();
			Date expiration = entry.getExpiration();
			
			event.disallow(Result.KICK_BANNED, banmsg(reason, expiration));
			return;
		}
		
		InetAddress ip = event.getAddress();
		BanList list = Bukkit.getBanList(BanList.Type.IP);
		BanEntry entry = list.getBanEntry(player.getName());
		
		if (entry != null) {
			String reason = entry.getReason();
			Date expiration = entry.getExpiration();
			
			event.disallow(Result.KICK_BANNED, banmsg(reason, expiration));
		}
		return;
	}
}
