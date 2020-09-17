package net.nuggetmc.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.nuggetmc.core.setup.Announcements;
import net.nuggetmc.core.util.ActionBar;

public class Main extends JavaPlugin {
	
	public Announcements announcements;
	
	public void onEnable() {
		PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		pluginManager.registerEvents(new ActionBar(null), this);
		this.announcements = new Announcements(this);
		announcements.run();
		return;
	}
}
