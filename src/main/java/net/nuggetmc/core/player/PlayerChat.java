package net.nuggetmc.core.player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.Checks;
import net.nuggetmc.core.util.ColorCodes;
import net.nuggetmc.core.util.TimeConverter;

public class PlayerChat {
	
	private Main plugin;
	private FileConfiguration mutes;
	
	public static List<String> filter;
	public static Map<Player, Long> msgTime;
	public static Map<Player, String> msgPrev;
	public static Map<Player, Long> msgPrevTime;
	
	public PlayerChat(Main plugin) {
		this.plugin = plugin;
		this.mutes = Configs.mutes.getConfig();
		PlayerChat.msgTime = new HashMap<>();
		PlayerChat.msgPrev = new HashMap<>();
		PlayerChat.msgPrevTime = new HashMap<>();
		this.setupFilter();
	}
	
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		event.setCancelled(true);
		
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
		
		String message = event.getMessage();
		
		if (!Checks.checkStaff(player)) {
			if (msgTime.containsKey(player)) {
				Long difference = msgTime.get(player) - System.currentTimeMillis() / 1000;
				if (difference > 0) {
					String s = "s";
					if (difference == 1) s = "";
					player.sendMessage(ChatColor.RED + "You are chatting too fast. Try again in " + ChatColor.YELLOW
							+ difference + ChatColor.RED + " second" + s + ".");
					return;
				}
				
				else {
					msgTime.remove(player);
				}
			}
			
			if (msgPrev.containsKey(player)) {
				if (message.equals(msgPrev.get(player))) {
					if (msgPrevTime.containsKey(player)) {
						Long difference = msgPrevTime.get(player) - System.currentTimeMillis() / 1000;
						if (difference > 0) {
							player.sendMessage(ChatColor.RED + "You can't say the same message twice!");
							return;
						}
						
						else {
							msgPrevTime.remove(player);
						}
					}
				}
			}
			
			for (String i : filter) {
				if (message.toLowerCase().replaceAll(" ", "").contains(i)) {
					player.sendMessage(ChatColor.YELLOW + "Omg that's racist!!!!1 1");
					return;
				}
			}
		}

		String playername = player.getName();
		String raw = message;
		String start = "";
		
		start = playername + ChatColor.WHITE + ": ";
		
		int playerkillsnum = Configs.playerstats.getConfig().getInt("players." + uuid + ".kills");
		PlayerStats.allign(player, uuid, playerkillsnum);
		int playerlevel = Configs.playerstats.getConfig().getInt("players." + uuid + ".level");
		
		start = ChatColor.DARK_GRAY + "[" + ColorCodes.levelToTag(playerlevel) + ChatColor.DARK_GRAY + "] " + ColorCodes.rankNameTag(uuid) + start;
		message = start + message;
		
		/*
		 * [TODO]
		 * Later add tags before the :
		 */
		
		if (Checks.checkXD(player)) {
			message = message.replaceAll("&", "§");
			if (message.contains("[i]")) {
				
				ChatComponentText text = null;
				ItemStack item = player.getItemInHand();
				
				if (!(item == null || item.getType() == Material.AIR)) {
					
					IChatBaseComponent itemMsg = bukkitStackToChatComponent(item);
					message += "-";
					
					String[] parts = message.split(Pattern.quote("[i]"));
					text = new ChatComponentText(parts[0]);
					
					for (int i = 1; i < parts.length; i++) {
						text.addSibling(itemMsg);
						if (i == parts.length - 1) {
							String trimmed = parts[i].substring(0, parts[i].length() - 1);
							ChatComponentText extra = new ChatComponentText(trimmed);
							text.addSibling(extra);
						}
					}
				}
				
				else {
					text = new ChatComponentText(message);
				}
				
				PacketPlayOutChat packet = new PacketPlayOutChat(text);
				
				if (Checks.checkStaff(player)) {
					if (raw.startsWith("#")) {
						raw = raw.substring(1);
						if (raw.startsWith(" ")) {
							raw = raw.substring(1);
						}
						if (!raw.equals("")) {
							staffChat(player, start, raw);
							return;
						}
					}
					
					for (Player all : Bukkit.getOnlinePlayers()) {
						((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
					}
				}
				
				else {
					for (Player all : Bukkit.getOnlinePlayers()) {
						if (!Configs.ignore.getConfig().getStringList(all.getUniqueId().toString()).contains(uuid.toString())) {
							((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
						}
					}
					
					if (!Checks.checkPog(player)) {
						msgTime.put(player, 2 + System.currentTimeMillis() / 1000);
						msgPrevTime.put(player, 30 + System.currentTimeMillis() / 1000);
						msgPrev.put(player, raw);
					}
				}
				
				Bukkit.getConsoleSender().sendMessage(message);
				return;
			}
		}
		
		if (Checks.checkStaff(player)) {
			if (raw.startsWith("#")) {
				raw = raw.substring(1);
				if (raw.startsWith(" ")) {
					raw = raw.substring(1);
				}
				if (!raw.equals("")) {
					staffChat(player, start, raw);
					return;
				}
			}
			
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(message);
			}
		}
		
		else {
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (!Configs.ignore.getConfig().getStringList(all.getUniqueId().toString()).contains(uuid.toString())) {
					all.sendMessage(message);
				}
			}
			
			if (!Checks.checkPog(player)) {
				msgTime.put(player, 2 + System.currentTimeMillis() / 1000);
				msgPrevTime.put(player, 30 + System.currentTimeMillis() / 1000);
				msgPrev.put(player, raw);
			}
		}
		
		Bukkit.getConsoleSender().sendMessage(message);
		return;
	}
	
	private void setupFilter() {
		PlayerChat.filter = Arrays.asList("nigga", "nigger");
		return;
	}
	
	private void staffChat(Player player, String start, String message) {
		Bukkit.broadcast(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "STAFF" + ChatColor.DARK_GRAY + "] "
				+ ChatColor.RESET + start + ChatColor.AQUA + message, "nmc.staff");
		return;
	}
	
	public static IChatBaseComponent bukkitStackToChatComponent(ItemStack stack) {
	    net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(stack);
	    return nms.C();
	}
}