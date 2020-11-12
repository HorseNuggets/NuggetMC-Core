package net.nuggetmc.core.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import net.nuggetmc.core.scoreboard.Sidebar;
import net.nuggetmc.core.util.ColorCodes;

public class Join implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length >= 1) {
				Player player = (Player) sender;
				switch (args[0].toLowerCase()) {
				case "ffa":
					if (FFADeathmatch.phase == 1) {
						if (!FFADeathmatch.list.contains(player)) {
							FFADeathmatch.list.add(player);
							Location loc = new Location(Bukkit.getWorld("main"), 21.5, 227, -69.5, 180, 0);
							player.teleport(loc);
							Sidebar.enable(player, (byte) 1);

							int playerCount = FFADeathmatch.list.size();
							for (Player others : FFADeathmatch.list) {
								Team playersWaiting = others.getScoreboard().getTeam("p");
								if (playersWaiting != null) {
									playersWaiting.setSuffix(String.valueOf(playerCount));
								}
								others.sendMessage(ColorCodes.colorName(player.getUniqueId(), player.getName()) + ChatColor.YELLOW + " joined the event.");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + "You have already joined this event!");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "This event has already started!");
					}
					return true;
					
				case "tournament":
					if (Tournament.enabled) {
						if (!Tournament.cont.contains(player)) {
							Tournament.cont.add(player);
							Location loc = new Location(Bukkit.getWorld("main"), 21.5, 227, -69.5, 180, 0);
							player.teleport(loc);
							Sidebar.enable(player, (byte) 2);

							int playerCount = Tournament.cont.size();
							for (Player others : Tournament.cont) {
								Team playersWaiting = others.getScoreboard().getTeam("p");
								if (playersWaiting != null) {
									playersWaiting.setSuffix(String.valueOf(playerCount));
								}
								others.sendMessage(ColorCodes.colorName(player.getUniqueId(), player.getName()) + ChatColor.YELLOW + " joined the event.");
							}
							
							player.sendMessage("§6If you leave the game at any time, you will be disqualified!");
							player.sendMessage("§6You can view the current tournament statistics with §e/tournament§6.");
						}
						else {
							player.sendMessage(ChatColor.RED + "You have already joined this event!");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "This event has already started!");
					}
					return true;
				}
			}
		}
		return true;
	}
}
