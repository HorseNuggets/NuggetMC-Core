package net.nuggetmc.core.events;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.scoreboard.Sidebar;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.TimeConverter;

public class FFADeathmatch implements CommandExecutor {
	
	private static Main plugin;
	private static Map<Player, Location> holdAreas;
	
	public static byte phase = 0;
	public static Map<Byte, Wrapper> timers = new HashMap<>();
	public static Set<Player> list = new HashSet<>();
	
	public FFADeathmatch(Main plugin) {
		FFADeathmatch.plugin = plugin;
		FFADeathmatch.holdAreas = new HashMap<>();
		runnablesSetup();
	}
	
	private void runnablesSetup() {
		for (byte i = 0; i < 3; i++) {
			timers.put(i, new Wrapper(null, (short) i));
		}
		return;
	}
	
	public class Wrapper {
		public Wrapper(BukkitRunnable task, short time) {
	       this.task = task;
	       this.time = time;
	    }
		
		private BukkitRunnable task;
	    private short time;
	    
	    public BukkitRunnable getTask() {
	    	return this.task;
	    }
	    
	    public short getTime() {
	    	return this.time;
	    }
	    
	    public void setTime(short value) {
	    	this.time = value;
	    	return;
	    }
	    
	    public void setTask(BukkitRunnable runnable) {
	    	this.task = runnable;
	    	return;
	    }
	}
	
	private BukkitRunnable newTask(byte type) {
		BukkitRunnable runnable = null;
		switch(type) {
		case 0:
			runnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					short count = timers.get((byte) 0).getTime();
					timers.get((byte) 0).setTime((short) (count - 1));
					
					if (count <= 0) {
						b();
						this.cancel();
						return;
					}
					
					for (Player player : list) {
						Team timer = player.getScoreboard().getTeam("c");
						if (timer != null) {
							String val = count + "s";
							timer.setSuffix(val);
						}
					}
					
					if (count == 1 || count == 2 || count == 3 || count == 4 || count == 5 || count == 10 || count == 30) {
						if (count == 10 || count == 30 || count == 60) {
							TextComponent message = new TextComponent("§8[§4Alert§8] §6An §c§lFFA Deathmatch §6begins in §c" + count + " §6seconds! ");
	            			message.setColor(ChatColor.RED);
	            			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join ffa"));
	            			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to enter the event!").color(ChatColor.GOLD).create()));
	            			message.addExtra("§c§l[Click ");
	            			message.addExtra("§c§lto ");
	            			message.addExtra("§c§ljoin]");
	            			
	            			Bukkit.spigot().broadcast(message);
						}
						for (Player player : list) {
							player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
							
							String s = "s";
							if (count == 1) {
								s = "";
							}
							
							player.sendMessage("§eThe event begins in §c" + count + " §esecond" + s + ".");
						}
					}
				}
			};
			break;
			
		case 1:
			runnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					if (list.size() <= 1) {
						list.clear();
						holdAreas.clear();
						this.cancel();
						return;
					}
					
					short count = timers.get((byte) 1).getTime();
					timers.get((byte) 1).setTime((short) (count - 1));
					
