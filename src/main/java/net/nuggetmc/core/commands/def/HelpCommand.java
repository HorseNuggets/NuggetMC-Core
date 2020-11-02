package net.nuggetmc.core.commands.def;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class HelpCommand implements CommandExecutor {
	private String linspace = ChatColor.GRAY + "--------------------------------------";
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(linspace);
		sender.sendMessage("General Commands:");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/rules" + ChatColor.GRAY + " » Views the rules.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/tpa <player>" + ChatColor.GRAY + " » Requests to teleport to a player.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/tphere <player>" + ChatColor.GRAY + " » Requests a player to tp to you.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/kit" + ChatColor.GRAY + " » Receives a kit.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/spawn" + ChatColor.GRAY + " » Warps to spawn.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/lead" + ChatColor.GRAY + " » Displays the server leaderboards.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/levels" + ChatColor.GRAY + " » Displays the server levels.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/warp <warp>" + ChatColor.GRAY + " » Warps to a warp.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/msg <player> <message>" + ChatColor.GRAY + " » Messages a player.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/r <message>" + ChatColor.GRAY + " » Replies to a message.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/ignore" + ChatColor.GRAY + " » Ignores a player.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/unignore" + ChatColor.GRAY + " » Unignores a player.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/ignorelist" + ChatColor.GRAY + " » Lists ignored players.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/sethome" + ChatColor.GRAY + " » Sets a home.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/home" + ChatColor.GRAY + " » Warps to your home.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/homes" + ChatColor.GRAY + " » Lists your homes.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/boat" + ChatColor.GRAY + " » Gives you a boat.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/blocks" + ChatColor.GRAY + " » Gives you sum blocc");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/wrt" + ChatColor.GRAY + " » Lists when the worlds will reset.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/stats" + ChatColor.GRAY + " » Lists a player's stats.");
		sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "/suicide" + ChatColor.GRAY + " » kys");
		sender.sendMessage(linspace);
		return true;
	}
}
