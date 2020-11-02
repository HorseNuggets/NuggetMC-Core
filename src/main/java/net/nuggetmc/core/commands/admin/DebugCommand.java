package net.nuggetmc.core.commands.admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.events.FFADeathmatch;
import net.nuggetmc.core.misc.Credits;
import net.nuggetmc.core.misc.ItemEffects;
import net.nuggetmc.core.scoreboard.Sidebar;
import net.nuggetmc.core.util.TimeConverter;

@SuppressWarnings("all")
public class DebugCommand implements CommandExecutor {

	private Main plugin;

	private FileConfiguration config;
	private TimeConverter timeConverter;

	public DebugCommand(Main plugin) {
		this.plugin = plugin;
		this.config = Configs.playerstats.getConfig();
	}
	
	public String getName(String uuid) {
		String url = "https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names";
		try {
			@SuppressWarnings("deprecation")
			String nameJson = IOUtils.toString(new URL(url));
			JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
			String playerSlot = nameValue.get(nameValue.size() - 1).toString();
			JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
			return nameObject.get("name").toString();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	public static void send(Player player, IChatBaseComponent chat) {
		TextComponent message = new TextComponent("lmao ");
		ChatComponentText text = new ChatComponentText("poggers ");
		text.addSibling(chat);
		final ComponentBuilder message2 = new ComponentBuilder("lmao");
	    message2.append(ChatColor.GRAY + "hh");
	    player.spigot().sendMessage(message2.create());
		PacketPlayOutChat packet = new PacketPlayOutChat(text);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public IChatBaseComponent bukkitStackToChatComponent(ItemStack stack) {
	    net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(stack);
	    return nms.C();
	}


	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//Credits.prize((Player) sender);
		Sidebar.enable((Player)sender, (byte) 0);
		//Bukkit.broadcastMessage(ItemEffects.boomBox.toString());
		/*Player player = (Player) sender;
		
		
		
		send(player, bukkitStackToChatComponent(new ItemStack(Material.IRON_SWORD)));*/
		
		
		
		
		/*Player player = Bukkit.getPlayer(args[0]);
		String ip = ((CraftPlayer) player).getHandle().playerConnection.networkManager.getRawAddress().toString();
		String ip2 = ((CraftPlayer) player).getHandle().playerConnection.networkManager.getSocketAddress().toString();
		sender.sendMessage(ip);
		sender.sendMessage(ip2);*/
		
		/*Player player = (Player) sender;
		ItemStack item = player.getInventory().getItemInHand();
		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		
		NBTTagCompound nbt = nmsStack.getTag();
		Bukkit.broadcastMessage(nbt.toString());
		
		String dir = plugin.getDataFolder().getAbsoluteFile().toString() + "\\playerdata";
		
		File folder = new File(dir);
		if (!folder.exists()) {
			folder.mkdir();
		}
		
		File fileTxt = new File(dir + "\\" + "output.txt");
		
		if (!fileTxt.exists()) {
			try {
				fileTxt.createNewFile();
			} catch (IOException e) {
				sender.sendMessage(e.getCause().toString());
			}
		}
		
		FileWriter itemsfw = null;
		try {
			itemsfw = new FileWriter(fileTxt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter itemspw = new PrintWriter(itemsfw);
		
		itemspw.println(nbt.toString());
		
		itemspw.close();*/
		
		
		/*List<Integer> nuggetList = new ArrayList<>();
		List<String> nameList = new ArrayList<>();
		String nametemp = "";
		
		for(String key : config.getConfigurationSection("players").getKeys(false)) {
			nuggetList.add(config.getInt("players." + key + ".nuggets"));
			nameList.add(config.getString("players." + key + ".name"));
		}
		
		int l, j, temp = 0;
		
		for (l = 0; l < nuggetList.size(); l++) {
			for (j = l + 1; j < nuggetList.size(); j++) {
				if (nuggetList.get(l) > nuggetList.get(j)) {
					temp = nuggetList.get(l);
					nametemp = nameList.get(l);
					nuggetList.set(l, nuggetList.get(j));
					nameList.set(l, nameList.get(j));
					nuggetList.set(j, temp);
					nameList.set(j, nametemp);
				}
			}
		}
		
		Collections.reverse(nuggetList);
		Collections.reverse(nameList);
		
		String dir = plugin.getDataFolder().getAbsoluteFile().toString() + "/playerdata";
		
		File folder = new File(dir);
		if (!folder.exists()) {
			folder.mkdir();
		}
		
		File fileTxt = new File(dir + "/" + "lead.txt");
		
		if (!fileTxt.exists()) {
			try {
				fileTxt.createNewFile();
			} catch (IOException e) {
				sender.sendMessage(e.getCause().toString());
			}
		}
		
		FileWriter itemsfw = null;
		try {
			itemsfw = new FileWriter(fileTxt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter itemspw = new PrintWriter(itemsfw);
		
		for (int i = 0; i < nuggetList.size(); i++) {
			itemspw.println("#" + (i + 1) + " - " + nameList.get(i) + ": " + nuggetList.get(i));
		}
		
		itemspw.close();*/
		
		return true;
	}
}
