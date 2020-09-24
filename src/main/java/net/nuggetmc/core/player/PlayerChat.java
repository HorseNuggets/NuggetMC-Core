package net.nuggetmc.core.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.nuggetmc.core.Main;

public class PlayerChat {
	
	public PlayerChat(Main plugin) {
		return;
	}
	
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName();
		String message = event.getMessage();
		
		event.setCancelled(true);
		
		message = playername + ChatColor.WHITE + ": " + message;
		
		if (player.isOp()) {
			// add ADMIN tag unless they are horsenuggets
		}
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.sendMessage(message);
		}
		
		Bukkit.getConsoleSender().sendMessage(message);
		return;
	}
}
