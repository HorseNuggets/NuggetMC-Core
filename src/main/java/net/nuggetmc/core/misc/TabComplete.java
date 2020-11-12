package net.nuggetmc.core.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.economy.KitCosts;

public class TabComplete {
	
	private Main plugin;
	
	public TabComplete(Main plugin) {
		this.plugin = plugin;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (command.getName().toLowerCase()) {
		
		case "nuggetmc":
			switch (args.length) {
			case 1:
				List<String> arguments = new ArrayList<>();
				arguments.add("info");
				arguments.add("jarreload");
				arguments.add("reload");
				arguments.add("world");
				Collections.sort(arguments);
				
				if (tabConditional(args[0])) {
					arguments = autofill(arguments, args[0]);
				}
				
				return arguments;
			case 2:
				switch (args[0].toLowerCase()) {
				case "world":
					List<World> worlds = Bukkit.getWorlds();
					List<String> worldnames = new ArrayList<>();
					for (int i = 0; i < worlds.size(); i++) {
						worldnames.add(worlds.get(i).getName());
					}
					Collections.sort(worldnames);
					
					if (tabConditional(args[1])) {
						worldnames = autofill(worldnames, args[1]);
					}
					
					return worldnames;
				case "reload":
					File dir = new File(plugin.getDataFolder().toString());
					File[] fList = dir.listFiles();
					List<String> configlist = new ArrayList<>();
					
					for (File file : fList) {
						if (file.isFile()) {
							configlist.add(file.getName());
			            }
						else if (file.isDirectory()) {
			            	dirFiles(file.getAbsolutePath(), configlist, file.getName() + "/");
			            }
					}
					Collections.sort(configlist);
					
					if (tabConditional(args[1])) {
						configlist = autofill(configlist, args[1]);
					}
					
					return configlist;
				}
			}
			break;
			
		case "rank":
			if (args.length == 2) {
				LuckPerms api = LuckPermsProvider.get();
				Set<Group> groups = api.getGroupManager().getLoadedGroups();
				List<String> groupnames = new ArrayList<>();
				
				for (Group group : groups) {
					groupnames.add(group.getName());
				}
				Collections.sort(groupnames);
				
				if (tabConditional(args[1])) {
					groupnames = autofill(groupnames, args[1]);
				}
				
				return groupnames;
			}
			break;
			
		case "kit":
			if (args.length == 1) {
				List<String> groupnames = new ArrayList<>(KitCosts.kitList);
				Collections.sort(groupnames);
				
				if (tabConditional(args[0])) {
					groupnames = autofill(groupnames, args[0]);
				}
				
				return groupnames;
			}
			
		case "lead":
			if (args.length == 1) {
				List<String> groupnames = new ArrayList<>();
				groupnames.add("kills");
				groupnames.add("nuggets");
				groupnames.add("rating");
				
				if (tabConditional(args[0])) {
					groupnames = autofill(groupnames, args[0]);
				}
				
				return groupnames;
			}
			
		case "unignore":
			if (args.length == 1) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					List<String> groupnames = new ArrayList<>();
					for (String key : Configs.ignore.getConfig().getStringList(player.getUniqueId().toString())) {
						String name = Configs.playerstats.getConfig().getString("players." + key + ".name");
						if (name != null) {
							groupnames.add(name);
						}
					}
					
					Collections.sort(groupnames);
					
					if (tabConditional(args[0])) {
						groupnames = autofill(groupnames, args[0]);
					}
					
					return groupnames;
				}
			}
			break;
			
		case "pardon-ip":
			if (args.length == 1) {
				List<String> groupnames = new ArrayList<>();
				
				for (String name : Configs.ips.getConfig().getKeys(false)) {
					groupnames.add(name);
				}
				
				Collections.sort(groupnames);
				
				if (tabConditional(args[0])) {
					groupnames = autofill(groupnames, args[0]);
				}
				
				return groupnames;
			}
			break;
		}
		return null;
	}
	
	private boolean tabConditional(String argument) {
		if (argument == null || argument == "" || argument == " " || argument.isEmpty()) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unused")
	private List<String> autofill(List<String> groupnames, String input) {
		List<String> newlist = new ArrayList<>();
		for (String entry : groupnames) {
			if (entry.length() >= input.length()) {
				if (input.equalsIgnoreCase(entry.substring(0, input.length()))) {
					newlist.add(entry);
				}
			}
		}
		
		if (newlist == null) {
			return groupnames;
		}
		return newlist;
	}
	
	private void dirFiles(String dirName, List<String> configlist, String path) {
		File dir = new File(dirName);
		File[] fList = dir.listFiles();
		
		for (File file : fList) {
			if (file.isFile()) {
				configlist.add(path + file.getName());
            }
			else if (file.isDirectory()) {
            	dirFiles(file.getAbsolutePath(), configlist, path + file.getName() + "/");
            }
		}
		return;
	}
}
