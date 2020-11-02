package net.nuggetmc.core.commands.def;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.player.Levelup;
import net.nuggetmc.core.setup.WorldManager;
import net.nuggetmc.core.util.Checks;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.TimeConverter;

public class DefaultCommand implements CommandExecutor {
	
	private Main plugin;
	private String spawnworld;
	private double x;
	private double y;
	private double z;
	private String linspace = ChatColor.GRAY + "--------------------------------------";

	public DefaultCommand(Main plugin) {
		this.defineConfigs();
		this.defineSpawn();
		this.defineWarps();
		this.defineMaps();
		this.defineLevelsMsg();
		this.plugin = plugin;
	}
	
	private FileConfiguration worldsettings;
	private FileConfiguration playerstats;
	private FileConfiguration lead;
	
	private void defineConfigs() {
		this.worldsettings = Configs.worldsettings.getConfig();
		this.playerstats = Configs.playerstats.getConfig();
		this.lead = Configs.lead.getConfig();
		return;
	}
	
	private void defineSpawn() {
		this.spawnworld = worldsettings.getString("spawn.world");
		this.x = worldsettings.getDouble("spawn.coordinates.x");
		this.y = worldsettings.getDouble("spawn.coordinates.y");
		this.z = worldsettings.getDouble("spawn.coordinates.z");
		return;
	}
	
	/*
	 * [TODO]
	 * Store the values as a float array
	 */
	
	private Map<String, String> warps = new HashMap<>();
	
	private void defineWarps() {
		warps.put("ushop", "world%28.5%222%15.5%270%0");
		return;
	}
	
	private static Map<Player, Long> boat;
	private static Map<Player, Long> blocks;
	
	private void defineMaps() {
		boat = new HashMap<>();
		blocks = new HashMap<>();
	}
	
	private List<String> levels;
	
	private void defineLevelsMsg() {
		levels = new ArrayList<String>();
		levels.add(linspace);
		levels.add("Leveling Kill Requirements:");
		for (int i = 1; i <= 12; i++) {
			levels.add(ChatColor.GRAY + " ▪ " + ChatColor.WHITE + ColorCodes.levelToName(i) + ChatColor.GRAY + " (" + ColorCodes.levelToTag(i)
				+ ChatColor.GRAY + ") " + ChatColor.WHITE + "("
				+ ChatColor.YELLOW + NumberFormat.getNumberInstance(Locale.US).format(Levelup.checkKills(i))
				+ ChatColor.WHITE + ")");
		}
		levels.add(linspace);
		return;
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label) {
		case "spawn":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				
				World world = Bukkit.getWorld(spawnworld);
				if (world == null) {
					player.sendMessage(ChatColor.RED + "The world you are trying to teleport to is currently inactive!");
				}
				else {
					player.teleport(new Location(Bukkit.getWorld(spawnworld), x, y, z));
					plugin.healthboost.healthSetup(player);
					player.sendMessage(ChatColor.WHITE + "You have warped to " + ChatColor.YELLOW + "spawn" + ChatColor.WHITE + ".");
				}
			}
			break;
			
