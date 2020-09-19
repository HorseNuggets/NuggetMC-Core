package net.nuggetmc.core.data;

import net.nuggetmc.core.Main;

public class Configs {
	
	private Main plugin;
	
	public ConfigManager announcements;
	public ConfigManager nofall;
	public ConfigManager playerdata;
	
	public Configs(Main plugin) {
		this.plugin = plugin;
		announcementsSetup();
		noFallSetup();
		playerdataSetup();
	}
	
	public ConfigManager get(String path) {
		switch (path) {
		case "announcements.yml":
			return announcements;
		case "nofall\\config.yml":
			return nofall;
		case "stats\\playerdata.yml":
			return playerdata;
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
	
	public void noFallSetup() {
		nofall = new ConfigManager(plugin);
		nofall.setup("nofall\\config.yml");
		return;
	}
	
	public void playerdataSetup() {
		playerdata = new ConfigManager(plugin);
		playerdata.setup("stats\\playerdata.yml");
		return;
	}
}
