package net.nuggetmc.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Listeners implements Listener {
	
	private Main plugin;
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void asyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		plugin.playerChat.onChat(event);
		return;
	}
	
	@EventHandler
	public void entityDamageEvent(EntityDamageEvent event) {
		plugin.fallListener.onFall(event);
		return;
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		plugin.healthboost.onJoin(event);
		plugin.moveListener.onJoin(event);
		plugin.packetHandler.injectPlayer(event.getPlayer());
		plugin.playerJoin.onJoin(event);
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
		plugin.healthboost.onRespawn(event);
		plugin.moveListener.onRespawn(event);
		return;
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event) {
		plugin.packetHandler.removePlayer(event.getPlayer());
		return;
	}
}
