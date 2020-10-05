package net.nuggetmc.core.commands.admin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

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
			Configs.playerstats.getConfig().set("players." + uuid + "." + type, Integer.parseInt(args[1]));
			Configs.playerstats.saveConfig();
			if (player != null) {
				plugin.sidebar.enable(player);
			}
		}
	}
}