package net.nuggetmc.core.util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;

@SuppressWarnings("all")
public class debug implements CommandExecutor {

	private Main plugin;

	public debug(Main plugin) {
		this.plugin = plugin;
	}

	private ConfigManager configManager;
	private TimeConverter timeConverter;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		timeConverter = new TimeConverter();
		String argsStr = "";
		
		for(int i = 0; i < args.length; i++) {
		    argsStr = argsStr + args[i];
		    if (!(i == args.length - 1)) {
		    	argsStr = argsStr + " ";
		    }
		}
		
		Bukkit.broadcastMessage("" + timeConverter.stringToInt(argsStr));
		
		
		
		return true;
	}
}
