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
	
	private void joinFFA(Player player) {
		FFADeathmatch.list.add(player);
		if (!FFADeathmatch.list.contains(player)) {
			joinFFA(player);
		}
		return;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length >= 1) {
				Player player = (Player) sender;
				switch (args[0].toLowerCase()) {
				case "ffa":
					if (FFADeathmatch.phase == 1) {
						if (!FFADeathmatch.list.contains(player)) {
							joinFFA(player);
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
					break;
				}
			}
		}
		return true;
	}
}
