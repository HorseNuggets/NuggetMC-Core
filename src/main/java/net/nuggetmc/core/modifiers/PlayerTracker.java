package net.nuggetmc.core.modifiers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.setup.WorldManager;

public class PlayerTracker implements Listener {
	
	private Main plugin;
	private HashMap<Player, Player> targets;
	private HashMap<Player, BukkitRunnable> task = new HashMap<Player, BukkitRunnable>();
	
	public PlayerTracker(Main plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		targets = new HashMap<Player, Player>();
	}
	
	public void delete(final Player player) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			if (targets.containsKey(player)) {
				targets.remove(player);
			}
			if (targets.containsValue(player)) {
				if (targets.containsKey(targets.get(player))) {
					targets.remove(targets.get(player));
				}
			}
		}, 5);
		return;
	}
	
	public void onLeave(PlayerQuitEvent event){
		for (Player key : targets.keySet()) {
			if (key == event.getPlayer() || (targets.get(key) == event.getPlayer())) {
				delete(key);
			}
		}
		return;
	}
	
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		task.put(player, new BukkitRunnable() {
			Player target;
			
			public void run() {
				if (Bukkit.getPlayer(player.getName()) == null) {
					task.remove(player);
					this.cancel();
	                return;
				}
				
				for (Player others : Bukkit.getOnlinePlayers()) {
					if (player.getWorld() == others.getWorld()) {
						if (player != others) {
							try {
								Location loc = others.getLocation();
								if (!WorldManager.isInSpawn(loc)) {
									if ((target == null) || player.getLocation().distance(loc) < target.getLocation().distance(player.getLocation())) {
										target = others;
									}
								}
							} catch (IllegalArgumentException e) {
								continue;
							}
						}
					}
				}
				
				if (target != null) {
					targets.put(player, target);
					if (!player.getCompassTarget().equals(targets.get(event.getPlayer()).getLocation())) {
						player.setCompassTarget(targets.get(event.getPlayer()).getLocation());
					}
				}
			}
		});
		
		task.get(player).runTaskTimerAsynchronously(plugin, 0, 10);
		return;
	}
}