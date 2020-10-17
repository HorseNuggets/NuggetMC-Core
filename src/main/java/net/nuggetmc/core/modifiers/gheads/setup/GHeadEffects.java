package net.nuggetmc.core.modifiers.gheads.setup;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.nuggetmc.core.data.Configs;

public class GHeadEffects {
	
	private FileConfiguration config;
	private HashMap<String, Integer[]> headEffects = new HashMap<String, Integer[]>();
	private HashMap<String, Integer[]> gheadEffects = new HashMap<String, Integer[]>();
	private String gheadName;
	
	public GHeadEffects() {
		this.config = Configs.gheads.getConfig();
		this.gheadName = config.getString("gheads.ghead-name").replaceAll("&", "§");
		this.assignEffects();
	}
	
	private void assignEffects() {
		if (headEffects != null) headEffects.clear();
		if (gheadEffects != null) gheadEffects.clear();
		
		String hkey = "heads.head-effects";
		String gkey = "gheads.ghead-effects";
		
		for(String key : config.getConfigurationSection(hkey).getKeys(false)) {
			Integer[] meta = new Integer[2];
			meta[0] = config.getInt(hkey + "." + key + ".duration");
			meta[1] = config.getInt(hkey + "." + key + ".amplifier");
			headEffects.put(key, meta);
		}
		
		for(String key : config.getConfigurationSection(gkey).getKeys(false)) {
			Integer[] meta = new Integer[2];
			meta[0] = config.getInt(gkey + "." + key + ".duration");
			meta[1] = config.getInt(gkey + "." + key + ".amplifier");
			gheadEffects.put(key, meta);
		}
		return;
	}
	
	public void eatHeadSort(Player player) {
		if (player.getItemInHand().hasItemMeta()) {
			if (player.getItemInHand().getItemMeta().hasDisplayName()) {
				if (player.getItemInHand().getItemMeta().getDisplayName().equals(gheadName)) {
					eatHead(player, "ghead");
					return;
				}
			}
		}
		eatHead(player, "head");
		return;
	}
	
	public void eatHead(Player player, String type) {
		
		boolean success = false;
		switch(type) {
		case "head":
			if (config.getBoolean("heads.enabled")) {
				if (config.getBoolean("heads.edible")) {
					for (String key : headEffects.keySet()) {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "effect " + player.getName() + " " + key
								+ " " + headEffects.get(key)[0] + " " + headEffects.get(key)[1]);
					}
					success = true;
				}
			}
			break;
		case "ghead":
			if (config.getBoolean("gheads.enabled")) {
				if (config.getBoolean("gheads.edible")) {
					for (String key : gheadEffects.keySet()) {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "effect " + player.getName() + " " + key
								+ " " + gheadEffects.get(key)[0] + " " + gheadEffects.get(key)[1]);
					}
					success = true;
				}
			}
			break;
		}
		
		if (success) {
			double x = player.getLocation().getX();
			double y = player.getLocation().getY();
			double z = player.getLocation().getZ();
			
			if (player.getInventory().getItemInHand().getAmount() == 1) player.getInventory().setItemInHand(null);
			else player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
			
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "playsound random.eat @a " + x + " " + y + " " + z);
		}
		return;
	}
}
