package net.nuggetmc.core.commands.admin;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.player.PlayerStats;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.ColorCodes;

public class AdminCommand implements CommandExecutor {
	
	private Main plugin;
	
	public AdminCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName()) {
		case "gma":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 2");
			}
			return true;
		case "gmc":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 1");
			}
			return true;
		case "gms":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 0");
			}
			return true;
		case "gmsp":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 3");
			}
			return true;
		case "setkills":
			setStats("kills", args);
			return true;
		case "setnuggets":
			setStats("nuggets", args);
			return true;
		case "tpall":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "tp @a " + sender.getName());
			}
			return true;
		case "wr":
			if (args.length >= 1) {
				WorldManager.worldReload(args[0], args);
			}
			else {
				sender.sendMessage(ChatColor.RED + "Too few arguments!");
				sender.sendMessage(ChatColor.RED + "Usage: /wr <world>");
			}
			return true;
		case "wrset":
			if (args.length >= 1) {
				WorldManager.count = Integer.parseInt(args[0]);
			}
			else {
				sender.sendMessage(ChatColor.RED + "Too few arguments!");
				sender.sendMessage(ChatColor.RED + "Usage: /wrset <time>");
			}
			return true;
		case "alert":
			if (args.length < 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /alert <msg>");
				return true;
			}

			String msg = "";

			for (int i = 0; i < args.length; i++)
				msg = msg + " " + args[i];

			msg = msg.replaceAll("&", "§");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Alert" + ChatColor.DARK_GRAY + "]"
					+ ChatColor.WHITE + msg);

			return true;
		case "justice":
			for (int i = 0; i < 20; i++) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"give @a iron_sword 1 250 {ench:[{id:19,lvl:10}],display:{Name:\"§eSword of JUSTICE\",Lore:[\"§7Lightning X\"]}}");
			}
			
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
					"playsound ambient.weather.thunder @a 10000 222 10000 1 1 1");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title @a times 0 100 20");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set 6000");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
					"playsound mob.enderdragon.end @a 10000 222 10000 1 1 1");

			Map<Boolean, Integer> justiceTime = new HashMap<>();
			Map<Boolean, BukkitRunnable> justiceTask = new HashMap<>();
			
			BukkitRunnable runnable = new BukkitRunnable() {
				public void run() {
					int a = justiceTime.get(true);
					a--;
					justiceTime.put(true, a);
					if (a <= 0) {
						this.cancel();
						return;
					}
					for (Player player : Bukkit.getOnlinePlayers()) {
						String message = "JUSTICE";
						String playername = player.getName();
						String start = "";
						UUID uid = player.getUniqueId();
						
						start = playername + ChatColor.WHITE + ": ";
						
						int playerlevel = Configs.playerstats.getConfig().getInt("players." + uid + ".level");
						
						start = ChatColor.DARK_GRAY + "[" + ColorCodes.levelToTag(playerlevel) + ChatColor.DARK_GRAY + "] " + ColorCodes.rankNameTag(uid) + start;
						message = start + message;
						
						Bukkit.broadcastMessage(message);
					}
				}
			};
			
			justiceTask.put(true, runnable);
			justiceTime.put(true, 20);
			
			runnable.runTaskTimerAsynchronously(plugin, 0, 1);
			
			Location loc = new Location(Bukkit.getWorld("main"), 0, 0, 0);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"playsound ambient.weather.thunder @a 10000 222 10000 1 1 1");
				Bukkit.getServer().getWorld("main").strikeLightning(loc);
				// Bukkit.broadcastMessage(msg);
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set 18000");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"title @a subtitle {\"text\":\"JUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICE\"}");
			}, 3);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"playsound ambient.weather.thunder @a 10000 222 10000 1 1 1");
				Bukkit.getServer().getWorld("main").strikeLightning(loc);
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set 6000");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"title @a title {\"text\":\"JUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICEJUSTICE\"}");
			}, 6);
			return true;
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void setStats(String type, String[] args) {
		Player player = Bukkit.getPlayer(args[0]);
		UUID uuid = null;
		
		if (player == null) {
			uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
		}
		
		else {
			uuid = player.getUniqueId();
		}
		
		if (uuid != null) {
			int kills = Integer.parseInt(args[1]);
			Configs.playerstats.getConfig().set("players." + uuid + "." + type, kills);
			Configs.playerstats.saveConfig();
			if (player != null) {
				Team killsDisplay = player.getScoreboard().getTeam(type);
				if (killsDisplay != null) {
					String val = NumberFormat.getNumberInstance(Locale.US).format(kills);
					killsDisplay.setSuffix(val);
				}
				
				if (type.equals("kills")) {
					PlayerStats.allign(player, uuid, kills);
					Team levelDisplay = player.getScoreboard().getTeam("level");
					if (levelDisplay != null) {
						int level = Configs.playerstats.getConfig().getInt("players." + uuid + ".level");
						levelDisplay.setSuffix(String.valueOf(level));
					}
				}
			}
		}
	}
}
