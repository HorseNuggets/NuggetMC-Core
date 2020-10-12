package net.nuggetmc.core.commands.def;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.nuggetmc.core.Main;

public class HelpCommand implements CommandExecutor {
	
	private Main plugin;
	
	public HelpCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		return true;
	}
}
