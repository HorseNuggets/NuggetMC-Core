package net.nuggetmc.core.events;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.setup.WorldManager;

public class Event implements CommandExecutor {
	
	private Main plugin;
	
	public Event(Main plugin) {
		this.plugin = plugin;
		timer();
	}
	
	private void timer() {
		/*Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "event ffa");
		}, 100, 19000);*/
		return;
	}
	
	public static boolean arena = false;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 1) {
			if (WorldManager.count >= 600) {
				String event = args[0].toLowerCase();
				switch (event) {
				case "ffa":
					if (!arena) {
						arena = true;
						plugin.dm.a();
						return true;
					}
					else {
						sender.sendMessage("§cAn arena event has already begun!");
						return true;
					}
					
				case "tournament":
					if (!arena) {
						arena = true;
						plugin.trn.a();
						return true;
					}
					else {
						sender.sendMessage("§cAn arena event has already begun!");
						return true;
					}
				}
			}
			else {
				sender.sendMessage("§cYou cannot schedule an event less than 10 minutes before the world reset!");
				return true;
			}
		}
		return true;
	}
}
