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
import org.bukkit.scoreboard.Team;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.modifiers.CombatTracker;
import net.nuggetmc.core.util.ColorCodes;

public class Sidebar {
	
	private Main plugin;
	
	public Sidebar(Main plugin) {
		this.plugin = plugin;
	}
	
	public void enable(Player player) {
		
		UUID uuid = player.getUniqueId();
		String playername = player.getName();
		
		if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
			
		}
		
		else {
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
			
			String status = ChatColor.GREEN + "Idle";
			
			if (CombatTracker.combatTime.containsKey(player)) {
				int combatTime = CombatTracker.combatTime.get(player) - 1;
				if (combatTime > 0) {
					status = ChatColor.RED + "In combat! " + ChatColor.GRAY + "(" + ChatColor.YELLOW + combatTime + "s" + ChatColor.GRAY + ")";
				}
			}
			
			Team nameDisplay = scoreboard.registerNewTeam("name");
			nameDisplay.addEntry(ChatColor.GOLD + playername);
			
			Team rankDisplay = scoreboard.registerNewTeam("rank");
			rankDisplay.addEntry(ChatColor.GRAY + " ▪ Rank: ");
			rankDisplay.setSuffix(rank);
			
			Team statusDisplay = scoreboard.registerNewTeam("status");
			statusDisplay.addEntry(ChatColor.GRAY + " ▪ Combat: " + ChatColor.YELLOW);
			statusDisplay.setSuffix(status);
			
			Team levelDisplay = scoreboard.registerNewTeam("level");
			levelDisplay.addEntry(ChatColor.GRAY + " ▪ Level: " + ChatColor.YELLOW);
			levelDisplay.setSuffix(String.valueOf(playerlevel));
			
			Team killsDisplay = scoreboard.registerNewTeam("kills");
			killsDisplay.addEntry(ChatColor.GRAY + " ▪ Kills: " + ChatColor.YELLOW);
			killsDisplay.setSuffix(playerkills);
			
			Team nuggetsDisplay = scoreboard.registerNewTeam("nuggets");
			nuggetsDisplay.addEntry(ChatColor.GRAY + " ▪ Nuggets: " + ChatColor.YELLOW);
			nuggetsDisplay.setSuffix(playernuggets);
			
			stats.getScore(ChatColor.WHITE + "" + ChatColor.GRAY + "" + ChatColor.BOLD + "-----------------").setScore(12);
			stats.getScore(ChatColor.GOLD + playername).setScore(11);
			stats.getScore(ChatColor.GRAY + " ▪ Rank: ").setScore(10);
			stats.getScore(ChatColor.GRAY + " ▪ Combat: " + ChatColor.YELLOW).setScore(9);
			stats.getScore("").setScore(8);
			stats.getScore(ChatColor.GOLD + "Stats").setScore(7);
			stats.getScore(ChatColor.GRAY + " ▪ Level: " + ChatColor.YELLOW).setScore(6);
			stats.getScore(ChatColor.GRAY + " ▪ Kills: " + ChatColor.YELLOW).setScore(5);
			stats.getScore(ChatColor.GRAY + " ▪ Nuggets: " + ChatColor.YELLOW).setScore(4);
			stats.getScore(" ").setScore(3);
			stats.getScore(ChatColor.GRAY + "nuggetmc.net").setScore(2);
			stats.getScore(ChatColor.GRAY + "" + ChatColor.BOLD + "-----------------").setScore(1);
			
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
