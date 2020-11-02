package net.nuggetmc.core.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class PlayerStats {
	
	private static Main plugin;
	
	public PlayerStats(Main plugin) {
		PlayerStats.plugin = plugin;
	}
	
	public static void allign(Player player, UUID uuid, int kills) {
		if (kills < 12) {
			Configs.playerstats.getConfig().set("players." + uuid + ".level", 1);
		}
		
		else if (kills >= 12 && kills < 56) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 2);
	    }
	    
	    else if (kills >= 56 && kills < 114) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 3);
	    }
	    
	    else if (kills >= 114 && kills < 440) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 4);
	    }
	    
	    else if (kills >= 440 && kills < 880) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 5);
	    }
	    
	    else if (kills >= 880 && kills < 1440) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 6);
	    }
	    
	    else if (kills >= 1440 && kills < 1860) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 7);
	    }
	    
	    else if (kills >= 1860 && kills < 2520) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 8);
	    }
	    
	    else if (kills >= 2520 && kills < 3740) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 9);
	    }
	    
	    else if (kills >= 3740 && kills < 5880) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 10);
	    }
	    
	    else if (kills >= 5880 && kills < 7320) {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 11);
	    }
	    
	    else {
	    	Configs.playerstats.getConfig().set("players." + uuid + ".level", 12);
	    }
		
		Configs.playerstats.saveConfig();
		return;
	}
	
	public static void asyncAllign(Player player, UUID uuid, int kills) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			allign(player, uuid, kills);
		});
	}
}
