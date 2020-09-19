package net.nuggetmc.core.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;

public class nmc implements CommandExecutor {

	private Main plugin;

	public nmc(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean checkDifferences(CommandSender sender, String keyOld, String entryOld, String keyNew, String entryNew) {
		if (keyOld.equals(keyNew)) {
			if (!entryOld.equals(entryNew)) {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.YELLOW + "[CHANGED] " + ChatColor.RESET + keyNew);
				sender.sendMessage(ChatColor.RED + "[OLD] " + ChatColor.RESET + entryOld);
				sender.sendMessage(ChatColor.GREEN + "[NEW] " + ChatColor.RESET + entryNew);
				return true;
			}
		}
		return false;
	}
	
	public void configIteration(HashMap<String, String> settings, ConfigManager configManager) {
		for(String key : configManager.getConfig().getConfigurationSection("").getKeys(true)) {
			if (!(configManager.getConfig().get(key) instanceof MemorySection)) {
				String entry = configManager.getConfig().get(key).toString();
				settings.put(key, entry);
			}
		}
		return;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			
			// [TODO] talking about subcommands
			
			return true;
		}
		else if (args.length >= 1) {
			switch (args[0].toLowerCase()) {
			case "reload":
				if (args.length >= 2) {
					try {
						String absoluteDir = plugin.getDataFolder().getAbsoluteFile().toString();
						String pathDisplay = args[1].replaceAll(Pattern.quote("\\"), "/");
						String pathCode = args[1].replaceAll("/", "\\\\");
						
						File file = new File(absoluteDir + "\\" + args[1]);
						
						if (!file.exists()) {
							sender.sendMessage(ChatColor.RED + pathDisplay + " does not exist!");
							return true;
						}
						
						HashMap<String, String> oldSettings = new HashMap<String, String>();
						HashMap<String, String> newSettings = new HashMap<String, String>();
						
						ConfigManager configManager = plugin.configs.get(pathCode);
						configIteration(oldSettings, configManager);
						configManager.reloadConfig();
						configIteration(newSettings, configManager);
						
						sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
						sender.sendMessage(ChatColor.GOLD + "[RELOADED] " + ChatColor.RESET + pathDisplay);
						
						ArrayList<Boolean> checks = new ArrayList<Boolean>();
						oldSettings.forEach((keyOld, entryOld) -> newSettings.forEach((keyNew, entryNew) -> checks.add(checkDifferences(sender, keyOld, entryOld, keyNew, entryNew))));
						if (!checks.contains(true)) {
							sender.sendMessage("");
							sender.sendMessage(ChatColor.YELLOW + "[CHANGED] " + ChatColor.RESET + "No changes have been made.");
						}
						sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
						plugin.configs.special(pathCode);
					} catch (NullPointerException e) {
						sender.sendMessage(ChatColor.RED + args[1] + " does not exist!");
						return true;
					}
				}
				else {
					plugin.loadConfigs();
					
					sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
					sender.sendMessage(ChatColor.GOLD + "[RELOADED] " + ChatColor.RESET + "All configuration files have been reloaded.");
					sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
				}
				break;
			}
		}
		return true;
	}
}
