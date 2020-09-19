package net.nuggetmc.core.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		
		//Bukkit.getServer().getWorlds().add("main");
		new WorldCreator("main").createWorld();
		Player player = (Player) sender;
		Location loc = new Location(Bukkit.getServer().getWorld("main"), 0.5, 223, 0.5);
		player.teleport(loc);
		
		return true;
	}
}
