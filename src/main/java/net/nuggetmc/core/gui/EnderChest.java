package net.nuggetmc.core.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public class EnderChest {
	
	private ItemStack[] chests;
	private ItemStack[] echests;
	
	public EnderChest() {
		this.chests = new ItemStack[21];
		this.echests = new ItemStack[21];
		this.guiSetup();
		this.animationSetup();
	}
	
	private void guiSetup() {
		for (int i = 0; i < 21; i++) {
			ItemStack echest  = new ItemStack(Material.ENDER_CHEST);
			ItemMeta echestMeta  = echest.getItemMeta();
			echestMeta.setDisplayName(ChatColor.YELLOW + "Vault #" + (i + 1));
			ArrayList<String> echestLore = new ArrayList<String>();
			echestLore.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
			echestLore.add("");
			echestLore.add(ChatColor.GRAY + "Your rank is not sufficient");
			echestLore.add(ChatColor.GRAY + "to access this vault!");
			echestMeta.setLore(echestLore);
			echest.setItemMeta(echestMeta);
			echests[i] = echest;
			
			ItemStack chest  = new ItemStack(Material.CHEST);
			ItemMeta chestMeta  = chest.getItemMeta();
			chestMeta.setDisplayName(ChatColor.YELLOW + "Vault #" + (i + 1));
			ArrayList<String> chestLore = new ArrayList<String>();
			chestLore.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			chestLore.add("");
			chestLore.add(ChatColor.GRAY + "Click to open!");
			chestMeta.setLore(chestLore);
			chest.setItemMeta(chestMeta);
			chests[i] = chest;
		}
		
		for (int i = 0; i < 21; i++) {
			if (i < 1) {
				invDefault.setItem(numConvert(i), chests[i]);
			} else {
				invDefault.setItem(numConvert(i), echests[i]);
			}
		}
		
		for (int i = 0; i < 21; i++) {
			if (i < 3) {
				invXD.setItem(numConvert(i), chests[i]);
			} else {
				invXD.setItem(numConvert(i), echests[i]);
			}
		}
		
		for (int i = 0; i < 21; i++) {
			if (i < 7) {
				invXDD.setItem(numConvert(i), chests[i]);
			} else {
				invXDD.setItem(numConvert(i), echests[i]);
			}
		}
		
		for (int i = 0; i < 21; i++) {
			if (i < 12) {
				invXDDD.setItem(numConvert(i), chests[i]);
			} else {
				invXDDD.setItem(numConvert(i), echests[i]);
			}
		}
		
		for (int i = 0; i < 21; i++) {
			invXDDDD.setItem(numConvert(i), chests[i]);
		}
		return;
	}
	
	private Inventory invXD = Bukkit.createInventory(null, 54, "Vaults");
	private Inventory invXDD = Bukkit.createInventory(null, 54, "Vaults");
	private Inventory invXDDD = Bukkit.createInventory(null, 54, "Vaults");
	private Inventory invXDDDD = Bukkit.createInventory(null, 54, "Vaults");
	private Inventory invDefault = Bukkit.createInventory(null, 54, "Vaults");
	
	private static LuckPerms api = LuckPermsProvider.get();
	
	public void vault(Player player) {
		player.updateInventory();
		
		UUID uuid = player.getUniqueId();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "default":
			player.openInventory(invDefault);
			return;
		case "xd":
			player.openInventory(invXD);
			return;
		case "xdd":
			player.openInventory(invXDD);
			return;
		case "xddd":
			player.openInventory(invXDDD);
			return;
		}
		
		player.openInventory(invXDDDD);
		return;
	}
	
	public static Map<Location, Set<Player>> anim;
	public static Map<Player, Location> activeChest;
	
	private void animationSetup() {
		EnderChest.anim = new HashMap<>();
		EnderChest.activeChest = new HashMap<>();
		return;
	}
	
	private int numConvert(int input) {
		int output = input + 10;
		int div = (int) (input / 7);
		output += div * 2;
		return output;
	}
	
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		String itemName = item.getItemMeta().getDisplayName();
		String val = itemName.substring(9);
		
		if (item.getType() == Material.CHEST) {
			if (itemName.contains("Vault")) {
				Bukkit.dispatchCommand(player, "pv " + val);
			}
		}
		return;
	}
}