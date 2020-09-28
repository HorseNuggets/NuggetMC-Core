package net.nuggetmc.core.player;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.ColorCodes;

public class PlayerKill {
	
	private Main plugin;
	private List<String> lvlupmsg;
	private FileConfiguration config;
	private FileConfiguration defaults;
	
	public PlayerKill(Main plugin) {
		this.plugin = plugin;
		this.config = Configs.playerstats.getConfig();
		this.defaults = Configs.defaults.getConfig();
		this.lvlupmsg = Configs.mainconfig.getConfig().getStringList("levelup-msg");
	}
	
	public void onKill(PlayerDeathEvent event) {
		Player victim = event.getEntity();
		
		if (victim.getKiller() instanceof Player) {
			Player player = victim.getKiller();
			String playername = player.getName();
			UUID playerUUID = player.getUniqueId();
			
			List<Integer> nuggetBounds = defaults.getIntegerList("on-kill.nuggets");
			
			int low = nuggetBounds.get(0);
			int high = nuggetBounds.get(1) + 1;
			
			int gainedNuggets = (int) (Math.random() * (high - low) + low);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				player.sendMessage(ChatColor.GRAY + " ▪" + ChatColor.WHITE + " Nuggets: " + ChatColor.GOLD + "+" + gainedNuggets);
			}, 1);
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + playername + " gold_nugget " + gainedNuggets);
			player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
			
			int kills = config.getInt("players." + playerUUID + ".kills");
			config.set("players." + playerUUID + ".kills", kills + 1);
			
			int levelup = plugin.levelup.check(player, playername, playerUUID, kills + 1);
			if (levelup != 0) {
				config.set("players." + playerUUID + ".level", levelup);
				
				for (int i = 0; i < lvlupmsg.size(); i++) {
					String line = lvlupmsg.get(i).replaceAll("<level>", String.valueOf(levelup));
					player.sendMessage(line);
				}
				
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				Bukkit.broadcastMessage(ChatColor.WHITE + player.getName() + " has ranked up to " + ChatColor.WHITE + ColorCodes.levelToName(levelup) + ChatColor.WHITE + " ("
						+ ChatColor.WHITE + ColorCodes.levelToTag(levelup) + ChatColor.WHITE + ")!");
			}
			
			Configs.playerstats.saveConfig();
			plugin.sidebar.enable(player);
			return;
		}
		return;
	}
}
