package net.nuggetmc.core.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.md_5.bungee.api.ChatColor;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.modifiers.CombatTracker;

public class WorldManager {
	
	private static Main plugin;
	private static String spawnworld;
	private static double x;
	private static double y;
	private static double z;
	public static int count = 10800;
	public static int countNether = 10802;
	public static int countEnd = 10802;
	
	/*
	 * [TODO]
	 * Add spawn range coordinates here
	 * when spawn doesn't exist, don't allow /spawn or /warp to anything in spawn
	 * when reloading worlds, only send warnings to people in that specific world
	 */
	
	public WorldManager(Main plugin) {
		WorldManager.plugin = plugin;
		spawnworld = Configs.worldsettings.getConfig().getString("spawn.world");
		x = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.x");
		y = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.y");
		z = Configs.worldsettings.getConfig().getDouble("spawn.coordinates.z");
		return;
	}
	
	public void worldReloadTimer() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			World world = Bukkit.getWorld(spawnworld);
			
			if (count < 0) {
				count = 10800;
			}
			
			if (count == 60 || count == 30 || count == 10 || count == 3 || count == 2) {
				for (Player player : world.getPlayers()) {
					player.sendMessage(ChatColor.YELLOW + "The world you are in will reset in " + ChatColor.RED + count + ChatColor.YELLOW + " seconds.");
				}
			}
			
			else if (count == 1) {
				for (Player player : world.getPlayers()) {
					player.sendMessage(ChatColor.YELLOW + "The world you are in will reset in " + ChatColor.RED + "1" + ChatColor.YELLOW + " second.");
				}
			}
			
			else if (count == 0) {
				worldReload(spawnworld);
			}
			
