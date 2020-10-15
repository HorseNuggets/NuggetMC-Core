package net.nuggetmc.core.data;

import net.nuggetmc.core.Main;

public class Configs {
	
	private Main plugin;
	
	public static ConfigManager mainconfig;
	public static ConfigManager announcements;
	public static ConfigManager defaults;
	public static ConfigManager gheads;
	public static ConfigManager homes;
	public static ConfigManager ignore;
	public static ConfigManager inventories;
	public static ConfigManager itemshop;
	public static ConfigManager kitsconfig;
	public static ConfigManager mutes;
	public static ConfigManager nofall;
	public static ConfigManager playerstats;
	public static ConfigManager playerstatsArchive;
	public static ConfigManager worldsettings;
	
	public Configs(Main plugin) {
		this.plugin = plugin;
		mainconfigSetup();
		announcementsSetup();
		defaultsSetup();
		gheadsSetup();
		homesSetup();
		ignoreSetup();
		inventoriesSetup();
		itemshopSetup();
		kitsconfigSetup();
		noFallSetup();
		mutesSetup();
		playerstatsSetup();
		playerstatsArchiveSetup();
		worldsettingsSetup();
	}
	
	public ConfigManager get(String path) {
		switch (path) {
		case "config.yml":
			return mainconfig;
		case "announcements.yml":
			return announcements;
		case "itemshop.yml":
			return itemshop;
		case "modifiers\\gheads.yml":
			return gheads;
		case "modifiers\\nofall.yml":
			return nofall;
		case "playerdata\\defaults\\config.yml":
			return defaults;
		case "playerdata\\homes.yml":
			return homes;
		case "playerdata\\ignore.yml":
			return ignore;
		case "playerdata\\inventories.yml":
			return inventories;
		case "playerdata\\kits.yml":
			return kitsconfig;
		case "playerdata\\mutes.yml":
			return mutes;
		case "playerdata\\stats.yml":
			return playerstats;
		case "playerdata\\archive\\config.yml":
			return playerstatsArchive;
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
	
	private void gheadsSetup() {
		gheads = new ConfigManager(plugin);
		gheads.setup("modifiers\\gheads.yml");
		return;
	}
	
	private void homesSetup() {
		homes = new ConfigManager(plugin);
		homes.setup("playerdata\\homes.yml");
		return;
	}
	
	private void ignoreSetup() {
		ignore = new ConfigManager(plugin);
		ignore.setup("playerdata\\ignore.yml");
		return;
	}
	
	private void inventoriesSetup() {
		inventories = new ConfigManager(plugin);
		inventories.setup("playerdata\\inventories.yml");
		return;
	}
	
	private void itemshopSetup() {
		itemshop = new ConfigManager(plugin);
		itemshop.setup("itemshop.yml");
		return;
	}
	
	private void kitsconfigSetup() {
		kitsconfig = new ConfigManager(plugin);
		kitsconfig.setup("playerdata\\kits.yml");
		return;
	}
	
	private void mutesSetup() {
		mutes = new ConfigManager(plugin);
		mutes.setup("playerdata\\mutes.yml");
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
	
	private void playerstatsArchiveSetup() {
		playerstatsArchive = new ConfigManager(plugin);
		playerstatsArchive.setup("playerdata\\archive\\config.yml");
		return;
	}
	
	private void worldsettingsSetup() {
		worldsettings = new ConfigManager(plugin);
		worldsettings.setup("worldsettings.yml");
		return;
	}
}
