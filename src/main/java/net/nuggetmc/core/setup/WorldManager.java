package net.nuggetmc.core.setup;

import java.util.List;

import org.bukkit.WorldCreator;

import net.nuggetmc.core.Main;

public class WorldManager {
	
	private Main plugin;

	public WorldManager(Main plugin) {
		this.plugin = plugin;
	}
	
	public void loadAllWorlds() {
		List<String> worlds = plugin.configs.worldsettings.getConfig().getStringList("non-default-worlds");
		for (int i = 0; i < worlds.size(); i++) {
			new WorldCreator(worlds.get(i)).createWorld();
		}
		return;
	}
}
