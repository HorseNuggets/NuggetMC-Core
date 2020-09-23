package net.nuggetmc.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.nuggetmc.core.commands.nmc;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.modifiers.HealthBoost;
import net.nuggetmc.core.modifiers.nofall.NoFall;
import net.nuggetmc.core.modifiers.nofall.listeners.FallListener;
import net.nuggetmc.core.modifiers.nofall.listeners.MoveListener;
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
	
	public HealthBoost healthboost;
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
		this.modifiersEnable();
		this.toolsEnable();
		return;
	}
	
	private void announcementsEnable() {
		this.announcements = new Announcements(this);
		this.announcements.setup();
		this.announcements.run();
		return;
	}
	
	private void listenersEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(this), this);
		return;
	}
	
	private void loggerEnable() {
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
	
	private void modifiersEnable() {
		this.fallListener = new FallListener(this);
		this.healthboost = new HealthBoost(this);
		this.moveListener = new MoveListener(this);
		this.noFall = new NoFall(this);
	}
	
	public void reloadConfigs() {
		this.configs = new Configs(this);
		this.toolsEnable();
		return;
	}
	
	private void commandsEnable() {
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
