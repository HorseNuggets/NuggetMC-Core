package net.nuggetmc.core;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.nuggetmc.core.commands.mod.BanCommand;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.events.FFADeathmatch;
import net.nuggetmc.core.events.Tournament;
import net.nuggetmc.core.gui.EnderChest;
import net.nuggetmc.core.gui.GUIMain;
import net.nuggetmc.core.misc.Credits;
import net.nuggetmc.core.misc.FlyVanish;
import net.nuggetmc.core.misc.ItemEffects;
import net.nuggetmc.core.modifiers.CombatTracker;
import net.nuggetmc.core.scoreboard.Sidebar;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.ItemSerializers;

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
	public void blockBreakEvent(BlockBreakEvent event) {
		if (FFADeathmatch.phase != 0) {
			FFADeathmatch.onBlockBreak(event);
		}
		if (Tournament.enabled) plugin.trn.onBlockBreak(event);
		return;
	}
	
	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent event) {
		plugin.gheads.onHeadPlace(event);
		ItemEffects.boomBox(event);
		if (FFADeathmatch.phase != 0) {
			FFADeathmatch.onBlockPlace(event);
		}
		if (Tournament.enabled) plugin.trn.onBlockPlace(event);
		return;
	}
	
	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		plugin.combatTracker.playerCombat(event);
		plugin.combatTracker.playerCombatProjectiles(event);
		plugin.gheads.headDetectPhysical(event);
		ItemEffects.onEntityDamage(event);
		if (FFADeathmatch.phase != 0) {
			FFADeathmatch.onPlayerDamage(event);
		}
		if (Tournament.enabled) plugin.trn.onPlayerDamage(event);
		return;
	}
	
	@EventHandler
	public void entityDamageEvent(EntityDamageEvent event) {
		plugin.combatTracker.combatContinue(event);
		plugin.fallListener.onFall(event);
		if (FFADeathmatch.phase != 0) {
			FFADeathmatch.onPlayerDamage2(event);
		}
		if (Tournament.enabled) plugin.trn.onPlayerDamage2(event);
		if (event.getEntity() instanceof Player) {
			if (event.getCause() != DamageCause.ENTITY_ATTACK) {
				Player victim = (Player) event.getEntity();
				if (victim.getMaximumNoDamageTicks() == 1) {
					victim.setMaximumNoDamageTicks(20);
					victim.setNoDamageTicks(20);
				}
			}
		}
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
	public void playerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
		if (FFADeathmatch.phase != 0) {
			FFADeathmatch.onBucketEmpty(event);
		}
		if (Tournament.enabled) plugin.trn.onBucketEmpty(event);
		return;
	}
	
	@EventHandler
	public void playerCommandPreProcess(PlayerCommandPreprocessEvent event) {
		plugin.combatTracker.inCombatCommand(event);
		return;
	}
	
	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			player.spigot().respawn();
		}, 12);
		
		Entity vehicle = player.getVehicle();
		if (vehicle != null) {
			vehicle.eject();
		}
		
		plugin.combatTracker.onDeath(event);
		plugin.gheads.onDeath(event);
		plugin.guiMain.hardRemove(player);
		plugin.playerKill.onKill(event);
		if (FFADeathmatch.phase != 0) {
			FFADeathmatch.onDeath(event);
		}
		if (Tournament.enabled) plugin.trn.onDeath(event);
		ItemEffects.onDeath(event);
	}
	
	@EventHandler
	public void playerInteractEntityEvent(PlayerInteractEntityEvent event) {
		ItemEffects.templar(event);
		return;
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@EventHandler
	public void playerItemConsumeEvent(PlayerItemConsumeEvent event) {
		ItemEffects.eat(event);
		return;
	}
	
	@EventHandler
	public void playerItemDamageEvent(PlayerItemDamageEvent event) {
		ItemEffects.durability(event);
		return;
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		plugin.playerJoin.onJoin(event);
		Player player = event.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			if (player.isDead()) {
				player.spigot().respawn();
			}
		}, 12);
		
		event.setJoinMessage(null);
		plugin.playerSpawnLocation.setSpawn(player);
		plugin.healthboost.onJoin(event);
		plugin.moveListener.onJoin(event);
		//plugin.packetHandler.injectPlayer(player);
		Sidebar.enable(player, (byte) 0);
		Credits.onJoin(event);
		
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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerKickEvent(PlayerKickEvent event) {
		CombatTracker.onKick(event);
		return;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerCombatQuitEvent(PlayerQuitEvent event) {
		CombatTracker.onQuit(event);
		return;
	}
	
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		plugin.moveListener.onMove(event);
		plugin.worldManager.onMove(event);
		if (FFADeathmatch.phase != 0) {
			FFADeathmatch.onMove(event);
		}
		if (Tournament.enabled) plugin.trn.onMove(event);
		FlyVanish.onPlayerMove(event);
		return;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerMoveEventLazy(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (WorldManager.isInArena(player.getLocation())) {
			arenaInvSet(player);
		}
		else {
			arenaInvRestore(player);
		}
		return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerPortalEvent(PlayerPortalEvent event) {
		plugin.worldManager.worldPortal(event);
		return;
	}
	
	@EventHandler
	public void playerTeleportEvent(PlayerTeleportEvent event) {
		plugin.moveListener.onTeleport(event);
		if (FFADeathmatch.phase != 0) {
			FFADeathmatch.onTeleport(event);
		}
		if (Tournament.enabled) plugin.trn.onTeleport(event);
		FlyVanish.onPlayerTeleport(event);
		
		Player player = event.getPlayer();
		if (WorldManager.isInArena(event.getTo())) {
			arenaInvSet(player);
		}
		else {
			arenaInvRestore(player);
		}
		return;
	}
	
	@EventHandler
	public void playerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		plugin.healthboost.onRespawn(event);
		plugin.kits.respawn(event);
		plugin.moveListener.onRespawn(event);
		plugin.playerSpawnLocation.setSpawn(player);
		Team display = player.getScoreboard().getTeam("status");
		if (display != null) {
			String output = ChatColor.GREEN + "Idle";
			display.setSuffix(output);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			final Team displayA = player.getScoreboard().getTeam("status");
			if (displayA != null) {
				String output = ChatColor.GREEN + "Idle";
				displayA.setSuffix(output);
			}
		}, 5);
		arenaInvRestore(player);
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
		
		if (FFADeathmatch.phase != 0) {
			FFADeathmatch.onQuit(event);
		}
		
		if (Tournament.enabled) plugin.trn.onQuit(event);
		plugin.guiMain.hardRemove(event.getPlayer());
		//plugin.packetHandler.removePlayer(player);
		if (plugin.playerTracker != null) {
			plugin.playerTracker.onLeave(event);
		}
		return;
	}
	
	@EventHandler
	public void potionSplashEvent(PotionSplashEvent event) {
		ItemEffects.splashPotion(event);
		return;
	}
	
	@EventHandler
	public void projectileHitEvent(ProjectileHitEvent event) {
		ItemEffects.onProjectileHit(event);
		return;
	}
	
	@EventHandler
	public void votifierEvent(VotifierEvent event) {
		try {
			Vote vote = event.getVote();
			String playername = vote.getUsername();
			Player player = Bukkit.getPlayer(playername);
			
			if (player != null) {
				Bukkit.broadcastMessage("§8[§4Alert§8] §f" + ColorCodes.colorName(player.getUniqueId(), player.getName()) + " §fjust voted and won §a1 Uncommon Nugget§f!");
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + playername + " gold_nugget 1 0 {display:{Name:\"§aUncommon Nugget§r\",Lore:[\"§7A pretty fine piece of§r\",\"§7nugget if you ask me.§r\"]},ench:[{id:51,lvl:1}],HideFlags:1}");
			}
		} catch (Exception e) {
			return;
		}
		return;
	}
	
	private void arenaInvRestore(Player player) {
		String uid = player.getUniqueId().toString();
		if (Configs.inventories.getConfig().contains(uid)) {
			ItemStack items[] = ItemSerializers.stringToItems(Configs.inventories.getConfig().getString(uid + ".items"));
			ItemStack armor[] = ItemSerializers.stringToItems(Configs.inventories.getConfig().getString(uid + ".armor"));
			player.getInventory().setContents(items);
			player.getInventory().setArmorContents(armor);
			Configs.inventories.getConfig().set(uid, null);
			Configs.inventories.saveConfig();
		}
		return;
	}
	
	private void arenaInvSet(Player player) {
		String uid = player.getUniqueId().toString();
		if (!Configs.inventories.getConfig().contains(uid)) {
			ItemStack[] invcontent = player.getInventory().getContents();
			ItemStack[] armorcontent = player.getInventory().getArmorContents();
	
			Configs.inventories.getConfig().set(uid + ".items", ItemSerializers.itemsToString(invcontent));
			Configs.inventories.getConfig().set(uid + ".armor", ItemSerializers.itemsToString(armorcontent));
			Configs.inventories.saveConfig();
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				ItemStack items[] = ItemSerializers.stringToItems("rO0ABXVyABBbTGphdmEudXRpbC5NYXA7/+CwhupHTAsCAAB4cAAAACRzcgAXamF2YS51dGlsLkxpbmtlZEhhc2hNYXA0wE5cEGzA+wIAAVoAC2FjY2Vzc09yZGVyeHIAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAAJ0AAR0eXBldAANRElBTU9ORF9TV09SRHQABG1ldGFzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAl4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAJ0AAltZXRhLXR5cGV0AAhlbmNoYW50c3VxAH4ACwAAAAJ0AApVTlNQRUNJRklDc3IAN2NvbS5nb29nbGUuY29tbW9uLmNvbGxlY3QuSW1tdXRhYmxlQmlNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAHhxAH4ACHVxAH4ACwAAAAF0AApEQU1BR0VfQUxMdXEAfgALAAAAAXNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEeABzcQB+AAI/QAAAAAAADHcIAAAAEAAAAAFxAH4ABXQAC0ZJU0hJTkdfUk9EeABzcQB+AAI/QAAAAAAADHcIAAAAEAAAAAJxAH4ABXQAA0JPV3EAfgAHc3EAfgAIdXEAfgALAAAAAnEAfgANcQB+AA51cQB+AAsAAAACcQB+ABBzcQB+ABF1cQB+AAsAAAABdAAMQVJST1dfREFNQUdFdXEAfgALAAAAAXNxAH4AFgAAAAJ4AHNxAH4AAj9AAAAAAAAMdwgAAAAQAAAAAnEAfgAFdAAMR09MREVOX0FQUExFdAAGYW1vdW50c3EAfgAWAAAABngAc3EAfgACP0AAAAAAAAx3CAAAABAAAAAEcQB+AAV0AApTS1VMTF9JVEVNdAAGZGFtYWdlc3IAD2phdmEubGFuZy5TaG9ydGhNNxM0YNpSAgABUwAFdmFsdWV4cQB+ABcAA3EAfgAnc3EAfgAWAAAAA3EAfgAHc3EAfgAIdXEAfgALAAAAA3EAfgANdAAMZGlzcGxheS1uYW1ldAAIaW50ZXJuYWx1cQB+AAsAAAADdAAFU0tVTEx0AA7Cp2VHb2xkZW4gSGVhZHQBYEg0c0lBQUFBQUFBQUFFMVBTMCtEUUJqOE5ERkI0cy93U3JJOExRY1BSbUs3cEN4Q2wwZjNWbUFKajZVMkZCcmhkL2tEWFc5T01wbE1adVl3S29BS1Q0ZCtGdUp6L0twYndSVzR4eFU4bXhzTC9VSGJsRFhTZEwyb3RNSXd1YWFqd25XNHhXdUVLaFZVT2Jyd2NXcjU5UkdVaVg5UDg4aXZLZ0RjS2ZDUW5zVE00WWN2UG1KNWc2cmNGK1dDSGVucEFZa1FkNWNYZkU2WDRoMDdlSkQ1N3MzWkwrNi9yajJkTWxzY1RiOWg1Mmd1aGhUdHpWandYYXlYUTNKak5ESkR5cm9naS91UUJpYnhlb05zOFVveVNhbkJpbTFHL1o1UTByRHQwV0plSkR1bFRpanJpZkhSRTg4WHdacllMSXM3WWlRcnl5SWphSDIzenRHcmZBQy9TeXFSZlJ3QkFBQT14AHNxAH4AAj9AAAAAAAAMdwgAAAAQAAAAAnEAfgAFdAAEV09PRHEAfgAnc3EAfgAWAAAAQHgAc3EAfgACP0AAAAAAAAx3CAAAABAAAAACcQB+AAV0AAtDT0JCTEVTVE9ORXEAfgAncQB+ADl4AHNxAH4AAj9AAAAAAAAMdwgAAAAQAAAAAXEAfgAFdAAMV0FURVJfQlVDS0VUeABzcQB+AAI/QAAAAAAADHcIAAAAEAAAAAFxAH4ABXQAC0xBVkFfQlVDS0VUeABzcQB+AAI/QAAAAAAADHcIAAAAEAAAAAFxAH4ABXEAfgA9eABzcQB+AAI/QAAAAAAADHcIAAAAEAAAAAFxAH4ABXEAfgA/eABzcQB+AAI/QAAAAAAADHcIAAAAEAAAAAFxAH4ABXQACElST05fQVhFeABzcQB+AAI/QAAAAAAADHcIAAAAEAAAAAFxAH4ABXQADElST05fUElDS0FYRXgAc3EAfgACP0AAAAAAAAx3CAAAABAAAAACcQB+AAV0AAVBUlJPV3EAfgAnc3EAfgAWAAAAGHgAc3EAfgACP0AAAAAAAAx3CAAAABAAAAACcQB+AAV0AAtDT09LRURfQkVFRnEAfgAnc3EAfgAWAAAADHgAc3EAfgADP0AAAAAAAAB3CAAAABAAAAAAeHNxAH4AAz9AAAAAAAAAdwgAAAAQAAAAAHhzcQB+AAM/QAAAAAAAAHcIAAAAEAAAAAB4c3EAfgADP0AAAAAAAAB3CAAAABAAAAAAeHNxAH4AAz9AAAAAAAAAdwgAAAAQAAAAAHhzcQB+AAM/QAAAAAAAAHcIAAAAEAAAAAB4c3EAfgADP0AAAAAAAAB3CAAAABAAAAAAeHNxAH4AAz9AAAAAAAAAdwgAAAAQAAAAAHhzcQB+AAM/QAAAAAAAAHcIAAAAEAAAAAB4c3EAfgADP0AAAAAAAAB3CAAAABAAAAAAeHNxAH4AAz9AAAAAAAAAdwgAAAAQAAAAAHhzcQB+AAM/QAAAAAAAAHcIAAAAEAAAAAB4c3EAfgADP0AAAAAAAAB3CAAAABAAAAAAeHNxAH4AAz9AAAAAAAAAdwgAAAAQAAAAAHhzcQB+AAM/QAAAAAAAAHcIAAAAEAAAAAB4c3EAfgADP0AAAAAAAAB3CAAAABAAAAAAeHNxAH4AAz9AAAAAAAAAdwgAAAAQAAAAAHhzcQB+AAM/QAAAAAAAAHcIAAAAEAAAAAB4c3EAfgADP0AAAAAAAAB3CAAAABAAAAAAeHNxAH4AAz9AAAAAAAAAdwgAAAAQAAAAAHhzcQB+AAM/QAAAAAAAAHcIAAAAEAAAAAB4");
				ItemStack armor[] = ItemSerializers.stringToItems("rO0ABXVyABBbTGphdmEudXRpbC5NYXA7/+CwhupHTAsCAAB4cAAAAARzcgAXamF2YS51dGlsLkxpbmtlZEhhc2hNYXA0wE5cEGzA+wIAAVoAC2FjY2Vzc09yZGVyeHIAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAAJ0AAR0eXBldAANRElBTU9ORF9CT09UU3QABG1ldGFzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAl4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAJ0AAltZXRhLXR5cGV0AAhlbmNoYW50c3VxAH4ACwAAAAJ0AApVTlNQRUNJRklDc3IAN2NvbS5nb29nbGUuY29tbW9uLmNvbGxlY3QuSW1tdXRhYmxlQmlNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAHhxAH4ACHVxAH4ACwAAAAF0ABhQUk9URUNUSU9OX0VOVklST05NRU5UQUx1cQB+AAsAAAABc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAF4AHNxAH4AAj9AAAAAAAAMdwgAAAAQAAAAAnEAfgAFdAAQRElBTU9ORF9MRUdHSU5HU3EAfgAHc3EAfgAIdXEAfgALAAAAAnEAfgANcQB+AA51cQB+AAsAAAACcQB+ABBzcQB+ABF1cQB+AAsAAAABcQB+ABR1cQB+AAsAAAABcQB+ABh4AHNxAH4AAj9AAAAAAAAMdwgAAAAQAAAAAnEAfgAFdAASRElBTU9ORF9DSEVTVFBMQVRFcQB+AAdzcQB+AAh1cQB+AAsAAAACcQB+AA1xAH4ADnVxAH4ACwAAAAJxAH4AEHNxAH4AEXVxAH4ACwAAAAFxAH4AFHVxAH4ACwAAAAFxAH4AGHgAc3EAfgACP0AAAAAAAAx3CAAAABAAAAACcQB+AAV0AA5ESUFNT05EX0hFTE1FVHEAfgAHc3EAfgAIdXEAfgALAAAAAnEAfgANcQB+AA51cQB+AAsAAAACcQB+ABBzcQB+ABF1cQB+AAsAAAABcQB+ABR1cQB+AAsAAAABcQB+ABh4AA==");
				player.getInventory().setContents(items);
				player.getInventory().setArmorContents(armor);
			}, 2);
		}
		return;
	}
}
