package net.nuggetmc.core.data;

import net.nuggetmc.core.Main;

public class Configs {
	
	private Main plugin;
	
	public ConfigManager announcements;
	public ConfigManager nofall;
	public ConfigManager playerstats;
	public ConfigManager worldconfig;
	public ConfigManager inventories;
	
	public Configs(Main plugin) {
		this.plugin = plugin;
		announcementsSetup();
		inventoriesSetup();
		noFallSetup();
		playerstatsSetup();
		worldconfigSetup();
	}
	
	public ConfigManager get(String path) {
		switch (path) {
		case "announcements.yml":
			return announcements;
		case "nofall\\config.yml":
			return nofall;
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
		case "announcements.yml":
			plugin.announcements.reset();
			break;
		}
		return;
	}
	
	public void announcementsSetup() {
		announcements = new ConfigManager(plugin);
		announcements.setup("announcements.yml");
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
