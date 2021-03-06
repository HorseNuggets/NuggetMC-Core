package net.nuggetmc.core.commands.admin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;

public class NMCMainCommand implements CommandExecutor {

	private Main plugin;
	private String linspace = (ChatColor.GRAY + "--------------------------------------");
	private String header = ChatColor.GOLD + "NuggetMC-Core " + ChatColor.GRAY + "[" + ChatColor.YELLOW + "v2.0" + ChatColor.GRAY + "]";

	public NMCMainCommand(Main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 1) {
			switch (args[0].toLowerCase()) {
			case "jr":
				args[0] = "jarreload";
				break;
			case "r":
				args[0] = "reload";
				break;
			case "worlds":
				args[0] = "world";
				break;
			}
			switch (args[0].toLowerCase()) {
			case "info":
				info(sender);
				return true;
			case "jarreload":
				jarreload(sender, args);
				return true;
			case "reload":
				reload(sender, args);
				return true;
			case "world":
				world(sender, args);
				return true;
			}
		}
		nullInput(sender);
		return true;
	}
	
	private void nullInput(CommandSender sender) {
		sender.sendMessage(linspace);
		sender.sendMessage(header);
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Plugin Manager Subcommands:");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "info" + ChatColor.GRAY + " » Information about the plugin.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "jarreload" + ChatColor.GRAY + " » Reloads the plugin .jar file.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "reload <filename>" + ChatColor.GRAY + " » Reloads one/all configuration file(s).");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "world <worldname>" + ChatColor.GRAY + " » Travels to a specific world.");
		sender.sendMessage(linspace);
		return;
	}
	
	private void info(CommandSender sender) {
		sender.sendMessage(linspace);
		sender.sendMessage(header);
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
		sender.sendMessage(linspace);
		return;
	}
	
	private boolean checkDifferences(CommandSender sender, String keyOld, String entryOld, String keyNew, String entryNew) {
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
	
	private void configIteration(HashMap<String, String> settings, ConfigManager configManager) {
		for(String key : configManager.getConfig().getKeys(true)) {
			if (!(configManager.getConfig().get(key) instanceof MemorySection)) {
				String entry = configManager.getConfig().get(key).toString();
				settings.put(key, entry);
			}
		}
		return;
	}
	
	private void reload(CommandSender sender, String[] args) {
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
				
				sender.sendMessage(linspace);
				sender.sendMessage(header);
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GOLD + "[RELOADED] " + ChatColor.RESET + pathDisplay);
				
				ArrayList<Boolean> checks = new ArrayList<Boolean>();
				oldSettings.forEach((keyOld, entryOld) -> newSettings.forEach((keyNew, entryNew) -> checks.add(checkDifferences(sender, keyOld, entryOld, keyNew, entryNew))));
				if (!checks.contains(true)) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.YELLOW + "[CHANGED] " + ChatColor.RESET + "No changes have been made.");
				}
				sender.sendMessage(linspace);
				plugin.configs.special(pathCode);
			} catch (NullPointerException e) {
				sender.sendMessage(ChatColor.RED + args[1] + " does not exist!");
				return;
			}
		}
		
		else {
			plugin.loadConfigs();
			
			sender.sendMessage(linspace);
			sender.sendMessage(header);
			sender.sendMessage("");
			sender.sendMessage(ChatColor.GOLD + "[RELOADED] " + ChatColor.RESET + "All configuration files have been reloaded.");
			sender.sendMessage(linspace);
		}
		return;
	}
	
	private void jarreload(CommandSender sender, String[] args) {
		PlayerCommandPreprocessEvent.getHandlerList().unregister(plugin);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reloader reload NuggetMC-Core");
		sender.sendMessage(linspace);
		sender.sendMessage(header);
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GOLD + "[RELOADED] " + ChatColor.RESET + "NuggetMC-Core.jar has been reloaded.");
		sender.sendMessage(linspace);
		return;
	}
	
	private void world(CommandSender sender, String[] args) {
		if (args.length >= 2) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				switch (args[1].toLowerCase()) {
				case "world_nether":
					Location nether = new Location(Bukkit.getWorld(args[1]), 0.5, 128, 0.5);
					player.teleport(nether);
					return;
				case "main":
					Location main = new Location(Bukkit.getWorld(args[1]), 0.5, 223, 0.5);
					player.teleport(main);
					return;
				case "world":
					Location world = new Location(Bukkit.getWorld(args[1]), 0.5, 65, 0.5);
					player.teleport(world);
					return;
				case "alpha":
					Location alpha = new Location(Bukkit.getWorld(args[1]), 0.5, 65, 0.5);
					player.teleport(alpha);
					return;
				}
				World world = Bukkit.getWorld(args[1]);
				if (world != null) {
					player.teleport(world.getSpawnLocation());
				}
			}
			return;
		}
		
		List<World> worlds = Bukkit.getWorlds();
		
		sender.sendMessage(linspace);
		sender.sendMessage(header);
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + "[WORLDS]");
		
		for (int i = 0; i < worlds.size(); i++) {
			String worldname = worlds.get(i).getName();
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.getWorld().getName().equals(worldname)) {
					worldname = ChatColor.GREEN + worldname + " (current)";
				}
			}
			sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.WHITE + worldname);
		}
		
		sender.sendMessage(linspace);
		return;
	}
}
