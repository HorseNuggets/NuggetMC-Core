package net.nuggetmc.core.gui;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryView;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.economy.KitCosts;

public class GUIMain {
	
	private Main plugin;
	private Set<Player> guiList = new HashSet<>();
	private Set<String> guiVaults = new HashSet<>();
	
	public Set<String> guiLockList = new HashSet<>();
	public Set<String> guiKitsLayer2 = new HashSet<>();
	public Set<String> guiKitsLayer3 = new HashSet<>();
	
	public GUIMain(Main plugin) {
		this.plugin = plugin;
		this.assignLockList();
		this.assignKitsLayers();
		this.assignVaultsList();
		return;
	}
	
	public void hardRemove(Player player) {
		if (guiList.contains(player)) {
			guiList.remove(player);
			hardRemove(player);
		}
		return;
	}
	
	public void onClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		player.updateInventory();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			InventoryView view = player.getOpenInventory();
			if (!guiLockList.contains(view.getTopInventory().getTitle().toLowerCase())) {
				hardRemove(player);
				player.updateInventory();
			}
			
			if (!guiVaults.contains(view.getTopInventory().getTitle().toLowerCase())) {
				if (EnderChest.activeChest.containsKey(player)) {
					Location loc = EnderChest.activeChest.get(player);
					EnderChest.activeChest.remove(player);
					
					if (EnderChest.anim.containsKey(loc)) {
						Set<Player> plist = EnderChest.anim.get(loc);
						if (plist != null) {
							if (plist.contains(player)) {
								plist.remove(player);
								if (plist.isEmpty()) {
									EnderChest.anim.remove(loc);
									BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
								    net.minecraft.server.v1_8_R3.World world = ((CraftWorld) loc.getWorld()).getHandle();
								    world.playBlockAction(pos, world.getType(pos).getBlock(), 1, 0);
								}
							}
						}
					}
				}
			}
		}, 5);

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			player.updateInventory();
		}, 2);
		return;
	}
	
	public void onOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		player.updateInventory();
		if (guiLockList.contains(event.getInventory().getTitle().toLowerCase())) {
			if (!guiList.contains(player)) {
				guiList.add(player);
			}
		}
		return;
	}
	
	public static void cancelAnvil(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType() == Material.ANVIL) {
				event.setCancelled(true);
			}
		}
		return;
	}
	
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (guiList.contains(player)) {
			event.setCancelled(true);
		}
		
		if (event.getInventory() != null) {
			if (event.getCurrentItem() != null) {
				if (event.getCurrentItem().hasItemMeta()) {
					if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
						if (guiLockList.contains(event.getInventory().getTitle().toLowerCase())) {
							plugin.kits.onClick(event);
							plugin.enderChest.onClick(event);
						}
					}
				}
			}
		}
		return;
	}
	
	private void assignLockList() {
		for (String entry : KitCosts.kitList) {
			guiLockList.add(entry);
			guiLockList.add("kit preview - " + entry);
		}
		guiLockList.add("kit selector");
		guiLockList.add("vaults");
		guiLockList.add("voting");
		return;
	}
	
	private void assignKitsLayers() {
		for (String entry : KitCosts.kitList) {
			guiKitsLayer2.add(entry);
		}
		for (String entry : KitCosts.kitList) {
			guiKitsLayer3.add("kit preview - " + entry);
		}
		return;
	}
	
	private void assignVaultsList() {
		guiVaults.add("vaults");
		for (int i = 1; i <= 21; i++) {
			guiVaults.add("vault #" + i);
		}
		return;
	}
}
