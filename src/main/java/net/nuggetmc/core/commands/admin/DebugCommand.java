package net.nuggetmc.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;
import net.nuggetmc.core.util.TimeConverter;

@SuppressWarnings("all")
public class DebugCommand implements CommandExecutor {

	private Main plugin;

	public DebugCommand(Main plugin) {
		this.plugin = plugin;
	}

	private ConfigManager configManager;
	private TimeConverter timeConverter;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Bukkit.broadcastMessage(TimeConverter.intToString(Integer.parseInt(args[0])));
		return true;
	}
}
