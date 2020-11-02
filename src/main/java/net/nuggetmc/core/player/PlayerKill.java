package net.nuggetmc.core.player;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.events.FFADeathmatch;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.ColorCodes;

public class PlayerKill {
	
	private Main plugin;
	private List<String> lvlupmsg;
	private FileConfiguration config;
	private Map<String, Long> antiboost;
	
	public PlayerKill(Main plugin) {
		this.plugin = plugin;
		this.config = Configs.playerstats.getConfig();
		this.lvlupmsg = new ArrayList<>();
		this.antiboost = new HashMap<>();
		
		List<String> lvlupmsgRaw = Configs.mainconfig.getConfig().getStringList("levelup-msg");
		
		for (int i = 0; i < lvlupmsgRaw.size(); i++) {
			lvlupmsg.add(lvlupmsgRaw.get(i).replaceAll("&", "§"));
		}
	}
	
	public void onKill(PlayerDeathEvent event) {
		Player victim = event.getEntity();
		
		if (victim.getKiller() instanceof Player) {
			victim.getWorld().strikeLightningEffect(victim.getLocation());
			Player player = victim.getKiller();
			
			if (player == victim) return;
			
			if (WorldManager.isInArena(victim.getLocation())) {
				String message = event.getDeathMessage();
				event.setDeathMessage(null);
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (!FFADeathmatch.list.contains(all)) {
						all.sendMessage(message);
					}
				}
			}
			
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				
				String playername = player.getName();
				String victimname = victim.getName();

				UUID playerUUID = player.getUniqueId();
				
				if (!FFADeathmatch.list.contains(player)) {
					String players = playername + "%" + victimname;
					if (antiboost.containsKey(players)) {
						Long difference = antiboost.get(players) - System.currentTimeMillis() / 1000;
						if (difference > 0) {
							player.sendMessage("You have already killed this player recently! You gain no kill credit.");
							return;
						}
						
						else {
							antiboost.remove(players);
						}
					}
					
					antiboost.put(players, 60 + System.currentTimeMillis() / 1000);
					
					int gainedNuggets = (int) (Math.random() * 7 + 8);
					
					Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
						player.sendMessage(ChatColor.GRAY + " ▪" + ChatColor.WHITE + " Nuggets: " + ChatColor.GOLD + "+" + gainedNuggets);
					}, 1);
					
					Bukkit.getScheduler().runTask(plugin, () -> {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + playername + " gold_nugget " + gainedNuggets);
						player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
					});
					
					int uncNugget = (int) (Math.random() * 25);
					
					if (uncNugget == 0) {
						Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
							player.sendMessage(ChatColor.GRAY + " ▪" + ChatColor.WHITE + " Uncommon Nuggets: " + ChatColor.GREEN + "+1");
						}, 1);
						Bukkit.getScheduler().runTask(plugin, () -> {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + playername + " gold_nugget 1 0 "
									+ "{display:{Name:\"§aUncommon Nugget§r\",Lore:[\"§7A pretty fine piece of§r\",\"§7nugget if you ask me.§r\"]},ench:[{id:51,lvl:1}],HideFlags:1}");
						});
					}
				}
				
				try {
					int kills = config.getInt("players." + playerUUID + ".kills");
					config.set("players." + playerUUID + ".kills", kills + 1);
					
					int levelup = plugin.levelup.check(kills + 1);
					
					Team display = player.getScoreboard().getTeam("kills");
					if (display != null) {
						String val = NumberFormat.getNumberInstance(Locale.US).format(kills + 1);
						display.setSuffix(val);
					}
					if (levelup != 0) {
						Team lvldisplay = player.getScoreboard().getTeam("level");
						if (lvldisplay != null) {
							String lvlval = NumberFormat.getNumberInstance(Locale.US).format(levelup);
							lvldisplay.setSuffix(lvlval);
						}
						
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
					
				} catch (Exception e) {
					Bukkit.broadcast("§cERROR in PlayerKill.java", "nmc");
					return;
				}
			});
			return;
		}
		return;
	}
}
