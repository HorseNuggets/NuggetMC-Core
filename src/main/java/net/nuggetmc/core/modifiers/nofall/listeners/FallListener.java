package net.nuggetmc.core.modifiers.nofall.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.nuggetmc.core.Main;

public class FallListener {

	private Main plugin;

	public FallListener(Main plugin) {
		this.plugin = plugin;
	}

	public void onFall(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && event.getCause() == DamageCause.FALL) {
			Player player = (Player) event.getEntity();
			
			for (int i = 0; i < plugin.noFall.worlds.size(); i++) {
				if (player.getWorld().getName().equals(plugin.noFall.worlds.get(i))) {
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
