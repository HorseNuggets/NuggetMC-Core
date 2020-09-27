package net.nuggetmc.core.setup;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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
	
	public void worldPortal(PlayerPortalEvent event) {
		Player player = event.getPlayer();

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            event.useTravelAgent(true);
            event.getPortalTravelAgent().setCanCreatePortal(true);
            Location location = null;
            if (player.getWorld() == Bukkit.getWorld("main")) {
                 location = new Location(Bukkit.getWorld("world_nether"), event.getFrom().getBlockX() / 8, event.getFrom().getBlockY(), event.getFrom().getBlockZ() / 8);
            } else {
                location = new Location(Bukkit.getWorld("main"), event.getFrom().getBlockX() * 8, event.getFrom().getBlockY(), event.getFrom().getBlockZ() * 8);
            }
            event.setTo(event.getPortalTravelAgent().findOrCreate(location));
        }
        
        else if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            if (player.getWorld() == Bukkit.getWorld("main")) {
                Location loc = new Location(Bukkit.getWorld("world_the_end"), 100, 50, 0);
                event.setTo(loc);
                Block block = loc.getBlock();
                for (int x = block.getX() - 2; x <= block.getX() + 2; x++) {
                    for (int z = block.getZ() - 2; z <= block.getZ() + 2; z++) {
                        Block platformBlock = loc.getWorld().getBlockAt(x, block.getY() - 1, z);
                        if (platformBlock.getType() != Material.OBSIDIAN) {
                            platformBlock.setType(Material.OBSIDIAN);
                        }
                        for (int yMod = 1; yMod <= 3; yMod++) {
                            Block b = platformBlock.getRelative(BlockFace.UP, yMod);
                            if (b.getType() != Material.AIR) {
                                b.setType(Material.AIR);
                            }
                        }
                    }
                }
            } else if (player.getWorld() == Bukkit.getWorld("world_the_end")) {
                event.setTo(Bukkit.getWorld("main").getSpawnLocation());   //eventually load this info from config
            }
        }
        return;
	}
}
