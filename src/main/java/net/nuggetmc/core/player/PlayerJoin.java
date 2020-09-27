package net.nuggetmc.core.player;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.ItemSerializers;
import net.nuggetmc.core.util.SuperRandom;

public class PlayerJoin {
	
	private List<String> joinmsg;
	private FileConfiguration config;
	private FileConfiguration defaults;
	
	public PlayerJoin() {
		this.config = Configs.playerstats.getConfig();
		this.defaults = Configs.defaults.getConfig();
		this.joinmsg = Configs.mainconfig.getConfig().getStringList("joinmsg");
	}
	
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName();
		UUID uuid = player.getUniqueId();
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (player != all) {
				player.sendMessage(ColorCodes.colorName(uuid, playername) + ChatColor.WHITE + " joined the game.");
			}
		}
		
		if (!(player.getGameMode() != GameMode.SURVIVAL)) {
			player.setGameMode(GameMode.SURVIVAL);
		}
		
		if (!config.contains("players." + uuid)) {
			config.set("players." + uuid + ".name", playername);
			config.set("players." + uuid + ".level", 1);
			config.set("players." + uuid + ".kills", 0);
			config.set("players." + uuid + ".nuggets", 100);
			Configs.playerstats.saveConfig();
			
			ItemStack items[] = ItemSerializers.stringToItems(defaults.getString("on-first-join.inventory.items"));
			ItemStack armor[] = ItemSerializers.stringToItems(defaults.getString("on-first-join.inventory.armor"));
			
			player.getInventory().setContents(items);
			player.getInventory().setArmorContents(armor);
			
			randomDiamondArmor(player, ((byte) (SuperRandom.generate() * 4)));
		}
		
		for (int i = 0; i < joinmsg.size(); i++) {
			player.sendMessage(joinmsg.get(i));
		}
		
		return;
	}
	
	public void randomDiamondArmor(Player player, byte roll) {
		switch (roll) {
			case 0:
				ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
				helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				player.getInventory().setHelmet(helmet);
				break;
			case 1:
				ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
				chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				player.getInventory().setChestplate(chestplate);
				break;
			case 2:
				ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
				leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				player.getInventory().setLeggings(leggings);
				break;
			case 3:
				ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
				boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				player.getInventory().setBoots(boots);
				break;
		}
		return;
	}
}
