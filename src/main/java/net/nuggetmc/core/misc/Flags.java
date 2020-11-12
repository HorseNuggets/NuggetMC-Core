package net.nuggetmc.core.misc;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.util.ColorCodes;

public class Flags implements CommandExecutor {
	
	private Main plugin;
	private Map<Player, Map<String, Short>> checks;
	
	public Flags(Main plugin) {
		this.plugin = plugin;
		this.checks = new HashMap<>();
		task();
	}
	
	private void task() {
		BukkitRunnable queue = new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Player player : checks.keySet()) {
					for (String type : checks.get(player).keySet()) {
						short n = checks.get(player).get(type);
						try {
							Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
								if (n > 0) {
									checks.get(player).put(type, (short) (checks.get(player).get(type) - 1));
								} else {
									checks.get(player).remove(type);
								}
							}, 1);
						} catch (ConcurrentModificationException e) {
							continue;
						}
					}
				}
			}
		};
		
		queue.runTaskTimerAsynchronously(plugin, 0, 100);
		return;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 2) {
			Player player = Bukkit.getPlayer(args[0]);
			if (player != null) {
				if (!checks.containsKey(player)) {
					checks.put(player, new HashMap<>());
				}
				
				if (!checks.get(player).containsKey(args[1])) {
					checks.get(player).put(args[1], (short) 1);
				} else {
					checks.get(player).put(args[1], (short) (checks.get(player).get(args[1]) + 1));
				}
				
				Bukkit.broadcast("§8[§c§l!§r§8] §f" + ColorCodes.colorName(player.getUniqueId(), player.getName()) + " §7failed §6" + args[1] + " §ex" + checks.get(player).get(args[1]), "nmc.staff");
				
				if (checks.get(player).get(args[1]) >= 10) {
					player.kickPlayer("Disconnected");
					checks.remove(player);
				}
			}
		}
		return true;
	}
}
