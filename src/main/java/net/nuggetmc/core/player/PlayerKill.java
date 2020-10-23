package net.nuggetmc.core.player;

import java.text.NumberFormat;
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
import net.nuggetmc.core.util.ColorCodes;

public class PlayerKill {
	
	private Main plugin;
	private List<String> lvlupmsg;
	private FileConfiguration config;
	private FileConfiguration defaults;
	private Map<String, Long> antiboost;
	
	public PlayerKill(Main plugin) {
		this.plugin = plugin;
		this.config = Configs.playerstats.getConfig();
		this.defaults = Configs.defaults.getConfig();
		this.lvlupmsg = Configs.mainconfig.getConfig().getStringList("levelup-msg");
		this.antiboost = new HashMap<>();
	}
	
	@SuppressWarnings("deprecation")
	public void onKill(PlayerDeathEvent event) {
		Player victim = event.getEntity();
		
		if (victim.getKiller() instanceof Player) {
			victim.getWorld().strikeLightningEffect(victim.getLocation());
			
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				Player player = victim.getKiller();
				
				if (player == victim) return;
				
				String playername = player.getName();
				String victimname = victim.getName();
				
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
				UUID playerUUID = player.getUniqueId();
				List<Integer> nuggetBounds = defaults.getIntegerList("on-kill.nuggets");
				
				int low = nuggetBounds.get(0);
				int high = nuggetBounds.get(1) + 1;
				
				int gainedNuggets = (int) (Math.random() * (high - low) + low);
				
				Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
					player.sendMessage(ChatColor.GRAY + " ▪" + ChatColor.WHITE + " Nuggets: " + ChatColor.GOLD + "+" + gainedNuggets);
				}, 1);
				
				Bukkit.getScheduler().runTask(plugin, () -> {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + playername + " gold_nugget " + gainedNuggets);
					player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
				});
				
				int uncNugget = (int) (Math.random() * 25);
				
				if (uncNugget == 0) {
					Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
						player.sendMessage(ChatColor.GRAY + " ▪" + ChatColor.WHITE + " Uncommon Nuggets: " + ChatColor.GREEN + "+1");
					}, 1);
					Bukkit.getScheduler().runTask(plugin, () -> {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + playername + " gold_nugget 1 0 "
								+ "{display:{Name:\"§aUncommon Nugget§r\",Lore:[\"§7A pretty fine piece of§r\",\"§7nugget if you ask me.§r\"]},ench:[{id:51,lvl:1}],HideFlags:1}");
					});
				}
				
				int kills = config.getInt("players." + playerUUID + ".kills");
				config.set("players." + playerUUID + ".kills", kills + 1);
				
				int levelup = plugin.levelup.check(kills + 1);
				
				Team display = player.getScoreboard().getTeam("kills");
				String val = NumberFormat.getNumberInstance(Locale.US).format(kills + 1);
				display.setSuffix(val);
				if (levelup != 0) {
					Team lvldisplay = player.getScoreboard().getTeam("level");
					String lvlval = NumberFormat.getNumberInstance(Locale.US).format(levelup);
					lvldisplay.setSuffix(lvlval);
					
					config.set("players." + playerUUID + ".level", levelup);
					
					for (int i = 0; i < lvlupmsg.size(); i++) {
						String line = lvlupmsg.get(i).replaceAll("<level>", String.valueOf(levelup));
						player.sendMessage(line.replaceAll("&", "§"));
					}
					
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
					Bukkit.broadcastMessage(ChatColor.WHITE + player.getName() + " has ranked up to " + ChatColor.WHITE + ColorCodes.levelToName(levelup) + ChatColor.WHITE + " ("
							+ ChatColor.WHITE + ColorCodes.levelToTag(levelup) + ChatColor.WHITE + ")!");
				}
				
				Configs.playerstats.saveConfig();
			});
			return;
		}
		return;
	}
}
