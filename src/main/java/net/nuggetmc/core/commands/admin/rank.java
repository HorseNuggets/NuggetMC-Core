package net.nuggetmc.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.nuggetmc.core.Main;

public class rank implements CommandExecutor {
	
	private Main plugin;

	public rank(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 2) {
			Bukkit.dispatchCommand(sender, "lp user " + args[0] + " group set " + args[1]);
			plugin.sidebar.refreshRank(sender, args[0]);
		}
		else {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Usage: /rank <player> <rank>");
		}
		return true;
	}
}