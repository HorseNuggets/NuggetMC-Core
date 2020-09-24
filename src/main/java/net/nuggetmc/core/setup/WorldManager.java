package net.nuggetmc.core.setup;

import java.util.List;

import org.bukkit.WorldCreator;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class WorldManager {

	public WorldManager(Main plugin) {
		return;
	}
	
	public void loadAllWorlds() {
		List<String> worlds = Configs.worldsettings.getConfig().getStringList("non-default-worlds");
		for (int i = 0; i < worlds.size(); i++) {
			new WorldCreator(worlds.get(i)).createWorld();
		}
		return;
	}
}
