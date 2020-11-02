package net.nuggetmc.core.misc;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.Checks;

public class FlyVanish implements CommandExecutor {
	
	private static Main plugin;
	
	public static Set<Player> vanish;
	
	public FlyVanish(Main plugin) {
		FlyVanish.plugin = plugin;
		FlyVanish.vanish = new HashSet<>();
	}
	
	public static void onPlayerMove(PlayerMoveEvent event) {
		fly(event.getPlayer());
		return;
	}
	
	public static void onPlayerTeleport(PlayerTeleportEvent event) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			fly(event.getPlayer());
		}, 5);
		return;
	}
	
	private static void fly(Player player) {
		Location loc = player.getLocation();
		if (!player.getWorld().getName().equals("main") || !Checks.checkXD(player) || !(WorldManager.isInSpawn(loc) && !WorldManager.isInArena(loc))) {
			if (!((player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.CREATIVE)))) {
				if (player.getAllowFlight()) {
					player.setAllowFlight(false);
				}
				
				if (player.isFlying()) {
					player.setFlying(false);
				}
			}
		}
		
		else {
			if (!player.getAllowFlight()) {
				player.setAllowFlight(true);
			}
		}
    	return;
    }
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		
		if (!vanish.contains(player)) {
			player.setGameMode(GameMode.SPECTATOR);
			vanish.add(player);
			
			for (Player others : Bukkit.getOnlinePlayers()) {
				try {
					if (!Checks.checkStaff(others)) {
						if (others.canSee(player)) {
							others.hidePlayer(player);
						}
					}
				} catch (NullPointerException e) {
					continue;
				}
			}
			
			player.sendMessage("You are now in vanish.");
			return true;
		}
		
		else if (vanish.contains(player)) {
			player.setGameMode(GameMode.SURVIVAL);
			vanish.remove(player);
			
			for (Player others : Bukkit.getOnlinePlayers()) {
				if (!others.canSee(player)) {
					others.showPlayer(player);
				}
			}
			
			player.sendMessage("You are now out of vanish.");
		}
		return true;
	}
}
