package net.nuggetmc.core.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class PlayerSpawnLocation {
	
	private Main plugin;
	private String worldname;
	private ConfigurationSection config;
	
	public Location spawn;
	
	public PlayerSpawnLocation(Main plugin) {
		this.plugin = plugin;
		this.config = Configs.worldsettings.getConfig();
		spawnSetup();
	}
	
	public void spawnSetup() {
		worldname = config.getString("spawn.world");
		double x = config.getDouble("spawn.coordinates.x");
		double y = config.getDouble("spawn.coordinates.y");
		double z = config.getDouble("spawn.coordinates.z");
		spawn = new Location(Bukkit.getWorld(worldname), x, y, z);
	}
	
	public void setSpawn(Player player) {
		if (player.isDead()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				player.spigot().respawn();
			}, 20);
		}
		
		player.setBedSpawnLocation(spawn, true);
		player.teleport(spawn);
		
		if (!player.getWorld().getName().equals(worldname)) {
			queue(player);
		}
		return;
	}
	
	public void queue(Player player) {
		BukkitRunnable teleportQueue = new BukkitRunnable() {
			@Override
			public void run() {
				player.teleport(spawn);
				if (player.getWorld().getName().equals(worldname)) {
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						for (Player all : Bukkit.getOnlinePlayers()) {
							
							/*
							 * [TODO] Unless they are vanished, show the player.
							 */
							
							all.showPlayer(player);
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

