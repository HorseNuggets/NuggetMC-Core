package net.nuggetmc.core.modifiers.nofall;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;

public class NoFall {
	
	public Set<Player> fallList = new HashSet<Player>();
	public HashMap<Player, Byte> downTime = new HashMap<Player, Byte>();
	public List<String> worlds;

	public NoFall(Main plugin) {
		worlds = Configs.nofall.getConfig().getStringList("enabled-worlds");
	}
}
