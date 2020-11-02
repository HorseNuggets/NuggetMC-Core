package net.nuggetmc.core.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;

public class Leaderboards {
	
	private Main plugin;
	private FileConfiguration config;
	private FileConfiguration lead;
	
	
	public Leaderboards(Main plugin) {
		this.plugin = plugin;
		this.config = Configs.playerstats.getConfig();
		this.lead = Configs.lead.getConfig();
		runAsync();
	}
	
	private void runAsync() {
		BukkitRunnable queue = new BukkitRunnable() {
			
			@Override
			public void run() {
				Bukkit.getConsoleSender().sendMessage("Updating leaderboard...");
				
				Map<String, Integer> nuggets = new HashMap<>();
				for(String key : config.getConfigurationSection("players").getKeys(false)) {
					nuggets.put(config.getString("players." + key + ".name"), config.getInt("players." + key + ".nuggets"));
				}
				
				List<Entry<String, Integer>> listn = new LinkedList<Entry<String, Integer>>(nuggets.entrySet());
				Collections.sort(listn, new Comparator<Entry<String, Integer>>() {
					
					@Override
					public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				});
				
				int sizen = listn.size();
				
				for (int i = 0; i < sizen; i++) {
					String[] entries = listn.get(i).toString().split("=");
					lead.set("nuggets." + (sizen - i) + ".name", entries[0]);
					lead.set("nuggets." + (sizen - i) + ".value", entries[1]);
				}
				
				Map<String, Integer> kills = new HashMap<>();
				for(String key : config.getConfigurationSection("players").getKeys(false)) {
					kills.put(config.getString("players." + key + ".name"), config.getInt("players." + key + ".kills"));
				}
				
				List<Entry<String, Integer>> listk = new LinkedList<Entry<String, Integer>>(kills.entrySet());
				Collections.sort(listk, new Comparator<Entry<String, Integer>>() {
					
					@Override
					public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				});
				
				int sizek = listk.size();
				
				for (int i = 0; i < sizek; i++) {
					String[] entries = listk.get(i).toString().split("=");
					lead.set("kills." + (sizek - i) + ".name", entries[0]);
					lead.set("kills." + (sizek - i) + ".value", entries[1]);
				}
				
				Configs.lead.saveConfig();
				Bukkit.getConsoleSender().sendMessage("Done!");
			}
		};
		
		queue.runTaskTimerAsynchronously(plugin, 0, 400);
	}
}
