package net.nuggetmc.core.commands.def;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class defaultcmd implements CommandExecutor {
	
	private Main plugin;
	private String spawnworld;
	private Location spawnloc;

	public defaultcmd(Main plugin) {
		this.plugin = plugin;
		this.spawnworld = Configs.worldsettings.getConfig().getString("spawn.world");
		double x = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.x");
		double y = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.y");
		double z = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.z");
		this.spawnloc = new Location(Bukkit.getWorld(spawnworld), x, y, z);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label) {
		case "spawn":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.teleport(spawnloc);
				player.sendMessage(ChatColor.WHITE + "You have warped to " + ChatColor.YELLOW + "spawn" + ChatColor.WHITE + ".");
			}
			break;
		}
		return true;
	}
}
