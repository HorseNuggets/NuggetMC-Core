package net.nuggetmc.core.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.player.PlayerChat;
import net.nuggetmc.core.util.ColorCodes;

public class Credits implements CommandExecutor {
	private static Main plugin;
	private FileConfiguration config;
	private static FileConfiguration promo;
	private String linspace = (ChatColor.GRAY + "--------------------------------------");
	
	public Credits(Main plugin) {
		Credits.plugin = plugin;
		Credits.promo = Configs.promo.getConfig();
		this.config = Configs.playerstats.getConfig();
		zeusSetup();
		zeusMessageSetup();
	}
	
	public static void onJoin(PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			Player player = event.getPlayer();
			UUID uid = player.getUniqueId();
			if (promo.contains("players." + uid)) {
				int credits = promo.getInt("players." + uid + ".credits");
				if (credits >= 5) {
					if (!promo.getBoolean("players." + uid + ".zeus")) {
						promo.set("players." + uid + ".zeus", true);
						Configs.promo.saveConfig();
						
						prize(player);
					}
				}
			}
		});
		return;
	}
	
	private static ItemStack zeus;
	
	private static void zeusSetup() {
		zeus = new ItemStack(Material.BOW);
		zeus.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		ItemMeta zeusMeta = zeus.getItemMeta();
		zeusMeta.setDisplayName("§1Zeus");
		ArrayList<String> zeusLore = new ArrayList<String>();
		zeusLore.add("§7Lightning X");
		zeusMeta.setLore(zeusLore);
		zeus.setItemMeta(zeusMeta);
		return;
	}
	
	private static PacketPlayOutChat packet;
	public static PacketPlayOutChat joinPacket;
	
	private static void zeusMessageSetup() {
		IChatBaseComponent itemMsg = PlayerChat.bukkitStackToChatComponent(zeus);
		ChatComponentText text = new ChatComponentText("You have reached §b5 §rcredits and have earned ");
		text.addSibling(itemMsg);
		ChatComponentText extra = new ChatComponentText("!");
		text.addSibling(extra);
		packet = new PacketPlayOutChat(text);
		
		ChatComponentText text2 = new ChatComponentText("If you can reach §b5 §rcredits before then, you can win ");
		text2.addSibling(itemMsg);
		ChatComponentText extra2 = new ChatComponentText(", a limited-time item you can only get once!");
		text2.addSibling(extra2);
		joinPacket = new PacketPlayOutChat(text2);
		return;
	}
	
	public static void prize(Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
		Bukkit.getScheduler().runTask(plugin, () -> {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " bow 1 0 {display:{Name:\"§1Zeus\",Lore:[\"§7Lightning X\"]},ench:[{id:49,lvl:1}]}");
		});
		return;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			switch (label.toLowerCase()) {
			case "credit":
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (args.length >= 1) {
						UUID playerUUID = player.getUniqueId();
						
						if (!player.getName().equals("HorseNuggets")) {
							if (!config.getBoolean("players." + playerUUID + ".promo")) {
								player.sendMessage("You have already used your credit or have joined too early to give credits.");
								return;
							}
							
							if (args[0].equalsIgnoreCase(player.getName())) {
								player.sendMessage("lmao ur tryna credit urself");
								return;
							}
						}
						
						Player credit = Bukkit.getPlayer(args[0]);
						String playername = null;
						UUID uid = null;
						
						if (credit != null) {
							playername = credit.getName();
							uid = credit.getUniqueId();
						}
						
						else {
							OfflinePlayer disc = Bukkit.getOfflinePlayer(args[0]);
							if (disc != null) {
								playername = disc.getName();
								uid = disc.getUniqueId();
							}
						}
						
						if (playername != null) {
							if (!config.contains("players." + uid)) {
								player.sendMessage(playername + " has never joined the server!");
								return;
							}
							
							int credits = 0;
							if (promo.contains("players." + uid)) {
								credits = promo.getInt("players." + uid + ".credits");
							}
							
							promo.set("players." + uid + ".name", playername);
							promo.set("players." + uid + ".credits", credits + 1);
							
							if (credit != null) {
								String rank = ColorCodes.getOfflineRankName(playerUUID);
								String creditname = ColorCodes.rankNameTagName(rank) + player.getName();
								
								credit.sendMessage("You have been credited by " + creditname + "§f! You now have §b"
										+ (credits + 1) + " §fcredits.");
								credit.playSound(credit.getLocation(), Sound.LEVEL_UP, 1, 1);
								
								if (credits == 4) {
									prize(credit);
									promo.set("players." + uid + ".zeus", true);
								}
							}
							
							Configs.promo.saveConfig();
							
							String rank = ColorCodes.getOfflineRankName(uid);
							playername = ColorCodes.rankNameTagName(rank) + playername;
							
							player.sendMessage("You have credited " + playername + "§r!");
							player.sendMessage(ChatColor.GRAY + " ▪" + ChatColor.WHITE + " Uncommon Nuggets: " + ChatColor.GREEN + "+1");
							config.set("players." + playerUUID + ".promo", null);
							Configs.kitsconfig.saveConfig();
							Bukkit.getScheduler().runTask(plugin, () -> {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName()
										+ " gold_nugget 1 0 {display:{Name:\"§aUncommon Nugget§r\",Lore:[\"§7A pretty fine piece "
										+ "of§r\",\"§7nugget if you ask me.§r\"]},ench:[{id:51,lvl:1}],HideFlags:1}");
							});
							
							sort();
						}
					}
					
					else {
						player.sendMessage("§cToo few arguments!");
						player.sendMessage("§cUsage: /credit <player>");
					}
				}
				return;
			case "promo":
				sender.sendMessage(linspace);
				
				if (!promo.contains("players")) {
					sender.sendMessage("The leaderboard is currently empty!");
					sender.sendMessage(linspace);
					return;
				}
				
				sender.sendMessage("TOP 10:");
				
				for (int i = 1; i <= 10; i++) {
					
					String space = " ";
					if (i == 10) {
						space = "";
					}
					
					if (promo.contains("lead." + i)) {
						String name = promo.getString("lead." + i + ".name");
						String credits = promo.getString("lead." + i + ".credits");
						
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
						
						String s = "s";
						
						if (credits.equals("1")) {
							s = "";
						}
						
						String output = "§e #" + i + space + " §7▪ §f" + playername + " §7- §b" + credits + " credit" + s;
						sender.sendMessage(output);
					}
					
					else {
						String output = "§e #" + i + space + " §7▪ ";
						sender.sendMessage(output);
					}
				}
				
				if (sender instanceof Player) {
					Player player = (Player) sender;
					for (int i = 1; i <= promo.getConfigurationSection("lead").getKeys(false).size(); i++) {
						if (promo.getString("lead." + i + ".name").equals(player.getName())) {
							sender.sendMessage("");
							sender.sendMessage("Your placement:");
							
							String credits = promo.getString("lead." + i + ".credits");
							
							String s = "s";
							
							if (credits.equals("1")) {
								s = "";
							}
							
							String space = " ";
							if (i == 10) {
								space = "";
							}
							
							UUID uid = player.getUniqueId();
							String rank = ColorCodes.getOfflineRankName(uid);
							String playername = ColorCodes.rankNameTagName(rank) + player.getName();
							
							String output = "§e #" + i + space + " §7▪ §f" + playername + " §7- §b" + credits + " credit" + s;
							sender.sendMessage(output);
						}
					}
				}
				sender.sendMessage(linspace);
				return;
			}
		});
		return true;
	}
	
	public void sort() {
		Map<String, Integer> stats = new HashMap<>();
		for(String key : promo.getConfigurationSection("players").getKeys(false)) {
			stats.put(promo.getString("players." + key + ".name"), promo.getInt("players." + key + ".credits"));
		}
		
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(stats.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		
		int size = list.size();
		
		for (int i = 0; i < size; i++) {
			String[] entries = list.get(i).toString().split("=");
			promo.set("lead." + (size - i) + ".name", entries[0]);
			promo.set("lead." + (size - i) + ".credits", entries[1]);
		}
		
		Configs.promo.saveConfig();
		return;
	}
}
