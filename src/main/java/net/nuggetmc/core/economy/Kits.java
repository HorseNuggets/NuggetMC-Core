package net.nuggetmc.core.economy;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.gui.GUIKits;

public class Kits {

	private Main plugin;
	
	public HashMap<String, Long> expiration;
	
	public Kits(Main plugin) {
		this.plugin = plugin;
		this.expiration = new HashMap<String, Long>();

	}
	
	public void respawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (expiration.containsKey(player.getName())) {
			expiration.remove(player.getName());
		}
		return;
	}
	
	public static void subPreview(Player player, String kit) {
		String uuid = player.getUniqueId().toString();
		int level = Configs.playerstats.getConfig().getInt("players." + uuid + ".level");

		Inventory inventory = Bukkit.createInventory(null, 54, kit);
		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta closeMeta = close.getItemMeta();
		closeMeta.setDisplayName(ChatColor.RED + "Close");
		close.setItemMeta(closeMeta);
		inventory.setItem(49, close);

		ItemStack kitPreview = new ItemStack(Material.CHEST);
		ItemMeta kitPreviewMeta = kitPreview.getItemMeta();
		kitPreviewMeta.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + kit);
		ArrayList<String> kitPreviewLore = new ArrayList<String>();
		
		kitPreviewLore.add(ChatColor.GRAY + "Click to view!");
		kitPreviewMeta.setLore(kitPreviewLore);
		kitPreview.setItemMeta(kitPreviewMeta);
		inventory.setItem(20, kitPreview);

		
		if (level < KitCosts.requiredLevel(kit)) {
			ItemStack kitUnlock = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta kitUnlockMeta = kitUnlock.getItemMeta();
			kitUnlockMeta.setDisplayName(ChatColor.YELLOW + "Unlock Kit " + kit);
			ArrayList<String> kitUnlockLore = new ArrayList<String>();
			kitUnlockLore.add(ChatColor.GRAY + "You are not high enough level");
			kitUnlockLore.add(ChatColor.GRAY + "to unlock this kit!");
			kitUnlockLore.add("");
			kitUnlockLore.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + KitCosts.requiredLevel(kit));
			kitUnlockMeta.setLore(kitUnlockLore);
			kitUnlock.setItemMeta(kitUnlockMeta);
			inventory.setItem(24, kitUnlock);
		}
		
		else {
			ItemStack kitPurchase = new ItemStack(Material.EMERALD, 1);
			ItemMeta kitPurchaseMeta = kitPurchase.getItemMeta();
			kitPurchaseMeta.setDisplayName(ChatColor.GREEN + "Buy Kit " + kit);
			ArrayList<String> kitPurchaseLore = new ArrayList<String>();
			kitPurchaseLore.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + KitCosts.cost(kit));
			kitPurchaseLore.add("");
			kitPurchaseLore.add(ChatColor.GRAY + "Click to purchase!");
			kitPurchaseMeta.setLore(kitPurchaseLore);
			kitPurchase.setItemMeta(kitPurchaseMeta);
			inventory.setItem(24, kitPurchase);
		}

		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta backMeta = back.getItemMeta();
		backMeta.setDisplayName(ChatColor.RESET + "Back");
		back.setItemMeta(backMeta);
		inventory.setItem(48, back);
		
		player.updateInventory();
		player.openInventory(inventory);
		return;
	}
	
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
		String itemNameNoColor = itemName.substring(2);
		
		if (KitCosts.kitList.contains(itemNameNoColor.toLowerCase())) {
			subPreview(player, itemNameNoColor);
			return;
		}
		
		else if (itemNameNoColor.contains("Buy Kit ")) {
			Bukkit.getServer().dispatchCommand(player, "kit " + itemNameNoColor.substring(8).toLowerCase());
			return;
		}
		
		else if (itemNameNoColor.contains("Kit Preview")) {
			switch (itemNameNoColor.substring(18).toLowerCase()) {
			case "swordsman":
				KitPreviews.swordsmank(player);
				break;
			case "brute":
				KitPreviews.brutek(player);
				break;
			case "scout":
				KitPreviews.scoutk(player);
				break;
			case "apprentice":
				KitPreviews.apprenticek(player);
				break;
			case "horseman":
				KitPreviews.horsemank(player);
				break;
			case "essence":
				KitPreviews.essencek(player);
				break;
			case "jouster":
				KitPreviews.jousterk(player);
				break;
			case "enderman":
				KitPreviews.endermank(player);
				break;
			case "assassin":
				KitPreviews.assassink(player);
				break;
			case "pyro":
				KitPreviews.pyrok(player);
				break;
			case "creeper":
				KitPreviews.creeperk(player);
				break;
			case "samurai":
				KitPreviews.samuraik(player);
				break;
			case "templar":
				KitPreviews.templark(player);
				break;
			case "prestige":
				KitPreviews.prestigek(player);
				break;
			case "valkyrie":
				KitPreviews.valkyriek(player);
				break;
			case "champion":
				KitPreviews.championk(player);
				break;
			case "witherman":
				KitPreviews.withermank(player);
				break;
			case "sans":
				KitPreviews.sansk(player);
				break;
			case "thanos":
				KitPreviews.thanosk(player);
				break;
			case "spoon":
				KitPreviews.spoonk(player);
				break;
			}
			return;
		}
		
		else if (itemName.contains("Back")) {
			String inventoryName = event.getInventory().getTitle().toLowerCase();
			
			if (plugin.guiMain.guiKitsLayer2.contains(inventoryName)) {
				GUIKits.kit(player);
			}
			
			else if (plugin.guiMain.guiKitsLayer3.contains(inventoryName)) {
				String kitName = inventoryName.substring(14);
				kitName = kitName.substring(0, 1).toUpperCase() + kitName.substring(1);
				subPreview(player, kitName);
			}
			
			return;
		}
		
		else if (itemName.equals(ChatColor.RED + "Close")) {
			player.closeInventory();
			return;
		}
		
		return;
	}
	
	// [TODO] Register PlayerJoinEvent later for daily free kits
}