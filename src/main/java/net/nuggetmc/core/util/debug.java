package net.nuggetmc.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;

@SuppressWarnings("all")
public class debug implements CommandExecutor {

	private Main plugin;

	public debug(Main plugin) {
		this.plugin = plugin;
	}

	private ConfigManager configManager;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		configManager = new ConfigManager(plugin);
		
		
		
		configManager.setup("nofall\\config.yml");
		
		Bukkit.broadcastMessage("" + configManager.getConfig().getInt("y-level"));
		
		//configManager.getConfig().set("lmao", 142);
		//configManager.saveConfig();
		
		return true;
	}
}