		case "shop":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				
				World world = Bukkit.getWorld(spawnworld);
				if (world == null) {
					player.sendMessage(ChatColor.RED + "The world you are trying to teleport to is currently inactive!");
				}
				else {
					player.teleport(new Location(Bukkit.getWorld(spawnworld), 30.5, 222, 15.5, 270, 0));
					plugin.healthboost.healthSetup(player);
					player.sendMessage(ChatColor.WHITE + "You have warped to " + ChatColor.YELLOW + "spawn" + ChatColor.WHITE + ".");
				}
			}
			break;
			
		case "lead":
			if (args.length > 0) {
				String type = args[0].toLowerCase();
				
				if (type.equals("kills") || type.equals("nuggets")) {
					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						sender.sendMessage("Loading leaderboard...");
						List<String> msg = new ArrayList<>();
						msg.add(linspace);
						msg.add("TOP 20:");
						
						for (int i = 1; i <= 20; i++) {
							
							String space = " ";
							if (i >= 10) {
								space = "";
							}
							
							if (lead.contains(type + "." + i)) {
								String name = lead.getString(type + "." + i + ".name");
								int value = Integer.parseInt(lead.getString(type + "." + i + ".value"));
								
								Player credit = Bukkit.getPlayer(name);
								UUID uid = null;
								
								if (credit != null) {
									uid = credit.getUniqueId();
								}
								
								else {
									OfflinePlayer disc = Bukkit.getOfflinePlayer(name);
									if (disc != null) {
										uid = disc.getUniqueId();
									}
								}
								
								String rank = ColorCodes.getOfflineRankName(uid);
								String playername = ColorCodes.rankNameTagName(rank) + name;
								
								String output = "§e #" + i + space + " §7▪ §f" + playername + " §7- §e"
										+ NumberFormat.getNumberInstance(Locale.US).format(value);
								msg.add(output);
							}
							
							else {
								String output = "§e #" + i + space + " §7▪ ";
								msg.add(output);
							}
						}
						
						if (sender instanceof Player) {
							Player player = (Player) sender;
							for (int i = 1; i <= lead.getConfigurationSection(type).getKeys(false).size(); i++) {
								if (lead.getString(type + "." + i + ".name").equals(player.getName())) {
									msg.add("");
									msg.add("Your placement:");
									
									int value = Integer.parseInt(lead.getString(type + "." + i + ".value"));
									
									String space = " ";
									if (i == 10) {
										space = "";
									}
									
									UUID uid = player.getUniqueId();
									String rank = ColorCodes.getOfflineRankName(uid);
									String playername = ColorCodes.rankNameTagName(rank) + player.getName();
									
									String output = "§e #" + i + space + " §7▪ §f" + playername + " §7- §e"
											+ NumberFormat.getNumberInstance(Locale.US).format(value);
									msg.add(output);
								}
							}
						}
						
						msg.add(linspace);
						
						for (String i : msg) {
							sender.sendMessage(i);
						}
					});
					return true;
				}
				else {
					sender.sendMessage("That leaderboard does not exist!");
				}
			}
			sender.sendMessage("Select a leaderboard to view: §e/lead kills§r, §e/lead nuggets");
			return true;
		
		case "warp":
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Too few arguments!");
				sender.sendMessage(ChatColor.RED + "Usage: /warp <location>");
			}
			
			else {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					String input = args[0].toLowerCase();
					
					switch (input) {
					case "spawn":
						Bukkit.dispatchCommand(sender, "spawn");
						break;
					}
					
					if (Checks.checkHighStaff(player)) {
						if (warps.containsKey(input)) {
							String obj = warps.get(input);
							String[] sep = obj.split("%");
							World world = Bukkit.getWorld(sep[0]);
							
							if (world == null) {
								player.sendMessage(ChatColor.RED + "The world you are trying to teleport to is currently inactive!");
							}
							
							else {
								float x = Float.parseFloat(sep[1]);
								float y = Float.parseFloat(sep[2]);
								float z = Float.parseFloat(sep[3]);
								float yaw = Float.parseFloat(sep[4]);
								float pitch = Float.parseFloat(sep[5]);
								
								Location loc = new Location(world, x, y, z, yaw, pitch);
								player.teleport(loc);
							}
						}
					}
				}
			}
			break;
		
		case "wrt":
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				sender.sendMessage(linspace);
				sender.sendMessage(ChatColor.WHITE + "World reset times:");
				sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "Overworld:" + ChatColor.RED + TimeConverter.intToString(WorldManager.count));
				sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "Nether:" + ChatColor.RED + TimeConverter.intToString(WorldManager.count));
				sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "The End:" + ChatColor.RED + TimeConverter.intToString(WorldManager.count));
				sender.sendMessage(linspace);
			});
			break;
			
		case "levels":
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				for (String i : levels) {
					sender.sendMessage(i);
				}
			});
			break;
			
		case "boat":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				
				if (boat.containsKey(player)) {
					Long difference = boat.get(player) - System.currentTimeMillis() / 1000;
					if (difference > 0) {
						String s = "s";
						if (difference == 1) s = "";
						player.sendMessage(ChatColor.WHITE + "You can't get a boat again for another " + ChatColor.YELLOW
								+ difference + ChatColor.WHITE + " second" + s + ".");
						return true;
					}
					
					else {
						boat.remove(player);
					}
				}
				
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " boat");
				player.sendMessage("You have recieved a boat.");
				boat.put(player, 10 + System.currentTimeMillis() / 1000);
			}
			break;
			
		case "blocks":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				
				if (blocks.containsKey(player)) {
					Long difference = blocks.get(player) - System.currentTimeMillis() / 1000;
					if (difference > 0) {
						String s = "s";
						if (difference == 1) s = "";
						player.sendMessage(ChatColor.WHITE + "You can't get blocks again for another " + ChatColor.YELLOW
								+ difference + ChatColor.WHITE + " second" + s + ".");
						return true;
					}
					
					else {
						blocks.remove(player);
					}
				}
				
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " cobblestone 64");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " planks 64");
				player.sendMessage("You have recieved blocks.");
				blocks.put(player, 60 + System.currentTimeMillis() / 1000);
			}
			break;
			
		case "suicide":
			if (sender instanceof Player) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + sender.getName());
			}
			break;
			
		case "rules":
			sender.sendMessage(linspace);
			sender.sendMessage("Server Rules:");
			sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "(1) No making cobble monsters");
			sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "(2) No cheating");
			sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "(3) If you wish to team, team against higher");
			sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + " level players, not lower level players.");
			sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "(4) No racism, toxicity");
			sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "(5) No kill boosting (spam-killing a player");
			sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + " who isn't playing just for levels)");
			sender.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + "(6) Obey your lord and savior horse nuggets");
			sender.sendMessage(linspace);
			break;
			
		case "stats":
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				if (args.length >= 1) {
					Player player = Bukkit.getPlayer(args[0]);
					String playername = args[0];
					UUID uuid = null;
					
					if (player == null) {
						OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(args[0]);
						if (offPlayer != null) {
							uuid = offPlayer.getUniqueId();
							playername = offPlayer.getName();
						}
						
						else {
							sender.sendMessage(ChatColor.GOLD + playername + ChatColor.YELLOW + " has never joined the server!");
							return;
						}
					}
					
					else {
						uuid = player.getUniqueId();
						playername = player.getName();
					}
					
					if (uuid != null) {
						String name = playerstats.getString("players." + uuid + ".name");
						
						if (name == null) {
							sender.sendMessage(ChatColor.GOLD + playername + ChatColor.YELLOW + " has never joined the server!");
							return;
						}
						
						String rank = ColorCodes.getOfflineRankName(uuid);
						playername = ColorCodes.rankNameTagName(rank) + playername;
						int level = playerstats.getInt("players." + uuid + ".level");
						int kills = playerstats.getInt("players." + uuid + ".kills");
						int nuggets = playerstats.getInt("players." + uuid + ".nuggets");
						
						String killsFormat = NumberFormat.getNumberInstance(Locale.US).format(kills);
						String nuggetsFormat = NumberFormat.getNumberInstance(Locale.US).format(nuggets);
						
						sender.sendMessage(linspace);
						sender.sendMessage(playername);
						sender.sendMessage(ChatColor.GRAY + " ▪ Level: " + ChatColor.YELLOW + level);
						sender.sendMessage(ChatColor.GRAY + " ▪ Kills: " + ChatColor.YELLOW + killsFormat);
						sender.sendMessage(ChatColor.GRAY + " ▪ Nuggets: " + ChatColor.YELLOW + nuggetsFormat);
						sender.sendMessage(linspace);
					}
				}
				
				else if (sender instanceof Player) {
					Bukkit.dispatchCommand(sender, "stats " + sender.getName());
				}
			});
			break;
		}
		return true;
	}
}