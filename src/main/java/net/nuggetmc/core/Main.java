package net.nuggetmc.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.nuggetmc.core.data.ConfigManager;
import net.nuggetmc.core.dnf.NoFall;
import net.nuggetmc.core.dnf.listeners.FallListener;
import net.nuggetmc.core.dnf.listeners.MoveListener;
import net.nuggetmc.core.setup.Announcements;
import net.nuggetmc.core.util.debug;

public class Main extends JavaPlugin {
	
	private Logger logger;
	private ConfigManager configManager;
	
	public Announcements announcements;
	public FallListener fallListener;
	public NoFall noFall;
	public MoveListener moveListener;
	
	public void onEnable() {
		announcementsEnable();
		commandsEnable();
		listenersEnable();
		loadConfigManager();
		loggerEnable();
		noFallEnable();
		return;
	}
	
	public void announcementsEnable() {
		this.announcements = new Announcements(this);
		announcements.run();
		return;
	}
	
	public void noFallEnable() {
		this.noFall = new NoFall();
		this.fallListener = new FallListener(this);
		this.moveListener = new MoveListener(this);
	}
	
	public void listenersEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(this), this);
		return;
	}
	
	public void loggerEnable() {
		logger = getLogger();
		return;
	}
	
	public void log(String message) {
		logger.info(message);
		return;
	}
	
	public void loadConfigManager() {
		configManager = new ConfigManager(this);
		configManager.setup("nofall\\config.yml");
		configManager.setup("stats\\playerdata.yml");
		return;
	}
	
	public void commandsEnable() {
		getCommand("debug").setExecutor(new debug(this));
		return;
	}
}
