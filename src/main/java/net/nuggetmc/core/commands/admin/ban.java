package net.nuggetmc.core.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.util.TimeConverter;

public class ban implements CommandExecutor {
	
	private Main plugin;
	
	public ban(Main plugin) {
		this.plugin = plugin;
	}
	
	/*
	 * https://bukkit.org/threads/how-to-temp-ban-ban-a-player-using-just-one-line.432451/
	 * 
	 */
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 1) {
			if (args.length >= 2) {
				sender.sendMessage("Banned player " + args[0] + " for " + TimeConverter.stringToInt(args[1]) + " seconds.");
			}
			
		}
		else {
			sender.sendMessage(ChatColor.RED + "Usage: /ban <player> <time> <reason> (--ip)");
		}
		return true;
	}
}
