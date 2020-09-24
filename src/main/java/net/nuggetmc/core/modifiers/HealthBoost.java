package net.nuggetmc.core.modifiers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.scoreboard.HPDisplay;

public class HealthBoost {
	
	private Main plugin;
	private HPDisplay hpdisplay;
	
	public HealthBoost(Main plugin) {
		this.plugin = plugin;
		this.hpdisplay = new HPDisplay(plugin);
	}

	public void healthSetup(final Player player) {
		if (!player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1000000, 4, true));
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 6));
		hpdisplay.enable(player);
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