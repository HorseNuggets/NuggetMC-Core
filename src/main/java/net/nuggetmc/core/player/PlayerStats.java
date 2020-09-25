package net.nuggetmc.core.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class PlayerStats {
	
	private FileConfiguration config;
	private Main plugin;
	
	public PlayerStats(Main plugin) {
		this.config = Configs.playerstats.getConfig();
		this.plugin = plugin;
	}
	
	public void allign(Player player, UUID uuid, int kills) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			if (kills < 12) {
				config.set("players." + uuid + ".level", 1);
			}
			
			else if (kills >= 12 && kills < 56) {
		    	config.set("players." + uuid + ".level", 2);
		    }
		    
		    else if (kills >= 56 && kills < 114) {
		    	config.set("players." + uuid + ".level", 3);
		    }
		    
		    else if (kills >= 114 && kills < 440) {
		    	config.set("players." + uuid + ".level", 4);
		    }
		    
		    else if (kills >= 440 && kills < 880) {
		    	config.set("players." + uuid + ".level", 5);
		    }
		    
		    else if (kills >= 880 && kills < 1440) {
		    	config.set("players." + uuid + ".level", 6);
		    }
		    
		    else if (kills >= 1440 && kills < 1860) {
		    	config.set("players." + uuid + ".level", 7);
		    }
		    
		    else if (kills >= 1860 && kills < 2520) {
		    	config.set("players." + uuid + ".level", 8);
		    }
		    
		    else if (kills >= 2520 && kills < 3740) {
		    	config.set("players." + uuid + ".level", 9);
		    }
		    
		    else if (kills >= 3740 && kills < 5880) {
		    	config.set("players." + uuid + ".level", 10);
		    }
		    
		    else if (kills >= 5880 && kills < 7320) {
		    	config.set("players." + uuid + ".level", 11);
		    }
		    
		    else {
		    	config.set("players." + uuid + ".level", 12);
		    }
			
			Configs.playerstats.saveConfig();
		});
		return;
	}
}
