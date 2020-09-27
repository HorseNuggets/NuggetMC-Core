package net.nuggetmc.core.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.ColorCodes;

public class PlayerChat {
	
	private Main plugin;
	
	public PlayerChat(Main plugin) {
		this.plugin = plugin;
	}
	
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String playername = player.getName();
		String message = event.getMessage();
		
		event.setCancelled(true);
		
		message = playername + ChatColor.WHITE + ": " + message;
		
		int playerkillsnum = Configs.playerstats.getConfig().getInt("players." + uuid + ".kills");
		plugin.playerStats.allign(player, uuid, playerkillsnum);
		int playerlevel = Configs.playerstats.getConfig().getInt("players." + uuid + ".level");
		
		message = ChatColor.DARK_GRAY + "[" + ColorCodes.levelToTag(playerlevel) + ChatColor.DARK_GRAY + "] " + ColorCodes.rankNameTag(uuid) + " " + message;
		
		/*
		 * [TODO]
		 * 
		 * Later add tags before the :
		 * Figure out why data from config isn't loading, try the whole string method
		 * 
		 */
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.sendMessage(message);
		}
		
		Bukkit.getConsoleSender().sendMessage(message);
		return;
	}
}
