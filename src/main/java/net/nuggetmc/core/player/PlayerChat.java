package net.nuggetmc.core.player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private List<String> filter;
	private Map<Player, Long> msgTime;
	private Map<Player, String> msgPrev;
	private Map<Player, Long> msgPrevTime;
	
	public PlayerChat(Main plugin) {
		this.plugin = plugin;
		this.mutes = Configs.mutes.getConfig();
		this.msgTime = new HashMap<>();
		this.msgPrev = new HashMap<>();
		this.msgPrevTime = new HashMap<>();
		this.setupFilter();
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
		
		String message = event.getMessage();
		
		if (!Checks.checkStaff(player)) {
			if (msgTime.containsKey(player)) {
				Long difference = msgTime.get(player) - System.currentTimeMillis() / 1000;
				if (difference > 0) {
					String s = "s";
					if (difference == 1) s = "";
					player.sendMessage(ChatColor.RED + "You are chatting too fast. Try again in " + ChatColor.YELLOW
							+ difference + ChatColor.RED + " second" + s + ".");
					return;
				}
				
				else {
					msgTime.remove(player);
				}
			}
			
			if (msgPrev.containsKey(player)) {
				if (message.equals(msgPrev.get(player))) {
					if (msgPrevTime.containsKey(player)) {
						Long difference = msgPrevTime.get(player) - System.currentTimeMillis() / 1000;
						if (difference > 0) {
							player.sendMessage(ChatColor.RED + "You can't say the same message twice!");
							return;
						}
						
						else {
							msgPrevTime.remove(player);
						}
					}
				}
			}
			
			for (String i : filter) {
				if (message.toLowerCase().replaceAll(" ", "").contains(i)) {
					player.sendMessage(ChatColor.YELLOW + "Omg that's racist!!!!1 1");
					return;
				}
			}
		}

		String playername = player.getName();
		String raw = message;
		String start = "";
		
		start = playername + ChatColor.WHITE + ": ";
		
		int playerkillsnum = Configs.playerstats.getConfig().getInt("players." + uuid + ".kills");
		plugin.playerStats.allign(player, uuid, playerkillsnum);
		int playerlevel = Configs.playerstats.getConfig().getInt("players." + uuid + ".level");
		
		start = ChatColor.DARK_GRAY + "[" + ColorCodes.levelToTag(playerlevel) + ChatColor.DARK_GRAY + "] " + ColorCodes.rankNameTag(uuid) + start;
		message = start + message;
		
		/*
		 * [TODO]
		 * Later add tags before the :
		 */
		
		if (Checks.checkXDD(player)) {
			message = message.replaceAll("&", "§");
		}
		
		if (Checks.checkStaff(player)) {
			if (raw.startsWith("#")) {
				raw = raw.substring(1);
				if (raw.startsWith(" ")) {
					raw = raw.substring(1);
				}
				if (!raw.equals("")) {
					staffChat(player, start, raw);
					return;
				}
			}
			
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
			
			msgTime.put(player, 2 + System.currentTimeMillis() / 1000);
			msgPrevTime.put(player, 30 + System.currentTimeMillis() / 1000);
			msgPrev.put(player, raw);
		}
		
		Bukkit.getConsoleSender().sendMessage(message);
		return;
	}
	
	private void setupFilter() {
		this.filter = Arrays.asList("nigga", "nigger");
		return;
	}
	
	private void staffChat(Player player, String start, String message) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (Checks.checkStaff(all)) {
				all.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "STAFF" + ChatColor.DARK_GRAY + "] "
						+ ChatColor.RESET + start + ChatColor.AQUA + message);
			}
		}
		return;
	}
}