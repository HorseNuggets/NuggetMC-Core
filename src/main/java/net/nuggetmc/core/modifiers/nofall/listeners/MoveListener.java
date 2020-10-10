package net.nuggetmc.core.modifiers.nofall.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class MoveListener implements Listener {
	
	private Main plugin;
	
	public MoveListener(Main plugin) {
		this.plugin = plugin;
		run();
	}
	
	public void unassign(Player player) {
		
		for (int i = 0; i < plugin.noFall.worlds.size(); i++) {
			if (player.getWorld().getName().equals(plugin.noFall.worlds.get(i))) {
				if (player.getLocation().getBlockY() >= Configs.nofall.getConfig().getInt("y-level")) {
					if (plugin.noFall.fallList.contains(player)) {
						plugin.noFall.fallList.remove(player);
						
						if (plugin.noFall.downTime.containsKey(player)) {
							plugin.noFall.downTime.remove(player);
						}
					}
				}
				break;
			}
		}
		return;
	}
	
	public void run() {
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					for (int i = 0; i < plugin.noFall.worlds.size(); i++) {
						if (player.getWorld().getName().equals(plugin.noFall.worlds.get(i))) {
							if (player.getLocation().getBlockY() < Configs.nofall.getConfig().getInt("y-level")) {
								if (!plugin.noFall.fallList.contains(player)) {
									if (!plugin.noFall.downTime.containsKey(player)) {
										plugin.noFall.downTime.put(player, (byte) 0);
									}
									else {
										plugin.noFall.downTime.put(player, (byte) (plugin.noFall.downTime.get(player) + 1));
									}
									
									if (plugin.noFall.downTime.get(player) >= 5) {
										plugin.noFall.fallList.add(player);
									}
								}
							}
						}
					}
				}
			}
		};
		
		runnable.runTaskTimerAsynchronously(plugin, 0, 20);
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
