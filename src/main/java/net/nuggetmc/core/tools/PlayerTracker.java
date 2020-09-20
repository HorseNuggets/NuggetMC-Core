package net.nuggetmc.core.tools;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;

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
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		for (Player key : targets.keySet()) {
			if (key == event.getPlayer() || (targets.get(key) == event.getPlayer())) {
				delete(key);
			}
		}
		return;
	}
	
	@EventHandler 
	public void onPlayerJoin(final PlayerJoinEvent event) {
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
					if (player.getWorld() != others.getWorld()) continue;
					if (player == others) continue;
					if ((target == null) || player.getLocation().distance(others.getLocation()) < target.getLocation().distance(player.getLocation())) {
						target = others;
					}
				}
				
				if (target == null) return;
				targets.put(player, target);
				if (!player.getCompassTarget().equals(targets.get(event.getPlayer()).getLocation())) {
					player.setCompassTarget(targets.get(event.getPlayer()).getLocation());
				}
			}
		});
		
		task.get(player).runTaskTimer(plugin, 0, 10);
		return;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if (targets.get(event.getPlayer()) != null) {
			if (!event.getPlayer().getCompassTarget().equals(targets.get(event.getPlayer()).getLocation())) {
				event.getPlayer().setCompassTarget(targets.get(event.getPlayer()).getLocation());
			}
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			Player target = targets.get(player);
			if (target == event.getPlayer()){
				if (player.getWorld() == event.getPlayer().getWorld()){
					player.setCompassTarget(event.getPlayer().getLocation());
				}
				else {
					targets.remove(player);
				}
			}
		}
		return;
	}
}