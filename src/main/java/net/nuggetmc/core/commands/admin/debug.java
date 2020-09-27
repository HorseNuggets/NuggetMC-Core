package net.nuggetmc.core.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;
import net.nuggetmc.core.util.TimeConverter;

@SuppressWarnings("all")
public class debug implements CommandExecutor {

	private Main plugin;

	public debug(Main plugin) {
		this.plugin = plugin;
	}

	private ConfigManager configManager;
	private TimeConverter timeConverter;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		
		return true;
	}
}
