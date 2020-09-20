package net.nuggetmc.core.data;

import net.nuggetmc.core.Main;

public class Configs {
	
	private Main plugin;
	
	public ConfigManager mainconfig;
	public ConfigManager announcements;
	public ConfigManager defaults;
	public ConfigManager inventories;
	public ConfigManager nofall;
	public ConfigManager playerstats;
	public ConfigManager worldconfig;
	
	public Configs(Main plugin) {
		this.plugin = plugin;
		mainconfigSetup();
		announcementsSetup();
		defaultsSetup();
		inventoriesSetup();
		noFallSetup();
		playerstatsSetup();
		worldconfigSetup();
	}
	
	public ConfigManager get(String path) {
		switch (path) {
		case "config.yml":
			return mainconfig;
		case "announcements.yml":
			return announcements;
		case "nofall\\config.yml":
			return nofall;
		case "playerdata\\defaults\\config.yml":
			return defaults;
		case "playerdata\\inventories.yml":
			return inventories;
		case "playerdata\\stats.yml":
			return playerstats;
		case "worldconfig.yml":
			return worldconfig;
		}
		return null;
	}
	
	public void special(String path) {
		switch (path) {
		case "config.yml":
			plugin.reloadConfigs();
			break;
		case "announcements.yml":
			plugin.announcements.reset();
			break;
		}
		return;
	}
	
	public void mainconfigSetup() {
		mainconfig = new ConfigManager(plugin);
		mainconfig.setup("config.yml");
		return;
	}
	
	public void announcementsSetup() {
		announcements = new ConfigManager(plugin);
		announcements.setup("announcements.yml");
		return;
	}
	
	public void defaultsSetup() {
		defaults = new ConfigManager(plugin);
		defaults.setup("playerdata\\defaults\\config.yml");
		return;
	}
	
	public void inventoriesSetup() {
		inventories = new ConfigManager(plugin);
		inventories.setup("playerdata\\inventories.yml");
		return;
	}
	
	public void noFallSetup() {
		nofall = new ConfigManager(plugin);
		nofall.setup("nofall\\config.yml");
		return;
	}
	
	public void playerstatsSetup() {
		playerstats = new ConfigManager(plugin);
		playerstats.setup("playerdata\\stats.yml");
		return;
	}
	
	public void worldconfigSetup() {
		worldconfig = new ConfigManager(plugin);
		worldconfig.setup("worldconfig.yml");
		return;
	}
}
