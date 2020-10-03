package net.nuggetmc.core.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIKits {
	public static void kit(Player player) {
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
}
