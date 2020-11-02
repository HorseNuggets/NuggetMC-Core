package net.nuggetmc.core.commands.mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.Checks;
import net.nuggetmc.core.util.TimeConverter;

public class BanCommand implements CommandExecutor {
	
	private Main plugin;
	private static FileConfiguration ips;
	
	public BanCommand(Main plugin) {
		this.plugin = plugin;
		BanCommand.ips = Configs.ips.getConfig();
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
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label.toLowerCase()) {
		case "ban":
			if (args.length >= 1) {
				
				String reason = "No reason specified";
				int bantime = TimeConverter.stringToInt("7d");
				boolean silence = false;
				
				if (args.length >= 2) {
					
					String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
					String[] ultraSubArgs = null;
					
					String timeResult = "";
					String reasonResult = "";
					
					for (int i = 0; i < subArgs.length; i++) {
						char end = subArgs[i].charAt(subArgs[i].length() - 1);
						if (end == 's' || end == 'm' || end == 'h' || end == 'd') {
							String numtest = subArgs[i].substring(0, subArgs[i].length() - 1);
							
							if (Checks.isInteger(numtest)) {
								timeResult = timeResult + subArgs[i] + " ";
								continue;
							}
							else {
								ultraSubArgs = Arrays.copyOfRange(subArgs, i, subArgs.length);
								break;
							}
						}
						else {
							ultraSubArgs = Arrays.copyOfRange(subArgs, i, subArgs.length);
							break;
						}
					}
					
					if (ultraSubArgs != null) {
						for (int i = 0; i < ultraSubArgs.length; i++) {
							if (ultraSubArgs[i].contains("-s")) {
								silence = true;
							}
							else {
								reasonResult = reasonResult + ultraSubArgs[i] + " ";
							}
						}
						
						if (reasonResult.endsWith(" ")) {
							reasonResult = reasonResult.substring(0, reasonResult.length() - 1);
						}
						
						if (!reasonResult.equals("")) {
							reason = reasonResult;
						}
					}
					
					if (timeResult.endsWith(" ")) {
						timeResult = timeResult.substring(0, timeResult.length() - 1);
					}
					
					if (!timeResult.equals("")) {
						bantime = TimeConverter.stringToInt(timeResult);
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
				
				if (silence) {
					final String bname = bannedPlayerName;
					final String breason = reason;
					final int btime = bantime;
					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						for (Player all : Bukkit.getOnlinePlayers()) {
							if (Checks.checkStaff(all)) {
								String msg = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "STAFF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED
										+ sender.getName() + " banned " + bname + ChatColor.RED + " with reason [" + ChatColor.YELLOW
										+ breason + ChatColor.RED + "] for" + ChatColor.YELLOW + TimeConverter.intToStringElongated(btime) + ChatColor.RED + ".";
								all.sendMessage(msg);
								Bukkit.getConsoleSender().sendMessage(msg);
							}
						}
					});
				}
				
				else {
					Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Alert" + ChatColor.DARK_GRAY + "] " + ChatColor.RED
							+ sender.getName() + " banned " + bannedPlayerName + ChatColor.RED + " with reason [" + ChatColor.YELLOW
							+ reason + ChatColor.RED + "] for" + ChatColor.YELLOW + TimeConverter.intToStringElongated(bantime) + ChatColor.RED + ".");
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
				List<String> msgList = new ArrayList<>();
				int count = 0;
				
				for (BanEntry entry : entries) {
					String name = entry.getTarget();
					String reason = entry.getReason();
					String by = entry.getSource();
					Date expiration = entry.getExpiration();
					
					int bantime = (int) ((expiration.getTime() - System.currentTimeMillis()) / 1000);
					
					if (bantime <= 0) {
						list.pardon(name);
						continue;
					}
					
					String bantimestr = TimeConverter.intToString(bantime);
					if (bantimestr.startsWith(" ")) {
						bantimestr = bantimestr.substring(1);
					}
					
					count++;
					msgList.add(name + " (by " + ChatColor.YELLOW + by + ChatColor.RESET + ") [" + ChatColor.YELLOW + reason
							+ ChatColor.RESET + "] (" + ChatColor.YELLOW + bantimestr + ChatColor.RESET + ")");
				}
				
				sender.sendMessage("There are " + count + " total banned players:");
				
				for (String key : msgList) {
					sender.sendMessage(key);
				}
			});
			break;
			
		case "ban-ip":
			if (args.length >= 1) {
				
				String reason = "No reason specified";
				
				if (args.length >= 2) {
					String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
					reason = "";
					for (int i = 0; i < subArgs.length; i++) {
						reason += subArgs[i];
					}
				}
				
				Player bannedPlayer = Bukkit.getPlayer(args[0]);
				String bannedPlayerName = args[0];
				
				if (bannedPlayer != null) {
					bannedPlayerName = bannedPlayer.getName();
					if (bannedPlayer.isOnline()) {
						bannedPlayer.kickPlayer(permbanmsg(reason));
					}
				}
				else {
					OfflinePlayer bannedOfflinePlayer = Bukkit.getOfflinePlayer(args[0]);
					if (bannedOfflinePlayer != null) {
						bannedPlayerName = bannedOfflinePlayer.getName();
					}
					sender.sendMessage(ChatColor.RED + bannedPlayerName + " is not online!");
					return true;
				}
				
				String ip = ((CraftPlayer) bannedPlayer).getHandle().playerConnection.networkManager.getRawAddress().toString();
				ip = ip.split(":")[0];
				ips.set(bannedPlayerName + ".ip", ip);
				ips.set(bannedPlayerName + ".reason", reason);
				ips.set(bannedPlayerName + ".by", sender.getName());
				Configs.ips.saveConfig();
				
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Alert" + ChatColor.DARK_GRAY + "] " + ChatColor.RED
						+ sender.getName() + " permanently banned " + bannedPlayerName + ChatColor.RED + " with reason [" + ChatColor.YELLOW
						+ reason + ChatColor.RED + "].");
			}
			else {
				sender.sendMessage(ChatColor.RED + "Usage: /ban-ip <player> <reason>");
			}
			break;
			
		case "pardon-ip":
			if (args.length >= 1) {
				
				Player bannedPlayer = Bukkit.getPlayer(args[0]);
				String bannedPlayerName = args[0];
				
				if (bannedPlayer != null) {
					bannedPlayerName = bannedPlayer.getName();
					if (bannedPlayer.isOnline()) {
					}
				}
				else {
					OfflinePlayer bannedOfflinePlayer = Bukkit.getOfflinePlayer(args[0]);
					if (bannedOfflinePlayer != null) {
						bannedPlayerName = bannedOfflinePlayer.getName();
					}
				}
				
				for (String names : ips.getKeys(false)) {
					if (names.equals(bannedPlayerName)) {
						ips.set(bannedPlayerName, null);
						Configs.ips.saveConfig();
						sender.sendMessage("Pardoned player " + bannedPlayerName);
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "Usage: /pardon-ip <player>");
			}
			break;
			
		case "ipbanlist":
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				List<String> msgList = new ArrayList<>();
				int count = 0;
				for (String names : ips.getKeys(false)) {
					String reason = ips.getString(names + ".reason");
					String by = ips.getString(names + ".by");
					msgList.add(names + " (by " + ChatColor.YELLOW + by + ChatColor.RESET + ") [" + ChatColor.YELLOW + reason
					+ ChatColor.RESET + "]");
					count++;
				}
				
				sender.sendMessage("There are " + count + " total banned players:");
				
				for (String key : msgList) {
					sender.sendMessage(key);
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
	
	private static String permbanmsg(String reason) {
		String result = ChatColor.RED + "You have been permanently banned from the network."
				+ "\n\n" + ChatColor.WHITE + "Reason: " + ChatColor.YELLOW + reason;
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static void onConnect(AsyncPlayerPreLoginEvent event) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(event.getName());
		
		String ip = event.getAddress().toString();
		
		for (String names : ips.getKeys(false)) {
			if (ips.getString(names + ".ip").equals(ip)) {
				String reason = ips.getString(names + ".reason");
				event.disallow(Result.KICK_BANNED, permbanmsg(reason));
			}
		}
		
		if (player.isBanned()) {
			BanList list = Bukkit.getBanList(BanList.Type.NAME);
			BanEntry entry = list.getBanEntry(player.getName());
			
			String reason = entry.getReason();
			Date expiration = entry.getExpiration();
			
			int bantime = (int) ((expiration.getTime() - System.currentTimeMillis()) / 1000);
			if (bantime <= 0) {
				list.pardon(player.getName());
			}
			
			else event.disallow(Result.KICK_BANNED, banmsg(reason, expiration));
		}
		return;
	}
}
