package net.nuggetmc.core.events;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.scoreboard.Sidebar;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.TimeConverter;

public class Tournament implements CommandExecutor {
	
	private static Main plugin;
	private static String linspace = ChatColor.GRAY + "--------------------------------------";
	private static List<String> results;
	private short rs = 0;
	private FileConfiguration config;

	public static Set<Player> active;
	public static boolean enabled = false;
	public static Map<Byte, Wrapper> timers = new HashMap<>();
	public static ArrayList<Player> cont;

	static List<ArrayList<Player>> adv;
	static Map<Player, Location> holdAreas;
	static short i = 0;
	
	/*
	 * [TODO]
	 * Remember to unenable enable
	 * eventually make scoreboard different for those in arena area generally
	 * eventually make Wrapper class global from Event
	 * You can leave the event with /leave
	 * command for resetting all of the variables
	 * check if both players are offline
	 * make it so when you die you leave cont
	 * set Match text back to idle between rounds (for all contestants)
	 * EVENTUALLY MAKE ONTELEPORT CHECK NOT JUST Y BUT INSPAWN AND INARENA
	 * eventually allow more commands while waiting
	 * check leaving before the match starts
	 * at end of tournament in announcement, do click here to view the full results
	 * create global variable for arena
	 * test time running out
	 * eventually don't use sin and cos for location creating
	 * 
	 * doesn't always teleport
	 * doesn't always cross out on list
	 */
	
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
	
	public Tournament(Main plugin) {
		Tournament.plugin = plugin;
		Tournament.holdAreas = new HashMap<>();
		Tournament.cont = new ArrayList<>();
		Tournament.adv = new ArrayList<>();
		Tournament.active = new HashSet<>();
		Tournament.results = new ArrayList<>();
		
		this.config = Configs.playerstats.getConfig();
		this.runnablesSetup();
	}
	
