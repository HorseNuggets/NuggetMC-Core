package net.nuggetmc.core.commands.def;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.TimeConverter;

public class DefaultCommand implements CommandExecutor {
	
	private Main plugin;
	private String spawnworld;
	private double x;
	private double y;
	private double z;
	private String linspace = ChatColor.GRAY + "--------------------------------------";

	public DefaultCommand(Main plugin) {
		this.defineSpawn();
		this.plugin = plugin;
	}
	
	private void defineSpawn() {
		this.spawnworld = Configs.worldsettings.getConfig().getString("spawn.world");
		this.x = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.x");
		this.y = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.y");
		this.z = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.z");
		return;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label) {
		case "spawn":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				
				World world = Bukkit.getWorld(spawnworld);
				if (world == null) {
					player.sendMessage(ChatColor.RED + "The world you are trying to teleport to is currently inactive!");
				}
				else {
					player.teleport(new Location(Bukkit.getWorld(spawnworld), x, y, z));
					player.sendMessage(ChatColor.WHITE + "You have warped to " + ChatColor.YELLOW + "spawn" + ChatColor.WHITE + ".");
				}
			}
			break;
		
		case "wrt":
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				sender.sendMessage(linspace);
				sender.sendMessage(ChatColor.WHITE + "World reload times:");
				sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "Overworld:" + ChatColor.RED + TimeConverter.intToString(WorldManager.count));
				sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "Nether:" + ChatColor.RED + TimeConverter.intToString(WorldManager.countNether));
				sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "The End:" + ChatColor.RED + TimeConverter.intToString(WorldManager.countEnd));
				sender.sendMessage(linspace);
			});
			break;
		}
		return true;
	}
}
