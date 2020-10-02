package net.nuggetmc.core.economy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class Kits implements Listener, CommandExecutor {

	private ArrayList<Player> gui = new ArrayList<Player>();
	private HashMap<String, Integer> cooldownTime;
	private HashMap<String, BukkitRunnable> cooldownTask;

	private Main plugin;

	public Kits(Main plugin) {
		this.plugin = plugin;
		cooldownTime = new HashMap<String, Integer>();
		cooldownTask = new HashMap<String, BukkitRunnable>();

	}

	@EventHandler
	public void respawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		Configs.kitsconfig.getConfig().set("cooldowncounter." + player.getName(), 0);
		return;
	}

	public void cooldownCount(Player player) {
		cooldownTime.put(player.getName(), 300);
		Configs.kitsconfig.getConfig().set("cooldowncounter." + player.getName(), 1);

		cooldownTask.put(player.getName(), new BukkitRunnable() {
			public void run() {
				cooldownTime.put(player.getName(), cooldownTime.get(player.getName()) - 1);
				if (cooldownTime.get(player.getName()) == 0
						|| Configs.kitsconfig.getConfig().getInt("cooldowncounter." + player.getName()) == 0) {
					cooldownTime.remove(player.getName());
					cooldownTask.remove(player.getName());
					this.cancel();
					return;
				}
			}
		});
		cooldownTask.get(player.getName()).runTaskTimer(plugin, 20, 20);
		return;
	}
	
	/*
	 * [TODO]
	 * Create a click secondary gui area for all kits (just one for all)
	 * Grab exceptions from megawallsclasses
	 */
	
	public void subPreview(Player player, String kit) {
		String uuid = player.getUniqueId().toString();
		int level = Configs.playerstats.getConfig().getInt("players." + uuid + ".level");

		Inventory inv = Bukkit.createInventory(null, 54, kit);
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + kit);
		ArrayList<String> lore1 = new ArrayList<String>();
		
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		
		if (level < KitCosts.requiredLevel(kit)) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit " + kit);
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "You are not high enough level");
			lore2.add(ChatColor.GRAY + "to unlock this kit!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + KitCosts.requiredLevel(kit));
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}
		else {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit " + kit);
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + KitCosts.cost(kit));
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		
		player.updateInventory();
		player.openInventory(inv);
		return;
	}

	/*public void swordsman(Player player) {
		String uuid = player.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".swordsman");

		Inventory inv = Bukkit.createInventory(null, 54, "Swordsman");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Swordsman");
		ArrayList<String> lore1 = new ArrayList<String>();
		
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Swordsman");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "You are not high enough level");
			lore2.add(ChatColor.GRAY + "to unlock this kit!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "1");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Swordsman");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "0");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		
		player.updateInventory();
		player.openInventory(inv);
		return;
	}*/

	public void brute(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".brute");

		Inventory inv = Bukkit.createInventory(null, 54, "Brute");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Brute");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Brute");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "1");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Brute");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "0");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void scout(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".scout");

		Inventory inv = Bukkit.createInventory(null, 54, "Scout");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Scout");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Scout");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "1");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Scout");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "0");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void apprentice(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".apprentice");

		Inventory inv = Bukkit.createInventory(null, 54, "Apprentice");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Apprentice");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Apprentice");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "1");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Apprentice");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "12");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void horseman(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".horseman");

		Inventory inv = Bukkit.createInventory(null, 54, "Horseman");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Horseman");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Horseman");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "2");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Horseman");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "12");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void essence(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".essence");

		Inventory inv = Bukkit.createInventory(null, 54, "Essence");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Essence");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Essence");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "2");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Essence");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "18");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void jouster(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".jouster");

		Inventory inv = Bukkit.createInventory(null, 54, "Jouster");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Jouster");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Jouster");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "2");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Jouster");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "18");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void enderman(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".enderman");

		Inventory inv = Bukkit.createInventory(null, 54, "Enderman");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Enderman");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Enderman");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "3");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Enderman");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "24");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void assassin(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".assassin");

		Inventory inv = Bukkit.createInventory(null, 54, "Assassin");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Assassin");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Assassin");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "3");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Assassin");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "24");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void pyro(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".pyro");

		Inventory inv = Bukkit.createInventory(null, 54, "Pyro");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Pyro");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Pyro");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "3");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Pyro");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "28");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void creeper(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".creeper");

		Inventory inv = Bukkit.createInventory(null, 54, "Creeper");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Creeper");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Creeper");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "4");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Creeper");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "28");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void samurai(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".samurai");

		Inventory inv = Bukkit.createInventory(null, 54, "Samurai");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Samurai");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Samurai");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "4");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Samurai");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "36");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void templar(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".templar");

		Inventory inv = Bukkit.createInventory(null, 54, "Templar");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Templar");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Templar");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "4");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Templar");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "36");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void prestige(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".prestige");

		Inventory inv = Bukkit.createInventory(null, 54, "Prestige");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Prestige");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Prestige");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "5");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Prestige");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "42");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void valkyrie(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".valkyrie");

		Inventory inv = Bukkit.createInventory(null, 54, "Valkyrie");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Valkyrie");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Valkyrie");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "5");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Valkyrie");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "42");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void champion(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".champion");

		Inventory inv = Bukkit.createInventory(null, 54, "Champion");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Champion");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Champion");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "5");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Champion");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "64");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void witherman(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".witherman");

		Inventory inv = Bukkit.createInventory(null, 54, "Witherman");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Witherman");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Witherman");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "6");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Witherman");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "72");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void sans(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".sans");

		Inventory inv = Bukkit.createInventory(null, 54, "Sans");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Sans");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Sans");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "7");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Sans");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "164");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void thanos(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".thanos");

		Inventory inv = Bukkit.createInventory(null, 54, "Thanos");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(
				ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Thanos");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Thanos");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "8");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Thanos");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "420");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void spoon(Player p) {
		String uuid = p.getUniqueId().toString();
		int swordsmanint = Configs.kitsconfig.getConfig().getInt("players." + uuid + ".spoon");

		Inventory inv = Bukkit.createInventory(null, 54, "Spoon");
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = kit1.getItemMeta();
		meta1.setDisplayName(ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Spoon");
		ArrayList<String> lore1 = new ArrayList<String>();
		// lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view!");
		meta1.setLore(lore1);
		kit1.setItemMeta(meta1);
		inv.setItem(20, kit1);

		// ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		if (swordsmanint == 0) {
			ItemStack kit2 = new ItemStack(Material.STONE, 1, (short) 4);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.YELLOW + "Unlock Kit Spoon");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "To unlock this kit, you must click");
			lore2.add(ChatColor.GRAY + "on the sign at this kit's capsule");
			lore2.add(ChatColor.GRAY + "you can find around spawn!");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Required Level: " + ChatColor.YELLOW + "9");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		if (swordsmanint == 1) {
			ItemStack kit2 = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
			ItemMeta meta2 = kit2.getItemMeta();
			meta2.setDisplayName(ChatColor.GREEN + "Buy Kit Spoon");
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "1,333");
			lore2.add("");
			lore2.add(ChatColor.GRAY + "Click to purchase!");
			meta2.setLore(lore2);
			kit2.setItemMeta(meta2);
			inv.setItem(24, kit2);
		}

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);
		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void swordsmank(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Swordsman");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Swordsman Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Sharpness II - III");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+(9.5-10.75) Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power I - III");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection I - III");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II - III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection I");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection II - III");
		bootsn.setLore(bootsl);

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void brutek(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Brute");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Brute Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Sharpness I - II");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+(7.25-8.5) Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power I - III");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.IRON_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection II - III");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II - III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection I - II");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection I - III");
		bootsn.setLore(bootsl);

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void scoutk(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Scout");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Scout Sword");
		sword.setItemMeta(swordn);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power II - III");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.IRON_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection II - III");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II - III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection II - III");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		bootsn.setDisplayName(ChatColor.GREEN + "Scout Boots");
		ArrayList<String> bootsl = new ArrayList<String>();
		// bootsl.add(ChatColor.GRAY + "Protection I");
		bootsl.add(ChatColor.GRAY + "Speed I");
		bootsl.add("");
		bootsl.add(ChatColor.DARK_GRAY + "Hitting players will grant");
		bootsl.add(ChatColor.DARK_GRAY + "Speed I for 2s.");
		bootsn.setLore(bootsl);

		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void apprenticek(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Apprentice");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Apprentice Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Sharpness II - III");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+(9.5-10.75) Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power II - IV");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection I - II");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection I - IV");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.IRON_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection II - III");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection II - III");
		bootsn.setLore(bootsl);

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void horsemank(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Horseman");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.IRON_AXE, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Horseman Axe");
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power II - IV");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack horse1 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
		ItemMeta t1 = horse1.getItemMeta();
		t1.setDisplayName(ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Horseman)");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_GRAY + "Health Boost I");
		t1.setLore(lore);
		horse1.setItemMeta(t1);

		ItemStack helm = new ItemStack(Material.IRON_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection II - III");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection III");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection II - III");
		bootsn.setLore(bootsl);

		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, horse1);
		inv.setItem(25, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void essencek(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Essence");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Essence Sword");
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power II - IV");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		pick.addEnchantment(Enchantment.DIG_SPEED, 1);
		ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
		axe.addEnchantment(Enchantment.DIG_SPEED, 1);
		ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
		shovel.addEnchantment(Enchantment.DIG_SPEED, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemStack table = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
		ItemStack book = new ItemStack(Material.BOOK, 42);
		ItemStack bottle = new ItemStack(Material.EXP_BOTTLE, 32);
		// ItemStack swordun = new ItemStack(Material.DIAMOND_SWORD, 1);
		// ItemStack bowun = new ItemStack(Material.BOW, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection II");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection II");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection II");
		bootsn.setLore(bootsl);

		ItemStack effects = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
		ItemMeta effectsn = effects.getItemMeta();
		effectsn.setDisplayName(
				ChatColor.YELLOW + "Active Effects" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Essence");
		ArrayList<String> effectsl = new ArrayList<String>();
		effectsl.add(ChatColor.DARK_GRAY + "Haste I (2:00)");
		effectsn.setLore(effectsl);
		effects.setItemMeta(effectsn);

		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);
		inv.setItem(4, effects);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, table);
		inv.setItem(25, bottle);
		// inv.setItem(26, bottle);
		inv.setItem(26, book);
		// inv.setItem(10, swordun);
		// inv.setItem(11, bowun);
		inv.setItem(9, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void jousterk(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Jouster");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.DIAMOND_AXE, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Jouster Axe");
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power II - IV");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack horse2 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
		ItemMeta t2 = horse2.getItemMeta();
		t2.setDisplayName(ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Jouster)");
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.DARK_GRAY + "Health Boost VIII");
		lore2.add(ChatColor.DARK_GRAY + "Jump Boost I");
		t2.setLore(lore2);
		horse2.setItemMeta(t2);

		ItemStack helm = new ItemStack(Material.IRON_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection II - III");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection III");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection II - III");
		bootsn.setLore(bootsl);

		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, horse2);
		inv.setItem(25, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void endermank(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Enderman");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Enderman Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Sharpness III - IV");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+(9.75-11) Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power II - IV");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack tpcr = new ItemStack(Material.NETHER_STAR, 8);
		ItemMeta i = tpcr.getItemMeta();
		i.setDisplayName(ChatColor.DARK_PURPLE + "Teleport Crystal");

		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.DARK_GRAY + "Teleport to a player within");
		lore1.add(ChatColor.DARK_GRAY + "10 meters.");
		i.setLore(lore1);

		tpcr.setItemMeta(i);

		ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 4);

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection I - II");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II - III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection IV");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		bootsn.setDisplayName(ChatColor.GREEN + "Enderman Boots");
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection III");
		bootsl.add(ChatColor.GRAY + "Feather Falling II");
		bootsn.setLore(bootsl);

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, tpcr);
		inv.setItem(25, pearl);
		inv.setItem(26, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void assassink(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Assassin");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Assassin Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Sharpness III - IV");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+(10.75-12) Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power V");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		// ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 3);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack pearl = new ItemStack(Material.IRON_HOE, 1);
		pearl.addEnchantment(Enchantment.DURABILITY, 1);
		pearl.setDurability((short) 250);
		ItemMeta pearln = pearl.getItemMeta();
		pearln.setDisplayName(ChatColor.RED + "The Scythe");
		ArrayList<String> pearl1 = new ArrayList<String>();
		pearl1.add(ChatColor.GRAY + "Executioner I");
		pearl1.add("");
		pearl1.add(ChatColor.DARK_GRAY + "Instantly kills anyone");
		pearl1.add(ChatColor.DARK_GRAY + "under 6 HP.");
		pearln.setLore(pearl1);

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection I - IV");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II - III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.IRON_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection II - III");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection II - III");
		bootsn.setLore(bootsl);

		ItemStack effects = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
		ItemMeta effectsn = effects.getItemMeta();
		effectsn.setDisplayName(
				ChatColor.YELLOW + "Active Effects" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Assassin");
		ArrayList<String> effectsl = new ArrayList<String>();
		effectsl.add(ChatColor.DARK_GRAY + "Strength I (0:30)");
		effectsn.setLore(effectsl);

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		pearln.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);
		effects.setItemMeta(effectsn);
		pearl.setItemMeta(pearln);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);
		inv.setItem(4, effects);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(32, steak);
		inv.setItem(31, gapple);
		inv.setItem(33, pick);
		inv.setItem(34, axe);
		inv.setItem(35, shovel);
		inv.setItem(19, planks);
		inv.setItem(20, cobble);
		inv.setItem(18, arrow);
		inv.setItem(22, water);
		inv.setItem(21, water);
		inv.setItem(24, lava);
		inv.setItem(23, lava);
		inv.setItem(30, pearl);
		inv.setItem(25, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void pyrok(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Pyro");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Pyro Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Sharpness III - IV");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+(10.75-12) Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Flame I");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection III - IV");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II - III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection I - III");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection II - III");
		bootsn.setLore(bootsl);

		ItemStack effects = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
		ItemMeta effectsn = effects.getItemMeta();
		effectsn.setDisplayName(
				ChatColor.YELLOW + "Active Effects" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Pyro");
		ArrayList<String> effectsl = new ArrayList<String>();
		effectsl.add(ChatColor.DARK_GRAY + "Fire Resistance I (3:00)");
		effectsn.setLore(effectsl);
		effects.setItemMeta(effectsn);

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		ItemStack fsword = new ItemStack(Material.GOLD_SWORD, 1);
		fsword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);
		inv.setItem(4, effects);

		inv.setItem(27, sword);
		inv.setItem(29, rod);
		inv.setItem(30, bow);
		inv.setItem(32, steak);
		inv.setItem(31, gapple);
		inv.setItem(33, pick);
		inv.setItem(34, axe);
		inv.setItem(35, shovel);
		inv.setItem(19, planks);
		inv.setItem(20, cobble);
		inv.setItem(18, arrow);
		inv.setItem(21, water);
		inv.setItem(22, water);
		inv.setItem(14, lava);
		inv.setItem(23, lava);
		inv.setItem(24, lava);
		inv.setItem(25, lava);
		inv.setItem(26, lava);
		inv.setItem(9, lava);
		inv.setItem(10, lava);
		inv.setItem(11, lava);
		inv.setItem(12, lava);
		inv.setItem(13, lava);
		inv.setItem(28, fsword);
		inv.setItem(15, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void creeperk(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Creeper");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Creeper Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Sharpness IV");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+12 Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power IV - V");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection II - III");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II - III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection IV");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection II - III");
		bootsn.setLore(bootsl);

		ItemStack tnt = new ItemStack(Material.TNT, 6);

		ItemStack boom = new ItemStack(Material.REDSTONE_BLOCK, 2);
		ItemMeta boomn = boom.getItemMeta();
		boomn.setDisplayName(ChatColor.RED + "Boom Box");

		ItemStack totem = new ItemStack(Material.NETHER_STALK, 1);
		totem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta totemn = totem.getItemMeta();
		totemn.setDisplayName(ChatColor.RED + "Blast Totem " + ChatColor.GRAY + "(16.67%)");

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		totemn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);
		boom.setItemMeta(boomn);
		totem.setItemMeta(totemn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, tnt);
		inv.setItem(25, boom);
		inv.setItem(26, totem);
		inv.setItem(9, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void samuraik(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Samurai");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Samurai Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Sharpness III");
		swordl.add(ChatColor.GRAY + "Block I");
		swordl.add("");
		swordl.add(ChatColor.DARK_GRAY + "Blocking with this sword");
		swordl.add(ChatColor.DARK_GRAY + "absorbs extra damage.");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+9.75 Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power III - V");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection I - III");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection I - IV");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection I - III");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection I - III");
		bootsn.setLore(bootsl);

		ItemStack effects = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
		ItemMeta effectsn = effects.getItemMeta();
		effectsn.setDisplayName(
				ChatColor.YELLOW + "Active Effects" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Samurai");
		ArrayList<String> effectsl = new ArrayList<String>();
		effectsl.add(ChatColor.DARK_GRAY + "Speed II (1:00)");
		effectsn.setLore(effectsl);
		effects.setItemMeta(effectsn);

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		ItemStack horse3 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
		ItemMeta t3 = horse3.getItemMeta();
		t3.setDisplayName(ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Areion)");
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.DARK_GRAY + "Health Boost IX");
		lore3.add(ChatColor.DARK_GRAY + "Jump Boost I");
		lore3.add(ChatColor.DARK_GRAY + "Regeneration I");
		t3.setLore(lore3);
		horse3.setItemMeta(t3);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);
		inv.setItem(4, effects);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, horse3);
		inv.setItem(25, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void templark(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Templar");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.GOLD_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
		sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		ItemStack sword2 = new ItemStack(Material.GOLD_SWORD, 1);
		sword2.addEnchantment(Enchantment.KNOCKBACK, 2);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Templar Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		// swordl.add(ChatColor.GRAY + "Sharpness V");
		// swordl.add(ChatColor.GRAY + "Unbreaking X");
		swordl.add(ChatColor.GRAY + "Healing II");
		swordl.add("");
		swordl.add(ChatColor.DARK_GRAY + "Right-click on non-combat-");
		swordl.add(ChatColor.DARK_GRAY + "tagged players to heal them");
		swordl.add(ChatColor.DARK_GRAY + "8 HP. (12s cooldown)");
		// swordl.add("");
		// swordl.add(ChatColor.BLUE + "+10.25 Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ItemMeta bown = bow.getItemMeta();
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "Power III - V");
		bown.setLore(bowl);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection II - III");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II - III");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection II - IV");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection II - IV");
		bootsn.setLore(bootsl);

		// swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, sword2);
		inv.setItem(29, rod);
		inv.setItem(30, bow);
		inv.setItem(32, steak);
		inv.setItem(31, gapple);
		inv.setItem(33, pick);
		inv.setItem(35, axe);
		inv.setItem(34, shovel);
		inv.setItem(19, planks);
		inv.setItem(20, cobble);
		inv.setItem(18, arrow);
		inv.setItem(21, water);
		inv.setItem(22, water);
		inv.setItem(23, lava);
		inv.setItem(24, lava);
		inv.setItem(25, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void prestigek(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Prestige");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Prestige Sword");
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
		ItemMeta bown = bow.getItemMeta();
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack oby = new ItemStack(Material.OBSIDIAN, 32);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack effects = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
		ItemMeta effectsn = effects.getItemMeta();
		effectsn.setDisplayName(
				ChatColor.YELLOW + "Active Effects" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Prestige");
		ArrayList<String> effectsl = new ArrayList<String>();
		effectsl.add(ChatColor.DARK_GRAY + "Absorption III (0:15)");
		effectsn.setLore(effectsl);
		effects.setItemMeta(effectsn);

		ItemStack bdr = new ItemStack(Material.BEDROCK, 1);
		ItemMeta bdrn = track.getItemMeta();
		bdrn.setDisplayName(ChatColor.RESET + "unsellable bedrock lol");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection III - IV");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection II - IV");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection II - IV");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection III - IV");
		bootsn.setLore(bootsl);

		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		bow.setItemMeta(bown);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);
		bdr.setItemMeta(bdrn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);
		inv.setItem(4, effects);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(20, oby);
		inv.setItem(35, arrow);
		inv.setItem(22, water);
		inv.setItem(21, water);
		inv.setItem(24, lava);
		inv.setItem(23, lava);
		inv.setItem(25, bdr);
		inv.setItem(26, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void valkyriek(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Valkyrie");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Valkyrie Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Lightning I");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection III - IV");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection III - IV");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection II - III");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		ItemMeta bootsn = boots.getItemMeta();
		bootsn.setDisplayName(ChatColor.GREEN + "Valkyrie Boots");
		ArrayList<String> bootsl = new ArrayList<String>();
		// bootsl.add(ChatColor.GRAY + "Protection I");
		bootsl.add(ChatColor.GRAY + "Speed I");
		bootsl.add("");
		bootsl.add(ChatColor.DARK_GRAY + "Hitting players will grant");
		bootsl.add(ChatColor.DARK_GRAY + "Speed I for 2s.");
		bootsn.setLore(bootsl);
		boots.setItemMeta(bootsn);

		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void championk(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Champion");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Champion Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Sharpness III - IV");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+(10.75-12) Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta helmn = helm.getItemMeta();
		ArrayList<String> helml = new ArrayList<String>();
		helml.add(ChatColor.GRAY + "Protection III - IV");
		helmn.setLore(helml);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta chestn = chest.getItemMeta();
		ArrayList<String> chestl = new ArrayList<String>();
		chestl.add(ChatColor.GRAY + "Protection III - IV");
		chestn.setLore(chestl);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta legsn = legs.getItemMeta();
		ArrayList<String> legsl = new ArrayList<String>();
		legsl.add(ChatColor.GRAY + "Protection III - IV");
		legsn.setLore(legsl);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemMeta bootsn = boots.getItemMeta();
		ArrayList<String> bootsl = new ArrayList<String>();
		bootsl.add(ChatColor.GRAY + "Protection III - IV");
		bootsn.setLore(bootsl);

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		sword.setItemMeta(swordn);
		helm.setItemMeta(helmn);
		chest.setItemMeta(chestn);
		legs.setItemMeta(legsn);
		boots.setItemMeta(bootsn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void withermank(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Witherman");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.DARK_GRAY + "Witherman Sword");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Wither X");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack pearl = new ItemStack(Material.IRON_HOE, 1);
		pearl.addEnchantment(Enchantment.DURABILITY, 1);
		pearl.setDurability((short) 250);
		ItemMeta pearln = pearl.getItemMeta();
		pearln.setDisplayName(ChatColor.RED + "The Scythe");
		ArrayList<String> pearl1 = new ArrayList<String>();
		pearl1.add(ChatColor.GRAY + "Executioner I");
		pearl1.add("");
		pearl1.add(ChatColor.DARK_GRAY + "Instantly kills anyone");
		pearl1.add(ChatColor.DARK_GRAY + "under 6 HP.");
		pearln.setLore(pearl1);
		pearl.setItemMeta(pearln);

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		sword.setItemMeta(swordn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(32, steak);
		inv.setItem(30, pearl);
		inv.setItem(31, gapple);
		inv.setItem(33, pick);
		inv.setItem(34, axe);
		inv.setItem(35, shovel);
		inv.setItem(19, planks);
		inv.setItem(20, cobble);
		inv.setItem(18, arrow);
		inv.setItem(22, water);
		inv.setItem(21, water);
		inv.setItem(24, lava);
		inv.setItem(23, lava);
		inv.setItem(25, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void sansk(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Sans");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.BONE, 1);
		sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Sans Bone");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+12.5 Attack Damage");
		swordn.setLore(swordl);
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);

		ItemStack bow = new ItemStack(Material.BOW, 1, (short) 355);
		ItemMeta t = bow.getItemMeta();
		t.setDisplayName(ChatColor.BLUE + "Gaster Blaster");
		ArrayList<String> bowl = new ArrayList<String>();
		bowl.add(ChatColor.GRAY + "do you wanna have a bad time?");
		t.setLore(bowl);
		bow.setItemMeta(t);

		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		sword.setItemMeta(swordn);
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(31, steak);
		inv.setItem(30, gapple);
		inv.setItem(32, pick);
		inv.setItem(33, axe);
		inv.setItem(34, shovel);
		inv.setItem(18, planks);
		inv.setItem(19, cobble);
		inv.setItem(35, arrow);
		inv.setItem(20, water);
		inv.setItem(21, water);
		inv.setItem(22, lava);
		inv.setItem(23, lava);
		inv.setItem(24, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void thanosk(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Thanos");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Thanos Sword");
		ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 12);
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12);
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
		ItemStack planks = new ItemStack(Material.WOOD, 64);
		ItemStack arrow = new ItemStack(Material.ARROW, 48);
		ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemStack track = new ItemStack(Material.COMPASS, 1);
		ItemMeta trackn = track.getItemMeta();
		trackn.setDisplayName(ChatColor.RESET + "Player Tracker");

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

		sword.setItemMeta(swordn);
		track.setItemMeta(trackn);

		ItemStack pearl = new ItemStack(Material.GOLD_NUGGET, 1);
		pearl.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemMeta pearln = pearl.getItemMeta();
		pearln.setDisplayName(ChatColor.DARK_PURPLE + "The Infinity Gauntlet");
		ArrayList<String> pearl1 = new ArrayList<String>();
		pearl1.add(ChatColor.GRAY + "Thanos I");
		pearl1.add("");
		pearl1.add(ChatColor.DARK_GRAY + "There is a 2% chance to cut");
		pearl1.add(ChatColor.DARK_GRAY + "a player's health in half.");
		pearl1.add("");
		pearl1.add(ChatColor.DARK_GRAY + "Right-click to detonate.");
		pearl1.add(ChatColor.DARK_GRAY + "(10s cooldown)");
		pearln.setLore(pearl1);
		pearl.setItemMeta(pearln);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);

		inv.setItem(27, sword);
		inv.setItem(28, rod);
		inv.setItem(29, bow);
		inv.setItem(32, steak);
		inv.setItem(30, pearl);
		inv.setItem(31, gapple);
		inv.setItem(33, pick);
		inv.setItem(34, axe);
		inv.setItem(35, shovel);
		inv.setItem(19, planks);
		inv.setItem(20, cobble);
		inv.setItem(18, arrow);
		inv.setItem(22, water);
		inv.setItem(21, water);
		inv.setItem(24, lava);
		inv.setItem(23, lava);
		inv.setItem(25, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public void spoonk(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Preview - Spoon");

		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();
		meta21.setDisplayName(ChatColor.RED + "Close");
		kit21.setItemMeta(meta21);
		inv.setItem(49, kit21);

		ItemStack kit3 = new ItemStack(Material.ARROW);
		ItemMeta meta3 = kit3.getItemMeta();
		meta3.setDisplayName(ChatColor.RESET + "Back");
		kit3.setItemMeta(meta3);
		inv.setItem(48, kit3);

		/*
		 * ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		 * sword.addEnchantment(Enchantment.DAMAGE_ALL, 1); ItemMeta swordn =
		 * sword.getItemMeta(); swordn.setDisplayName(ChatColor.AQUA + "Iron Sword");
		 * ArrayList<String> swordl = new ArrayList<String>(); swordl.add(ChatColor.GRAY
		 * + "Sharpness I - II"); swordl.add(""); swordl.add(ChatColor.BLUE +
		 * "+(7.25-8.5) Attack Damage"); swordn.setLore(swordl); ItemStack rod = new
		 * ItemStack(Material.FISHING_ROD, 1); ItemStack bow = new
		 * ItemStack(Material.BOW, 1); bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		 * ItemMeta bown = bow.getItemMeta(); ArrayList<String> bowl = new
		 * ArrayList<String>(); bowl.add(ChatColor.GRAY + "Power I - III");
		 * bown.setLore(bowl); ItemStack steak = new ItemStack(Material.COOKED_BEEF,
		 * 12); ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 12); ItemStack
		 * pick = new ItemStack(Material.IRON_PICKAXE, 1); ItemStack axe = new
		 * ItemStack(Material.IRON_AXE, 1); ItemStack shovel = new
		 * ItemStack(Material.IRON_SPADE, 1); ItemStack planks = new
		 * ItemStack(Material.WOOD, 64); ItemStack arrow = new ItemStack(Material.ARROW,
		 * 48); ItemStack cobble = new ItemStack(Material.COBBLESTONE, 64); ItemStack
		 * water = new ItemStack(Material.WATER_BUCKET, 1); ItemStack lava = new
		 * ItemStack(Material.LAVA_BUCKET, 1); ItemStack track = new
		 * ItemStack(Material.COMPASS, 1); ItemMeta trackn = track.getItemMeta();
		 * trackn.setDisplayName(ChatColor.RESET + "Player Tracker");
		 * 
		 * ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
		 * helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1); ItemMeta helmn
		 * = helm.getItemMeta(); ArrayList<String> helml = new ArrayList<String>();
		 * helml.add(ChatColor.GRAY + "Protection II - III"); helmn.setLore(helml);
		 * 
		 * ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		 * chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1); ItemMeta
		 * chestn = chest.getItemMeta(); ArrayList<String> chestl = new
		 * ArrayList<String>(); chestl.add(ChatColor.GRAY + "Protection II - III");
		 * chestn.setLore(chestl);
		 * 
		 * ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		 * legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1); ItemMeta legsn
		 * = legs.getItemMeta(); ArrayList<String> legsl = new ArrayList<String>();
		 * legsl.add(ChatColor.GRAY + "Protection I - II"); legsn.setLore(legsl);
		 * 
		 * ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		 * boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1); ItemMeta
		 * bootsn = boots.getItemMeta(); ArrayList<String> bootsl = new
		 * ArrayList<String>(); bootsl.add(ChatColor.GRAY + "Protection I - III");
		 * bootsn.setLore(bootsl);
		 * 
		 * swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		 * helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		 * chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		 * legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		 * bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		 * bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		 * 
		 * sword.setItemMeta(swordn); bow.setItemMeta(bown); helm.setItemMeta(helmn);
		 * chest.setItemMeta(chestn); legs.setItemMeta(legsn);
		 * boots.setItemMeta(bootsn); track.setItemMeta(trackn);
		 * 
		 * inv.setItem(0, helm); inv.setItem(1, chest); inv.setItem(2, legs);
		 * inv.setItem(3, boots);
		 * 
		 * inv.setItem(27, sword); inv.setItem(28, rod); inv.setItem(29, bow);
		 * inv.setItem(31, steak); inv.setItem(30, gapple); inv.setItem(32, pick);
		 * inv.setItem(33, axe); inv.setItem(34, shovel); inv.setItem(18, planks);
		 * inv.setItem(19, cobble); inv.setItem(35, arrow); inv.setItem(20, water);
		 * inv.setItem(21, water); inv.setItem(22, lava); inv.setItem(23, lava);
		 * inv.setItem(24, track);
		 */

		ItemStack spoon = new ItemStack(Material.IRON_SPADE, 1);
		spoon.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
		spoon.addUnsafeEnchantment(Enchantment.DIG_SPEED, 8);
		spoon.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta spoonn = spoon.getItemMeta();
		spoonn.setDisplayName(ChatColor.RESET + "§9§kX§r §9The Spoon §kX§r");
		ArrayList<String> spoonl = new ArrayList<String>();
		spoonl.add(ChatColor.GRAY + "Lightning V");
		spoonn.setLore(spoonl);
		spoon.setItemMeta(spoonn);

		inv.setItem(22, spoon);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	@EventHandler
	public void close(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		p.updateInventory();
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				InventoryView view = p.getOpenInventory();
				if (!(view.getTopInventory().getTitle().equals("Kit Selector")
						|| view.getTopInventory().getTitle().equals("Swordsman")
						|| view.getTopInventory().getTitle().equals("Brute")
						|| view.getTopInventory().getTitle().equals("Scout")
						|| view.getTopInventory().getTitle().equals("Apprentice")
						|| view.getTopInventory().getTitle().equals("Horseman")
						|| view.getTopInventory().getTitle().equals("Essence")
						|| view.getTopInventory().getTitle().equals("Jouster")
						|| view.getTopInventory().getTitle().equals("Enderman")
						|| view.getTopInventory().getTitle().equals("Assassin")
						|| view.getTopInventory().getTitle().equals("Pyro")
						|| view.getTopInventory().getTitle().equals("Creeper")
						|| view.getTopInventory().getTitle().equals("Samurai")
						|| view.getTopInventory().getTitle().equals("Templar")
						|| view.getTopInventory().getTitle().equals("Prestige")
						|| view.getTopInventory().getTitle().equals("Valkyrie")
						|| view.getTopInventory().getTitle().equals("Champion")
						|| view.getTopInventory().getTitle().equals("Witherman")
						|| view.getTopInventory().getTitle().equals("Sans")
						|| view.getTopInventory().getTitle().equals("Thanos")
						|| view.getTopInventory().getTitle().equals("Spoon")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Scout")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Apprentice")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Horseman")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Essence")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Jouster")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Enderman")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Assassin")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Pyro")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Creeper")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Samurai")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Templar")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Prestige")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Valkyrie")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Champion")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Witherman")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Sans")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Thanos")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Spoon")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Brute")
						|| view.getTopInventory().getTitle().equals("Vaults")
						|| view.getTopInventory().getTitle().equals("Voting")
						|| view.getTopInventory().getTitle().equals("Kit Preview - Swordsman"))) {
					gui.remove(p);
					gui.remove(p);
					gui.remove(p);
					gui.remove(p);
					gui.remove(p);
					gui.remove(p);
					p.updateInventory();
				}
			}
		}, 5);

		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				p.updateInventory();
			}
		}, 2);
		return;
	}

	@EventHandler
	public void open(InventoryOpenEvent e) {
		Player p = (Player) e.getPlayer();
		p.updateInventory();
		if (e.getInventory().getTitle().equals("Kit Selector") || e.getInventory().getTitle().equals("Swordsman")
				|| e.getInventory().getTitle().equals("Brute") || e.getInventory().getTitle().equals("Scout")
				|| e.getInventory().getTitle().equals("Apprentice") || e.getInventory().getTitle().equals("Horseman")
				|| e.getInventory().getTitle().equals("Essence") || e.getInventory().getTitle().equals("Jouster")
				|| e.getInventory().getTitle().equals("Enderman") || e.getInventory().getTitle().equals("Assassin")
				|| e.getInventory().getTitle().equals("Pyro") || e.getInventory().getTitle().equals("Creeper")
				|| e.getInventory().getTitle().equals("Samurai") || e.getInventory().getTitle().equals("Templar")
				|| e.getInventory().getTitle().equals("Prestige") || e.getInventory().getTitle().equals("Valkyrie")
				|| e.getInventory().getTitle().equals("Champion") || e.getInventory().getTitle().equals("Witherman")
				|| e.getInventory().getTitle().equals("Sans") || e.getInventory().getTitle().equals("Thanos")
				|| e.getInventory().getTitle().equals("Spoon")
				|| e.getInventory().getTitle().equals("Kit Preview - Scout")
				|| e.getInventory().getTitle().equals("Kit Preview - Apprentice")
				|| e.getInventory().getTitle().equals("Kit Preview - Horseman")
				|| e.getInventory().getTitle().equals("Kit Preview - Essence")
				|| e.getInventory().getTitle().equals("Kit Preview - Jouster")
				|| e.getInventory().getTitle().equals("Kit Preview - Enderman")
				|| e.getInventory().getTitle().equals("Kit Preview - Assassin")
				|| e.getInventory().getTitle().equals("Kit Preview - Pyro")
				|| e.getInventory().getTitle().equals("Kit Preview - Creeper")
				|| e.getInventory().getTitle().equals("Kit Preview - Samurai")
				|| e.getInventory().getTitle().equals("Kit Preview - Templar")
				|| e.getInventory().getTitle().equals("Kit Preview - Prestige")
				|| e.getInventory().getTitle().equals("Kit Preview - Valkyrie")
				|| e.getInventory().getTitle().equals("Kit Preview - Champion")
				|| e.getInventory().getTitle().equals("Kit Preview - Witherman")
				|| e.getInventory().getTitle().equals("Kit Preview - Sans")
				|| e.getInventory().getTitle().equals("Kit Preview - Thanos")
				|| e.getInventory().getTitle().equals("Kit Preview - Spoon")
				|| e.getInventory().getTitle().equals("Kit Preview - Brute")
				|| e.getInventory().getTitle().equals("Vaults") || e.getInventory().getTitle().equals("Voting")
				|| e.getInventory().getTitle().equals("Kit Preview - Swordsman")) {
			if (!gui.contains(p)) {
				gui.add(p);
			}
		}
		if (e.getInventory() instanceof AnvilInventory) {
			p.closeInventory();
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					p.closeInventory();
				}
			}, 2);
			return;
		}
		return;
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.VILLAGER) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ENDER_CHEST) {
			e.setCancelled(true);
			if (e.getPlayer().getWorld() == Bukkit.getServer().getWorld("creative")) {
				return;
			} else {
				vault(e.getPlayer());
			}
		}
	}

	public void vote(Player player) {
		player.updateInventory();
		Inventory inv = Bukkit.createInventory(null, 36, "Voting");

		ItemStack v1 = new ItemStack(Material.PAPER);
		ItemMeta meta1 = v1.getItemMeta();
		ItemStack v2 = new ItemStack(Material.PAPER);
		ItemMeta meta2 = v2.getItemMeta();
		ItemStack v3 = new ItemStack(Material.PAPER);
		ItemMeta meta3 = v3.getItemMeta();
		ItemStack v4 = new ItemStack(Material.PAPER);
		ItemMeta meta4 = v4.getItemMeta();
		ItemStack v5 = new ItemStack(Material.PAPER);
		ItemMeta meta5 = v5.getItemMeta();
		ItemStack v6 = new ItemStack(Material.PAPER);
		ItemMeta meta6 = v6.getItemMeta();
		ItemStack v7 = new ItemStack(Material.PAPER);
		ItemMeta meta7 = v7.getItemMeta();

		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta metaclose = close.getItemMeta();

		meta1.setDisplayName(ChatColor.WHITE + "Vote on " + ChatColor.DARK_AQUA + "minecraftservers.org");
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GRAY + "Reward: " + ChatColor.GOLD + "200 Nuggets");
		lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to vote!");
		meta1.setLore(lore1);

		meta2.setDisplayName(ChatColor.WHITE + "Vote on " + ChatColor.DARK_AQUA + "minestatus.net");
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GRAY + "Reward: " + ChatColor.AQUA + "1 Diamond");
		lore2.add("");
		lore2.add(ChatColor.GRAY + "Click to vote!");
		meta2.setLore(lore2);

		meta3.setDisplayName(ChatColor.WHITE + "Vote on " + ChatColor.DARK_AQUA + "votemc.com");
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.GRAY + "Reward: " + ChatColor.YELLOW + "69 Points");
		lore3.add("");
		lore3.add(ChatColor.GRAY + "Click to vote!");
		meta3.setLore(lore3);

		meta4.setDisplayName(ChatColor.YELLOW + "Vote on " + ChatColor.WHITE + "<url>");
		meta5.setDisplayName(ChatColor.YELLOW + "Vote on " + ChatColor.WHITE + "<url>");
		meta6.setDisplayName(ChatColor.YELLOW + "Vote on " + ChatColor.WHITE + "<url>");
		meta7.setDisplayName(ChatColor.YELLOW + "Vote on " + ChatColor.WHITE + "<url>");

		metaclose.setDisplayName(ChatColor.RED + "Close");

		v1.setItemMeta(meta1);
		v2.setItemMeta(meta2);
		v3.setItemMeta(meta3);
		v4.setItemMeta(meta4);
		v5.setItemMeta(meta5);
		v6.setItemMeta(meta6);
		v7.setItemMeta(meta7);
		close.setItemMeta(metaclose);

		inv.setItem(10, v1);
		inv.setItem(11, v2);
		inv.setItem(12, v3);
		inv.setItem(13, v4);
		inv.setItem(14, v5);
		inv.setItem(15, v6);
		inv.setItem(16, v7);

		inv.setItem(31, close);
		player.updateInventory();
		player.openInventory(inv);
	}

	public void vault(Player player) {
		player.updateInventory();
		Inventory inv = Bukkit.createInventory(null, 54, "Vaults");
		ItemStack v1 = new ItemStack(Material.CHEST);
		ItemMeta meta1 = v1.getItemMeta();
		ItemStack v2 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta2 = v2.getItemMeta();
		ItemStack v3 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta3 = v3.getItemMeta();
		ItemStack v4 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta4 = v4.getItemMeta();
		ItemStack v5 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta5 = v5.getItemMeta();
		ItemStack v6 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta6 = v6.getItemMeta();
		ItemStack v7 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta7 = v7.getItemMeta();
		ItemStack v8 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta8 = v8.getItemMeta();
		ItemStack v9 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta9 = v9.getItemMeta();
		ItemStack v10 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta10 = v10.getItemMeta();
		ItemStack v11 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta11 = v11.getItemMeta();
		ItemStack v12 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta12 = v12.getItemMeta();
		ItemStack v13 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta13 = v13.getItemMeta();
		ItemStack v14 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta14 = v14.getItemMeta();
		ItemStack v15 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta15 = v15.getItemMeta();
		ItemStack v16 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta16 = v16.getItemMeta();
		ItemStack v17 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta17 = v17.getItemMeta();
		ItemStack v18 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta18 = v18.getItemMeta();
		ItemStack v19 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta19 = v19.getItemMeta();
		ItemStack v20 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta20 = v20.getItemMeta();
		ItemStack v21 = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta21 = v21.getItemMeta();

		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta metaclose = close.getItemMeta();

		meta1.setDisplayName(ChatColor.YELLOW + "Vault #1");
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
		lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to open!");
		meta1.setLore(lore1);

		meta2.setDisplayName(ChatColor.YELLOW + "Vault #2");
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore2.add("");
		lore2.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore2.add(ChatColor.GRAY + "to access this vault!");
		meta2.setLore(lore2);

		meta3.setDisplayName(ChatColor.YELLOW + "Vault #3");
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore3.add("");
		lore3.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore3.add(ChatColor.GRAY + "to access this vault!");
		meta3.setLore(lore3);

		meta4.setDisplayName(ChatColor.YELLOW + "Vault #4");
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore4.add("");
		lore4.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore4.add(ChatColor.GRAY + "to access this vault!");
		meta4.setLore(lore4);

		meta5.setDisplayName(ChatColor.YELLOW + "Vault #5");
		ArrayList<String> lore5 = new ArrayList<String>();
		lore5.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore5.add("");
		lore5.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore5.add(ChatColor.GRAY + "to access this vault!");
		meta5.setLore(lore5);

		meta6.setDisplayName(ChatColor.YELLOW + "Vault #6");
		ArrayList<String> lore6 = new ArrayList<String>();
		lore6.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore6.add("");
		lore6.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore6.add(ChatColor.GRAY + "to access this vault!");
		meta6.setLore(lore6);

		meta7.setDisplayName(ChatColor.YELLOW + "Vault #7");
		ArrayList<String> lore7 = new ArrayList<String>();
		lore7.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore7.add("");
		lore7.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore7.add(ChatColor.GRAY + "to access this vault!");
		meta7.setLore(lore7);

		meta8.setDisplayName(ChatColor.YELLOW + "Vault #8");
		ArrayList<String> lore8 = new ArrayList<String>();
		lore8.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore8.add("");
		lore8.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore8.add(ChatColor.GRAY + "to access this vault!");
		meta8.setLore(lore8);

		meta9.setDisplayName(ChatColor.YELLOW + "Vault #9");
		ArrayList<String> lore9 = new ArrayList<String>();
		lore9.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore9.add("");
		lore9.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore9.add(ChatColor.GRAY + "to access this vault!");
		meta9.setLore(lore9);

		meta10.setDisplayName(ChatColor.YELLOW + "Vault #10");
		ArrayList<String> lore10 = new ArrayList<String>();
		lore10.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore10.add("");
		lore10.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore10.add(ChatColor.GRAY + "to access this vault!");
		meta10.setLore(lore10);

		meta11.setDisplayName(ChatColor.YELLOW + "Vault #11");
		ArrayList<String> lore11 = new ArrayList<String>();
		lore11.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore11.add("");
		lore11.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore11.add(ChatColor.GRAY + "to access this vault!");
		meta11.setLore(lore11);

		meta12.setDisplayName(ChatColor.YELLOW + "Vault #12");
		ArrayList<String> lore12 = new ArrayList<String>();
		lore12.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore12.add("");
		lore12.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore12.add(ChatColor.GRAY + "to access this vault!");
		meta12.setLore(lore12);

		meta13.setDisplayName(ChatColor.YELLOW + "Vault #13");
		ArrayList<String> lore13 = new ArrayList<String>();
		lore13.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore13.add("");
		lore13.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore13.add(ChatColor.GRAY + "to access this vault!");
		meta13.setLore(lore13);

		meta14.setDisplayName(ChatColor.YELLOW + "Vault #14");
		ArrayList<String> lore14 = new ArrayList<String>();
		lore14.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore14.add("");
		lore14.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore14.add(ChatColor.GRAY + "to access this vault!");
		meta14.setLore(lore14);

		meta15.setDisplayName(ChatColor.YELLOW + "Vault #15");
		ArrayList<String> lore15 = new ArrayList<String>();
		lore15.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore15.add("");
		lore15.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore15.add(ChatColor.GRAY + "to access this vault!");
		meta15.setLore(lore15);

		meta16.setDisplayName(ChatColor.YELLOW + "Vault #16");
		ArrayList<String> lore16 = new ArrayList<String>();
		lore16.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore16.add("");
		lore16.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore16.add(ChatColor.GRAY + "to access this vault!");
		meta16.setLore(lore16);

		meta17.setDisplayName(ChatColor.YELLOW + "Vault #17");
		ArrayList<String> lore17 = new ArrayList<String>();
		lore17.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore17.add("");
		lore17.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore17.add(ChatColor.GRAY + "to access this vault!");
		meta17.setLore(lore17);

		meta18.setDisplayName(ChatColor.YELLOW + "Vault #18");
		ArrayList<String> lore18 = new ArrayList<String>();
		lore18.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore18.add("");
		lore18.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore18.add(ChatColor.GRAY + "to access this vault!");
		meta18.setLore(lore18);

		meta19.setDisplayName(ChatColor.YELLOW + "Vault #19");
		ArrayList<String> lore19 = new ArrayList<String>();
		lore19.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore19.add("");
		lore19.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore19.add(ChatColor.GRAY + "to access this vault!");
		meta19.setLore(lore19);

		meta20.setDisplayName(ChatColor.YELLOW + "Vault #20");
		ArrayList<String> lore20 = new ArrayList<String>();
		lore20.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore20.add("");
		lore20.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore20.add(ChatColor.GRAY + "to access this vault!");
		meta20.setLore(lore20);

		meta21.setDisplayName(ChatColor.YELLOW + "Vault #21");
		ArrayList<String> lore21 = new ArrayList<String>();
		lore21.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "LOCKED");
		lore21.add("");
		lore21.add(ChatColor.GRAY + "Your rank is not sufficient");
		lore21.add(ChatColor.GRAY + "to access this vault!");
		meta21.setLore(lore21);

		if (player.hasPermission("playervaults.amount.3")) {
			v2 = new ItemStack(Material.CHEST);
			meta2.setDisplayName(ChatColor.YELLOW + "Vault #2");
			ArrayList<String> nlore2 = new ArrayList<String>();
			nlore2.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore2.add("");
			nlore2.add(ChatColor.GRAY + "Click to open!");
			meta2.setLore(nlore2);

			v3 = new ItemStack(Material.CHEST);
			meta3.setDisplayName(ChatColor.YELLOW + "Vault #3");
			ArrayList<String> nlore3 = new ArrayList<String>();
			nlore3.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore3.add("");
			nlore3.add(ChatColor.GRAY + "Click to open!");
			meta3.setLore(nlore3);
		}

		if (player.hasPermission("playervaults.amount.6")) {
			v2 = new ItemStack(Material.CHEST);
			meta2.setDisplayName(ChatColor.YELLOW + "Vault #2");
			ArrayList<String> nlore2 = new ArrayList<String>();
			nlore2.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore2.add("");
			nlore2.add(ChatColor.GRAY + "Click to open!");
			meta2.setLore(nlore2);

			v3 = new ItemStack(Material.CHEST);
			meta3.setDisplayName(ChatColor.YELLOW + "Vault #3");
			ArrayList<String> nlore3 = new ArrayList<String>();
			nlore3.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore3.add("");
			nlore3.add(ChatColor.GRAY + "Click to open!");
			meta3.setLore(nlore3);

			v4 = new ItemStack(Material.CHEST);
			meta4.setDisplayName(ChatColor.YELLOW + "Vault #4");
			ArrayList<String> nlore4 = new ArrayList<String>();
			nlore4.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore4.add("");
			nlore4.add(ChatColor.GRAY + "Click to open!");
			meta4.setLore(nlore4);

			v5 = new ItemStack(Material.CHEST);
			meta5.setDisplayName(ChatColor.YELLOW + "Vault #5");
			ArrayList<String> nlore5 = new ArrayList<String>();
			nlore5.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore5.add("");
			nlore5.add(ChatColor.GRAY + "Click to open!");
			meta5.setLore(nlore5);

			v6 = new ItemStack(Material.CHEST);
			meta6.setDisplayName(ChatColor.YELLOW + "Vault #6");
			ArrayList<String> nlore6 = new ArrayList<String>();
			nlore6.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore6.add("");
			nlore6.add(ChatColor.GRAY + "Click to open!");
			meta6.setLore(nlore6);
		}

		if (player.hasPermission("playervaults.amount.12")) {
			v2 = new ItemStack(Material.CHEST);
			meta2.setDisplayName(ChatColor.YELLOW + "Vault #2");
			ArrayList<String> nlore2 = new ArrayList<String>();
			nlore2.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore2.add("");
			nlore2.add(ChatColor.GRAY + "Click to open!");
			meta2.setLore(nlore2);

			v3 = new ItemStack(Material.CHEST);
			meta3.setDisplayName(ChatColor.YELLOW + "Vault #3");
			ArrayList<String> nlore3 = new ArrayList<String>();
			nlore3.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore3.add("");
			nlore3.add(ChatColor.GRAY + "Click to open!");
			meta3.setLore(nlore3);

			v4 = new ItemStack(Material.CHEST);
			meta4.setDisplayName(ChatColor.YELLOW + "Vault #4");
			ArrayList<String> nlore4 = new ArrayList<String>();
			nlore4.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore4.add("");
			nlore4.add(ChatColor.GRAY + "Click to open!");
			meta4.setLore(nlore4);

			v5 = new ItemStack(Material.CHEST);
			meta5.setDisplayName(ChatColor.YELLOW + "Vault #5");
			ArrayList<String> nlore5 = new ArrayList<String>();
			nlore5.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore5.add("");
			nlore5.add(ChatColor.GRAY + "Click to open!");
			meta5.setLore(nlore5);

			v6 = new ItemStack(Material.CHEST);
			meta6.setDisplayName(ChatColor.YELLOW + "Vault #6");
			ArrayList<String> nlore6 = new ArrayList<String>();
			nlore6.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore6.add("");
			nlore6.add(ChatColor.GRAY + "Click to open!");
			meta6.setLore(nlore6);

			v7 = new ItemStack(Material.CHEST);
			meta7.setDisplayName(ChatColor.YELLOW + "Vault #7");
			ArrayList<String> nlore7 = new ArrayList<String>();
			nlore7.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore7.add("");
			nlore7.add(ChatColor.GRAY + "Click to open!");
			meta7.setLore(nlore7);

			v8 = new ItemStack(Material.CHEST);
			meta8.setDisplayName(ChatColor.YELLOW + "Vault #8");
			ArrayList<String> nlore8 = new ArrayList<String>();
			nlore8.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore8.add("");
			nlore8.add(ChatColor.GRAY + "Click to open!");
			meta8.setLore(nlore8);

			v9 = new ItemStack(Material.CHEST);
			meta9.setDisplayName(ChatColor.YELLOW + "Vault #9");
			ArrayList<String> nlore9 = new ArrayList<String>();
			nlore9.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore9.add("");
			nlore9.add(ChatColor.GRAY + "Click to open!");
			meta9.setLore(nlore9);

			v10 = new ItemStack(Material.CHEST);
			meta10.setDisplayName(ChatColor.YELLOW + "Vault #10");
			ArrayList<String> nlore10 = new ArrayList<String>();
			nlore10.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore10.add("");
			nlore10.add(ChatColor.GRAY + "Click to open!");
			meta10.setLore(nlore10);

			v11 = new ItemStack(Material.CHEST);
			meta11.setDisplayName(ChatColor.YELLOW + "Vault #11");
			ArrayList<String> nlore11 = new ArrayList<String>();
			nlore11.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore11.add("");
			nlore11.add(ChatColor.GRAY + "Click to open!");
			meta11.setLore(nlore11);

			v12 = new ItemStack(Material.CHEST);
			meta12.setDisplayName(ChatColor.YELLOW + "Vault #12");
			ArrayList<String> nlore12 = new ArrayList<String>();
			nlore12.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore12.add("");
			nlore12.add(ChatColor.GRAY + "Click to open!");
			meta12.setLore(nlore12);
		}

		if (player.hasPermission("playervaults.amount.21")) {
			v2 = new ItemStack(Material.CHEST);
			meta2.setDisplayName(ChatColor.YELLOW + "Vault #2");
			ArrayList<String> nlore2 = new ArrayList<String>();
			nlore2.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore2.add("");
			nlore2.add(ChatColor.GRAY + "Click to open!");
			meta2.setLore(nlore2);

			v3 = new ItemStack(Material.CHEST);
			meta3.setDisplayName(ChatColor.YELLOW + "Vault #3");
			ArrayList<String> nlore3 = new ArrayList<String>();
			nlore3.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore3.add("");
			nlore3.add(ChatColor.GRAY + "Click to open!");
			meta3.setLore(nlore3);

			v4 = new ItemStack(Material.CHEST);
			meta4.setDisplayName(ChatColor.YELLOW + "Vault #4");
			ArrayList<String> nlore4 = new ArrayList<String>();
			nlore4.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore4.add("");
			nlore4.add(ChatColor.GRAY + "Click to open!");
			meta4.setLore(nlore4);

			v5 = new ItemStack(Material.CHEST);
			meta5.setDisplayName(ChatColor.YELLOW + "Vault #5");
			ArrayList<String> nlore5 = new ArrayList<String>();
			nlore5.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore5.add("");
			nlore5.add(ChatColor.GRAY + "Click to open!");
			meta5.setLore(nlore5);

			v6 = new ItemStack(Material.CHEST);
			meta6.setDisplayName(ChatColor.YELLOW + "Vault #6");
			ArrayList<String> nlore6 = new ArrayList<String>();
			nlore6.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore6.add("");
			nlore6.add(ChatColor.GRAY + "Click to open!");
			meta6.setLore(nlore6);

			v7 = new ItemStack(Material.CHEST);
			meta7.setDisplayName(ChatColor.YELLOW + "Vault #7");
			ArrayList<String> nlore7 = new ArrayList<String>();
			nlore7.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore7.add("");
			nlore7.add(ChatColor.GRAY + "Click to open!");
			meta7.setLore(nlore7);

			v8 = new ItemStack(Material.CHEST);
			meta8.setDisplayName(ChatColor.YELLOW + "Vault #8");
			ArrayList<String> nlore8 = new ArrayList<String>();
			nlore8.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore8.add("");
			nlore8.add(ChatColor.GRAY + "Click to open!");
			meta8.setLore(nlore8);

			v9 = new ItemStack(Material.CHEST);
			meta9.setDisplayName(ChatColor.YELLOW + "Vault #9");
			ArrayList<String> nlore9 = new ArrayList<String>();
			nlore9.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore9.add("");
			nlore9.add(ChatColor.GRAY + "Click to open!");
			meta9.setLore(nlore9);

			v10 = new ItemStack(Material.CHEST);
			meta10.setDisplayName(ChatColor.YELLOW + "Vault #10");
			ArrayList<String> nlore10 = new ArrayList<String>();
			nlore10.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore10.add("");
			nlore10.add(ChatColor.GRAY + "Click to open!");
			meta10.setLore(nlore10);

			v11 = new ItemStack(Material.CHEST);
			meta11.setDisplayName(ChatColor.YELLOW + "Vault #11");
			ArrayList<String> nlore11 = new ArrayList<String>();
			nlore11.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore11.add("");
			nlore11.add(ChatColor.GRAY + "Click to open!");
			meta11.setLore(nlore11);

			v12 = new ItemStack(Material.CHEST);
			meta12.setDisplayName(ChatColor.YELLOW + "Vault #12");
			ArrayList<String> nlore12 = new ArrayList<String>();
			nlore12.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore12.add("");
			nlore12.add(ChatColor.GRAY + "Click to open!");
			meta12.setLore(nlore12);

			v13 = new ItemStack(Material.CHEST);
			meta13.setDisplayName(ChatColor.YELLOW + "Vault #13");
			ArrayList<String> nlore13 = new ArrayList<String>();
			nlore13.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore13.add("");
			nlore13.add(ChatColor.GRAY + "Click to open!");
			meta13.setLore(nlore13);

			v14 = new ItemStack(Material.CHEST);
			meta14.setDisplayName(ChatColor.YELLOW + "Vault #14");
			ArrayList<String> nlore14 = new ArrayList<String>();
			nlore14.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore14.add("");
			nlore14.add(ChatColor.GRAY + "Click to open!");
			meta14.setLore(nlore14);

			v15 = new ItemStack(Material.CHEST);
			meta15.setDisplayName(ChatColor.YELLOW + "Vault #15");
			ArrayList<String> nlore15 = new ArrayList<String>();
			nlore15.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore15.add("");
			nlore15.add(ChatColor.GRAY + "Click to open!");
			meta15.setLore(nlore15);

			v16 = new ItemStack(Material.CHEST);
			meta16.setDisplayName(ChatColor.YELLOW + "Vault #16");
			ArrayList<String> nlore16 = new ArrayList<String>();
			nlore16.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore16.add("");
			nlore16.add(ChatColor.GRAY + "Click to open!");
			meta16.setLore(nlore16);

			v17 = new ItemStack(Material.CHEST);
			meta17.setDisplayName(ChatColor.YELLOW + "Vault #17");
			ArrayList<String> nlore17 = new ArrayList<String>();
			nlore17.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore17.add("");
			nlore17.add(ChatColor.GRAY + "Click to open!");
			meta17.setLore(nlore17);

			v18 = new ItemStack(Material.CHEST);
			meta18.setDisplayName(ChatColor.YELLOW + "Vault #18");
			ArrayList<String> nlore18 = new ArrayList<String>();
			nlore18.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore18.add("");
			nlore18.add(ChatColor.GRAY + "Click to open!");
			meta18.setLore(nlore18);

			v19 = new ItemStack(Material.CHEST);
			meta19.setDisplayName(ChatColor.YELLOW + "Vault #19");
			ArrayList<String> nlore19 = new ArrayList<String>();
			nlore19.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore19.add("");
			nlore19.add(ChatColor.GRAY + "Click to open!");
			meta19.setLore(nlore19);

			v20 = new ItemStack(Material.CHEST);
			meta20.setDisplayName(ChatColor.YELLOW + "Vault #20");
			ArrayList<String> nlore20 = new ArrayList<String>();
			nlore20.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore20.add("");
			nlore20.add(ChatColor.GRAY + "Click to open!");
			meta20.setLore(nlore20);

			v21 = new ItemStack(Material.CHEST);
			meta21.setDisplayName(ChatColor.YELLOW + "Vault #21");
			ArrayList<String> nlore21 = new ArrayList<String>();
			nlore21.add(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "UNLOCKED");
			nlore21.add("");
			nlore21.add(ChatColor.GRAY + "Click to open!");
			meta21.setLore(nlore21);
		}

		metaclose.setDisplayName(ChatColor.RED + "Close");

		v1.setItemMeta(meta1);
		v2.setItemMeta(meta2);
		v3.setItemMeta(meta3);
		v4.setItemMeta(meta4);
		v5.setItemMeta(meta5);
		v6.setItemMeta(meta6);
		v7.setItemMeta(meta7);
		v8.setItemMeta(meta8);
		v9.setItemMeta(meta9);
		v10.setItemMeta(meta10);
		v11.setItemMeta(meta11);
		v12.setItemMeta(meta12);
		v13.setItemMeta(meta13);
		v14.setItemMeta(meta14);
		v15.setItemMeta(meta15);
		v16.setItemMeta(meta16);
		v17.setItemMeta(meta17);
		v18.setItemMeta(meta18);
		v19.setItemMeta(meta19);
		v20.setItemMeta(meta20);
		v21.setItemMeta(meta21);
		close.setItemMeta(metaclose);

		inv.setItem(10, v1);
		inv.setItem(11, v2);
		inv.setItem(12, v3);
		inv.setItem(13, v4);
		inv.setItem(14, v5);
		inv.setItem(15, v6);
		inv.setItem(16, v7);
		inv.setItem(19, v8);
		inv.setItem(20, v9);
		inv.setItem(21, v10);
		inv.setItem(22, v11);
		inv.setItem(23, v12);
		inv.setItem(24, v13);
		inv.setItem(25, v14);
		inv.setItem(28, v15);
		inv.setItem(29, v16);
		inv.setItem(30, v17);
		inv.setItem(31, v18);
		inv.setItem(32, v19);
		inv.setItem(33, v20);
		inv.setItem(34, v21);

		inv.setItem(49, close);
		player.updateInventory();
		player.openInventory(inv);
	}

	/*
	 * @EventHandler public void drop(PlayerDropItemEvent e) { Player p = (Player)
	 * e.getPlayer();
	 * 
	 * int x = p.getLocation().getBlockX(); int y = p.getLocation().getBlockY(); int
	 * z = p.getLocation().getBlockZ();
	 * 
	 * if (x >= -8 && x <= 48 && y >= 180 && y <= 300 & z >= -118 && z <= -62) { if
	 * (!(x >= -7 && x <= 47 && y >= 181 && y <= 299 & z >= -117 && z <= -63)) {
	 * p.sendMessage(ChatColor.RED + "You can't adjust your inventory here!");
	 * e.setCancelled(true); } } return; }
	 */

	@EventHandler
	public void click(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		/*
		 * int x = p.getLocation().getBlockX(); int y = p.getLocation().getBlockY(); int
		 * z = p.getLocation().getBlockZ();
		 * 
		 * if (x >= -8 && x <= 48 && y >= 180 && y <= 300 & z >= -118 && z <= -62) { if
		 * (!(x >= -7 && x <= 47 && y >= 181 && y <= 299 & z >= -117 && z <= -63)) {
		 * p.sendMessage(ChatColor.RED + "You can't adjust your inventory here!");
		 * e.setCancelled(true); } }
		 */

		if (gui.contains(p))
			e.setCancelled(true);
		if (e.getInventory() == null) {
			return;
		}
		if (e.getCurrentItem() == null) {
			return;
		}
		if (!e.getCurrentItem().hasItemMeta()) {
			return;
		}
		if (e.getInventory() instanceof AnvilInventory) {
			p.closeInventory();
			e.setCancelled(true);
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					p.closeInventory();
					e.setCancelled(true);
				}
			}, 2);
			return;
		}
		if (e.getInventory().getTitle().equals("Kit Selector") || e.getInventory().getTitle().equals("Swordsman")
				|| e.getInventory().getTitle().equals("Brute") || e.getInventory().getTitle().equals("Scout")
				|| e.getInventory().getTitle().equals("Apprentice") || e.getInventory().getTitle().equals("Horseman")
				|| e.getInventory().getTitle().equals("Essence") || e.getInventory().getTitle().equals("Jouster")
				|| e.getInventory().getTitle().equals("Enderman") || e.getInventory().getTitle().equals("Assassin")
				|| e.getInventory().getTitle().equals("Pyro") || e.getInventory().getTitle().equals("Creeper")
				|| e.getInventory().getTitle().equals("Samurai") || e.getInventory().getTitle().equals("Templar")
				|| e.getInventory().getTitle().equals("Prestige") || e.getInventory().getTitle().equals("Valkyrie")
				|| e.getInventory().getTitle().equals("Champion") || e.getInventory().getTitle().equals("Witherman")
				|| e.getInventory().getTitle().equals("Sans") || e.getInventory().getTitle().equals("Thanos")
				|| e.getInventory().getTitle().equals("Spoon")
				|| e.getInventory().getTitle().equals("Kit Preview - Scout")
				|| e.getInventory().getTitle().equals("Kit Preview - Apprentice")
				|| e.getInventory().getTitle().equals("Kit Preview - Horseman")
				|| e.getInventory().getTitle().equals("Kit Preview - Essence")
				|| e.getInventory().getTitle().equals("Kit Preview - Jouster")
				|| e.getInventory().getTitle().equals("Kit Preview - Enderman")
				|| e.getInventory().getTitle().equals("Kit Preview - Assassin")
				|| e.getInventory().getTitle().equals("Kit Preview - Pyro")
				|| e.getInventory().getTitle().equals("Kit Preview - Creeper")
				|| e.getInventory().getTitle().equals("Kit Preview - Samurai")
				|| e.getInventory().getTitle().equals("Kit Preview - Templar")
				|| e.getInventory().getTitle().equals("Kit Preview - Prestige")
				|| e.getInventory().getTitle().equals("Kit Preview - Valkyrie")
				|| e.getInventory().getTitle().equals("Kit Preview - Champion")
				|| e.getInventory().getTitle().equals("Kit Preview - Witherman")
				|| e.getInventory().getTitle().equals("Kit Preview - Sans")
				|| e.getInventory().getTitle().equals("Kit Preview - Thanos")
				|| e.getInventory().getTitle().equals("Kit Preview - Spoon")
				|| e.getInventory().getTitle().equals("Kit Preview - Brute")
				|| e.getInventory().getTitle().equals("Vaults") || e.getInventory().getTitle().equals("Voting")
				|| e.getInventory().getTitle().equals("Kit Preview - Swordsman")) {

			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Close")) {
				p.closeInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName()
					.equals(ChatColor.WHITE + "Vote on " + ChatColor.DARK_AQUA + "minecraftservers.org")) {
				double x1 = Double.valueOf(p.getLocation().getX());
				double y1 = Double.valueOf(p.getLocation().getY());
				double z1 = Double.valueOf(p.getLocation().getZ());
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"playsound random.pop " + p.getName() + " " + x1 + " " + y1 + " " + z1 + " 1 1 1");
				p.closeInventory();
				p.sendMessage(ChatColor.YELLOW + "--------------------------------------");
				p.sendMessage("");
				p.sendMessage(ChatColor.YELLOW + "               Vote for " + ChatColor.GOLD + ChatColor.BOLD
						+ "NuggetMC" + ChatColor.RESET + ChatColor.YELLOW + "!");
				p.sendMessage(ChatColor.DARK_AQUA + "      minecraftservers.org/vote/589423");
				p.sendMessage("");
				p.sendMessage(ChatColor.YELLOW + "--------------------------------------");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName()
					.equals(ChatColor.WHITE + "Vote on " + ChatColor.DARK_AQUA + "minestatus.net")) {
				double x1 = Double.valueOf(p.getLocation().getX());
				double y1 = Double.valueOf(p.getLocation().getY());
				double z1 = Double.valueOf(p.getLocation().getZ());
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"playsound random.pop " + p.getName() + " " + x1 + " " + y1 + " " + z1 + " 1 1 1");
				p.closeInventory();
				p.sendMessage(ChatColor.YELLOW + "--------------------------------------");
				p.sendMessage("");
				p.sendMessage(ChatColor.YELLOW + "               Vote for " + ChatColor.GOLD + ChatColor.BOLD
						+ "NuggetMC" + ChatColor.RESET + ChatColor.YELLOW + "!");
				p.sendMessage(ChatColor.DARK_AQUA + "   minestatus.net/server/vote/nuggetmc.net");
				p.sendMessage("");
				p.sendMessage(ChatColor.YELLOW + "--------------------------------------");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName()
					.equals(ChatColor.WHITE + "Vote on " + ChatColor.DARK_AQUA + "votemc.com")) {
				double x1 = Double.valueOf(p.getLocation().getX());
				double y1 = Double.valueOf(p.getLocation().getY());
				double z1 = Double.valueOf(p.getLocation().getZ());
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"playsound random.pop " + p.getName() + " " + x1 + " " + y1 + " " + z1 + " 1 1 1");
				p.closeInventory();
				p.sendMessage(ChatColor.YELLOW + "--------------------------------------");
				p.sendMessage("");
				p.sendMessage(ChatColor.YELLOW + "               Vote for " + ChatColor.GOLD + ChatColor.BOLD
						+ "NuggetMC" + ChatColor.RESET + ChatColor.YELLOW + "!");
				p.sendMessage(ChatColor.DARK_AQUA + "         votemc.com/nuggetmc.479/vote");
				p.sendMessage("");
				p.sendMessage(ChatColor.YELLOW + "--------------------------------------");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #1")) {
				Bukkit.getServer().dispatchCommand(p, "pv 1");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #2")) {
				Bukkit.getServer().dispatchCommand(p, "pv 2");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #3")) {
				Bukkit.getServer().dispatchCommand(p, "pv 3");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #4")) {
				Bukkit.getServer().dispatchCommand(p, "pv 4");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #5")) {
				Bukkit.getServer().dispatchCommand(p, "pv 5");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #6")) {
				Bukkit.getServer().dispatchCommand(p, "pv 6");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #7")) {
				Bukkit.getServer().dispatchCommand(p, "pv 7");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #8")) {
				Bukkit.getServer().dispatchCommand(p, "pv 8");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #9")) {
				Bukkit.getServer().dispatchCommand(p, "pv 9");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #10")) {
				Bukkit.getServer().dispatchCommand(p, "pv 10");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #11")) {
				Bukkit.getServer().dispatchCommand(p, "pv 11");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #12")) {
				Bukkit.getServer().dispatchCommand(p, "pv 12");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #13")) {
				Bukkit.getServer().dispatchCommand(p, "pv 13");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #14")) {
				Bukkit.getServer().dispatchCommand(p, "pv 14");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #15")) {
				Bukkit.getServer().dispatchCommand(p, "pv 15");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #16")) {
				Bukkit.getServer().dispatchCommand(p, "pv 16");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #17")) {
				Bukkit.getServer().dispatchCommand(p, "pv 17");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #18")) {
				Bukkit.getServer().dispatchCommand(p, "pv 18");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #19")) {
				Bukkit.getServer().dispatchCommand(p, "pv 19");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #20")) {
				Bukkit.getServer().dispatchCommand(p, "pv 20");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Vault #21")) {
				Bukkit.getServer().dispatchCommand(p, "pv 21");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Swordsman")) {
				subPreview(p, "Swordsman");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName()
					.equals(ChatColor.GREEN + "Buy Kit Swordsman")) {
				Bukkit.getServer().dispatchCommand(p, "kit swordsman");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Brute")) {
				brute(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Brute")) {
				Bukkit.getServer().dispatchCommand(p, "kit brute");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Scout")) {
				scout(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Scout")) {
				Bukkit.getServer().dispatchCommand(p, "kit scout");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Apprentice")) {
				apprentice(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName()
					.equals(ChatColor.GREEN + "Buy Kit Apprentice")) {
				Bukkit.getServer().dispatchCommand(p, "kit apprentice");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Horseman")) {
				horseman(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Horseman")) {
				Bukkit.getServer().dispatchCommand(p, "kit horseman");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Essence")) {
				essence(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Essence")) {
				Bukkit.getServer().dispatchCommand(p, "kit essence");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Jouster")) {
				jouster(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Jouster")) {
				Bukkit.getServer().dispatchCommand(p, "kit jouster");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Enderman")) {
				enderman(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Enderman")) {
				Bukkit.getServer().dispatchCommand(p, "kit enderman");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Assassin")) {
				assassin(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Assassin")) {
				Bukkit.getServer().dispatchCommand(p, "kit assassin");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Pyro")) {
				pyro(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Pyro")) {
				Bukkit.getServer().dispatchCommand(p, "kit pyro");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Creeper")) {
				creeper(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Creeper")) {
				Bukkit.getServer().dispatchCommand(p, "kit creeper");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Samurai")) {
				samurai(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Samurai")) {
				Bukkit.getServer().dispatchCommand(p, "kit samurai");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Templar")) {
				templar(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Templar")) {
				Bukkit.getServer().dispatchCommand(p, "kit templar");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Prestige")) {
				prestige(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Prestige")) {
				Bukkit.getServer().dispatchCommand(p, "kit prestige");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Valkyrie")) {
				valkyrie(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Valkyrie")) {
				Bukkit.getServer().dispatchCommand(p, "kit valkyrie");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Champion")) {
				champion(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Champion")) {
				Bukkit.getServer().dispatchCommand(p, "kit champion");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Witherman")) {
				witherman(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName()
					.equals(ChatColor.GREEN + "Buy Kit Witherman")) {
				Bukkit.getServer().dispatchCommand(p, "kit witherman");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Sans")) {
				sans(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Sans")) {
				Bukkit.getServer().dispatchCommand(p, "kit sans");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Thanos")) {
				thanos(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Thanos")) {
				Bukkit.getServer().dispatchCommand(p, "kit thanos");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Spoon")) {
				spoon(p);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Buy Kit Spoon")) {
				Bukkit.getServer().dispatchCommand(p, "kit spoon");
			}
			e.setCancelled(true);
		}
		if (e.getInventory().getTitle().equals("Swordsman")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Swordsman")) {
				swordsmank(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Swordsman")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				subPreview(p, "Swordsman");
			}
		}
		if (e.getInventory().getTitle().equals("Brute")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Brute")) {
				brutek(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Brute")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				brute(p);
			}
		}
		if (e.getInventory().getTitle().equals("Scout")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Scout")) {
				scoutk(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Scout")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				scout(p);
			}
		}
		if (e.getInventory().getTitle().equals("Apprentice")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Apprentice")) {
				apprenticek(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Apprentice")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				apprentice(p);
			}
		}
		if (e.getInventory().getTitle().equals("Horseman")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Horseman")) {
				horsemank(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Horseman")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				horseman(p);
			}
		}
		if (e.getInventory().getTitle().equals("Essence")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Essence")) {
				essencek(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Essence")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				essence(p);
			}
		}
		if (e.getInventory().getTitle().equals("Jouster")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Jouster")) {
				jousterk(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Jouster")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				jouster(p);
			}
		}
		if (e.getInventory().getTitle().equals("Enderman")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Enderman")) {
				endermank(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Enderman")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				enderman(p);
			}
		}
		if (e.getInventory().getTitle().equals("Assassin")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Assassin")) {
				assassink(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Assassin")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				assassin(p);
			}
		}
		if (e.getInventory().getTitle().equals("Pyro")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName()
					.equals(ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Pyro")) {
				pyrok(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Pyro")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				pyro(p);
			}
		}
		if (e.getInventory().getTitle().equals("Creeper")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Creeper")) {
				creeperk(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Creeper")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				creeper(p);
			}
		}
		if (e.getInventory().getTitle().equals("Samurai")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Samurai")) {
				samuraik(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Samurai")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				samurai(p);
			}
		}
		if (e.getInventory().getTitle().equals("Templar")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Templar")) {
				templark(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Templar")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				templar(p);
			}
		}
		if (e.getInventory().getTitle().equals("Prestige")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Prestige")) {
				prestigek(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Prestige")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				prestige(p);
			}
		}
		if (e.getInventory().getTitle().equals("Valkyrie")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Valkyrie")) {
				valkyriek(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Valkyrie")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				valkyrie(p);
			}
		}
		if (e.getInventory().getTitle().equals("Champion")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Champion")) {
				championk(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Champion")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				champion(p);
			}
		}
		if (e.getInventory().getTitle().equals("Witherman")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Witherman")) {
				withermank(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Witherman")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				witherman(p);
			}
		}
		if (e.getInventory().getTitle().equals("Sans")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName()
					.equals(ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Sans")) {
				sansk(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Sans")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				sans(p);
			}
		}
		if (e.getInventory().getTitle().equals("Thanos")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Thanos")) {
				thanosk(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Thanos")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				thanos(p);
			}
		}
		if (e.getInventory().getTitle().equals("Spoon")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				kit(p);
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
					ChatColor.YELLOW + "Kit Preview" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Spoon")) {
				spoonk(p);
			}
		}
		if (e.getInventory().getTitle().equals("Kit Preview - Spoon")) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RESET + "Back")) {
				spoon(p);
			}
		}
		return;
	}

	public void kit(Player player) {
		player.updateInventory();
		Inventory inv = Bukkit.createInventory(null, 54, "Kit Selector");
		ItemStack kit1 = new ItemStack(Material.IRON_SWORD);
		ItemMeta meta1 = kit1.getItemMeta();
		ItemStack kit2 = new ItemStack(Material.IRON_CHESTPLATE);
		ItemMeta meta2 = kit2.getItemMeta();
		ItemStack kit3 = new ItemStack(Material.IRON_BOOTS);
		ItemMeta meta3 = kit3.getItemMeta();
		ItemStack kit4 = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta meta4 = kit4.getItemMeta();
		ItemStack kit5 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
		ItemMeta meta5 = kit5.getItemMeta();
		ItemStack kit6 = new ItemStack(Material.ENCHANTMENT_TABLE);
		ItemMeta meta6 = kit6.getItemMeta();
		ItemStack kit7 = new ItemStack(Material.IRON_AXE);
		ItemMeta meta7 = kit7.getItemMeta();
		ItemStack kit8 = new ItemStack(Material.ENDER_PEARL);
		ItemMeta meta8 = kit8.getItemMeta();
		ItemStack kit9 = new ItemStack(Material.REDSTONE);
		ItemMeta meta9 = kit9.getItemMeta();
		ItemStack kit10 = new ItemStack(Material.LAVA_BUCKET);
		ItemMeta meta10 = kit10.getItemMeta();
		ItemStack kit11 = new ItemStack(Material.TNT);
		ItemMeta meta11 = kit11.getItemMeta();
		ItemStack kit12 = new ItemStack(Material.GOLD_CHESTPLATE);
		ItemMeta meta12 = kit12.getItemMeta();
		ItemStack kit13 = new ItemStack(Material.GOLD_SWORD);
		ItemMeta meta13 = kit13.getItemMeta();
		ItemStack kit14 = new ItemStack(Material.BEDROCK);
		ItemMeta meta14 = kit14.getItemMeta();
		ItemStack kit15 = new ItemStack(Material.IRON_HELMET);
		ItemMeta meta15 = kit15.getItemMeta();
		ItemStack kit16 = new ItemStack(Material.BEACON);
		ItemMeta meta16 = kit16.getItemMeta();
		ItemStack kit17 = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
		ItemMeta meta17 = kit17.getItemMeta();
		ItemStack kit18 = new ItemStack(Material.BONE);
		ItemMeta meta18 = kit18.getItemMeta();
		ItemStack kit19 = new ItemStack(Material.NETHER_STALK);
		ItemMeta meta19 = kit19.getItemMeta();
		ItemStack kit20 = new ItemStack(Material.IRON_SPADE);
		kit20.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta meta20 = kit20.getItemMeta();
		ItemStack kit21 = new ItemStack(Material.BARRIER);
		ItemMeta meta21 = kit21.getItemMeta();

		ItemStack soon = new ItemStack(Material.STONE, 1, (short) 4);
		ItemMeta soonm = kit21.getItemMeta();

		soonm.setDisplayName(ChatColor.YELLOW + "Coming Soon!");

		meta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta4.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta7.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta13.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta20.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

		meta1.setDisplayName(ChatColor.YELLOW + "Swordsman");
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "0");
		lore1.add("");
		lore1.add(ChatColor.GRAY + "Click to view more!");
		meta1.setLore(lore1);

		meta2.setDisplayName(ChatColor.YELLOW + "Brute");
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "0");
		lore2.add("");
		lore2.add(ChatColor.GRAY + "Click to view more!");
		meta2.setLore(lore2);

		meta3.setDisplayName(ChatColor.YELLOW + "Scout");
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "0");
		lore3.add("");
		lore3.add(ChatColor.GRAY + "Click to view more!");
		meta3.setLore(lore3);

		meta4.setDisplayName(ChatColor.YELLOW + "Apprentice");
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "12");
		lore4.add("");
		lore4.add(ChatColor.GRAY + "Click to view more!");
		meta4.setLore(lore4);

		meta5.setDisplayName(ChatColor.YELLOW + "Horseman");
		ArrayList<String> lore5 = new ArrayList<String>();
		lore5.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "12");
		lore5.add("");
		lore5.add(ChatColor.GRAY + "Click to view more!");
		meta5.setLore(lore5);

		meta6.setDisplayName(ChatColor.YELLOW + "Essence");
		ArrayList<String> lore6 = new ArrayList<String>();
		lore6.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "18");
		lore6.add("");
		lore6.add(ChatColor.GRAY + "Click to view more!");
		meta6.setLore(lore6);

		meta7.setDisplayName(ChatColor.YELLOW + "Jouster");
		ArrayList<String> lore7 = new ArrayList<String>();
		lore7.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "18");
		lore7.add("");
		lore7.add(ChatColor.GRAY + "Click to view more!");
		meta7.setLore(lore7);

		meta8.setDisplayName(ChatColor.YELLOW + "Enderman");
		ArrayList<String> lore8 = new ArrayList<String>();
		lore8.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "24");
		lore8.add("");
		lore8.add(ChatColor.GRAY + "Click to view more!");
		meta8.setLore(lore8);

		meta9.setDisplayName(ChatColor.YELLOW + "Assassin");
		ArrayList<String> lore9 = new ArrayList<String>();
		lore9.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "24");
		lore9.add("");
		lore9.add(ChatColor.GRAY + "Click to view more!");
		meta9.setLore(lore9);

		meta10.setDisplayName(ChatColor.YELLOW + "Pyro");
		ArrayList<String> lore10 = new ArrayList<String>();
		lore10.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "28");
		lore10.add("");
		lore10.add(ChatColor.GRAY + "Click to view more!");
		meta10.setLore(lore10);

		meta11.setDisplayName(ChatColor.YELLOW + "Creeper");
		ArrayList<String> lore11 = new ArrayList<String>();
		lore11.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "28");
		lore11.add("");
		lore11.add(ChatColor.GRAY + "Click to view more!");
		meta11.setLore(lore11);

		meta12.setDisplayName(ChatColor.YELLOW + "Samurai");
		ArrayList<String> lore12 = new ArrayList<String>();
		lore12.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "36");
		lore12.add("");
		lore12.add(ChatColor.GRAY + "Click to view more!");
		meta12.setLore(lore12);

		meta13.setDisplayName(ChatColor.YELLOW + "Templar");
		ArrayList<String> lore13 = new ArrayList<String>();
		lore13.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "36");
		lore13.add("");
		lore13.add(ChatColor.GRAY + "Click to view more!");
		meta13.setLore(lore13);

		meta14.setDisplayName(ChatColor.YELLOW + "Prestige");
		ArrayList<String> lore14 = new ArrayList<String>();
		lore14.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "42");
		lore14.add("");
		lore14.add(ChatColor.GRAY + "Click to view more!");
		meta14.setLore(lore14);

		meta15.setDisplayName(ChatColor.YELLOW + "Valkyrie");
		ArrayList<String> lore15 = new ArrayList<String>();
		lore15.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "42");
		lore15.add("");
		lore15.add(ChatColor.GRAY + "Click to view more!");
		meta15.setLore(lore15);

		meta16.setDisplayName(ChatColor.YELLOW + "Champion");
		ArrayList<String> lore16 = new ArrayList<String>();
		lore16.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "64");
		lore16.add("");
		lore16.add(ChatColor.GRAY + "Click to view more!");
		meta16.setLore(lore16);

		meta17.setDisplayName(ChatColor.YELLOW + "Witherman");
		ArrayList<String> lore17 = new ArrayList<String>();
		lore17.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "72");
		lore17.add("");
		lore17.add(ChatColor.GRAY + "Click to view more!");
		meta17.setLore(lore17);

		meta18.setDisplayName(ChatColor.YELLOW + "Sans");
		ArrayList<String> lore18 = new ArrayList<String>();
		lore18.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "164");
		lore18.add("");
		lore18.add(ChatColor.GRAY + "Click to view more!");
		meta18.setLore(lore18);

		meta19.setDisplayName(ChatColor.YELLOW + "Thanos");
		ArrayList<String> lore19 = new ArrayList<String>();
		lore19.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "420");
		lore19.add("");
		lore19.add(ChatColor.GRAY + "Click to view more!");
		meta19.setLore(lore19);

		meta20.setDisplayName(ChatColor.YELLOW + "Spoon");
		ArrayList<String> lore20 = new ArrayList<String>();
		lore20.add(ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + "1,333");
		lore20.add("");
		lore20.add(ChatColor.GRAY + "Click to view more!");
		meta20.setLore(lore20);

		meta21.setDisplayName(ChatColor.RED + "Close");

		kit1.setItemMeta(meta1);
		kit2.setItemMeta(meta2);
		kit3.setItemMeta(meta3);
		kit4.setItemMeta(meta4);
		kit5.setItemMeta(meta5);
		kit6.setItemMeta(meta6);
		kit7.setItemMeta(meta7);
		kit8.setItemMeta(meta8);
		kit9.setItemMeta(meta9);
		kit10.setItemMeta(meta10);
		kit11.setItemMeta(meta11);
		kit12.setItemMeta(meta12);
		kit13.setItemMeta(meta13);
		kit14.setItemMeta(meta14);
		kit15.setItemMeta(meta15);
		kit16.setItemMeta(meta16);
		kit17.setItemMeta(meta17);
		kit18.setItemMeta(meta18);
		kit19.setItemMeta(meta19);
		kit20.setItemMeta(meta20);
		kit21.setItemMeta(meta21);
		soon.setItemMeta(soonm);

		inv.setItem(10, kit1);
		inv.setItem(11, kit2);
		inv.setItem(12, kit3);
		inv.setItem(13, kit4);
		inv.setItem(14, kit5);
		inv.setItem(15, kit6);
		inv.setItem(16, kit7);
		inv.setItem(19, kit8);
		inv.setItem(20, kit9);
		inv.setItem(21, kit10);
		inv.setItem(22, kit11);
		inv.setItem(23, kit12);
		inv.setItem(24, kit13);
		inv.setItem(25, kit14);
		inv.setItem(28, kit15);
		inv.setItem(29, kit16);
		inv.setItem(30, kit17);
		inv.setItem(31, kit18);
		inv.setItem(32, kit19);
		inv.setItem(33, kit20);
		/*
		 * inv.setItem(24, soon); inv.setItem(25, soon); inv.setItem(28, soon);
		 * inv.setItem(29, soon); inv.setItem(30, soon); inv.setItem(31, soon);
		 * inv.setItem(32, soon); inv.setItem(33, soon);
		 */

		inv.setItem(49, kit21);
		player.updateInventory();
		player.openInventory(inv);
	}

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		if (!Configs.kitsconfig.getConfig().contains("players." + uuid)) {
			Configs.kitsconfig.getConfig().set("players." + uuid + ".swordsman", 1);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".brute", 1);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".scout", 1);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".apprentice", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".horseman", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".essence", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".jouster", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".enderman", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".assassin", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".pyro", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".creeper", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".samurai", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".templar", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".prestige", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".valkyrie", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".champion", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".witherman", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".sans", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".thanos", 0);
			Configs.kitsconfig.getConfig().set("players." + uuid + ".spoon", 0);
			Configs.kitsconfig.saveConfig();
		}
		if (!Configs.kitsconfig.getConfig().contains("cooldowncounter." + player.getName())) {
			Configs.kitsconfig.getConfig().set("cooldowncounter." + player.getName(), 0);
			Configs.kitsconfig.saveConfig();
		}
		return;
	}

	public void InstantFirework(FireworkEffect fe, Location loc) {
		Firework f = (Firework) loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(fe);
		f.setFireworkMeta(fm);
		try {
			Class<?> entityFireworkClass = getClass("net.minecraft.server.", "EntityFireworks");
			Class<?> craftFireworkClass = getClass("org.bukkit.craftbukkit.", "entity.CraftFirework");
			Object firework = craftFireworkClass.cast(f);
			Method handle = firework.getClass().getMethod("getHandle");
			Object entityFirework = handle.invoke(firework);
			Field expectedLifespan = entityFireworkClass.getDeclaredField("expectedLifespan");
			Field ticksFlown = entityFireworkClass.getDeclaredField("ticksFlown");
			ticksFlown.setAccessible(true);
			ticksFlown.setInt(entityFirework, expectedLifespan.getInt(entityFirework) - 1);
			ticksFlown.setAccessible(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Class<?> getClass(String prefix, String nmsClassString) throws ClassNotFoundException {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String name = prefix + version + nmsClassString;
		Class<?> nmsClass = Class.forName(name);
		return nmsClass;
	}

	public void firework(Location loc, Player player) {
		Firework f = (Firework) player.getWorld().spawn(loc, Firework.class);

		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(Type.BURST).withColor(Color.YELLOW)
				.withFade(Color.WHITE).build());
		fm.setPower(3);

		FireworkEffect fe = FireworkEffect.builder().flicker(true).trail(true).with(Type.BURST).withColor(Color.YELLOW)
				.withFade(Color.WHITE).build();

		InstantFirework(fe, loc);

		return;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getClickedBlock() != null) {

			Block block = event.getClickedBlock();
			Material signm = block.getType();
			if (signm.equals(Material.SIGN_POST) || signm.equals(Material.WALL_SIGN)) {

				Player player = event.getPlayer();
				String uuid = player.getUniqueId().toString();
				Sign sign = (Sign) block.getState();
				String[] ln = sign.getLines();

				double x = block.getX() + 0.5;
				double y = block.getY() + 0.5;
				double z = block.getZ() + 0.5;

				Location loc = new Location(Bukkit.getServer().getWorld("main"), x, y, z);

				if (ln[1].toLowerCase().equalsIgnoreCase("Swordsman")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".swordsman") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 1) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".swordsman", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Swordsman"
								+ ChatColor.GRAY + "!");

						firework(loc, player);

						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 1) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "1"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Brute")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".brute") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 1) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".brute", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Brute"
								+ ChatColor.GRAY + "!");

						firework(loc, player);

						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 1) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "1"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Scout")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".scout") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 1) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".scout", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Scout"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 1) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "1"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Apprentice")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".apprentice") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 1) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".apprentice", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Apprentice"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 1) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "1"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Horseman")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".horseman") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 2) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".horseman", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Horseman"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 2) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "2"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Essence")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".essence") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 2) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".essence", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Essence"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 2) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "2"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Jouster")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".jouster") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 2) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".jouster", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Jouster"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 2) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "2"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Enderman")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".enderman") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 3) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".enderman", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Enderman"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 3) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "3"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Assassin")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".assassin") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 3) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".assassin", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Assassin"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 3) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "3"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Pyro")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".pyro") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 3) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".pyro", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Pyro"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 3) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "3"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Creeper")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".creeper") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 4) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".creeper", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Creeper"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 4) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "4"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Samurai")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".samurai") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 4) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".samurai", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Samurai"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 4) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "4"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Templar")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".templar") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 4) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".templar", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Templar"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 4) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "4"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Prestige")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".prestige") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 5) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".prestige", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Prestige"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 5) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "5"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Valkyrie")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".valkyrie") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 5) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".valkyrie", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Valkyrie"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 5) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "5"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Champion")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".champion") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 5) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".champion", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Champion"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 5) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "5"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Witherman")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".witherman") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 6) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".witherman", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Witherman"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 6) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "6"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Sans")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".sans") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 7) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".sans", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Sans"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 7) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "7"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Thanos")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".thanos") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 8) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".thanos", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Thanos"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 8) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "8"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
				if (ln[1].toLowerCase().equalsIgnoreCase("Spoon")) {
					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".spoon") == 0
							&& Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") >= 9) {
						Configs.kitsconfig.getConfig().set("players." + uuid + ".spoon", 1);
						player.sendMessage(ChatColor.GRAY + "You have unlocked kit " + ChatColor.YELLOW + "Spoon"
								+ ChatColor.GRAY + "!");
						firework(loc, player);
						Configs.kitsconfig.saveConfig();
					}
					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 9) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "9"
								+ ChatColor.GRAY + " to unlock this kit!");
					}
				}
			}
			return;
		}
		return;
	}

	@EventHandler
	public void preprocess(PlayerCommandPreprocessEvent e) {

		String base = e.getMessage().split(" ")[0];

		if (base.equalsIgnoreCase("/vote")) {
			Player p = e.getPlayer();
			e.setCancelled(true);
			vote(p);
		}

		return;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("vote")) {
			Player player = (Player) sender;
			vote(player);
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("kit")) {

			if (args.length == 0) {
				Player player = (Player) sender;
				kit(player);
				return true;
			}

			Player player = Bukkit.getPlayer(sender.getName());
			String uuid = player.getUniqueId().toString();

			ItemStack helm1 = new ItemStack(Material.IRON_HELMET, 1);
			helm1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack helm2 = new ItemStack(Material.IRON_HELMET, 1);
			helm2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			ItemStack helm3 = new ItemStack(Material.DIAMOND_HELMET, 1);
			helm3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack helm4 = new ItemStack(Material.DIAMOND_HELMET, 1);
			helm4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack helm5 = new ItemStack(Material.DIAMOND_HELMET, 1);
			helm5.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			ItemStack helm6 = new ItemStack(Material.DIAMOND_HELMET, 1);
			helm6.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

			ItemStack chest1 = new ItemStack(Material.IRON_CHESTPLATE, 1);
			chest1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack chest2 = new ItemStack(Material.IRON_CHESTPLATE, 1);
			chest2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			ItemStack chest3 = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
			chest3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack chest4 = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
			chest4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack chest5 = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
			chest5.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			ItemStack chest6 = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
			chest6.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

			ItemStack leg1 = new ItemStack(Material.IRON_LEGGINGS, 1);
			leg1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack leg2 = new ItemStack(Material.IRON_LEGGINGS, 1);
			leg2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			ItemStack leg3 = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
			leg3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack leg4 = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
			leg4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack leg5 = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
			leg5.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			ItemStack leg6 = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
			leg6.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

			ItemStack b1 = new ItemStack(Material.IRON_BOOTS, 1);
			b1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack b2 = new ItemStack(Material.IRON_BOOTS, 1);
			b2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			ItemStack b3 = new ItemStack(Material.DIAMOND_BOOTS, 1);
			b3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack b4 = new ItemStack(Material.DIAMOND_BOOTS, 1);
			b4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack b5 = new ItemStack(Material.DIAMOND_BOOTS, 1);
			b5.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			ItemStack b6 = new ItemStack(Material.DIAMOND_BOOTS, 1);
			b6.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);

			ItemStack bow1 = new ItemStack(Material.BOW, 1);
			bow1.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
			ItemStack bow2 = new ItemStack(Material.BOW, 1);
			bow2.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
			ItemStack bow3 = new ItemStack(Material.BOW, 1);
			bow3.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
			ItemStack bow4 = new ItemStack(Material.BOW, 1);
			bow4.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
			ItemStack bow5 = new ItemStack(Material.BOW, 1);
			bow5.addEnchantment(Enchantment.ARROW_DAMAGE, 5);

			ItemStack s1 = new ItemStack(Material.IRON_SWORD, 1);
			s1.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			ItemMeta smeta1 = s1.getItemMeta();

			ItemStack s2 = new ItemStack(Material.DIAMOND_SWORD, 1);
			s2.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			ItemMeta smeta2 = s2.getItemMeta();

			ItemStack s3 = new ItemStack(Material.IRON_SWORD, 1);
			s3.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			ItemMeta smeta3 = s3.getItemMeta();

			ItemStack s4 = new ItemStack(Material.DIAMOND_SWORD, 1);
			s4.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			ItemMeta smeta4 = s4.getItemMeta();

			ItemStack s5 = new ItemStack(Material.IRON_SWORD, 1);
			s5.addEnchantment(Enchantment.DAMAGE_ALL, 3);
			ItemMeta smeta5 = s5.getItemMeta();

			ItemStack s6 = new ItemStack(Material.DIAMOND_SWORD, 1);
			s6.addEnchantment(Enchantment.DAMAGE_ALL, 3);
			ItemMeta smeta6 = s6.getItemMeta();

			ItemStack s7 = new ItemStack(Material.IRON_SWORD, 1);
			s7.addEnchantment(Enchantment.DAMAGE_ALL, 4);
			ItemMeta smeta7 = s7.getItemMeta();

			ItemStack s8 = new ItemStack(Material.DIAMOND_SWORD, 1);
			s8.addEnchantment(Enchantment.DAMAGE_ALL, 4);
			ItemMeta smeta8 = s8.getItemMeta();

			ItemStack s9 = new ItemStack(Material.IRON_SWORD, 1);
			s9.addEnchantment(Enchantment.DAMAGE_ALL, 5);
			ItemMeta smeta9 = s9.getItemMeta();

			ItemStack s10 = new ItemStack(Material.DIAMOND_SWORD, 1);
			s10.addEnchantment(Enchantment.DAMAGE_ALL, 5);
			ItemMeta smeta10 = s10.getItemMeta();

			int helm = 0;
			int chest = 0;
			int leg = 0;
			int b = 0;
			int bow = 0;
			int s = 0;

			boolean h = true;
			boolean check = false;
			boolean kit = false;

			int pts = Configs.playerstats.getConfig().getInt("players." + uuid + ".Points");
			int lvl = Configs.playerstats.getConfig().getInt("players." + uuid + ".Level");

			if (args.length == 1) {

				int li = Configs.playerstats.getConfig().getInt("players." + uuid + ".li");

				// if (li == 1) {
				if (cooldownTime.containsKey(player.getName())) {
					if (cooldownTime.get(player.getName()) != 1)
						player.sendMessage(ChatColor.GRAY + "You can't get a kit again for another " + ChatColor.YELLOW
								+ cooldownTime.get(player.getName()) + ChatColor.GRAY
								+ " seconds or until you respawn.");
					if (cooldownTime.get(player.getName()) == 1)
						player.sendMessage(ChatColor.GRAY + "You can't get a kit again for another " + ChatColor.YELLOW
								+ cooldownTime.get(player.getName()) + ChatColor.GRAY
								+ " second or until you respawn.");
					return true;
				}
				// return true;
				// }

				if (args[0].equalsIgnoreCase("swordsman")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".swordsman") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW
								+ "Swordsman" + ChatColor.GRAY + ".");
						return true;
					}

					helm = (int) (Math.random() * 3 + 3);
					chest = (int) (Math.random() * 2 + 1);
					leg = (int) (Math.random() * 1 + 3);
					b = (int) (Math.random() * 2 + 1);
					bow = (int) (Math.random() * 3 + 1);
					s = (int) (Math.random() * 3 + 4);

					h = false;
					kit = true;
					check = true;

					smeta4.setDisplayName(ChatColor.AQUA + "Swordsman Sword");
					smeta5.setDisplayName(ChatColor.AQUA + "Swordsman Sword");
					smeta6.setDisplayName(ChatColor.AQUA + "Swordsman Sword");
					s4.setItemMeta(smeta4);
					s5.setItemMeta(smeta5);
					s6.setItemMeta(smeta6);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					Configs.kitsconfig.saveConfig();
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Swordsman"
							+ ChatColor.GRAY + ".");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "refresh " + player.getName());
				}

				else if (args[0].equalsIgnoreCase("brute")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".brute") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Brute"
								+ ChatColor.GRAY + ".");
						return true;
					}

					helm = (int) (Math.random() * 2 + 1);
					chest = (int) (Math.random() * 2 + 4);
					leg = (int) (Math.random() * 2 + 3);
					b = (int) (Math.random() * 3 + 3);
					bow = (int) (Math.random() * 3 + 1);
					s = (int) (Math.random() * 3 + 2);

					h = false;
					kit = true;
					check = true;

					smeta4.setDisplayName(ChatColor.AQUA + "Brute Sword");
					smeta2.setDisplayName(ChatColor.AQUA + "Brute Sword");
					smeta3.setDisplayName(ChatColor.AQUA + "Brute Sword");
					s4.setItemMeta(smeta4);
					s2.setItemMeta(smeta2);
					s3.setItemMeta(smeta3);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					Configs.kitsconfig.saveConfig();
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Brute"
							+ ChatColor.GRAY + ".");
				}

				else if (args[0].equalsIgnoreCase("scout")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".scout") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Scout"
								+ ChatColor.GRAY + ".");
						return true;
					}

					helm = (int) (Math.random() * 2 + 1);
					chest = (int) (Math.random() * 2 + 1);
					leg = (int) (Math.random() * 2 + 4);
					b = 0;

					ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
					boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					ItemMeta bootsn = boots.getItemMeta();
					bootsn.setDisplayName(ChatColor.GREEN + "Scout Boots");
					ArrayList<String> bootsl = new ArrayList<String>();
					// bootsl.add(ChatColor.GRAY + "Protection I");
					bootsl.add(ChatColor.GRAY + "Speed I");
					bootsl.add("");
					bootsl.add(ChatColor.DARK_GRAY + "Hitting players will grant");
					bootsl.add(ChatColor.DARK_GRAY + "Speed I for 2s.");
					bootsn.setLore(bootsl);

					bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

					boots.setItemMeta(bootsn);

					if (player.getInventory().getBoots() == null) {
						player.getInventory().setBoots(boots);
						/*
						 * Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						 * "replaceitem entity " + player.getName() +
						 * " slot.armor.feet diamond_boots 1 0 {display:{Name:\"§aScout Boots§r\",Lore:[\"§7Speed I§r\"]},AttributeModifiers:[{"
						 * +
						 * "AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:0.02,Operation:0,UUIDMost:52425,UUIDLeast:700563}],ench:[{id:0,lvl:1}],HideFlags:2}"
						 * );
						 */
					} else {
						player.getInventory().addItem(boots);
						/*
						 * Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " +
						 * player.getName() +
						 * " diamond_boots 1 0 {display:{Name:\"§aScout Boots§r\",Lore:[\"§7Speed I§r\"]},AttributeModifiers:[{"
						 * +
						 * "AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:0.02,Operation:0,UUIDMost:52425,UUIDLeast:700563}],ench:[{id:0,lvl:1}],HideFlags:2}"
						 * );
						 */
					}

					bow = (int) (Math.random() * 2 + 2);
					s = 4;

					h = false;
					kit = true;
					check = true;

					smeta4.setDisplayName(ChatColor.AQUA + "Scout Sword");
					s4.setItemMeta(smeta4);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					Configs.kitsconfig.saveConfig();
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Scout"
							+ ChatColor.GRAY + ".");
				}

				else if (args[0].equalsIgnoreCase("apprentice")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".apprentice") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW
								+ "Apprentice" + ChatColor.GRAY + ".");
						return true;
					}

					if (pts < 12) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "12"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 2 + 3);
					chest = (int) (Math.random() * 4 + 3);
					leg = (int) (Math.random() * 2 + 1);
					b = (int) (Math.random() * 2 + 4);
					bow = (int) (Math.random() * 3 + 2);
					s = (int) (Math.random() * 3 + 4);

					kit = true;
					check = true;

					smeta4.setDisplayName(ChatColor.AQUA + "Apprentice Sword");
					smeta5.setDisplayName(ChatColor.AQUA + "Apprentice Sword");
					smeta6.setDisplayName(ChatColor.AQUA + "Apprentice Sword");
					s4.setItemMeta(smeta4);
					s5.setItemMeta(smeta5);
					s6.setItemMeta(smeta6);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Apprentice"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 12);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("horseman")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".horseman") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Horseman"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 2) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "2"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 12) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "12"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					ItemStack sword = new ItemStack(Material.IRON_AXE, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Horseman Axe");
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					helm = (int) (Math.random() * 2 + 1);
					chest = 5;
					leg = 5;
					b = (int) (Math.random() * 2 + 1);
					bow = (int) (Math.random() * 3 + 2);

					kit = true;
					check = true;

					ItemStack horse1 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
					ItemMeta t1 = horse1.getItemMeta();
					t1.setDisplayName(ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Horseman)");
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(ChatColor.DARK_GRAY + "Health Boost I");
					t1.setLore(lore);
					horse1.setItemMeta(t1);

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							player.getInventory().addItem(horse1);
						}
					}, 2);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Horseman"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 12);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("essence")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".essence") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Essence"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 2) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "2"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 18) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "18"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = 4;
					chest = 4;
					leg = 4;
					b = 4;
					bow = (int) (Math.random() * 3 + 2);
					s = 4;

					smeta4.setDisplayName(ChatColor.AQUA + "Essence Sword");
					s4.setItemMeta(smeta4);

					kit = false;
					check = true;

					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"effect " + player.getName() + " haste 120");

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " fishing_rod");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " golden_apple 12");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " arrow 48");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " diamond_pickaxe 1 0 {ench:[{id:32,lvl:1}]}");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " diamond_axe 1 0 {ench:[{id:32,lvl:1}]}");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " diamond_shovel 1 0 {ench:[{id:32,lvl:1}]}");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " cobblestone 64");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " planks 64");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " water_bucket 2");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " lava_bucket 2");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " cooked_beef 12");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " enchanting_table");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " experience_bottle 32");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " book 42");
						}
					}, 2);

					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " compass 1 0 {display:{Name:\"§fPlayer Tracker\"}}");
						}
					}, 3);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Essence"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 18);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("jouster")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".jouster") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Jouster"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 2) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "2"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 18) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "18"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					ItemStack sword = new ItemStack(Material.DIAMOND_AXE, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Jouster Axe");
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					helm = (int) (Math.random() * 2 + 1);
					chest = 5;
					leg = 5;
					b = (int) (Math.random() * 2 + 1);
					bow = (int) (Math.random() * 3 + 2);

					kit = true;
					check = true;

					ItemStack horse2 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
					ItemMeta t2 = horse2.getItemMeta();
					t2.setDisplayName(ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Jouster)");
					ArrayList<String> lore2 = new ArrayList<String>();
					lore2.add(ChatColor.DARK_GRAY + "Health Boost VIII");
					lore2.add(ChatColor.DARK_GRAY + "Jump Boost I");
					t2.setLore(lore2);
					horse2.setItemMeta(t2);

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							player.getInventory().addItem(horse2);
						}
					}, 2);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Jouster"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 18);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("assassin")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".assassin") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Assassin"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 3) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "3"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 24) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "24"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 4 + 3);
					chest = (int) (Math.random() * 2 + 1);
					leg = (int) (Math.random() * 2 + 1);
					b = (int) (Math.random() * 2 + 1);
					bow = 5;
					s = (int) (Math.random() * 3 + 6);

					smeta6.setDisplayName(ChatColor.AQUA + "Assassin Sword");
					smeta7.setDisplayName(ChatColor.AQUA + "Assassin Sword");
					smeta8.setDisplayName(ChatColor.AQUA + "Assassin Sword");
					s6.setItemMeta(smeta6);
					s7.setItemMeta(smeta7);
					s8.setItemMeta(smeta8);

					kit = true;
					check = true;

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"effect " + player.getName() + " strength 30");
							ItemStack pearl = new ItemStack(Material.IRON_HOE, 1);
							pearl.addEnchantment(Enchantment.DURABILITY, 1);
							pearl.setDurability((short) 250);
							ItemMeta pearln = pearl.getItemMeta();
							pearln.setDisplayName(ChatColor.RED + "The Scythe");
							ArrayList<String> pearl1 = new ArrayList<String>();
							pearl1.add(ChatColor.GRAY + "Executioner I");
							pearl1.add("");
							pearl1.add(ChatColor.DARK_GRAY + "Instantly kills anyone");
							pearl1.add(ChatColor.DARK_GRAY + "under 6 HP.");
							pearln.setLore(pearl1);

							pearln.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
							pearl.setItemMeta(pearln);
							player.getInventory().addItem(pearl);
						}
					}, 2);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Assassin"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 24);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("enderman")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".enderman") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Enderman"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 3) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "3"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 24) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "24"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 2 + 3);
					chest = (int) (Math.random() * 2 + 1);
					leg = 6;
					b = 0;
					bow = (int) (Math.random() * 3 + 2);
					s = (int) (Math.random() * 3 + 5);

					ItemStack enderboots = new ItemStack(Material.DIAMOND_BOOTS, 1);
					enderboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
					enderboots.addEnchantment(Enchantment.PROTECTION_FALL, 2);
					ItemMeta endermeta = enderboots.getItemMeta();
					endermeta.setDisplayName(ChatColor.GREEN + "Enderman Boots");
					enderboots.setItemMeta(endermeta);

					if (player.getInventory().getBoots() == null) {
						player.getInventory().setBoots(enderboots);
					} else {
						player.getInventory().addItem(enderboots);
					}

					smeta6.setDisplayName(ChatColor.AQUA + "Enderman Sword");
					smeta7.setDisplayName(ChatColor.AQUA + "Enderman Sword");
					smeta5.setDisplayName(ChatColor.AQUA + "Enderman Sword");
					s6.setItemMeta(smeta6);
					s7.setItemMeta(smeta7);
					s5.setItemMeta(smeta5);

					kit = true;
					check = true;

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " ender_pearl 4");

							ItemStack tpcr = new ItemStack(Material.NETHER_STAR, 8);
							ItemMeta i = tpcr.getItemMeta();
							i.setDisplayName(ChatColor.DARK_PURPLE + "Teleport Crystal");

							ArrayList<String> lore1 = new ArrayList<String>();
							lore1.add(ChatColor.DARK_GRAY + "Teleport to a player within");
							lore1.add(ChatColor.DARK_GRAY + "10 meters.");
							i.setLore(lore1);

							tpcr.setItemMeta(i);
							player.getInventory().addItem(tpcr);
						}
					}, 2);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Enderman"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 24);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("pyro")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".pyro") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Pyro"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 3) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "3"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 28) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "28"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 2 + 5);
					chest = (int) (Math.random() * 2 + 4);
					leg = (int) (Math.random() * 3 + 3);
					b = (int) (Math.random() * 2 + 1);
					s = (int) (Math.random() * 3 + 5);

					smeta6.setDisplayName(ChatColor.AQUA + "Pyro Sword");
					smeta7.setDisplayName(ChatColor.AQUA + "Pyro Sword");
					smeta5.setDisplayName(ChatColor.AQUA + "Pyro Sword");
					s6.setItemMeta(smeta6);
					s7.setItemMeta(smeta7);
					s5.setItemMeta(smeta5);

					kit = false;
					check = true;

					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"effect " + player.getName() + " fire_resistance 180");

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " golden_sword 1 0 {ench:[{id:20,lvl:1}]}");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " bow 1 0 {ench:[{id:50,lvl:1}]}");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " iron_pickaxe");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " iron_axe");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " iron_shovel");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " arrow 48");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " fishing_rod");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " cobblestone 64");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " planks 64");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " golden_apple 12");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " water_bucket 2");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " lava_bucket 10");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " cooked_beef 12");
						}
					}, 2);
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " compass 1 0 {display:{Name:\"§fPlayer Tracker\"}}");
						}
					}, 3);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Pyro"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 28);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("creeper")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".creeper") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Creeper"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 4) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "4"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 28) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "28"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 2 + 4);
					chest = (int) (Math.random() * 2 + 4);
					leg = 6;
					b = (int) (Math.random() * 2 + 4);
					bow = (int) (Math.random() * 2 + 4);
					s = 7;

					smeta7.setDisplayName(ChatColor.AQUA + "Creeper Sword");
					s7.setItemMeta(smeta7);

					kit = true;
					check = true;

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " tnt 6");
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " redstone_block 2 0 {display:{Name:\"§cBoom Box\"}}");
						}
					}, 2);

					int totem = (int) (Math.random() * 6 + 1);
					if (totem == 4) {
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName()
										+ " nether_wart 1 0 {display:{Name:\"§cBlast Totem§r\"},ench:[{id:34,lvl:1}],HideFlags:1}");
							}
						}, 2);
					}

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Creeper"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 28);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("samurai")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".samurai") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Samurai"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 4) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "4"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 36) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "36"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 3 + 3);
					chest = (int) (Math.random() * 4 + 3);
					leg = (int) (Math.random() * 3 + 3);
					b = (int) (Math.random() * 3 + 3);
					bow = (int) (Math.random() * 3 + 3);
					s = 0;

					ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Samurai Sword");
					ArrayList<String> swordl = new ArrayList<String>();
					swordl.add(ChatColor.GRAY + "Block I");
					swordl.add("");
					swordl.add(ChatColor.DARK_GRAY + "Blocking with this sword");
					swordl.add(ChatColor.DARK_GRAY + "absorbs extra damage.");
					swordn.setLore(swordl);
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							ItemStack horse3 = new ItemStack(Material.MONSTER_EGG, 1, (short) 100);
							ItemMeta t3 = horse3.getItemMeta();
							t3.setDisplayName(ChatColor.RESET + "Spawn Horse " + ChatColor.GRAY + "(Areion)");
							ArrayList<String> lore3 = new ArrayList<String>();
							lore3.add(ChatColor.DARK_GRAY + "Health Boost IX");
							lore3.add(ChatColor.DARK_GRAY + "Jump Boost I");
							lore3.add(ChatColor.DARK_GRAY + "Regeneration I");
							t3.setLore(lore3);
							horse3.setItemMeta(t3);
							player.getInventory().addItem(horse3);
						}
					}, 2);

					kit = true;
					check = true;

					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"effect " + player.getName() + " speed 60 1");

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Samurai"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 36);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("templar")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".templar") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Templar"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 4) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "4"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 36) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "36"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 2 + 4);
					chest = (int) (Math.random() * 2 + 4);
					leg = (int) (Math.random() * 3 + 4);
					b = (int) (Math.random() * 3 + 4);
					bow = (int) (Math.random() * 3 + 3);
					// s = (int)(Math.random() * 1 + 7);

					ItemStack sword = new ItemStack(Material.GOLD_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
					sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
					ItemStack sword2 = new ItemStack(Material.GOLD_SWORD, 1);
					sword2.addEnchantment(Enchantment.KNOCKBACK, 2);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Templar Sword");
					ArrayList<String> swordl = new ArrayList<String>();
					// swordl.add(ChatColor.GRAY + "Sharpness V");
					// swordl.add(ChatColor.GRAY + "Unbreaking X");
					swordl.add(ChatColor.GRAY + "Healing II");
					swordl.add("");
					swordl.add(ChatColor.DARK_GRAY + "Right-click on non-combat-");
					swordl.add(ChatColor.DARK_GRAY + "tagged players to heal them");
					swordl.add(ChatColor.DARK_GRAY + "8 HP. (12s cooldown)");
					// swordl.add("");
					// swordl.add(ChatColor.BLUE + "+10.25 Attack Damage");
					swordn.setLore(swordl);
					// swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
					sword.setItemMeta(swordn);

					player.getInventory().addItem(sword);
					player.getInventory().addItem(sword2);

					kit = true;
					check = true;

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Templar"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 36);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("prestige")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".prestige") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Prestige"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 5) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "5"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 42) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "42"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 2 + 5);
					chest = (int) (Math.random() * 3 + 4);
					leg = (int) (Math.random() * 3 + 4);
					b = (int) (Math.random() * 2 + 5);
					bow = 5;

					ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Prestige Sword");
					sword.setItemMeta(swordn);

					player.getInventory().addItem(sword);

					kit = true;
					check = true;

					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"effect " + player.getName() + " absorption 15 2");

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
									"give " + player.getName() + " obsidian 32");
						}
					}, 2);
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							ItemStack bdr = new ItemStack(Material.BEDROCK, 1);
							ItemMeta bdrn = bdr.getItemMeta();
							bdrn.setDisplayName(ChatColor.WHITE + "unsellable bedrock lol");
							bdr.setItemMeta(bdrn);
							player.getInventory().addItem(bdr);
						}
					}, 5);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Prestige"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 42);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("valkyrie")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".valkyrie") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Valkyrie"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 5) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "5"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 42) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "42"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 2 + 5);
					chest = (int) (Math.random() * 2 + 5);
					leg = (int) (Math.random() * 2 + 4);
					bow = 5;

					ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.AQUA + "Valkyrie Sword");
					ArrayList<String> swordl = new ArrayList<String>();
					swordl.add(ChatColor.GRAY + "Lightning I");
					swordn.setLore(swordl);
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
					boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
					ItemMeta bootsn = boots.getItemMeta();
					bootsn.setDisplayName(ChatColor.GREEN + "Valkyrie Boots");
					ArrayList<String> bootsl = new ArrayList<String>();
					// bootsl.add(ChatColor.GRAY + "Protection I");
					bootsl.add(ChatColor.GRAY + "Speed I");
					bootsl.add("");
					bootsl.add(ChatColor.DARK_GRAY + "Hitting players will grant");
					bootsl.add(ChatColor.DARK_GRAY + "Speed I for 2s.");
					bootsn.setLore(bootsl);
					boots.setItemMeta(bootsn);

					bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

					if (player.getInventory().getBoots() == null) {
						player.getInventory().setBoots(boots);
					} else {
						player.getInventory().addItem(boots);
					}

					kit = true;
					check = true;

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Valkyrie"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 42);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("champion")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".champion") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Champion"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 5) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "5"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 64) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "64"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = (int) (Math.random() * 2 + 5);
					chest = (int) (Math.random() * 2 + 5);
					leg = (int) (Math.random() * 2 + 5);
					b = (int) (Math.random() * 2 + 5);
					bow = 5;
					s = (int) (Math.random() * 3 + 6);

					smeta6.setDisplayName(ChatColor.AQUA + "Champion Sword");
					smeta7.setDisplayName(ChatColor.AQUA + "Champion Sword");
					smeta8.setDisplayName(ChatColor.AQUA + "Champion Sword");
					s6.setItemMeta(smeta6);
					s7.setItemMeta(smeta7);
					s8.setItemMeta(smeta8);

					kit = true;
					check = true;

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Champion"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 64);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("thanos")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".thanos") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Thanos"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 8) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "8"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 420) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "420"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = 6;
					chest = 6;
					leg = 6;
					b = 6;

					bow = 5;
					s = 8;

					kit = true;
					check = true;

					smeta8.setDisplayName(ChatColor.AQUA + "Thanos Sword");
					s8.setItemMeta(smeta8);

					ItemStack pearl = new ItemStack(Material.GOLD_NUGGET, 1);
					pearl.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					ItemMeta pearln = pearl.getItemMeta();
					pearln.setDisplayName(ChatColor.DARK_PURPLE + "The Infinity Gauntlet");
					ArrayList<String> pearl1 = new ArrayList<String>();
					pearl1.add(ChatColor.GRAY + "Thanos I");
					pearl1.add("");
					pearl1.add(ChatColor.DARK_GRAY + "There is a 2% chance to cut");
					pearl1.add(ChatColor.DARK_GRAY + "a player's health in half.");
					pearl1.add("");
					pearl1.add(ChatColor.DARK_GRAY + "Right-click to detonate.");
					pearl1.add(ChatColor.DARK_GRAY + "(10s cooldown)");
					pearln.setLore(pearl1);
					pearl.setItemMeta(pearln);

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							player.getInventory().addItem(pearl);
						}
					}, 5);

					int q = (int) (Math.random() * 9 + 1);
					if (q == 1)
						sender.sendMessage(ChatColor.DARK_PURPLE + "The end is near.");
					if (q == 2)
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "You’re strong. But I could snap my fingers, and you’d all cease to exist.");
					if (q == 3)
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "Fun isn’t something one considers when balancing the universe. But this… does put a smile on my face.");
					if (q == 4)
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "Stark… you have my respect. I hope the people of Earth will remember you.");
					if (q == 5)
						sender.sendMessage(ChatColor.DARK_PURPLE + "Perfectly balanced, as all things should be.");
					if (q == 6)
						sender.sendMessage(ChatColor.DARK_PURPLE + "You should have gone for the head.");
					if (q == 7)
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "I know what it’s like to lose. To feel so desperately that you’re right, yet to fail nonetheless. Dread it. Run from it. Destiny still arrives. Or should I say, I have.");
					if (q == 8)
						sender.sendMessage(ChatColor.DARK_PURPLE
								+ "I ignored my destiny once, I can not do that again. Even for you. I’m sorry Little one.");
					if (q == 9)
						sender.sendMessage(ChatColor.DARK_PURPLE + "The hardest choices require the strongest wills.");

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Thanos"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 420);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("witherman")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".witherman") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW
								+ "Witherman" + ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 6) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "6"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 72) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "72"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = 6;
					chest = 6;
					leg = 6;
					b = 6;
					bow = 5;

					ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
					sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
					sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
					ItemMeta swordn = sword.getItemMeta();
					swordn.setDisplayName(ChatColor.DARK_GRAY + "Witherman Sword");
					ArrayList<String> swordl = new ArrayList<String>();
					swordl.add(ChatColor.GRAY + "Wither X");
					swordn.setLore(swordl);
					sword.setItemMeta(swordn);
					player.getInventory().addItem(sword);

					kit = true;
					check = true;

					ItemStack pearl = new ItemStack(Material.IRON_HOE, 1);
					pearl.addEnchantment(Enchantment.DURABILITY, 1);
					pearl.setDurability((short) 250);
					ItemMeta pearln = pearl.getItemMeta();
					pearln.setDisplayName(ChatColor.RED + "The Scythe");
					ArrayList<String> pearl1 = new ArrayList<String>();
					pearl1.add(ChatColor.GRAY + "Executioner I");
					pearl1.add("");
					pearl1.add(ChatColor.DARK_GRAY + "Instantly kills anyone");
					pearl1.add(ChatColor.DARK_GRAY + "under 6 HP.");
					pearln.setLore(pearl1);
					pearl.setItemMeta(pearln);
					player.getInventory().addItem(pearl);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Witherman"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 72);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("sans")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".sans") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Sans"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 7) {
						player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "7"
								+ ChatColor.GRAY + " to unlock this kit!");
						return true;
					}

					if (pts < 164) {
						sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "164"
								+ ChatColor.GRAY + " points to purchase this kit.");
						return true;
					}

					helm = 6;
					chest = 6;
					leg = 6;
					b = 6;
					bow = 0;

					kit = true;
					check = true;

					ItemStack boen = new ItemStack(Material.BONE, 1);
					boen.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
					ItemMeta boenn = boen.getItemMeta();
					boenn.setDisplayName(ChatColor.AQUA + "Sans Bone");
					ArrayList<String> boenl = new ArrayList<String>();
					boenl.add("");
					boenl.add(ChatColor.BLUE + "+12.5 Attack Damage");
					boenn.setLore(boenl);
					boen.setItemMeta(boenn);

					ItemStack boww = new ItemStack(Material.BOW, 1, (short) 355);
					ItemMeta t = boww.getItemMeta();
					t.setDisplayName(ChatColor.BLUE + "Gaster Blaster");
					ArrayList<String> bowl = new ArrayList<String>();
					bowl.add(ChatColor.GRAY + "do you wanna have a bad time?");
					t.setLore(bowl);
					boww.setItemMeta(t);

					player.getInventory().addItem(boen);
					player.getInventory().addItem(boww);

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Sans"
							+ ChatColor.GRAY + ".");
					Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 164);
					Configs.kitsconfig.saveConfig();
				}

				else if (args[0].equalsIgnoreCase("spoon")) {

					if (Configs.kitsconfig.getConfig().getInt("players." + uuid + ".spoon") == 0) {
						sender.sendMessage(ChatColor.GRAY + "You have not unlocked kit " + ChatColor.YELLOW + "Spoon"
								+ ChatColor.GRAY + ".");
						return true;
					}

					if (player.hasPermission("tml.spoon")) {

						sender.sendMessage(
								ChatColor.BLUE + "Your spoon rank grants you the power to use this kit freely.");

						/*
						 * helm = (int)(Math.random() * 1 + 6); chest = (int)(Math.random() * 1 + 6);
						 * leg = (int)(Math.random() * 1 + 6); b = (int)(Math.random() * 1 + 6); bow =
						 * (int)(Math.random() * 1 + 5);
						 * 
						 * kit = true;
						 */
						check = true;

						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName()
								+ " iron_shovel 1 0 {display:{Name:\"§r§9§kX§r"
								+ " §9The Spoon §kX§r\",Lore:[\"§7Lightning V\"]},ench:[{id:16,lvl:8},{id:32,lvl:8},{id:34,lvl:3}]}");

						// A SUPREME HEAD
						// ****************************************************************************************************************************************

					}

					else {

						if (Configs.playerstats.getConfig().getInt("players." + uuid + ".Level") < 9) {
							player.sendMessage(ChatColor.GRAY + "You must be at least level " + ChatColor.YELLOW + "9"
									+ ChatColor.GRAY + " to unlock this kit!");
							return true;
						}

						if (pts < 1333) {
							sender.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.YELLOW + "1,333"
									+ ChatColor.GRAY + " points to purchase this kit.");
							return true;
						}

						/*
						 * helm = (int)(Math.random() * 1 + 6); chest = (int)(Math.random() * 1 + 6);
						 * leg = (int)(Math.random() * 1 + 6); b = (int)(Math.random() * 1 + 6); bow =
						 * (int)(Math.random() * 1 + 5);
						 * 
						 * kit = true;
						 */
						check = true;

						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName()
								+ " iron_shovel 1 0 {display:{Name:\"§r§9§kX§r"
								+ " §9The Spoon §kX§r\",Lore:[\"§7Lightning V\"]},ench:[{id:16,lvl:8},{id:32,lvl:8},{id:34,lvl:3}]}");

						// A SUPREME HEAD
						// ****************************************************************************************************************************************

						Configs.playerstats.getConfig().set("players." + uuid + ".Points", pts - 1333);
					}

					Configs.playerstats.getConfig().set("players." + uuid + ".li", 1);
					sender.sendMessage(ChatColor.GRAY + "You have received kit " + ChatColor.YELLOW + "Spoon"
							+ ChatColor.GRAY + ".");
					Configs.kitsconfig.saveConfig();
				}

				else {
					sender.sendMessage(ChatColor.GRAY + "That kit does not exist.");
				}
				if (check == true) {

					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "refresh " + player.getName());

					cooldownCount(player);

					ItemStack tpcry = new ItemStack(Material.NETHER_STAR);
					ItemMeta i = tpcry.getItemMeta();
					i.setDisplayName(ChatColor.YELLOW + "Kit Selector " + ChatColor.GRAY + "(Right Click)");
					tpcry.setItemMeta(i);

					int amount = 0;
					if (!(tpcry == null)) {
						for (int ii = 0; ii < 36; ii++) {
							ItemStack slot = player.getInventory().getItem(ii);
							if (slot == null || !slot.isSimilar(tpcry))
								continue;
							amount += slot.getAmount();
						}
					}

					for (int ii = 1; ii <= amount; ii++) {
						player.getInventory().removeItem(tpcry);
					}

					ItemStack votem = new ItemStack(Material.PAPER, 1);
					ItemMeta im = votem.getItemMeta();
					im.setDisplayName(ChatColor.DARK_AQUA + "Vote! " + ChatColor.GRAY + "(Right Click)");
					votem.setItemMeta(im);

					int amount0 = 0;
					if (!(votem == null)) {
						for (int ii = 0; ii < 36; ii++) {
							ItemStack slot = player.getInventory().getItem(ii);
							if (slot == null || !slot.isSimilar(votem))
								continue;
							amount += slot.getAmount();
						}
					}

					for (int ii = 1; ii <= amount; ii++) {
						player.getInventory().removeItem(votem);
					}

					ItemStack disc = new ItemStack(Material.CLAY_BALL, 1);
					ItemMeta id = disc.getItemMeta();
					id.setDisplayName(ChatColor.BLUE + "Discord " + ChatColor.GRAY + "(Right Click)");
					disc.setItemMeta(id);

					int amount1 = 0;
					if (!(disc == null)) {
						for (int ii = 0; ii < 36; ii++) {
							ItemStack slot = player.getInventory().getItem(ii);
							if (slot == null || !slot.isSimilar(disc))
								continue;
							amount += slot.getAmount();
						}
					}

					for (int ii = 1; ii <= amount; ii++) {
						player.getInventory().removeItem(disc);
					}

					if (s == 1)
						player.getInventory().addItem(s1);
					if (s == 2)
						player.getInventory().addItem(s2);
					if (s == 3)
						player.getInventory().addItem(s3);
					if (s == 4)
						player.getInventory().addItem(s4);
					if (s == 5)
						player.getInventory().addItem(s5);
					if (s == 6)
						player.getInventory().addItem(s6);
					if (s == 7)
						player.getInventory().addItem(s7);
					if (s == 8)
						player.getInventory().addItem(s8);
					if (s == 9)
						player.getInventory().addItem(s9);
					if (s == 10)
						player.getInventory().addItem(s10);

					if (bow == 1)
						player.getInventory().addItem(bow1);
					if (bow == 2)
						player.getInventory().addItem(bow2);
					if (bow == 3)
						player.getInventory().addItem(bow3);
					if (bow == 4)
						player.getInventory().addItem(bow4);
					if (bow == 5)
						player.getInventory().addItem(bow5);

					if (helm == 1) {
						if (player.getInventory().getHelmet() == null)
							player.getInventory().setHelmet(helm1);
						else
							player.getInventory().addItem(helm1);
					}
					if (helm == 2) {
						if (player.getInventory().getHelmet() == null)
							player.getInventory().setHelmet(helm2);
						else
							player.getInventory().addItem(helm2);
					}
					if (helm == 3) {
						if (player.getInventory().getHelmet() == null)
							player.getInventory().setHelmet(helm3);
						else
							player.getInventory().addItem(helm3);
					}
					if (helm == 4) {
						if (player.getInventory().getHelmet() == null)
							player.getInventory().setHelmet(helm4);
						else
							player.getInventory().addItem(helm4);
					}
					if (helm == 5) {
						if (player.getInventory().getHelmet() == null)
							player.getInventory().setHelmet(helm5);
						else
							player.getInventory().addItem(helm5);
					}
					if (helm == 6) {
						if (player.getInventory().getHelmet() == null)
							player.getInventory().setHelmet(helm6);
						else
							player.getInventory().addItem(helm6);
					}

					if (chest == 1) {
						if (player.getInventory().getChestplate() == null)
							player.getInventory().setChestplate(chest1);
						else
							player.getInventory().addItem(chest1);
					}
					if (chest == 2) {
						if (player.getInventory().getChestplate() == null)
							player.getInventory().setChestplate(chest2);
						else
							player.getInventory().addItem(chest2);
					}
					if (chest == 3) {
						if (player.getInventory().getChestplate() == null)
							player.getInventory().setChestplate(chest3);
						else
							player.getInventory().addItem(chest3);
					}
					if (chest == 4) {
						if (player.getInventory().getChestplate() == null)
							player.getInventory().setChestplate(chest4);
						else
							player.getInventory().addItem(chest4);
					}
					if (chest == 5) {
						if (player.getInventory().getChestplate() == null)
							player.getInventory().setChestplate(chest5);
						else
							player.getInventory().addItem(chest5);
					}
					if (chest == 6) {
						if (player.getInventory().getChestplate() == null)
							player.getInventory().setChestplate(chest6);
						else
							player.getInventory().addItem(chest6);
					}

					if (leg == 1) {
						if (player.getInventory().getLeggings() == null)
							player.getInventory().setLeggings(leg1);
						else
							player.getInventory().addItem(leg1);
					}
					if (leg == 2) {
						if (player.getInventory().getLeggings() == null)
							player.getInventory().setLeggings(leg2);
						else
							player.getInventory().addItem(leg2);
					}
					if (leg == 3) {
						if (player.getInventory().getLeggings() == null)
							player.getInventory().setLeggings(leg3);
						else
							player.getInventory().addItem(leg3);
					}
					if (leg == 4) {
						if (player.getInventory().getLeggings() == null)
							player.getInventory().setLeggings(leg4);
						else
							player.getInventory().addItem(leg4);
					}
					if (leg == 5) {
						if (player.getInventory().getLeggings() == null)
							player.getInventory().setLeggings(leg5);
						else
							player.getInventory().addItem(leg5);
					}
					if (leg == 6) {
						if (player.getInventory().getLeggings() == null)
							player.getInventory().setLeggings(leg6);
						else
							player.getInventory().addItem(leg6);
					}

					if (b == 1) {
						if (player.getInventory().getBoots() == null)
							player.getInventory().setBoots(b1);
						else
							player.getInventory().addItem(b1);
					}
					if (b == 2) {
						if (player.getInventory().getBoots() == null)
							player.getInventory().setBoots(b2);
						else
							player.getInventory().addItem(b2);
					}
					if (b == 3) {
						if (player.getInventory().getBoots() == null)
							player.getInventory().setBoots(b3);
						else
							player.getInventory().addItem(b3);
					}
					if (b == 4) {
						if (player.getInventory().getBoots() == null)
							player.getInventory().setBoots(b4);
						else
							player.getInventory().addItem(b4);
					}
					if (b == 5) {
						if (player.getInventory().getBoots() == null)
							player.getInventory().setBoots(b5);
						else
							player.getInventory().addItem(b5);
					}
					if (b == 6) {
						if (player.getInventory().getBoots() == null)
							player.getInventory().setBoots(b6);
						else
							player.getInventory().addItem(b6);
					}

					if (kit == true) {

						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " fishing_rod");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " golden_apple 12");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " arrow 48");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " iron_pickaxe");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " iron_axe");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " iron_shovel");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " cobblestone 64");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " planks 64");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " water_bucket 2");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " lava_bucket 2");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
								"give " + player.getName() + " cooked_beef 12");
						BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName()
										+ " compass 1 0 {display:{Name:\"§fPlayer Tracker\"}}");
							}
						}, 3);
					}

					player.updateInventory();
					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							player.updateInventory();
						}
					}, 5);
				}
			}

			pts = Configs.playerstats.getConfig().getInt("players." + uuid + ".Points");
			return true;

		}
		return false;
	}
}