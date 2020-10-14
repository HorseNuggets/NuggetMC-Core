package net.nuggetmc.core.commands.def;

import java.util.HashMap;
import java.util.Map;

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
import net.nuggetmc.core.util.Checks;
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
		this.defineWarps();
		this.plugin = plugin;
	}
	
	private void defineSpawn() {
		this.spawnworld = Configs.worldsettings.getConfig().getString("spawn.world");
		this.x = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.x");
		this.y = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.y");
		this.z = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.z");
		return;
	}
	
	private Map<String, String> warps = new HashMap<>();
	
	/*
	 * [TODO]
	 * Store the values as a float array
	 */
	
	private void defineWarps() {
		warps.put("ushop", "world%28.5%222%15.5%270%0");
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
		
		case "warp":
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Too few arguments!");
				sender.sendMessage(ChatColor.RED + "Usage: /warp <location>");
			}
			
			else {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					String input = args[0].toLowerCase();
					
					switch (input) {
					case "spawn":
						Bukkit.dispatchCommand(sender, "spawn");
						break;
					}
					
					if (Checks.checkHighStaff(player)) {
						if (warps.containsKey(input)) {
							String obj = warps.get(input);
							String[] sep = obj.split("%");
							World world = Bukkit.getWorld(sep[0]);
							
							if (world == null) {
								player.sendMessage(ChatColor.RED + "The world you are trying to teleport to is currently inactive!");
							}
							
							else {
								float x = Float.parseFloat(sep[1]);
								float y = Float.parseFloat(sep[2]);
								float z = Float.parseFloat(sep[3]);
								float yaw = Float.parseFloat(sep[4]);
								float pitch = Float.parseFloat(sep[5]);
								
								Location loc = new Location(world, x, y, z, yaw, pitch);
								player.teleport(loc);
							}
						}
					}
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