					if (count <= 0) {
						for (Player player : list) {
							player.sendMessage("§eThe match has begun! Fight!");
							player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1, 1);
						}
						holdAreas.clear();
						e();
						this.cancel();
						return;
					}
					
					else {
						for (Player player : list) {
							Team timer = player.getScoreboard().getTeam("c");
							if (timer != null) {
								timer.setSuffix(count + "s");
							}
						}
					}
					
					if (count == 1 || count == 2 || count == 3 || count == 4 || count == 5 || count == 10) {
						for (Player player : list) {
							player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
							
							String s = "s";
							if (count == 1) {
								s = "";
							}
							
							player.sendMessage("§eThe match begins in §c" + count + " §esecond" + s + ".");
						}
					}
				}
			};
			break;
			
		case 2:
			runnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					if (list.size() <= 1) {
						list.clear();
						holdAreas.clear();
						this.cancel();
						return;
					}
					
					short count = timers.get((byte) 2).getTime();
					timers.get((byte) 2).setTime((short) (count - 1));
					
					if (count <= 0) {
						Player winner = null;
						double tophealth = 0;
						for (Player player : list) {
							if (player.getHealth() > tophealth) {
								tophealth = player.getHealth();
								winner = player;
							}
						}
						
						for (Player player : list) {
							if (player != winner) {
								Bukkit.getScheduler().runTask(plugin, () -> {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getName());
									player.getWorld().strikeLightningEffect(player.getLocation());
								});
							}
						}
						
						Team timer = winner.getScoreboard().getTeam("c");
						if (timer != null) {
							timer.setSuffix("0s");
						}
						
						this.cancel();
						return;
					}
					
					for (Player player : list) {
						Team timer = player.getScoreboard().getTeam("c");
						if (timer != null) {
							String val = TimeConverter.intToString(count);
							if (val.startsWith(" ")) val = val.substring(1);
							timer.setSuffix(val);
						}
					}
					
					if (count == 1 || count == 2 || count == 3 || count == 4 || count == 5 || count == 10 || count == 30 || count == 60) {
						for (Player player : list) {
							player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
							
							String s = "s";
							if (count == 1) {
								s = "";
							}
							
							player.sendMessage("§eThe match ends in §c" + count + " §esecond" + s + ".");
						}
					}
				}
			};
		}
		return runnable;
	}
	
	public void a() {
		Wrapper timer = timers.get((byte) 0);
		timer.setTime((short) 30);
		timer.setTask(newTask((byte) 0));
		timer.getTask().runTaskTimerAsynchronously(plugin, 0, 20);
		phase = 1;
		return;
	}
	
	private void b() {
		switch (list.size()) {
		case 0:
			phase = 0;
			return;
		
		case 1:
			for (Player player : list) {
				player.sendMessage(ChatColor.YELLOW + "There were not enough players to start the event!");
				Bukkit.getScheduler().runTask(plugin, () -> {
					Sidebar.enable(player, (byte) 0);
					leave(player);
				});
				Location loc = new Location(Bukkit.getWorld("main"), 0.5, 223, 0.5, 0, 0);
				player.teleport(loc);
			}
			list.clear();
			phase = 0;
			return;
			
		default:
			c();
			return;
		}
	}
	
	private void c() {
		double angle = ((int) ((360.0 / (double) list.size()) * 100)) / 100.0;
		
		int i = 0;
		for (Player player : list) {
    		int degrees = (int) (angle * i);
    		double radians = Math.toRadians(degrees);
    		
    		int x = (int) Math.round(15 * Math.cos(radians));
    		int y = (int) Math.round(15 * Math.sin(radians));
    		
    		Team playercount = player.getScoreboard().getTeam("p");
			if (playercount != null) {
				playercount.setPrefix("§7 ▪ Remaining");
			}
    		
    		Team match = player.getScoreboard().getTeam("status");
			if (match != null) {
				match.setSuffix("§4Match");
			}
			
			Location loc = new Location(Bukkit.getServer().getWorld("main"), x + 21.5, 215, y - 89.5, degrees + 90, 0);
			Bukkit.getScheduler().runTask(plugin, () -> {
				player.closeInventory();
				player.teleport(loc);
			});
			holdAreas.put(player, loc);
			
			i++;
		}
		
		phase = 2;
		d();
		return;
	}
	
	private void d() {
		Wrapper timer = timers.get((byte) 1);
		timer.setTime((short) 13);
		timer.setTask(newTask((byte) 1));
		timer.getTask().runTaskTimerAsynchronously(plugin, 0, 20);
		return;
	}
	
	private void e() {
		for (Player player : list) {
			Team playercount = player.getScoreboard().getTeam("c");
			if (playercount != null) {
				playercount.setPrefix("§7 ▪ Time");
				playercount.setSuffix("5m");
			}
		}
		
		Wrapper timer = timers.get((byte) 2);
		timer.setTime((short) 300);
		timer.setTask(newTask((byte) 2));
		timer.getTask().runTaskTimerAsynchronously(plugin, 0, 20);
		return;
	}
	
	private static void win(Player player) {
		player.sendMessage("§7--------------------------------------");
		player.sendMessage("");
		player.sendMessage("§e                   §kX§r§6 YOU WIN! §e§kX");
		player.sendMessage("");
		player.sendMessage("§7--------------------------------------");
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
		Bukkit.broadcastMessage("§8[§4Alert§8] §f" + ColorCodes.colorName(player.getUniqueId(), player.getName()) + " §ejust won §c§lFFA Deathmatch§r§e!");
		
		World world = Bukkit.getWorld("main");
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
			Location loc = new Location(Bukkit.getWorld("main"), 0.5, 223, 0.5);
			Bukkit.getScheduler().runTask(plugin, () -> {
				player.closeInventory();
				player.teleport(loc);
			});
			for (Entity e : world.getEntities()) {
				if (e instanceof Item) {
					if (WorldManager.isInArena(e.getLocation())) {
						e.remove();
					}
				}
			}
			list.clear();
			holdAreas.clear();
			phase = 0;
			for (byte i = 0; i < 3; i++) {
				BukkitRunnable task = timers.get(i).getTask();
				if (Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId())) {
					task.cancel();
				}
			}
		}, 50);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			Sidebar.enable(player, (byte) 0);
			loadArea(world, "plugins/WorldEdit/schematics/arena.schematic");
			player.sendMessage("§eYou earned §a1 Uncommon Nugget§e for winning the event!");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " gold_nugget 1 0 "
					+ "{display:{Name:\"§aUncommon Nugget§r\",Lore:[\"§7A pretty fine piece of§r\",\"§7nugget if you ask me.§r\"]},ench:[{id:51,lvl:1}],HideFlags:1}");
		}, 52);
		return;
	}
	
	private static com.sk89q.worldedit.Vector origin = new com.sk89q.worldedit.Vector(21, 226, -70);
	
	@SuppressWarnings("deprecation")
	private static void loadArea(World world, String str) {
        File file = new File(str);
        com.sk89q.worldedit.EditSession es = new com.sk89q.worldedit.EditSession(new com.sk89q.worldedit.bukkit.BukkitWorld(world), 99999);
        com.sk89q.worldedit.CuboidClipboard cc = null;
		try {
			cc = com.sk89q.worldedit.CuboidClipboard.loadSchematic(file);
		} catch (com.sk89q.worldedit.world.DataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			cc.paste(es, origin, false);
		} catch (com.sk89q.worldedit.MaxChangedBlocksException e) {
			e.printStackTrace();
		}
    	return;
    }
	
	public static void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (holdAreas.containsKey(player)) {
			event.setCancelled(true);
	    	return;
		}
		if (list.contains(player)) {
			Material blocktype = event.getBlock().getType();
			if (!uhcBlocks(blocktype)) {
				event.setCancelled(true);
			}
		}
		return;
	}
	
	public static void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (holdAreas.containsKey(player)) {
			event.setCancelled(true);
	    	return;
		}
		if (list.contains(player)) {
			Material blocktype = event.getBlock().getType();
			if (!uhcBlocks(blocktype)) {
				event.setCancelled(true);
			}
		}
		return;
	}
	
	private static boolean uhcBlocks(Material type) {
		switch (type) {
		case COBBLESTONE:
			return true;
		case WOOD:
			return true;
		case COBBLESTONE_STAIRS:
			return true;
		case WOOD_STAIRS:
			return true;
		case WOOD_PLATE:
			return true;
		case WOOD_STEP:
			return true;
		case OBSIDIAN:
			return true;
		case STONE:
			return true;
		case WORKBENCH:
			return true;
		case WOOD_DOOR:
			return true;
		case WOODEN_DOOR:
			return true;
		default:
			return false;
		}
	}
	
	public static void onBucketEmpty(PlayerBucketEmptyEvent event) {
		if (holdAreas.containsKey(event.getPlayer())) {
			event.setCancelled(true);
	    	return;
		}
		return;
	}
	
	public static void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!WorldManager.isInSpawn(player.getLocation())) {
			leave(player);
			return;
		}
		if (holdAreas.containsKey(player)) {
			if (player.getLocation().distance(holdAreas.get(player)) >= 1) {
				Location loc = player.getLocation();
				Location to = holdAreas.get(player);
				to.setYaw(loc.getYaw());
				to.setPitch(loc.getPitch());
				player.teleport(to);
			}
		}
		return;
	}
	
	public static void onTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if (event.getTo().getBlockY() < 130) {
			leave(player);
		}
		return;
	}
	
	public static void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (holdAreas.containsKey(player)) {
				event.setCancelled(true);
			}
		}
	}
	
	public static void onPlayerDamage2(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (holdAreas.containsKey(player)) {
				event.setCancelled(true);
			}
		}
	}
	
	public static void onDeath(PlayerDeathEvent event) {
		leave(event.getEntity());
	}
	
	public static void onRespawn(PlayerRespawnEvent event) {
		leave(event.getPlayer());
	}
	
	public static void onQuit(PlayerQuitEvent event) {
		leave(event.getPlayer());
	}
	
	private static void leave(Player player) {
		if (list.contains(player)) {
			list.remove(player);
			if (player != null) {
				if (phase == 1) player.sendMessage("§eYou left the event.");
				Sidebar.enable(player, (byte) 0);
			}
			int playerCount = FFADeathmatch.list.size();
			for (Player others : list) {
				Team playersWaiting = others.getScoreboard().getTeam("p");
				if (playersWaiting != null) {
					playersWaiting.setSuffix(String.valueOf(playerCount));
				}
				
				int size = list.size();
				switch (phase) {
				case 1:
					others.sendMessage(ColorCodes.colorName(player.getUniqueId(), player.getName())
							+ "§e left the event.");
					break;
				case 2:
					String remain = " §c" + size + "§e players remain!";
					if (size == 1) {
						remain = "";
					}
					others.sendMessage(ColorCodes.colorName(player.getUniqueId(), player.getName())
							+ "§e has been eliminated!" + remain);
				}
				if (size <= 1) {
					win(others);
				}
			}
		}
		return;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		loadArea(Bukkit.getServer().getWorld("main"), "plugins/WorldEdit/schematics/arena.schematic");
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			list.clear();
			phase = 0;
			for (byte i = 0; i < 3; i++) {
				BukkitRunnable task = timers.get(i).getTask();
				if (Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId())) {
					task.cancel();
				}
			}
		});
		for (Player player : Bukkit.getOnlinePlayers()) {
			Sidebar.enable(player, (byte) 0);
		}
		return true;
	}
}
