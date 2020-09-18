package net.nuggetmc.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Listeners implements Listener {
	
	private Main plugin;
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void entityDamageEvent(EntityDamageEvent event) {
		plugin.fallListener.onFall(event);
		return;
	}
	
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		plugin.moveListener.onMove(event);
		return;
	}
	
	@EventHandler
	public void playerTeleportEvent(PlayerTeleportEvent event) {
		plugin.moveListener.onTeleport(event);
		return;
	}
	
	@EventHandler
	public void playerRespawnEvent(PlayerRespawnEvent event) {
		plugin.moveListener.onRespawn(event);
		return;
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		plugin.moveListener.onJoin(event);
		return;
	}
}
