package net.nuggetmc.core.player;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import net.nuggetmc.core.Main;

public class PlayerJoin {
	
	private Main plugin;
	private List<String> joinmsg;
	private FileConfiguration config;
	
	public PlayerJoin(Main plugin) {
		this.plugin = plugin;
		this.config = plugin.configs.playerstats.getConfig();
		this.joinmsg = plugin.configs.mainconfig.getConfig().getStringList("joinmsg");
	}
	
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName();
		UUID uuid = player.getUniqueId();
		
		if (!config.contains("players." + uuid)) {
			config.set("players." + uuid + ".name", playername);
			config.set("players." + uuid + ".kills", 0);
			config.set("players." + uuid + ".level", 1);
			config.set("players." + uuid + ".nuggets", 100);
			config.set("players." + uuid + ".respawnstatus", 0);
			plugin.configs.playerstats.saveConfig();
		}
		
		for (int i = 0; i < joinmsg.size(); i++) {
			player.sendMessage(joinmsg.get(i));
		}
		
		return;
	}
}
