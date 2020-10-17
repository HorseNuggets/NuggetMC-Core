package net.nuggetmc.core.commands.mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.Checks;
import net.nuggetmc.core.util.TimeConverter;

public class MuteCommand implements CommandExecutor {
	
	private Main plugin;
	private FileConfiguration mutes;
	
	public MuteCommand(Main plugin) {
		this.plugin = plugin;
		this.mutes = Configs.mutes.getConfig();
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label.toLowerCase()) {
		case "mute":
			if (args.length >= 1) {
				
				String reason = "No reason specified";
				int mutetime = TimeConverter.stringToInt("1d");
				
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
						}
						else {
							ultraSubArgs = Arrays.copyOfRange(subArgs, i, subArgs.length);
							break;
						}
					}
					
					if (ultraSubArgs != null) {
						for (int i = 0; i < ultraSubArgs.length; i++) {
							reasonResult = reasonResult + ultraSubArgs[i] + " ";
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
						mutetime = TimeConverter.stringToInt(timeResult);
					}
				}
				
				Long exp = (System.currentTimeMillis() / 1000) + mutetime;
				
				Player mutedPlayer = Bukkit.getPlayer(args[0]);
				String mutedPlayerName = args[0];
				UUID uuid = null;
				
				if (mutedPlayer != null) {
					mutedPlayerName = mutedPlayer.getName();
					uuid = mutedPlayer.getUniqueId();
				}
				
				else {
					OfflinePlayer mutedOfflinePlayer = Bukkit.getOfflinePlayer(args[0]);
					if (mutedOfflinePlayer != null) {
						mutedPlayerName = mutedOfflinePlayer.getName();
						uuid = mutedOfflinePlayer.getUniqueId();
					}
				}
				
				if (uuid != null) {
					mutes.set(uuid.toString() + ".name", mutedPlayerName);
					mutes.set(uuid.toString() + ".by", sender.getName());
					mutes.set(uuid.toString() + ".reason", reason);
					mutes.set(uuid.toString() + ".exp", exp);
					Configs.mutes.saveConfig();
				}
				
				else {
					sender.sendMessage(ChatColor.RED + mutedPlayerName + " does not exist!");
				}
				
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Alert" + ChatColor.DARK_GRAY + "] " + ChatColor.RED
						+ sender.getName() + " muted player " + mutedPlayerName + ChatColor.RED + " with reason [" + ChatColor.YELLOW
						+ reason + ChatColor.RED + "] and time" + ChatColor.YELLOW + TimeConverter.intToString(mutetime) + ChatColor.RED + ".");
			}
			else {
				sender.sendMessage(ChatColor.RED + "Usage: /mute <player> <time> <reason>");
			}
			break;
			
		case "unmute":
			if (args.length >= 1) {
				Player mutedPlayer = Bukkit.getPlayer(args[0]);
				String mutedPlayerName = args[0];
				UUID uuid = null;
				
				if (mutedPlayer != null) {
					mutedPlayerName = mutedPlayer.getName();
					uuid = mutedPlayer.getUniqueId();
				}
				
				else {
					OfflinePlayer mutedOfflinePlayer = Bukkit.getOfflinePlayer(args[0]);
					if (mutedOfflinePlayer != null) {
						mutedPlayerName = mutedOfflinePlayer.getName();
						uuid = mutedOfflinePlayer.getUniqueId();
					}
				}
				
				unmute(uuid, sender, mutedPlayerName);
			}
			
			else {
				sender.sendMessage(ChatColor.RED + "Usage: /unmute <player>");
			}
			break;
			
		case "mutelist":
			
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				Set<String> keys = mutes.getKeys(false);
				List<String> msgList = new ArrayList<>();
				int count = 0;
				
				for (String key : keys) {
					String name = mutes.getString(key + ".name");
					String by = mutes.getString(key + ".by");
					String reason = mutes.getString(key + ".reason");
					
					int exp = mutes.getInt(key + ".exp");
					int remaining = (int) (exp - (System.currentTimeMillis() / 1000));
					
					if (remaining <= 0) {
						mutes.set(key, null);
						Configs.mutes.saveConfig();;
						continue;
					}
					
					String rStr = TimeConverter.intToString(remaining);
					if (rStr.startsWith(" ")) {
						rStr = rStr.substring(1);
					}
					
					count++;
					msgList.add(name + " (by " + ChatColor.YELLOW + by + ChatColor.RESET + ") [" + ChatColor.YELLOW + reason
							+ ChatColor.RESET + "] (" + ChatColor.YELLOW + rStr + ChatColor.RESET + ")");
				}
				
				sender.sendMessage("There are " + count + " total muted players:");
				
				for (String key : msgList) {
					sender.sendMessage(key);
				}
			});
			break;
		}
		return true;
	}
	
	public void unmute(UUID uuid, CommandSender sender, String name) {
		if (uuid != null) {
			if (mutes.contains(uuid.toString())) {
				mutes.set(uuid.toString(), null);
				Configs.mutes.saveConfig();
				if (sender != null) sender.sendMessage("Unmuted player " + name);
			}
			
			else {
				sender.sendMessage(name + " was never muted!");
			}
		}
		
		else {
			sender.sendMessage(ChatColor.RED + name + " does not exist!");
		}
		return;
	}
}
