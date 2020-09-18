package net.nuggetmc.core.dnf.listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;

public class MoveListener implements Listener {
	
	private Main plugin;
	private ConfigManager configManager;
	
	public MoveListener(Main plugin) {
		this.plugin = plugin;
		configManager = new ConfigManager(plugin);
		configManager.setup("nofall\\config.yml");
	}
	
	public void unassign(Player player) {
		List<String> worlds = configManager.getConfig().getStringList("world-names");
		for (int i = 0; i < worlds.size(); i++) {
			if (player.getWorld().getName().equals("world")) {
				if (player.getLocation().getBlockY() >= configManager.getConfig().getInt("y-level")) {
					if (plugin.noFall.fallList.contains(player)) {
						plugin.noFall.fallList.remove(player);
					}
				}
				break;
			}
		}
		return;
	}
	
	public void onMove(PlayerMoveEvent event) {
		unassign(event.getPlayer());
		return;
	}
	
	public void onTeleport(PlayerTeleportEvent event) {
		unassign(event.getPlayer());
		return;
	}
	
	public void onRespawn(PlayerRespawnEvent event) {
		unassign(event.getPlayer());
		return;
	}
	
	public void onJoin(PlayerJoinEvent event) {
		unassign(event.getPlayer());
		return;
	}
}