	private void runnablesSetup() {
		for (byte i = 0; i < 4; i++) {
			timers.put(i, new Wrapper(null, (short) i));
		}
		return;
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
					
					int playerCount = cont.size();
					
					for (Player player : cont) {
						Team timer = player.getScoreboard().getTeam("c");
						if (timer != null) {
							String val = count + "s";
							timer.setSuffix(val);
						}
						
						Team pw = player.getScoreboard().getTeam("p");
						if (pw != null) {
							pw.setSuffix(String.valueOf(playerCount));
						}
					}
					
					if (count == 1 || count == 2 || count == 3 || count == 4 || count == 5 || count == 10 || count == 30) {
						if (count == 10 || count == 30 || count == 60) {
							TextComponent message = new TextComponent("§8[§4Alert§8] §6A §c§lTournament §6begins in §c" + count + " §6seconds! ");
	            			message.setColor(ChatColor.RED);
	            			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join tournament"));
	            			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to enter the event!").color(ChatColor.GOLD).create()));
	            			message.addExtra("§c§l[Click ");
	            			message.addExtra("§c§lto ");
	            			message.addExtra("§c§ljoin]");
	            			
	            			Bukkit.spigot().broadcast(message);
						}
						for (Player player : cont) {
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
					if (active.size() <= 1) {
						active.clear();
						holdAreas.clear();
						this.cancel();
						return;
					}
					
					short count = timers.get((byte) 1).getTime();
					timers.get((byte) 1).setTime((short) (count - 1));
					
					if (count <= 0) {
						for (Player player : active) {
							player.sendMessage("§eThe match has begun! Fight!");
							player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1, 1);
						}
						holdAreas.clear();
						g();
						this.cancel();
						return;
					}
					
					else {
						for (Player player : active) {
							Team timer = player.getScoreboard().getTeam("t");
							if (timer != null) {
								timer.setSuffix(count + "s");
							}
						}
					}
					
					if (count == 1 || count == 2 || count == 3 || count == 4 || count == 5 || count == 10) {
						for (Player player : active) {
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
					if (active.size() <= 1) {
						active.clear();
						holdAreas.clear();
						this.cancel();
						return;
					}
					
					short count = timers.get((byte) 2).getTime();
					timers.get((byte) 2).setTime((short) (count - 1));
					
					if (count <= 0) {
						Player winner = null;
						double tophealth = 0;
						for (Player player : active) {
							if (player.getHealth() > tophealth) {
								tophealth = player.getHealth();
								winner = player;
							}
						}
						
						for (Player player : active) {
							if (player != winner) {
								Bukkit.getScheduler().runTask(plugin, () -> {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getName());
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
					
					for (Player player : active) {
						Team timer = player.getScoreboard().getTeam("t");
						if (timer != null) {
							String val = TimeConverter.intToString(count);
							if (val.startsWith(" ")) val = val.substring(1);
							timer.setSuffix(val);
						}
					}
					
					if (count == 1 || count == 2 || count == 3 || count == 4 || count == 5 || count == 10 || count == 30 || count == 60) {
						for (Player player : active) {
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
			break;
			
		case 3:
			runnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					if (cont.size() <= 1) {
						this.cancel();
						return;
					}
					
					if (active.size() <= 1) {
						this.cancel();
						return;
					}
					
					short count = timers.get((byte) 3).getTime();
					timers.get((byte) 3).setTime((short) (count - 1));
					
					if (count <= 0) {
						for (Player player : active) {
							player.sendMessage("§eTeleporting...");
						}
						e();
						this.cancel();
						return;
					}
					
					if (count == 1 || count == 2 || count == 3 || count == 4 || count == 5 || count == 10) {
						for (Player player : active) {
							player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
							
							String s = "s";
							if (count == 1) {
								s = "";
							}
							
							player.sendMessage("§eTeleporting in §c" + count + " §esecond" + s + "...");
						}
					}
				}
			};
			break;
		}
		return runnable;
	}
	
	public void a() {
		Wrapper timer = timers.get((byte) 0);
		timer.setTime((short) 30);
		timer.setTask(newTask((byte) 0));
		timer.getTask().runTaskTimerAsynchronously(plugin, 0, 20);
		enabled = true;
		return;
	}
	
	@SuppressWarnings("unchecked")
	private void b() {
		switch (cont.size()) {
		case 0:
			enabled = false;
			Event.arena = false;
			return;
		
		case 1:
			for (Player player : cont) {
				player.sendMessage(ChatColor.YELLOW + "There were not enough players to start the event!");
				Bukkit.getScheduler().runTask(plugin, () -> {
					Sidebar.enable(player, (byte) 0);
					leave(player);
				});
				Location loc = new Location(Bukkit.getWorld("main"), 0.5, 223, 0.5, 0, 0);
				player.teleport(loc);
			}
			cont.clear();
			adv.clear();
			enabled = false;
			Event.arena = false;
			return;
			
		default:
			Collections.shuffle(cont);
			for (Player player : cont) {
				UUID uid = player.getUniqueId();
				Team round = player.getScoreboard().getTeam("p");
				
				rs = (short) Math.ceil(Math.log(cont.size()) / Math.log(2));
				
				if (round != null) {
					round.setPrefix("§7 ▪ Round");
					round.setSuffix("§e1/" + rs);
				}
				
				int rating = 1500;
				
				if (!config.contains("players." + uid + ".rating")) {
					config.set("players." + uid + ".rating", 1500);
					Bukkit.getScheduler().runTask(plugin, () -> {
						Configs.playerstats.saveConfig();
					});
				} else {
					rating = config.getInt("players." + uid + ".rating");
				}
				
				Team elo = player.getScoreboard().getTeam("c");
				if (elo != null) {
					elo.setPrefix("§7 ▪ Rating");
					elo.setSuffix("§e" + NumberFormat.getNumberInstance(Locale.US).format(rating));
				}
			}
			adv.add((ArrayList<Player>) cont.clone());
			c(true);
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void c(boolean next) {
		if (next) adv.add(new ArrayList<>());
		short n = (short) (adv.size() - 1);
		short size = (short) adv.get(n - 1).size();
		results.clear();
		
		for (Player player : cont) {
			Team round = player.getScoreboard().getTeam("p");
			if (round != null) {
				round.setSuffix("§e" + n + "/" + rs);
			}
		}
		
		String round = "ROUND " + n;
		if (size <= 2) {
			round = "FINALS";
		} else if (size > 2 && size <= 4) {
			round = "SEMI-FINALS";
		}
		
		sendp(linspace);
		sendp("§6§lTournament §r§7- §e" + round);
		sendp("");
		
		for (short i = 0; i < adv.get(n - 1).size(); i++) {
			Player player = adv.get(n - 1).get(i);
			Player opp = null;
			String name = player.getName();
			UUID uid = null;
			
			try {
				uid = player.getUniqueId();
			} catch (NullPointerException e) {
				uid = Bukkit.getOfflinePlayer(player.getName()).getUniqueId();
			}
			
			String elo = "1,500";
			
			try {
				elo = NumberFormat.getNumberInstance(Locale.US).format(config.getInt("players." + uid + ".rating"));
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			
			if (size > 1 && size % 2 == 1 && i == size - 1) {
				sendp("");
				sendp("§eBYE");
				sendp("§7 ▪ §r" + ColorCodes.offlineRankTagUUID(uid) + name + " §r§7(§e" + elo + "§7)");
				break;
			}
			
			if (i % 2 == 0) {
				if (i < size) {
					opp = adv.get(n - 1).get(i + 1);
				}
			} else {
				opp = adv.get(n - 1).get(i - 1);
			}
			
			if (i % 2 == 0 && i < size - 1) {
				int mnum = (i / 2) + 1;
				String cur = "";
				if (mnum == (int) (Tournament.i / 2) + 1) {
					cur = " §7(§aCURRENT§7)";
				}
				sendp("§eMATCH " + mnum + cur);
			}
			
			String text = ColorCodes.offlineRankTagUUID(uid) + name;
			if (opp != null) {
				if (adv.get(n).contains(opp)) {
					text = ColorCodes.offlineStrikeRankTagUUID(uid) + name;
				}
			}
			
			sendp("§7 ▪ §r" + text + "§r §7(§e" + elo + "§7)");
			
			if (i % 2 == 1 && i < size - 2) {
				sendp("");
			}
		}
		sendp(linspace);
		
		if (next) {
			if (size > 1) {
				d();
			}
		}
		return;
	}
	
	private void d() {
		short n = (short) adv.size();
		
		if (i == 0) {
			if (adv.get(n - 2).size() % 2 == 1) {
				Player player = adv.get(n - 2).get(adv.get(n - 2).size() - 1);
				
				if (!adv.get(n - 1).contains(player)) {
					adv.get(n - 1).add(player);
					if (player != null) {
						player.sendMessage("§6You have advanced to §eRound " + adv.size() + "§6 as a bye.");
					}
				}
			}
		}
		
		if (i < adv.get(n - 2).size()) {
			if (i == adv.get(n - 2).size() - 1) {
				i = 0;
			} else {
				Player p1 = adv.get(n - 2).get(i);
				if (!cont.contains(p1)) {
					h(p1);
					return;
				}
				
				Player p2 = adv.get(n - 2).get(i + 1);
				if (!cont.contains(p2)) {
					h(p2);
					return;
				}
				
				active.add(p1);
				active.add(p2);
				
				i += 2;
				
				for (Player player : active) {
					player.sendMessage("§6You are up next!");
				}
				
				Wrapper timer = timers.get((byte) 3);
				timer.setTime((short) 13);
				timer.setTask(newTask((byte) 3));
				timer.getTask().runTaskTimerAsynchronously(plugin, 0, 20);
			}
		} else {
			i = 0;
		}
		return;
	}
	
	private void e() {
		double angle = ((int) ((360.0 / (double) active.size()) * 100)) / 100.0;
		
		int i = 0;
		for (Player player : active) {
    		int degrees = (int) (angle * i);
    		double radians = Math.toRadians(degrees);
    		
    		int x = (int) Math.round(15 * Math.cos(radians));
    		int y = (int) Math.round(15 * Math.sin(radians));
    		
    		Scoreboard s = player.getScoreboard();
    		
    		Team t = s.registerNewTeam("t");
			t.addEntry(": §f§e");
			t.setSuffix("13s");
			t.setPrefix("§7 ▪ Starting in");
			
			Objective obj = s.getObjective("stats");
			
			obj.getScore("§6Match").setScore(4);
			obj.getScore(": §f§e").setScore(3);
			obj.getScore("  ").setScore(2);
    		
    		Team match = player.getScoreboard().getTeam("status");
			if (match != null) {
				match.setSuffix("§4Match");
			}
			
			Location loc = new Location(Bukkit.getServer().getWorld("main"), x + 21.5, 215, y - 89.5, degrees + 90, 0);
			Bukkit.getScheduler().runTask(plugin, () -> {
				player.closeInventory();
				for (PotionEffect effect : player.getActivePotionEffects()) {
					PotionEffectType type = effect.getType();
					if (!type.equals(PotionEffectType.HEALTH_BOOST) && !type.equals(PotionEffectType.NIGHT_VISION)) {
						player.removePotionEffect(type);
					}
				}
				player.teleport(loc);
			});
			holdAreas.put(player, loc);
			
			i++;
		}
		
		f();
		return;
	}
	
	private void f() {
		Wrapper timer = timers.get((byte) 1);
		timer.setTime((short) 13);
		timer.setTask(newTask((byte) 1));
		timer.getTask().runTaskTimerAsynchronously(plugin, 0, 20);
		return;
	}
	
	private void g() {
		for (Player player : active) {
			Team time = player.getScoreboard().getTeam("t");
			if (time != null) {
				time.setPrefix("§7 ▪ Time");
				time.setSuffix("5m");
			}
		}
		
		Wrapper timer = timers.get((byte) 2);
		timer.setTime((short) 300);
		timer.setTask(newTask((byte) 2));
		timer.getTask().runTaskTimerAsynchronously(plugin, 0, 20);
		return;
	}
	
	@SuppressWarnings("deprecation")
	private void h(Player loser) {
		short n = (short) adv.size();
		Player winner = null;
		
		if (active.size() == 1) {
			for (Player rem : active) {
				winner = rem;
			}
		} else if (cont.size() == 1) {
			for (Player rem : cont) {
				winner = rem;
			}
		}
		
		final Player player = winner;
		
		if (player != null) {
			UUID uidw = null;
			UUID uidl = null;
			
			try {
				uidw = player.getUniqueId();
			} catch (NullPointerException e) {
				uidw = Bukkit.getOfflinePlayer(player.getName()).getUniqueId();
			}
			
			try {
				uidl = loser.getUniqueId();
			} catch (NullPointerException e) {
				uidl = Bukkit.getOfflinePlayer(loser.getName()).getUniqueId();
			}
			
			int elow = config.getInt("players." + uidw + ".rating");
			int elol = config.getInt("players." + uidl + ".rating");
			
			int diff = elow - elol;
			int cng = (int) Math.round(42 / (1 + Math.exp(0.01 * diff)));
			
			elow += cng;
			elol -= cng;
			
			config.set("players." + uidw + ".rating", elow);
			config.set("players." + uidl + ".rating", elol);
			
			Bukkit.getScheduler().runTask(plugin, () -> {
				Configs.playerstats.saveConfig();
			});
			
			Team elo = player.getScoreboard().getTeam("c");
			if (elo != null) {
				elo.setSuffix("§e" + NumberFormat.getNumberInstance(Locale.US).format(elow));
			}
			
			if (player != null) {
				player.sendMessage("§7--------------------------------------");
				player.sendMessage("");
				player.sendMessage("§e                   §kX§r§6 YOU WIN! §e§kX");
				player.sendMessage("");
				player.sendMessage("§7--------------------------------------");
				
				List<String> eloupdate = new ArrayList<>();
				eloupdate.add(linspace);
				eloupdate.add("§eMATCH RESULTS");
				eloupdate.add("§7 ▪ " + ColorCodes.offlineRankTagUUID(uidw) + player.getName() + " §r§7(§e"
						+ NumberFormat.getNumberInstance(Locale.US).format(elow) + "§7)"
						+ " (§a+" + cng + "§7)");
				
				eloupdate.add("§7 ▪ " + ColorCodes.offlineStrikeRankTagUUID(uidl) + loser.getName() + "§r §7(§e"
						+ NumberFormat.getNumberInstance(Locale.US).format(elol) + "§7)"
						+ " (§c-" + cng + "§7)");
				
				eloupdate.add(linspace);
				
				for (String msg : eloupdate) {
					if (player.isOnline()) player.sendMessage(msg);
					if (loser != null) {
						if (loser.isOnline()) loser.sendMessage(msg);
					}
				}
				
				if (adv.get(n - 2).size() > 2) {
					player.sendMessage("§6You have advanced to §eRound " + adv.size() + "§6.");
					adv.get(n - 1).add(player);
				} else {
					player.sendMessage("§6You won the tournament!");
					Bukkit.broadcastMessage("§8[§4Alert§8] §f" + ColorCodes.colorName(player.getUniqueId(), player.getName()) + " §ejust won §c§lTournament§r§e!");
					
					World world = Bukkit.getWorld("main");
					Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
						Location loc = new Location(Bukkit.getWorld("main"), 0.5, 223, 0.5);
						Bukkit.getScheduler().runTask(plugin, () -> {
							player.closeInventory();
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect " + player.getName() + " 6 6 6");
							player.teleport(loc);
							Sidebar.enable(player, (byte) 0);
						});
						
						active.clear();
						holdAreas.clear();
						i = 0;
						adv.clear();
						cont.clear();
						Event.arena = false;
						enabled = false;
						results.clear();
						
						for (Entity e : world.getEntities()) {
							if (e instanceof Item) {
								if (WorldManager.isInArena(e.getLocation())) {
									e.remove();
								}
							}
						}
						
						for (byte i = 0; i < 3; i++) {
							BukkitRunnable task = timers.get(i).getTask();
							if (task != null) {
								if (Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId())) {
									task.cancel();
								}
							}
						}
					}, 50);
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						FFADeathmatch.loadArea(world, "plugins/WorldEdit/schematics/arena.schematic");
					}, 52);
					
					return;
				}
			}
			
			i_(player);
		}
		
		active.clear();
		holdAreas.clear();
		return;
	}
	
	private void i_(Player player) {
		short n = (short) adv.size();
		boolean on = true;
		if (player == null) on = false;
		final boolean onn = on;
		
		World world = Bukkit.getWorld("main");
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
			if (onn) {
				Location loc = new Location(Bukkit.getWorld("main"), 21.5, 227, -69.5, 180, 0);
				Bukkit.getScheduler().runTask(plugin, () -> {
					player.closeInventory();
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect " + player.getName() + " 6 6 6");
					player.teleport(loc);
				});
				
				Scoreboard s = player.getScoreboard();
	    		
	    		Team match = s.getTeam("status");
				if (match != null) {
					match.setSuffix("§aIdle");
				}
				
				Team time = s.getTeam("t");
				if (time != null) {
					time.unregister();
				}
				
				s.resetScores("§6Match");
				s.resetScores(": §f§e");
	    		s.resetScores("  ");
			}
			
			for (Entity e : world.getEntities()) {
				if (e instanceof Item) {
					if (WorldManager.isInArena(e.getLocation())) {
						e.remove();
					}
				}
			}
			
			holdAreas.clear();
			for (byte i = 0; i < 3; i++) {
				BukkitRunnable task = timers.get(i).getTask();
				if (task != null) {
					if (Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId())) {
						task.cancel();
					}
				}
			}
			
			if (i < adv.get(n - 2).size() - 1) {
				c(false);
				d();
			} else {
				i = 0;
				c(true);
			}
		}, 50);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			FFADeathmatch.loadArea(world, "plugins/WorldEdit/schematics/arena.schematic");
		}, 52);
		return;
	}
	
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (holdAreas.containsKey(player)) {
			event.setCancelled(true);
	    	return;
		}
		if (cont.contains(player)) {
			Material blocktype = event.getBlock().getType();
			if (!FFADeathmatch.uhcBlocks(blocktype)) {
				event.setCancelled(true);
			}
		}
		return;
	}
	
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (holdAreas.containsKey(player)) {
			event.setCancelled(true);
	    	return;
		}
		if (cont.contains(player)) {
			Material blocktype = event.getBlock().getType();
			if (!FFADeathmatch.uhcBlocks(blocktype)) {
				event.setCancelled(true);
			}
		}
		return;
	}
	
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		if (holdAreas.containsKey(event.getPlayer())) {
			event.setCancelled(true);
	    	return;
		}
		return;
	}
	
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (cont.contains(player)) {
			if (!WorldManager.isInSpawn(player.getLocation())) {
				leave(player);
				return;
			}
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
	
	public void onTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if (cont.contains(player)) {
			if (event.getTo().getBlockY() < 130) {
				leave(player);
			}
		}
		return;
	}
	
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (holdAreas.containsKey(player)) {
				event.setCancelled(true);
			}
		}
	}
	
	public void onPlayerDamage2(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (holdAreas.containsKey(player)) {
				event.setCancelled(true);
			}
		}
	}
	
	public void onDeath(PlayerDeathEvent event) {
		if (cont.contains(event.getEntity())) {
			leave(event.getEntity());
		}
	}
	
	public void onRespawn(PlayerRespawnEvent event) {
		if (cont.contains(event.getPlayer())) {
			leave(event.getPlayer());
		}
	}
	
	public void onQuit(PlayerQuitEvent event) {
		if (cont.contains(event.getPlayer())) {
			leave(event.getPlayer());
		}
	}
	
	private void send(String msg) {
		for (Player player : cont) {
			player.sendMessage(msg);
		}
	}
	
	private void sendp(String msg) {
		send(msg);
		results.add(msg);
	}
	
	private void leave(Player player) {
		cont.remove(player);
		if (player != null) {
			Team pw = player.getScoreboard().getTeam("p");
			if (pw != null) {
				if (pw.getPrefix().equals("§7 ▪ Players")) {
					player.sendMessage("§eYou left the event.");
				}
			}
			Sidebar.enable(player, (byte) 0);
		}
		
		int playerCount = cont.size();
		for (Player others : cont) {
			Team pw = others.getScoreboard().getTeam("p");
			if (pw != null) {
				if (pw.getPrefix().equals("§7 ▪ Players")) {
					pw.setSuffix(String.valueOf(playerCount));
					others.sendMessage(ColorCodes.colorName(player.getUniqueId(), player.getName())
							+ "§e left the event.");
				}
			}
		}
		
		if (active.contains(player)) {
			active.remove(player);
			if (!player.isDead()) {
				if (WorldManager.isInArena(player.getLocation())) {
					player.setHealth(0);
				}
			}
			player.getWorld().strikeLightningEffect(player.getLocation());
			h(player);
		}
		return;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!results.isEmpty()) {
			for (String msg : results) {
				sender.sendMessage(msg);
			}
		} else {
			sender.sendMessage("§cThere are no current tournament stats to display!");
		}
		return true;
	}
}
