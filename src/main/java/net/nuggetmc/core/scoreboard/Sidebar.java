package net.nuggetmc.core.scoreboard;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.ColorCodes;

public class Sidebar {
	
	private Main plugin;
	
	public Sidebar(Main plugin) {
		this.plugin = plugin;
	}
	
	public void enable(Player player) {
		
		/*
		 * [TODO]
		 * 
		 * Check if stats is registered or not, then build off of that
		 * Use an array for the scoreboard
		 * Eventually make the scoreboard able to be modified from config
		 */
		
		UUID uuid = player.getUniqueId();
		String playername = player.getName();
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		int playerkillsnum = Configs.playerstats.getConfig().getInt("players." + uuid + ".kills");
		int playernuggetsnum = Configs.playerstats.getConfig().getInt("players." + uuid + ".nuggets");
		
		plugin.playerStats.allign(player, uuid, playerkillsnum);
		
		String playerkills = NumberFormat.getNumberInstance(Locale.US).format(playerkillsnum);
		String playernuggets = NumberFormat.getNumberInstance(Locale.US).format(playernuggetsnum);
		
		String rank = ColorCodes.rankNameSidebar(uuid);
		
		Objective stats = scoreboard.registerNewObjective("stats", "dummy");
		stats.setDisplayName("§6§l NuggetMC §r");
		stats.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		int playerlevel = Configs.playerstats.getConfig().getInt("players." + uuid + ".level");
		
		if (playerkills.equals("69")) playerkills += ChatColor.LIGHT_PURPLE + " (NICE)";
		if (playernuggets.equals("69")) playernuggets += ChatColor.LIGHT_PURPLE + " (NICE)";
		
		Score[] lines = new Score[15];
		lines[12] = stats.getScore(ChatColor.WHITE + "" + ChatColor.GRAY + "" + ChatColor.BOLD + "----------------");
		lines[11] = stats.getScore(ChatColor.GOLD + playername);
		lines[10] = stats.getScore(ChatColor.GRAY + " ▪ " + ChatColor.GRAY + "Rank: " + rank);
		lines[9] = stats.getScore(ChatColor.GRAY + " ▪ " + ChatColor.GRAY + "Status: " + ChatColor.GREEN + "Idle");
		lines[8] = stats.getScore("");
		lines[7] = stats.getScore(ChatColor.GOLD + "Stats");
		lines[6] = stats.getScore(ChatColor.GRAY + " ▪ " + ChatColor.GRAY + "Level: " + ChatColor.YELLOW + playerlevel);
		lines[5] = stats.getScore(ChatColor.GRAY + " ▪ " + ChatColor.GRAY + "Kills: " + ChatColor.YELLOW + playerkills);
		lines[4] = stats.getScore(ChatColor.GRAY + " ▪ " + ChatColor.GRAY + "Nuggets: " + ChatColor.YELLOW + playernuggets);
		lines[3] = stats.getScore(" ");
		lines[2] = stats.getScore(ChatColor.GRAY + "nuggetmc.net");
		lines[1] = stats.getScore(ChatColor.GRAY + "" + ChatColor.BOLD +                        "----------------");
		
		for (int i = 1; i < 13; i++) {
			lines[i].setScore(i);
		}
		
		final Objective health = scoreboard.registerNewObjective("hp", "health");
		health.setDisplayName(ChatColor.RED + "HP");
		health.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			for (Player all : Bukkit.getOnlinePlayers()) {
				Score hp = health.getScore(all.getName());
				if (hp.getScore() == 0)
					hp.setScore((int) all.getHealth());
			}
		}, 1);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			player.setScoreboard(scoreboard);
		}, 2);
	}
	
	public void refreshRank(CommandSender sender, String playername) {
		Player player = Bukkit.getPlayer(playername);
		if (player == null) {
			return;
		}
		else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				enable(player);
			}, 10);
		}
		return;
	}
}
