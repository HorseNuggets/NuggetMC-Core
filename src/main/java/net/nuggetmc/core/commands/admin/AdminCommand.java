package net.nuggetmc.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label) {
		case "gma":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 2");
			}
			break;
		case "gmc":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 1");
			}
			break;
		case "gms":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 0");
			}
			break;
		case "gmsp":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "gamemode 3");
			}
			break;
		case "tpall":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(sender, "tp @a " + sender.getName());
			}
			break;
		}
		return true;
	}
}
