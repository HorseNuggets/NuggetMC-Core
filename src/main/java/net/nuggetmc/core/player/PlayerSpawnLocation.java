package net.nuggetmc.core.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.misc.FlyVanish;

public class PlayerSpawnLocation {
	
	private Main plugin;
	private String worldname;
	private double x;
	private double y;
	private double z;
	private ConfigurationSection config;
	
	public PlayerSpawnLocation(Main plugin) {
		this.plugin = plugin;
		this.config = Configs.worldsettings.getConfig();
		spawnSetup();
	}
	
	public void spawnSetup() {
		this.worldname = config.getString("spawn.world");
		this.x = config.getDouble("spawn.coordinates.x");
		this.y = config.getDouble("spawn.coordinates.y");
		this.z = config.getDouble("spawn.coordinates.z");
	}
	
	public void setSpawn(Player player) {
		World world = Bukkit.getWorld(worldname);
		if (world != null) {
			Location spawn = new Location(world, x, y, z);
			player.teleport(spawn);
			player.setBedSpawnLocation(spawn, true);
		}
		
		if (!player.getWorld().getName().equals(worldname)) {
			queue(player);
		}
		return;
	}
	
	public void queue(Player player) {
		BukkitRunnable teleportQueue = new BukkitRunnable() {
			@Override
			public void run() {
				World world = Bukkit.getWorld(worldname);
				if (world != null) {
					Location spawn = new Location(world, x, y, z);
					player.teleport(spawn);
					player.setBedSpawnLocation(spawn, true);
				}
				
				if (player.getWorld().getName().equals(worldname)) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						for (Player all : Bukkit.getOnlinePlayers()) {
							if (!FlyVanish.vanish.contains(player)) {
								all.showPlayer(player);
							}
						}
					}, 20);
					
					this.cancel();
					return;
				}
			}
		};
		
		teleportQueue.runTaskTimer(plugin, 10, 10);
		return;
	}
}

