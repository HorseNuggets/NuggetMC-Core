package net.nuggetmc.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.nuggetmc.core.commands.nmc;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.dnf.NoFall;
import net.nuggetmc.core.dnf.listeners.FallListener;
import net.nuggetmc.core.dnf.listeners.MoveListener;
import net.nuggetmc.core.setup.Announcements;
import net.nuggetmc.core.util.debug;

public class Main extends JavaPlugin {
	
	private Logger logger;
	
	public Announcements announcements;
	public Configs configs;
	public FallListener fallListener;
	public NoFall noFall;
	public MoveListener moveListener;
	
	public void onEnable() {
		loadConfigs();
		announcementsEnable();
		commandsEnable();
		listenersEnable();
		loggerEnable();
		noFallEnable();
		return;
	}
	
	public void announcementsEnable() {
		this.announcements = new Announcements(this);
		announcements.setup();
		announcements.run();
		return;
	}
	
	public void noFallEnable() {
		this.noFall = new NoFall(this);
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
	
	public void loadConfigs() {
		this.configs = new Configs(this);
		return;
	}
	
	public void commandsEnable() {
		getCommand("debug").setExecutor(new debug(this));
		getCommand("nuggetmc").setExecutor(new nmc(this));
		return;
	}
}
