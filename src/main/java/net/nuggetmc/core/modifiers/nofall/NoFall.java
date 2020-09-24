package net.nuggetmc.core.modifiers.nofall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class NoFall {
	
	public ArrayList<Player> fallList = new ArrayList<Player>();
	public HashMap<Player, Byte> downTime = new HashMap<Player, Byte>();
	public List<String> worlds;

	public NoFall(Main plugin) {
		worlds = Configs.nofall.getConfig().getStringList("enabled-worlds");
	}
}
