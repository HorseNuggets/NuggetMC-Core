package net.nuggetmc.core.setup;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.TimeConverter;

public class Announcements {
	
	private Main plugin;
	private List<String> messages;
	
	public byte cycle = 0;
	public double resetKey = Math.random();
	
	public Announcements(Main plugin) {
		this.plugin = plugin;
	}
	
	public void setup() {
		if (Configs.mainconfig.getConfig().getBoolean("enabled.announcements")) {
			messages = Configs.announcements.getConfig().getStringList("messages");
			for (int i = 0; i < messages.size(); i++) {
				messages.set(i, messages.get(i).replaceAll("<prefix>", Configs.announcements.getConfig().getString("prefix")));
			}
		}
		return;
	}
	
	public void run() {
		double key = resetKey;
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				if (key != resetKey) {
					this.cancel();
					return;
				}
				else {
					Bukkit.broadcastMessage(messages.get(cycle));
					if (cycle < messages.size() - 1)
						cycle++;
					else
						cycle = 0;
				}
			}
		};
		
		runnable.runTaskTimer(plugin, 20 * TimeConverter.stringToInt(Configs.announcements.getConfig().getString("time.delay")),
				20 * TimeConverter.stringToInt(Configs.announcements.getConfig().getString("time.period")));
		return;
	}
	
	public void reset() {
		resetKey = Math.random();
		cycle = 0;
		setup();
		run();
		return;
	}
}