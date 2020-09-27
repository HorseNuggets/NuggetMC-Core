package net.nuggetmc.core.modifiers.autorespawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class AutoRespawn {
	
	private Main plugin;
	private int ticks;

	public AutoRespawn(Main plugin) {
		this.plugin = plugin;
		this.ticks = Configs.mainconfig.getConfig().getInt("autorespawn-ticks");
	}

	public void onPlayerDeath(final PlayerDeathEvent event) {
		final Location deathLoc = event.getEntity().getLocation();
		final Player player = event.getEntity();

		PlayerPreAutoRespawnEvent ppare = new PlayerPreAutoRespawnEvent(player, deathLoc);
		Bukkit.getPluginManager().callEvent((Event) ppare);
		if (ppare.isCancelled())
			return;

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			if (player.isOnline()) {
				player.spigot().respawn();
				Location respawnLoc = event.getEntity().getLocation();
				Bukkit.getPluginManager().callEvent((Event) new PlayerAutoRespawnEvent(event.getEntity(), deathLoc, respawnLoc));
			}
		}, ticks);
	}
}