package net.nuggetmc.core.commands;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.plugin.java.JavaPlugin;

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
			nullInput(sender);
			return true;
		}
		
		else if (args.length >= 1) {
			switch (args[0].toLowerCase()) {
			case "info":
				info(sender);
				break;
			case "reload":
				reload(sender, args);
				break;
			}
		}
		return true;
	}
	
	public void nullInput(CommandSender sender) {
		sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
		sender.sendMessage(ChatColor.GOLD + "NuggetMC-Core " + ChatColor.GRAY + "[" + ChatColor.YELLOW + "v2.0" + ChatColor.GRAY + "]");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Plugin Manager Subcommands:");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "info" + ChatColor.GRAY + " » Information about the plugin.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "reload <filename>" + ChatColor.GRAY + " » Reloads one/all configuration file(s).");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "world <worldname>" + ChatColor.GRAY + " » Travels to a specific world.");
		sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
		return;
	}
	
	public void info(CommandSender sender) {
		sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
		sender.sendMessage(ChatColor.GOLD + "NuggetMC-Core " + ChatColor.GRAY + "[" + ChatColor.YELLOW + "v2.0" + ChatColor.GRAY + "]");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Plugin Information:");
		sender.sendMessage(ChatColor.GRAY + "Author » " + ChatColor.YELLOW + "HorseNuggets");
		sender.sendMessage(ChatColor.GRAY + "Since » " + ChatColor.YELLOW + "2/22/2017");
		
		Method getFileMethod = null;
		try {
			getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		getFileMethod.setAccessible(true);
		
		File jarFile = null;
		try {
			jarFile = (File) getFileMethod.invoke(plugin);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		try {
			
            int bytes = (int) Files.size(jarFile.toPath());
            double kiloBytes = bytes / 1000;
            
            sender.sendMessage(ChatColor.GRAY + "Filesize » " + ChatColor.YELLOW + kiloBytes + " KB");

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		sender.sendMessage(ChatColor.GRAY + "Source Code » " + ChatColor.YELLOW + "git.io/JU2IS");
		sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
		return;
	}
	
	public void reload(CommandSender sender, String[] args) {
		if (args.length >= 2) {
			try {
				String absoluteDir = plugin.getDataFolder().getAbsoluteFile().toString();
				String pathDisplay = args[1].replaceAll(Pattern.quote("\\"), "/");
				String pathCode = args[1].replaceAll("/", "\\\\");
				
				File file = new File(absoluteDir + "\\" + args[1]);
				
				if (!file.exists()) {
					sender.sendMessage(ChatColor.RED + pathDisplay + " does not exist!");
					return;
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
				return;
			}
		}
		
		else {
			plugin.loadConfigs();
			
			sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
			sender.sendMessage(ChatColor.GOLD + "[RELOADED] " + ChatColor.RESET + "All configuration files have been reloaded.");
			sender.sendMessage(ChatColor.GRAY + "--------------------------------------");
		}
		return;
	}
}
