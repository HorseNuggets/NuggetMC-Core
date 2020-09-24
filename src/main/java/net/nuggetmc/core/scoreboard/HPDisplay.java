package net.nuggetmc.core.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import net.nuggetmc.core.Main;

public class HPDisplay {
	private Main plugin;
	
	public HPDisplay(Main plugin) {
		this.plugin = plugin;
	}
	
	public void enable(Player player) {
		
		final Scoreboard scoreBoard = player.getScoreboard();
		
		if (scoreBoard.getObjective("hp") == null) {
			final Objective obj = scoreBoard.registerNewObjective("hp", "health");
			obj.setDisplayName(ChatColor.RED + "HP");
			obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				for (Player all : Bukkit.getOnlinePlayers()) {
					Score hp = obj.getScore(all.getName());
					if (hp.getScore() == 0)
						hp.setScore((int) all.getHealth());
				}
			}, 1);
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				player.setScoreboard(scoreBoard);
			}, 2);
		}
		return;
	}
}
