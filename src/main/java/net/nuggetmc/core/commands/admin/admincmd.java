package net.nuggetmc.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class admincmd implements CommandExecutor {
	
	private Main plugin;
	private String spawnworld;
	private Location spawnloc;

	public admincmd(Main plugin) {
		this.plugin = plugin;
		this.spawnworld = Configs.worldsettings.getConfig().getString("spawn.world");
		double x = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.x");
		double y = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.y");
		double z = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.z");
		this.spawnloc = new Location(Bukkit.getWorld(spawnworld), x, y, z);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label) {
		case "gma":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 2");
			}
			break;
		case "gmc":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 1");
			}
			break;
		case "gms":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 0");
			}
			break;
		case "gmsp":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 3");
			}
			break;
		case "tpall":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "tp @a " + sender.getName());
			}
			break;
		}
		return true;
	}
}
