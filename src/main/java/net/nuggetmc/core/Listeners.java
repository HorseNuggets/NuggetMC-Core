package net.nuggetmc.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.nuggetmc.core.commands.mod.BanCommand;
import net.nuggetmc.core.gui.GUIMain;

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
		return;
	}
	
	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		plugin.combatTracker.playerCombat(event);
		plugin.combatTracker.playerCombatProjectiles(event);
		plugin.gheads.headDetectPhysical(event);
		return;
	}
	
	@EventHandler
	public void entityDamageEvent(EntityDamageEvent event) {
		plugin.combatTracker.combatContinue(event);
		plugin.fallListener.onFall(event);
		return;
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
	public void playerChatTabCompleteEvent(PlayerChatTabCompleteEvent event) {
		plugin.tabComplete.tab(event);
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
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		GUIMain.cancelAnvil(event);
		plugin.gheads.headDetectInteract(event);
		plugin.itemShop.transaction(event);
		return;
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		plugin.playerSpawnLocation.setSpawn(event.getPlayer());
		plugin.healthboost.onJoin(event);
		plugin.moveListener.onJoin(event);
		plugin.packetHandler.injectPlayer(event.getPlayer());
		plugin.playerJoin.onJoin(event);
		plugin.sidebar.enable(event.getPlayer());
		if (plugin.playerTracker != null) {
			plugin.playerTracker.onJoin(event);
		}
		return;
	}
	
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		plugin.moveListener.onMove(event);
		plugin.worldManager.onMove(event);
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
		return;
	}
	
	@EventHandler
	public void playerRespawnEvent(PlayerRespawnEvent event) {
		plugin.combatTracker.onRespawn(event);
		plugin.healthboost.onRespawn(event);
		plugin.kits.respawn(event);
		plugin.moveListener.onRespawn(event);
		plugin.playerSpawnLocation.setSpawn(event.getPlayer());
		return;
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		plugin.combatTracker.onQuit(event);
		plugin.guiMain.hardRemove(event.getPlayer());
		plugin.packetHandler.removePlayer(event.getPlayer());
		if (plugin.playerTracker != null) {
			plugin.playerTracker.onLeave(event);
		}
		return;
	}
}
