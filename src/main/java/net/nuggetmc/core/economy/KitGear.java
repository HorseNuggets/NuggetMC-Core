package net.nuggetmc.core.economy;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitGear {
	
	public static ItemStack armor(String type, int input) {
		Material iron = Material.IRON_HELMET;
		Material diamond = Material.DIAMOND_HELMET;
		
		switch (type) {
		case "chestplate":
			iron = Material.IRON_CHESTPLATE;
			diamond = Material.DIAMOND_CHESTPLATE;
			break;
		case "leggings":
			iron = Material.IRON_LEGGINGS;
			diamond = Material.DIAMOND_LEGGINGS;
			break;
		case "boots":
			iron = Material.IRON_BOOTS;
			diamond = Material.DIAMOND_BOOTS;
			break;
		}
		
		switch (input) {
		case 2:
			ItemStack armorPiece2 = new ItemStack(iron, 1);
			armorPiece2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			return armorPiece2;
		case 3:
			ItemStack armorPiece3 = new ItemStack(diamond, 1);
			armorPiece3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			return armorPiece3;
		case 4:
			ItemStack armorPiece4 = new ItemStack(diamond, 1);
			armorPiece4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			return armorPiece4;
		case 5:
			ItemStack armorPiece5 = new ItemStack(diamond, 1);
			armorPiece5.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			return armorPiece5;
		case 6:
			ItemStack armorPiece6 = new ItemStack(diamond, 1);
			armorPiece6.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			return armorPiece6;
		}
		
		ItemStack armorPiece1 = new ItemStack(iron, 1);
		armorPiece1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		return armorPiece1;
	}
	
	public static ItemStack sword(int input, String name) {
		
		Material material = Material.IRON_SWORD;
		if (input % 2 == 0) {
			material = Material.DIAMOND_SWORD;
		}
		
		ItemStack sword = new ItemStack(material, 1);
		int factor = (int) ((input / 2) + 0.5);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, factor);
		
		ItemMeta swordMeta = sword.getItemMeta();
		swordMeta.setDisplayName(name);
		sword.setItemMeta(swordMeta);
		
		return sword;
	}
	
	public static ItemStack bow(int input) {
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, input);
		return bow;
	}
}
