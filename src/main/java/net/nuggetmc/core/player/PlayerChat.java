package net.nuggetmc.core.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.Checks;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.TimeConverter;

public class PlayerChat {
	
	private Main plugin;
	private FileConfiguration mutes;
	
	public PlayerChat(Main plugin) {
		this.plugin = plugin;
		this.mutes = Configs.mutes.getConfig();
	}
	
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		event.setCancelled(true);
		
		if (mutes.contains(uuid.toString())) {
			int remaining = (int) (mutes.getLong(uuid + ".exp") - (System.currentTimeMillis() / 1000));
			if (remaining <= 0) {
				plugin.muteCommand.unmute(uuid, null, player.getName());
			} else {
				player.sendMessage(ChatColor.RED + "You are server muted for the next"
						+ ChatColor.YELLOW + TimeConverter.intToString(remaining) + ChatColor.RED + ".");
				return;
			}
		}
		
		String playername = player.getName();
		String message = event.getMessage();
		
		message = playername + ChatColor.WHITE + ": " + message;
		
		int playerkillsnum = Configs.playerstats.getConfig().getInt("players." + uuid + ".kills");
		plugin.playerStats.allign(player, uuid, playerkillsnum);
		int playerlevel = Configs.playerstats.getConfig().getInt("players." + uuid + ".level");
		
		message = ChatColor.DARK_GRAY + "[" + ColorCodes.levelToTag(playerlevel) + ChatColor.DARK_GRAY + "] " + ColorCodes.rankNameTag(uuid) + message;
		
		/*
		 * [TODO]
		 * Later add tags before the :
		 */
		
		if (Checks.checkXDD(player)) {
			message = message.replaceAll("&", "§");
		}
		
		if (Checks.checkStaff(player)) {
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(message);
			}
		}
		
		else {
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (!Configs.ignore.getConfig().getStringList(all.getUniqueId().toString()).contains(uuid.toString())) {
					all.sendMessage(message);
				}
			}
		}
		
		Bukkit.getConsoleSender().sendMessage(message);
		return;
	}
}
