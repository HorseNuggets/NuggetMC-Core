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
	public ConfigManager worldsettings;
	
	public Configs(Main plugin) {
		this.plugin = plugin;
		mainconfigSetup();
		announcementsSetup();
		defaultsSetup();
		inventoriesSetup();
		noFallSetup();
		playerstatsSetup();
		worldsettingsSetup();
	}
	
	public ConfigManager get(String path) {
		switch (path) {
		case "config.yml":
			return mainconfig;
		case "announcements.yml":
			return announcements;
		case "modifiers\\nofall.yml":
			return nofall;
		case "playerdata\\defaults\\config.yml":
			return defaults;
		case "playerdata\\inventories.yml":
			return inventories;
		case "playerdata\\stats.yml":
			return playerstats;
		case "worldsettings.yml":
			return worldsettings;
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
	
	private void mainconfigSetup() {
		mainconfig = new ConfigManager(plugin);
		mainconfig.setup("config.yml");
		return;
	}
	
	private void announcementsSetup() {
		announcements = new ConfigManager(plugin);
		announcements.setup("announcements.yml");
		return;
	}
	
	private void defaultsSetup() {
		defaults = new ConfigManager(plugin);
		defaults.setup("playerdata\\defaults\\config.yml");
		return;
	}
	
	private void inventoriesSetup() {
		inventories = new ConfigManager(plugin);
		inventories.setup("playerdata\\inventories.yml");
		return;
	}
	
	private void noFallSetup() {
		nofall = new ConfigManager(plugin);
		nofall.setup("modifiers\\nofall.yml");
		return;
	}
	
	private void playerstatsSetup() {
		playerstats = new ConfigManager(plugin);
		playerstats.setup("playerdata\\stats.yml");
		return;
	}
	
	private void worldsettingsSetup() {
		worldsettings = new ConfigManager(plugin);
		worldsettings.setup("worldsettings.yml");
		return;
	}
}
