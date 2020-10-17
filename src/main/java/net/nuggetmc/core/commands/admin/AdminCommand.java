package net.nuggetmc.core.commands.admin;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
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
			if (args.length >= 2) {
				switch(args[0]) {
				case "main":
					WorldManager.count = Integer.parseInt(args[1]);
					break;
				case "nether":
					WorldManager.countNether = Integer.parseInt(args[1]);
					break;
				case "end:":
					WorldManager.countEnd = Integer.parseInt(args[1]);
					break;
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "Too few arguments!");
				sender.sendMessage(ChatColor.RED + "Usage: /wrset <world> <time>");
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
		case "votealert":
			Player player = Bukkit.getServer().getPlayer(args[0]);
			UUID uuid = player.getUniqueId();
			
			if (args[1].equals("100nuggets")) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"alert " + ColorCodes.colorName(uuid, player.getName()) + " &fjust voted and won &e100 Nuggets&f!");
			}
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
			plugin.playerStats.allign(player, uuid, kills);
			Configs.playerstats.saveConfig();
			if (player != null) {
				Team killsDisplay = player.getScoreboard().getTeam(type);
				String val = NumberFormat.getNumberInstance(Locale.US).format(kills);
				killsDisplay.setSuffix(val);
				
				if (type.equals("kills")) {
					Team levelDisplay = player.getScoreboard().getTeam("level");
					int level = Configs.playerstats.getConfig().getInt("players." + uuid + ".level");
					levelDisplay.setSuffix(String.valueOf(level));
				}
			}
		}
	}
}
