package net.nuggetmc.core.modifiers.gheads;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.modifiers.gheads.setup.GHeadCraft;
import net.nuggetmc.core.modifiers.gheads.setup.GHeadEffects;
import net.nuggetmc.core.modifiers.gheads.util.TextureProfileField;

public class GHeads {
	
	private FileConfiguration config;
	
	public GHeadCraft gheadCraft;
	public GHeadEffects gheadEffects;
	
	public GHeads(Main plugin) {
		this.config = Configs.gheads.getConfig();
		this.gheadEffects = new GHeadEffects();
		this.setup();
	}
	
	private void setup() {
		if (config.getBoolean("gheads.craftable")) {
			GHeadCraft.setup();
		}
		return;
	}
	
	public void headDetectInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().getType() == Material.SKULL_ITEM) {
			gheadEffects.eatHeadSort(player);
		}
		return;
	}
	
	public void headDetectPhysical(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (player.getItemInHand().getType() == Material.SKULL_ITEM) {
				gheadEffects.eatHeadSort(player);
			}
		}
		return;
	}
	
	public void onHeadPlace(BlockPlaceEvent event) {
		if (event.getBlock().getType() == Material.SKULL) {
			if (config.getBoolean("heads.enabled") || config.getBoolean("gheads.enabled")) {
				if (config.getBoolean("heads.edible") || config.getBoolean("gheads.edible")) {
					event.setCancelled(true);
				}
			}
		}
		return;
	}
	
	public void onDeath(PlayerDeathEvent event) {
		if (config.getBoolean("heads.enabled")) {
			if (config.getBoolean("heads.drop-heads")) {
				if (event.getEntity() instanceof Player) {
					
					Player player = event.getEntity();
					Location location = player.getLocation();
					location.setY(location.getY() + 1);
					
					TextureProfileField TextureProfileField = new TextureProfileField();
					player.getWorld().dropItemNaturally(location, TextureProfileField.headPlayer(player));
				}
			}
		}
		return;
	}
}
