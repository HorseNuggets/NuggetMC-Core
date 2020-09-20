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
import net.nuggetmc.core.tools.PlayerTracker;
import net.nuggetmc.core.util.debug;

public class Main extends JavaPlugin {
	
	/**
	 * NuggetMC-Core
	 * Version: 2.0
	 * The core plugin for the NuggetMC Network [nuggetmc.net].
	 * 
	 * @author HorseNuggets
	 * @since 2/22/2017
	 * 
	 * [DISCORD] @HorseNuggets#3141
	 * [PHONE] +1 (346) 233-7975
	 * [EMAIL] batchprogrammer314@gmail.com
	 */
	
	private Logger logger;
	
	public Announcements announcements;
	public Configs configs;
	public FallListener fallListener;
	public NoFall noFall;
	public MoveListener moveListener;
	public PlayerTracker playerTracker;
	
	public void onEnable() {
		this.loadConfigs();
		this.announcementsEnable();
		this.commandsEnable();
		this.listenersEnable();
		this.loggerEnable();
		this.noFallEnable();
		this.toolsEnable();
		return;
	}
	
	public void announcementsEnable() {
		this.announcements = new Announcements(this);
		this.announcements.setup();
		this.announcements.run();
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
		this.logger = getLogger();
		return;
	}
	
	public void log(String message) {
		this.logger.info(message);
		return;
	}
	
	public void loadConfigs() {
		this.configs = new Configs(this);
		return;
	}
	
	public void reloadConfigs() {
		this.configs = new Configs(this);
		this.toolsEnable();
		return;
	}
	
	public void commandsEnable() {
		this.getCommand("debug").setExecutor(new debug(this));
		this.getCommand("nuggetmc").setExecutor(new nmc(this));
		return;
	}
	
	public void toolsEnable() {
		if (configs.mainconfig.getConfig().getBoolean("enabled.playertracker")) {
			if (this.playerTracker == null) {
				this.playerTracker = new PlayerTracker(this);
			}
		}
		else {
			this.playerTracker = null;
		}
		return;
	}
}
