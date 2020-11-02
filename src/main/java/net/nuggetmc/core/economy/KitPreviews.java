package net.nuggetmc.core.economy;

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

public class KitPreviews {
	
	/*
	 * [TODO]
	 * Eventually make private inventory variables with the inventories for all of the classes loaded,
	 * so that a new one does not have to be created every time its method is called.
	 * 
	 */
	
	public static void swordsmank(Player p) {
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

	public static void brutek(Player p) {
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

	public static void scoutk(Player p) {
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

	public static void apprenticek(Player p) {
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

	public static void horsemank(Player p) {
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
		sword.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Horseman Axe");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Jousting I");
		swordl.add("");
		swordl.add(ChatColor.DARK_GRAY + "This weapon's damage doubles if");
		swordl.add(ChatColor.DARK_GRAY + "you are on a horse.");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+5 " + ChatColor.DARK_BLUE + "(+10)" + ChatColor.BLUE + " Attack Damage");
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

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
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

	public static void essencek(Player p) {
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

	public static void jousterk(Player p) {
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
		sword.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Jouster Axe");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add(ChatColor.GRAY + "Jousting I");
		swordl.add("");
		swordl.add(ChatColor.DARK_GRAY + "This weapon's damage doubles if");
		swordl.add(ChatColor.DARK_GRAY + "you are on a horse.");
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+6 " + ChatColor.DARK_BLUE + "(+12)" + ChatColor.BLUE + " Attack Damage");
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

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
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

	public static void endermank(Player p) {
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
		lore1.add(ChatColor.GRAY + "Teleport to a player");
		lore1.add(ChatColor.GRAY + "within " + ChatColor.YELLOW + "10 " + ChatColor.GRAY + "meters.");
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

	public static void assassink(Player p) {
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
		pearln.setDisplayName(ChatColor.DARK_RED + "The Scythe");
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

	public static void pyrok(Player p) {
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
		ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 10);
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
		inv.setItem(23, lava);
		inv.setItem(28, fsword);
		inv.setItem(24, track);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}

	public static void creeperk(Player p) {
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
		boom.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		ItemMeta boomn = boom.getItemMeta();
		boomn.setDisplayName(ChatColor.RED + "Boom Box");

		ItemStack totem = new ItemStack(Material.NETHER_STALK, 1);
		totem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta totemn = totem.getItemMeta();
		totemn.setDisplayName(ChatColor.RED + "Blast Totem " + ChatColor.GRAY + "(16.67%)");
		ArrayList<String> toteml = new ArrayList<String>();
		toteml.add(ChatColor.GRAY + "Create an explosion");
		toteml.add(ChatColor.GRAY + "on death.");
		totemn.setLore(toteml);

		swordn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		helmn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		chestn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		legsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bootsn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		bown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		totemn.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		boomn.addItemFlags(ItemFlag.HIDE_ENCHANTS);

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

	public static void samuraik(Player p) {
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

	public static void templark(Player p) {
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

	public static void prestigek(Player p) {
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

	public static void valkyriek(Player p) {
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

	public static void championk(Player p) {
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

	public static void withermank(Player p) {
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
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
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
		pearln.setDisplayName(ChatColor.DARK_RED + "The Scythe");
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

	public static void sansk(Player p) {
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
		sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
		ItemMeta swordn = sword.getItemMeta();
		swordn.setDisplayName(ChatColor.AQUA + "Sans Bone");
		ArrayList<String> swordl = new ArrayList<String>();
		swordl.add("");
		swordl.add(ChatColor.BLUE + "+11.25 Attack Damage");
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

	public static void thanosk(Player p) {
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

	public static void spoonk(Player p) {
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
		track.setItemMeta(trackn);

		inv.setItem(0, helm);
		inv.setItem(1, chest);
		inv.setItem(2, legs);
		inv.setItem(3, boots);
		
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

		inv.setItem(27, spoon);

		p.updateInventory();
		p.openInventory(inv);
		return;
	}
}
