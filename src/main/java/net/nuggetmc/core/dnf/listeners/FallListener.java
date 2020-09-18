package net.nuggetmc.core.dnf.listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;

public class FallListener {
	
	private Main plugin;
	private ConfigManager configManager;
	
	public FallListener(Main plugin) {
		this.plugin = plugin;
		configManager = new ConfigManager(plugin);
		configManager.setup("nofall\\config.yml");
	}
	
	public void onFall(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && event.getCause() == DamageCause.FALL) {
			Player player = (Player) event.getEntity();
			List<String> worlds = configManager.getConfig().getStringList("world-names");
			for (int i = 0; i < worlds.size(); i++) {
				if (player.getWorld().getName().equals("world")) {
					if (!plugin.noFall.fallList.contains(player)) {
						plugin.noFall.fallList.add(player);
						event.setCancelled(true);
					}
					break;
				}
			}
		}
		return;
	}
}
