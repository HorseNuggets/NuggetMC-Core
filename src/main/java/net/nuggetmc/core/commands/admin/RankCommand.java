package net.nuggetmc.core.commands.admin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class RankCommand implements CommandExecutor {
	
	private Main plugin;

	public RankCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 2) {
			Bukkit.dispatchCommand(sender, "lp user " + args[0] + " group set " + args[1]);
			Player player = Bukkit.getPlayer(args[0]);
			UUID uuid = null;
			
			if (player == null) {
				OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(args[0]);
				if (offPlayer != null) {
					uuid = offPlayer.getUniqueId();
				}
			}
			
			else {
				uuid = player.getUniqueId();
			}
			
			if (uuid != null) {
				Configs.playerstats.getConfig().set("players." + uuid + ".rank", args[1]);
				Configs.playerstats.saveConfig();
			}
			
			plugin.sidebar.refreshRank(sender, args[0]);
		}
		else {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Usage: /rank <player> <rank>");
		}
		return true;
	}
}