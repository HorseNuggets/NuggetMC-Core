package net.nuggetmc.core.player;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.ItemSerializers;

public class PlayerJoin {
	
	private Main plugin;
	private List<String> joinmsg;
	private FileConfiguration config;
	private FileConfiguration defaults;
	
	public PlayerJoin(Main plugin) {
		this.plugin = plugin;
		this.configSetup();
		this.defaultInvSetup();
	}
	
	private void configSetup() {
		this.config = Configs.playerstats.getConfig();
		this.defaults = Configs.defaults.getConfig();
		this.joinmsg = new ArrayList<>();

		List<String> joinmsgRaw = Configs.mainconfig.getConfig().getStringList("join-msg");
		
		for (int i = 0; i < joinmsgRaw.size(); i++) {
			joinmsg.add(joinmsgRaw.get(i).replaceAll("&", "§"));
		}
		
		return;
	}

	private ItemStack[] items;
	private ItemStack[] armor;
	
	private void defaultInvSetup() {
		this.items = ItemSerializers.stringToItems(defaults.getString("on-first-join.inventory.items"));
		this.armor = ItemSerializers.stringToItems(defaults.getString("on-first-join.inventory.armor"));
		return;
	}
	
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName();
		UUID uuid = player.getUniqueId();
		
		if (player.getGameMode() != GameMode.SURVIVAL) {
			player.setGameMode(GameMode.SURVIVAL);
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			try {
				if (!config.contains("players." + uuid)) {
					config.set("players." + uuid + ".name", playername);
					config.set("players." + uuid + ".rank", "default");
					config.set("players." + uuid + ".level", 1);
					config.set("players." + uuid + ".kills", 0);
					config.set("players." + uuid + ".nuggets", 100);
					config.set("players." + uuid + ".promo", true);
					
					int count = config.getInt("count") + 1;
					config.set("count", count);
					
					Bukkit.broadcastMessage("Welcome " + playername + " to NuggetMC! " + ChatColor.GRAY + "(" + ChatColor.YELLOW + "#"
							+ NumberFormat.getNumberInstance(Locale.US).format(count) + ChatColor.GRAY + ")");
					
					player.getInventory().setContents(items);
					player.getInventory().setArmorContents(armor);
					
					randomDiamondArmor(player, ((byte) (Math.random() * 4)));
				}
				
				for (int i = 0; i < joinmsg.size(); i++) {
					player.sendMessage(joinmsg.get(i));
				}
				
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (player != all) {
						all.sendMessage(ColorCodes.colorName(uuid, playername) + ChatColor.WHITE + " joined the game.");
					}
				}
				
				config.set("players." + uuid + ".rank", ColorCodes.getRankName(uuid));
				
				Configs.playerstats.saveConfig();
			} catch (Exception e) {
				Bukkit.broadcast("§cERROR in PlayerJoin.java", "nmc");
				return;
			}
		});
		
		return;
	}
	
	public void randomDiamondArmor(Player player, byte roll) {
		switch (roll) {
			case 0:
				final ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
				helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				Bukkit.getScheduler().runTask(plugin, () -> {
					player.getInventory().setHelmet(helmet);
				});
				return;
			case 1:
				final ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
				chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				Bukkit.getScheduler().runTask(plugin, () -> {
					player.getInventory().setChestplate(chestplate);
				});
				return;
			case 2:
				final ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
				leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				Bukkit.getScheduler().runTask(plugin, () -> {
					player.getInventory().setLeggings(leggings);
				});
				return;
			case 3:
				final ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
				boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				Bukkit.getScheduler().runTask(plugin, () -> {
					player.getInventory().setBoots(boots);
				});
				return;
		}
		return;
	}
}
