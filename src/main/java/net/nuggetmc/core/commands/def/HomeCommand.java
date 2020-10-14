package net.nuggetmc.core.commands.def;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.Checks;

public class HomeCommand implements CommandExecutor {
	
	private FileConfiguration homes;

	public HomeCommand() {
		this.homes = Configs.homes.getConfig();
	}
	
	/*
	 * [TODO]
	 * set a home limit even for donators
	 * cannot name home "home"
	 */
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName().toLowerCase()) {
		case "sethome":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				UUID uuid = player.getUniqueId();
				String key = "";
				
				if (args.length == 0) {
					key = uuid + ".home";
					player.sendMessage(ChatColor.YELLOW + "Home set successfuly.");
				}
				
				else {
					if (Checks.checkDef(player)) {
						player.sendMessage("You must be rank " + ChatColor.YELLOW + "XD " + ChatColor.WHITE + "or higher to set multiple homes!");
						return true;
					}
					
					else {
						key = uuid + "." + args[0].toLowerCase();
						player.sendMessage(ChatColor.YELLOW + "Home " + ChatColor.GOLD + args[0].toLowerCase() + ChatColor.YELLOW + " set successfuly.");
					}
				}
				
				Location location = player.getLocation();
				String worldname = location.getWorld().getName();
				double x = location.getX();
				double y = location.getY();
				double z = location.getZ();
				float yaw = location.getYaw();
				float pitch = location.getPitch();
				
				homes.set(key + ".world", worldname);
				homes.set(key + ".x", x);
				homes.set(key + ".y", y);
				homes.set(key + ".z", z);
				homes.set(key + ".yaw", yaw);
				homes.set(key + ".pitch", pitch);
				Configs.homes.saveConfig();
			}
			return true;
		
		case "home":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				UUID uuid = player.getUniqueId();
				String key = "";
				
				if (args.length == 0) {
					key = uuid + ".home";
				}
				
				else {
					if (Checks.checkDef(player)) {
						player.sendMessage("You must be rank " + ChatColor.YELLOW + "XD " + ChatColor.WHITE + "or higher to teleport to an extra home!");
						return true;
					}
					
					else {
						key = uuid + "." + args[0].toLowerCase();
					}
				}
				
				if (homes.getConfigurationSection(key) == null) {
					if (args.length == 0) {
						player.sendMessage(ChatColor.YELLOW + "You do not currently have this home set. Do " + ChatColor.GOLD + "/sethome "
								+ ChatColor.YELLOW + "to set a home.");
					}
					
					else {
						player.sendMessage(ChatColor.YELLOW + "That home does not exist!");
					}
					return true;
				}
				
				World world = Bukkit.getWorld(homes.getString(key + ".world"));
				
				double x = homes.getDouble(key + ".x");
				double y = homes.getDouble(key + ".y");
				double z = homes.getDouble(key + ".z");
				float yaw = (float) homes.getDouble(key + ".yaw");
				float pitch = (float) homes.getDouble(key + ".pitch");
				
				if (world != null) {
					Location location = new Location(world, x, y, z, yaw, pitch);
					player.teleport(location);
					player.sendMessage(ChatColor.YELLOW + "Welcome home, " + player.getName());
				}
				
				else {
					player.sendMessage(ChatColor.RED + "The world you are trying to teleport to is currently inactive!");
				}
			}
			return true;
		
		case "delhome":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				UUID uuid = player.getUniqueId();
				
				if (args.length == 0) {
					player.sendMessage(ChatColor.YELLOW + "Please specify a home to delete!");
				}
				
				String key = args[0].toLowerCase();
				
				ConfigurationSection section = homes.getConfigurationSection(uuid + "." + key);
				if (section != null) {
					homes.set(uuid + "." + key, null);
					Configs.homes.saveConfig();
					player.sendMessage(ChatColor.YELLOW + "Home " + ChatColor.GOLD + key + ChatColor.YELLOW + " deleted!");
				}
				
				else {
					player.sendMessage(ChatColor.YELLOW + "That home does not exist!");
				}
			}
			return true;
				
		case "homes":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				UUID uuid = player.getUniqueId();

				String linspace = (ChatColor.GRAY + "--------------------------------------");
				player.sendMessage(linspace);
				player.sendMessage("Your homes:");
				
				for (String key : homes.getConfigurationSection(uuid.toString()).getKeys(false)) {
					player.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.GOLD + key);
				}
				
				player.sendMessage(linspace);
				return true;
			}
		}
		return true;
	}
}