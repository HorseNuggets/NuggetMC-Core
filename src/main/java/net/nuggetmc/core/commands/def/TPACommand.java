package net.nuggetmc.core.commands.def;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nuggetmc.core.Main;

public class TPACommand implements CommandExecutor {
	
	private Main plugin;
	private Map<String, BukkitRunnable> requestTask;
	private Map<Player, Player> recent;
	private String linspace = ChatColor.GRAY + "--------------------------------------";
	
	public TPACommand(Main plugin) {
		this.plugin = plugin;
		this.requestTask = new HashMap<>();
		this.recent = new HashMap<>();
	}
	
	private void assign(Player player, Player requestee, String playername, String requesteename, String type) {
		String players = playername + "%" + requesteename + "%" + type;
		
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				
				if (requestTask.containsKey(players)) {
					requestTask.remove(players);
				}
				
				if (recent.containsKey(requestee)) {
					recent.remove(requestee);
				}
				
				if (player != null) {
					player.sendMessage(linspace);
					player.sendMessage(ChatColor.YELLOW + "Your teleport request to " + ChatColor.GOLD + requesteename + ChatColor.YELLOW + " has expired.");
					player.sendMessage(linspace);
				}
				
				if (requestee != null) {
					requestee.sendMessage(linspace);
					requestee.sendMessage(ChatColor.YELLOW + "The teleport request from " + ChatColor.GOLD + playername + ChatColor.YELLOW + " has expired.");
					requestee.sendMessage(linspace);
				}
			}
		};
		
		requestTask.put(players, runnable);
		requestTask.get(players).runTaskLaterAsynchronously(plugin, 600);
		recent.put(requestee, player);
		
		player.sendMessage(linspace);
		player.sendMessage(ChatColor.YELLOW + "Teleport request sent to " + ChatColor.GOLD + requestee.getName()
		+ ChatColor.YELLOW + "! They have 30 seconds to accept.");
		player.sendMessage(linspace);
		
		String portion = " wants to teleport to you! ";
		if (type.equals("inverse")) {
			portion = " wants you to teleport to them! ";
		}
		
		TextComponent message = new TextComponent("");
		message.setColor(ChatColor.RED);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpyes " + playername));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept!").color(ChatColor.YELLOW).create()));
		message.addExtra(ChatColor.GOLD + "" + playername + ChatColor.YELLOW + portion);
		message.addExtra(ChatColor.RED + "" + ChatColor.BOLD + "[Click ");
		message.addExtra(ChatColor.RED + "" + ChatColor.BOLD + "to ");
		message.addExtra(ChatColor.RED + "" + ChatColor.BOLD + "accept]");
		
		requestee.sendMessage(linspace);
		requestee.spigot().sendMessage(message);
		requestee.sendMessage(linspace);
		return;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			switch (label.toLowerCase()) {
			case "tpa":
				preAssign(player, args, "normal");
				return true;
				
			case "tphere":
				preAssign(player, args, "inverse");
				return true;
				
			case "tpyes":
				Player to = null;
				
				if (args.length > 0) to = Bukkit.getPlayer(args[0]);
				else to = recent.get(player);
				
				if (to != null) {
					String players = to.getName() + "%" + player.getName();
					
					if (requestTask.containsKey(players + "%normal")) {
						String key = players + "%normal";
						requestTask.get(key).cancel();
						requestTask.remove(key);
						if (recent.containsKey(player)) recent.remove(player);
						to.teleport(player.getLocation());
						return true;
					}
					
					else if (requestTask.containsKey(players + "%inverse")) {
						String key = players + "%inverse";
						requestTask.get(key).cancel();
						requestTask.remove(key);
						if (recent.containsKey(player)) recent.remove(player);
						player.teleport(to.getLocation());
						return true;
					}
				}
				
				player.sendMessage(ChatColor.RED + "This request is either invalid or has expired!");
				return true;
			}
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void preAssign(Player player, String[] args, String type) {
		if (args.length == 0) {
			player.sendMessage(ChatColor.RED + "Too few arguments!");
			player.sendMessage(ChatColor.RED + "Usage: /tpa <player>");
			return;
		}
		
		else {
			Player requestee = Bukkit.getPlayer(args[0]);
			if (requestee == null) {
				OfflinePlayer offlineRequestee = Bukkit.getOfflinePlayer(args[0]);
				String name = args[0];
				if (offlineRequestee != null) {
					name = offlineRequestee.getName();
				}
				player.sendMessage(ChatColor.RED + name + " is not online!");
				return;
			}
			
			else if (player == requestee) {
				player.sendMessage("lol y u tryna teleport to urself noob");
			}
			
			else {
				String playername = player.getName();
				String requesteename = requestee.getName();
				if (requestTask.containsKey(playername + "%" + requesteename + "%normal")
						|| requestTask.containsKey(playername + "%" + requesteename + "%inverse")) {
					player.sendMessage(ChatColor.YELLOW + "You have already requested to teleport to " + ChatColor.GOLD + requesteename
							+ ChatColor.YELLOW + "! Wait for them to accept!");
					return;
				}
				assign(player, requestee, playername, requesteename, type);
			}
		}
		return;
	}
}
