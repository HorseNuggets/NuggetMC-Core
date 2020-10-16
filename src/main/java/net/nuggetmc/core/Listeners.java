package net.nuggetmc.core;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.nuggetmc.core.commands.mod.BanCommand;
import net.nuggetmc.core.gui.EnderChest;
import net.nuggetmc.core.gui.GUIMain;
import net.nuggetmc.core.misc.FlyVanish;
import net.nuggetmc.core.misc.ItemEffects;

public class Listeners implements Listener {
	
	private Main plugin;
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void asyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		plugin.playerChat.onChat(event);
		return;
	}
	
	@EventHandler
	public void asyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
		BanCommand.onConnect(event);
		return;
	}
	
	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent event) {
		plugin.gheads.onHeadPlace(event);
		ItemEffects.boomBox(event);
		return;
	}
	
	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		plugin.combatTracker.playerCombat(event);
		plugin.combatTracker.playerCombatProjectiles(event);
		plugin.gheads.headDetectPhysical(event);
		ItemEffects.onEntityDamage(event);
		return;
	}
	
	@EventHandler
	public void entityDamageEvent(EntityDamageEvent event) {
		plugin.combatTracker.combatContinue(event);
		plugin.fallListener.onFall(event);
		return;
	}
	
	@EventHandler
	public void entityShootBowEvent(EntityShootBowEvent event) {
		ItemEffects.onEntityShootBow(event);
	}
	
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event) {
		plugin.guiMain.onClick(event);
		return;
	}
	
	@EventHandler
	public void inventoryCloseEvent(InventoryCloseEvent event) {
		plugin.guiMain.onClose(event);
		return;
	}
	
	@EventHandler
	public void inventoryOpenEvent(InventoryOpenEvent event) {
		plugin.guiMain.onOpen(event);
		return;
	}
	
	@EventHandler
	public void playerCommandPreProcess(PlayerCommandPreprocessEvent event) {
		plugin.combatTracker.inCombatCommand(event);
		return;
	}
	
	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) {
		plugin.autoRespawn.onPlayerDeath(event);
		plugin.combatTracker.onDeath(event);
		plugin.gheads.onDeath(event);
		plugin.guiMain.hardRemove(event.getEntity());
		plugin.playerKill.onKill(event);
		ItemEffects.onDeath(event);
	}
	
	@EventHandler
	public void playerInteractEntityEvent(PlayerInteractEntityEvent event) {
		ItemEffects.templar(event);
		return;
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		GUIMain.cancelAnvil(event);
		plugin.gheads.headDetectInteract(event);
		plugin.itemShop.transaction(event);
		ItemEffects.itemInteract(event);
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType() == Material.ENDER_CHEST) {
				event.setCancelled(true);
				
				Player player = event.getPlayer();
			    plugin.enderChest.vault(player);
			    
				Block block = event.getClickedBlock();
				Location loc = block.getLocation();
				EnderChest.activeChest.put(player, loc);

				Set<Player> plist = EnderChest.anim.get(loc);
				if (plist == null) {
					plist = new HashSet<>();
				}
				plist.add(player);
				EnderChest.anim.put(loc, plist);
				
				BlockPosition pos = new BlockPosition(block.getX(), block.getY(), block.getZ());
			    net.minecraft.server.v1_8_R3.World world = ((CraftWorld) loc.getWorld()).getHandle();
			    world.playBlockAction(pos, world.getType(pos).getBlock(), 1, 1);
			}
		}
		return;
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		event.setJoinMessage(null);
		plugin.playerSpawnLocation.setSpawn(player);
		plugin.healthboost.onJoin(event);
		plugin.moveListener.onJoin(event);
		//plugin.packetHandler.injectPlayer(player);
		plugin.playerJoin.onJoin(event);
		plugin.sidebar.enable(player);
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (FlyVanish.vanish.contains(all)) {
				player.hidePlayer(all);
			}
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				all.showPlayer(player);
			}, 16);
		}
		
		if (plugin.playerTracker != null) {
			plugin.playerTracker.onJoin(event);
		}
		return;
	}
	
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		plugin.moveListener.onMove(event);
		plugin.worldManager.onMove(event);
		FlyVanish.onPlayerMove(event);
		if (plugin.playerTracker != null) {
			plugin.playerTracker.onMove(event);
		}
		return;
	}
	
	@EventHandler
	public void playerPortalEvent(PlayerPortalEvent event) {
		plugin.worldManager.worldPortal(event);
		return;
	}
	
	@EventHandler
	public void playerTeleportEvent(PlayerTeleportEvent event) {
		plugin.moveListener.onTeleport(event);
		FlyVanish.onPlayerTeleport(event);
		return;
	}
	
	@EventHandler
	public void playerRespawnEvent(PlayerRespawnEvent event) {
		plugin.healthboost.onRespawn(event);
		plugin.kits.respawn(event);
		plugin.moveListener.onRespawn(event);
		plugin.playerSpawnLocation.setSpawn(event.getPlayer());
		return;
	}
	
	@EventHandler
	public void playerSpawnLocationEvent(PlayerSpawnLocationEvent event) {
		World main = Bukkit.getWorld("main");
		if (main != null) {
			event.setSpawnLocation(new Location(main, 0.5, 223, 0.5));
		}
		return;
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		
		Player player = event.getPlayer();
		Entity vehicle = player.getVehicle();
		if (vehicle != null) {
			vehicle.eject();
		}
		
		plugin.combatTracker.onQuit(event);
		plugin.guiMain.hardRemove(event.getPlayer());
		//plugin.packetHandler.removePlayer(player);
		if (plugin.playerTracker != null) {
			plugin.playerTracker.onLeave(event);
		}
		return;
	}
	
	@EventHandler
	public void projectileHitEvent(ProjectileHitEvent event) {
		ItemEffects.onProjectileHit(event);
		return;
	}
}