			count--;
		}, 0, 20);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			World world = Bukkit.getWorld("nether");
			
			if (countNether < 0) {
				countNether = 10800;
			}
			
			if (countNether == 60 || countNether == 30 || countNether == 10 || countNether == 3 || countNether == 2) {
				for (Player player : world.getPlayers()) {
					player.sendMessage(ChatColor.YELLOW + "The world you are in will reset in " + ChatColor.RED + countNether + ChatColor.YELLOW + " seconds.");
				}
			}
			
			else if (countNether == 1) {
				for (Player player : world.getPlayers()) {
					player.sendMessage(ChatColor.YELLOW + "The world you are in will reset in " + ChatColor.RED + "1" + ChatColor.YELLOW + " second.");
				}
			}
			
			else if (countNether == 0) {
				worldReload("nether");
			}
			
			countNether--;
		}, 0, 20);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			World world = Bukkit.getWorld("end");
			
			if (countEnd < 0) {
				countEnd = 10800;
			}
			
			if (countEnd == 60 || countEnd == 30 || countEnd == 10 || countEnd == 3 || countEnd == 2) {
				for (Player player : world.getPlayers()) {
					player.sendMessage(ChatColor.YELLOW + "The world you are in will reset in " + ChatColor.RED + countEnd + ChatColor.YELLOW + " seconds.");
				}
			}
			
			else if (countEnd == 1) {
				for (Player player : world.getPlayers()) {
					player.sendMessage(ChatColor.YELLOW + "The world you are in will reset in " + ChatColor.RED + "1" + ChatColor.YELLOW + " second.");
				}
			}
			
			else if (countEnd == 0) {
				worldReload("end");
			}
			
			countEnd--;
		}, 0, 20);
		return;
	}
	
	public void loadAllWorlds() {
		List<String> worlds = Configs.worldsettings.getConfig().getStringList("non-default-worlds");
		for (int i = 0; i < worlds.size(); i++) {
			World world = Bukkit.getWorld(worlds.get(i));
			if (world != null) {
				new WorldCreator(worlds.get(i)).createWorld();
			}
		}
		
		List<String> worldsNether = Configs.worldsettings.getConfig().getStringList("non-default-worlds-nether");
		for (int i = 0; i < worldsNether.size(); i++) {
			World world = Bukkit.getWorld(worldsNether.get(i));
			if (world != null) {
				WorldCreator creatorNether = new WorldCreator(worldsNether.get(i));
				creatorNether.environment(Environment.NETHER);
				creatorNether.createWorld();
			}
		}
		
		List<String> worldsEnd = Configs.worldsettings.getConfig().getStringList("non-default-worlds-the-end");
		for (int i = 0; i < worldsEnd.size(); i++) {
			World world = Bukkit.getWorld(worldsEnd.get(i));
			if (world != null) {
				WorldCreator creatorEnd = new WorldCreator(worldsEnd.get(i));
				creatorEnd.environment(Environment.THE_END);
				creatorEnd.createWorld();
			}
		}
		return;
	}
	
	public void worldPortal(PlayerPortalEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
			event.useTravelAgent(true);
			event.getPortalTravelAgent().setCanCreatePortal(true);
			Location location = null;
			
			if (world == Bukkit.getWorld("nether")) {
				int y = event.getFrom().getBlockY();
				if (y > 140) y = 140;
				location = new Location(Bukkit.getWorld(spawnworld), event.getFrom().getBlockX() * 8,
						y, event.getFrom().getBlockZ() * 8);
			}
			else {
				location = new Location(Bukkit.getWorld("nether"), event.getFrom().getBlockX() / 8,
						event.getFrom().getBlockY(), event.getFrom().getBlockZ() / 8);
			}
			event.setTo(event.getPortalTravelAgent().findOrCreate(location));
		}

		else if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
			if (world == Bukkit.getWorld(spawnworld)) {
				Location loc = new Location(Bukkit.getWorld("end"), 100, 50, 0);
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
			} else if (world == Bukkit.getWorld("end")) {
				event.setTo(new Location(Bukkit.getWorld(spawnworld), x, y, z));
			}
		}
        return;
	}
	
	public static void worldReload(String worldname) {
		final String name = worldname.toLowerCase();
		World world = Bukkit.getWorld(name);
    	File path = world.getWorldFolder();
    	
		switch (name) {
		case "main":
			Set<Player> waiting = new HashSet<>();
		    if(!world.equals(null)) {
		    	Location temp = new Location(Bukkit.getWorld("world"), 0.5, 65, 0.5);
		    	for (Player player : world.getPlayers()) {
		    		if (player.getWorld().getName().equals(name)) {
		    			player.teleport(temp);
		    			player.sendMessage(ChatColor.YELLOW + "Resetting world...");
		    			waiting.add(player);
		    			if (CombatTracker.combatTime.containsKey(player)) {
		    				CombatTracker.combatTime.remove(player);
		    				plugin.sidebar.enable(player);
		    			}
		    		}
		    	}
		    	
		        Bukkit.getServer().unloadWorld(world, false);
		        deleteWorld(path);
		    }
		    
		    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
		    	File sourceFolder = new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "\\backup");
				File targetFolder = new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "\\" + name);
					
				copyWorld(sourceFolder, targetFolder);
				
				Bukkit.getScheduler().runTask(plugin, () -> {
					new WorldCreator(name).createWorld();
					
					for (Player player : waiting) {
						player.sendMessage(ChatColor.YELLOW + "Done!");
						Location spawn = new Location(Bukkit.getWorld(spawnworld), x, y, z);
						player.teleport(spawn);
						count = 10800;
					}
				});
		    });
		    break;
		    
		case "nether":
		    if(!world.equals(null)) {
		    	Location spawn = new Location(Bukkit.getWorld(spawnworld), x, y, z);
		    	for (Player player : world.getPlayers()) {
		    		if (player.getWorld().getName().equals(name)) {
		    			player.teleport(spawn);
		    			player.sendMessage(ChatColor.YELLOW + "Resetting world...");
		    			player.sendMessage(ChatColor.YELLOW + "Done!");
		    			if (CombatTracker.combatTime.containsKey(player)) {
		    				CombatTracker.combatTime.remove(player);
		    				plugin.sidebar.enable(player);
		    			}
		    		}
		    	}
		    	
		        Bukkit.getServer().unloadWorld(world, false);
		        deleteWorld(path);
		    }
		    
		    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
		    	File sourceFolder = new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "\\world_nether");
				File targetFolder = new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "\\" + name);
					
				copyWorld(sourceFolder, targetFolder);
				
				Bukkit.getScheduler().runTask(plugin, () -> {
					WorldCreator creator = new WorldCreator(name);
					creator.environment(Environment.NETHER);
					creator.createWorld();
					countNether = 10800;
				});
		    });
			break;
		    
		case "end":
		    if(!world.equals(null)) {
		    	Location spawn = new Location(Bukkit.getWorld(spawnworld), x, y, z);
		    	for (Player player : world.getPlayers()) {
		    		if (player.getWorld().getName().equals(name)) {
		    			player.teleport(spawn);
		    			player.sendMessage(ChatColor.YELLOW + "Resetting world...");
		    			player.sendMessage(ChatColor.YELLOW + "Done!");
		    			if (CombatTracker.combatTime.containsKey(player)) {
		    				CombatTracker.combatTime.remove(player);
		    				plugin.sidebar.enable(player);
		    			}
		    		}
		    	}
		    	
		        Bukkit.getServer().unloadWorld(world, false);
		        deleteWorld(path);
		    }
		    
		    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
		    	File sourceFolder = new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "\\world_the_end");
				File targetFolder = new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "\\" + name);
					
				copyWorld(sourceFolder, targetFolder);
				
				Bukkit.getScheduler().runTask(plugin, () -> {
					WorldCreator creator = new WorldCreator(name);
					creator.environment(Environment.THE_END);
					creator.createWorld();
					countEnd = 10800;
				});
		    });
			break;
		}
		return;
	}
	
	public static void deleteWorld(File path) {
		if(path.exists()) {
			File[] files = path.listFiles();
          	for(int i=0; i<files.length; i++) {
          		if(files[i].isDirectory()) {
          			deleteWorld(files[i]);
          		} else {
          			if (!files[i].getName().equals("uid.dat")) {
          				files[i].delete();
          			}
          		}
          	}
		}
		return;
	}
	
	public static void copyWorld(File source, File target) {
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
	    }
	}
}
