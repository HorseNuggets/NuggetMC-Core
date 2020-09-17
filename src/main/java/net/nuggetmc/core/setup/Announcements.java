package net.nuggetmc.core.setup;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;

public class Announcements {
	
	private Main plugin;
	private String tag = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Alert" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;
	private List<String> messages = Arrays.asList(
			tag + "Don't forget to vote! " + ChatColor.YELLOW + "/vote",
			tag + "You can do " + ChatColor.YELLOW + "/help" + ChatColor.WHITE + " to view the general commands.",
			tag + "Do " + ChatColor.YELLOW + "/rules" + ChatColor.WHITE + " to view the rules.",
			tag + "Do " + ChatColor.YELLOW + "/kit" + ChatColor.WHITE + " to recieve a kit.");
	
	private byte cycle = 0;
	
	public Announcements(Main plugin) {
		this.plugin = plugin;
	}
	
	public void run() {
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage(messages.get(cycle));
				if (cycle < messages.size() - 1)
					cycle++;
				else
					cycle = 0;
			}
		};
		
		runnable.runTaskTimer(plugin, 20, 4000);
		return;
	}
}
