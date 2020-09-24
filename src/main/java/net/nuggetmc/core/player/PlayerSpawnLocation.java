package net.nuggetmc.core.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;

public class PlayerSpawnLocation {
	
	private Main plugin;
	private Location spawn;
	private String worldname;
	
	public PlayerSpawnLocation(Main plugin) {
		this.plugin = plugin;
		spawnSetup();
	}
	
	public void spawnSetup() {
		worldname = plugin.configs.worldsettings.getConfig().getString("spawn.world");
		spawn = new Location(Bukkit.getWorld(worldname), 0.5, 223, 0.5);
	}
	
	public void setSpawn(Player player) {
		
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
					this.cancel();
					return;
				}
			}
		};
		
		teleportQueue.runTaskTimer(plugin, 10, 10);
		return;
	}
}

