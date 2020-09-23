package net.nuggetmc.core.modifiers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import net.nuggetmc.core.Main;

public class HealthBoost {
	
	private Main plugin;
	
	public HealthBoost(Main plugin) {
		this.plugin = plugin;
	}

	public void healthSetup(final Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1000000, 4, true));
		player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 6));
		
		final Scoreboard scoreBoard = player.getScoreboard();
		
		if (scoreBoard.getObjective("health") == null) {
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
		
		/*
		 * POTENTIAL ELSE IF IT ENDS UP KEEPING PEOLPE'S HP AT ZERO
		 */
		
		return;
	}
	
	public void onJoin(PlayerJoinEvent event) {
		healthSetup(event.getPlayer());
	}
	
	public void onRespawn(final PlayerRespawnEvent event) {
		healthSetup(event.getPlayer());
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			healthSetup(event.getPlayer());
		}, 4);
	}
}