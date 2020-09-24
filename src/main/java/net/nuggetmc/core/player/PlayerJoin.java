package net.nuggetmc.core.player;

import org.bukkit.event.player.PlayerJoinEvent;

import net.nuggetmc.core.Main;

public class PlayerJoin {
	
	private Main plugin;
	
	public PlayerJoin(Main plugin) {
		this.plugin = plugin;
	}
	
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		return;
	}
}
