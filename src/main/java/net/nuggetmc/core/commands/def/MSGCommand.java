package net.nuggetmc.core.commands.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.player.PlayerChat;
import net.nuggetmc.core.util.Checks;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.MathTools;
import net.nuggetmc.core.util.TimeConverter;

public class MSGCommand implements CommandExecutor {
	
	private Main plugin;
	private FileConfiguration mutes;
	private FileConfiguration ignore;
	private Map<Player, Player> replies;
	private String linspace = (ChatColor.GRAY + "--------------------------------------");
	
	public MSGCommand(Main plugin) {
		this.plugin = plugin;
		this.mutes = Configs.mutes.getConfig();
		this.ignore = Configs.ignore.getConfig();
		this.replies = new HashMap<>();
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				Player player = (Player) sender;
				switch (label.toLowerCase()) {
				case "msg":
					if (args.length >= 2) {
						Player to = Bukkit.getPlayer(args[0]);
						if (to == null) {
							String name = args[0];
							OfflinePlayer offTo = Bukkit.getOfflinePlayer(name);
							if (offTo != null) {
								name = offTo.getName();
							}
							
							player.sendMessage(ChatColor.RED + name + " is not online!");
						}
						
						else {
							String[] argsShort = MathTools.removeElement(args, 0);
							message(player, to, argsShort);
						}
					}
					
					else {
						sender.sendMessage(ChatColor.RED + "Usage: /msg <player> <message>");
					}
					break;
					
				case "r":
					if (replies.containsKey(player)) {
						Player to = replies.get(player);
						if (to.isOnline()) {
							message(player, to, args);
							return;
						}
						
						else {
							player.sendMessage(ChatColor.RED + to.getName() + " is not online!");
						}
					}
					
					else {
						player.sendMessage(ChatColor.RED + "You have no one to reply to!");
					}
					break;
					
				case "ignore":
					if (args.length >= 1) {
						UUID uuid = player.getUniqueId();
						List<String> ignorelist = new ArrayList<>();
						try {
							ignorelist = ignore.getStringList(uuid.toString());
						} catch (NullPointerException e) {}
						
						Player igPlayer = Bukkit.getPlayer(args[0]);
						String igPlayerName = args[0];
						UUID uuidIgnore = null;
						
						if (igPlayer != null) {
							igPlayerName = igPlayer.getName();
							uuidIgnore = igPlayer.getUniqueId();
							if (uuidIgnore == uuid) {
								player.sendMessage("lol y u tryna ignore urself");
								return;
							}
						}
						
						else {
							OfflinePlayer igOfflinePlayer = Bukkit.getOfflinePlayer(args[0]);
							if (igOfflinePlayer != null) {
								igPlayerName = igOfflinePlayer.getName();
								uuidIgnore = igOfflinePlayer.getUniqueId();
							}
						}
						
						if (uuidIgnore != null) {
							if (Checks.checkStaffUUID(uuidIgnore)) {
								player.sendMessage(ChatColor.RED + "You can't ignore staff!");
								return;
							}
							
							if (!ignorelist.contains(uuidIgnore.toString())) {
								
								String name = Configs.playerstats.getConfig().getString("players." + uuidIgnore + ".name");
								if (name == null) {
									player.sendMessage(ChatColor.GOLD + igPlayerName + ChatColor.YELLOW + " has never joined the server!");
									return;
								}
								
								ignorelist.add(uuidIgnore.toString());
								ignore.set(uuid.toString(), ignorelist);
								Configs.ignore.saveConfig();
								player.sendMessage(ChatColor.YELLOW + "You have ignored " + ChatColor.GOLD + igPlayerName + ChatColor.YELLOW + ".");
							} else {
								player.sendMessage(ChatColor.YELLOW + "You already have " + ChatColor.GOLD + igPlayerName + ChatColor.YELLOW + " ignored!");
							}
						}
						
						else {
							sender.sendMessage(ChatColor.RED + igPlayerName + " does not exist!");
						}
					}
					
					else {
						player.sendMessage(ChatColor.RED + "Usage: /ignore <player>");
					}
					break;
					
				case "unignore":
					if (args.length >= 1) {
						UUID uuid = player.getUniqueId();
						List<String> ignorelist = new ArrayList<>();
						try {
							ignorelist = ignore.getStringList(uuid.toString());
						} catch (NullPointerException e) {}
						
						Player igPlayer = Bukkit.getPlayer(args[0]);
						String igPlayerName = args[0];
						UUID uuidIgnore = null;
						
						if (igPlayer != null) {
							igPlayerName = igPlayer.getName();
							uuidIgnore = igPlayer.getUniqueId();
							if (uuidIgnore == uuid) {
								player.sendMessage("bruh are ya stupid");
								return;
							}
						}
						
						else {
							OfflinePlayer igOfflinePlayer = Bukkit.getOfflinePlayer(args[0]);
							if (igOfflinePlayer != null) {
								igPlayerName = igOfflinePlayer.getName();
								uuidIgnore = igOfflinePlayer.getUniqueId();
							}
						}
						
						if (uuidIgnore != null) {
							if (ignorelist.contains(uuidIgnore.toString())) {
								ignorelist.remove(uuidIgnore.toString());
								ignore.set(uuid.toString(), ignorelist);
								Configs.ignore.saveConfig();
								player.sendMessage(ChatColor.YELLOW + "You have unignored " + ChatColor.GOLD + igPlayerName + ChatColor.YELLOW + ".");
							} else {
								player.sendMessage(ChatColor.YELLOW + "You never had " + ChatColor.GOLD + igPlayerName + ChatColor.YELLOW + " ignored!");
							}
						}
						
						else {
							sender.sendMessage(ChatColor.RED + igPlayerName + " does not exist!");
						}
					}
					
					else {
						player.sendMessage(ChatColor.RED + "Usage: /unignore <player>");
					}
					break;
					
				case "ignorelist":
					if (args.length >= 1) {
						if (args[0].equals("clear")) {
							ignore.set(player.getUniqueId().toString(), null);
							player.sendMessage(ChatColor.YELLOW + "You ignore list has been cleared!");
							Configs.ignore.saveConfig();
							return;
						}
					}
					
					player.sendMessage(linspace);
					player.sendMessage("Your ignored players:");
					for (String key : ignore.getStringList(player.getUniqueId().toString())) {
						String name = Configs.playerstats.getConfig().getString("players." + key + ".name");
						if (name != null) {
							player.sendMessage(ChatColor.GRAY + " ▪ " + ChatColor.YELLOW + name);
						}
					}
					player.sendMessage(linspace);
				}
			});
		}
		return true;
	}
	
	private void message(Player player, Player to, String[] args) {
		UUID uuid = player.getUniqueId();
		if (mutes.contains(uuid.toString())) {
			int remaining = (int) (mutes.getLong(uuid + ".exp") - (System.currentTimeMillis() / 1000));
			if (remaining <= 0) {
				plugin.muteCommand.unmute(uuid, null, player.getName());
			} else {
				player.sendMessage(ChatColor.RED + "You are server muted for the next"
						+ ChatColor.YELLOW + TimeConverter.intToStringElongated(remaining) + ChatColor.RED + ".");
				return;
			}
		}
		
		String msg = "";
		
		for (int i = 0; i < args.length; i++)
			msg = msg + " " + args[i];
		
		if (!Checks.checkStaff(player)) {
			if (PlayerChat.msgTime.containsKey(player)) {
				Long difference = PlayerChat.msgTime.get(player) - System.currentTimeMillis() / 1000;
				if (difference > 0) {
					String s = "s";
					if (difference == 1) s = "";
					player.sendMessage(ChatColor.RED + "You are chatting too fast. Try again in " + ChatColor.YELLOW
							+ difference + ChatColor.RED + " second" + s + ".");
					return;
				}
				
				else {
					PlayerChat.msgTime.remove(player);
				}
			}
			
			if (PlayerChat.msgPrev.containsKey(player)) {
				if (msg.equals(PlayerChat.msgPrev.get(player))) {
					if (PlayerChat.msgPrevTime.containsKey(player)) {
						Long difference = PlayerChat.msgPrevTime.get(player) - System.currentTimeMillis() / 1000;
						if (difference > 0) {
							player.sendMessage(ChatColor.RED + "You can't say the same message twice!");
							return;
						}
						
						else {
							PlayerChat.msgPrevTime.remove(player);
						}
					}
				}
			}
			
			for (String i : PlayerChat.filter) {
				if (msg.toLowerCase().replaceAll(" ", "").contains(i)) {
					player.sendMessage(ChatColor.YELLOW + "Omg that's racist!!!!1 1");
					return;
				}
			}
		}
		
		String name = to.getName();
		
		replies.put(player, to);
		player.sendMessage(ChatColor.YELLOW + "(To " + ChatColor.RESET + ColorCodes.rankNameTag(to.getUniqueId()) + name + ChatColor.YELLOW + ")" + ChatColor.YELLOW + msg);
		
		if (!ignore.getStringList(to.getUniqueId().toString()).contains(uuid.toString())) {
			replies.put(to, player);
			to.sendMessage(ChatColor.YELLOW + "(From " + ChatColor.RESET + ColorCodes.rankNameTag(uuid) + player.getName() + ChatColor.YELLOW + ")" + ChatColor.YELLOW + msg);
		}
		
		if (!Checks.checkXDDD(player)) {
			PlayerChat.msgTime.put(player, 2 + System.currentTimeMillis() / 1000);
			PlayerChat.msgPrevTime.put(player, 30 + System.currentTimeMillis() / 1000);
			PlayerChat.msgPrev.put(player, msg);
		}
		return;
	}
}
