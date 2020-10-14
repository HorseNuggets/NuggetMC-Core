package net.nuggetmc.core.commands.admin;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import net.md_5.bungee.api.ChatColor;
import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.setup.WorldManager;
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

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		sender.sendMessage("[main] " + ChatColor.YELLOW + TimeConverter.intToString(WorldManager.count));
		sender.sendMessage("[nether] " + ChatColor.YELLOW + TimeConverter.intToString(WorldManager.countNether));
		sender.sendMessage("[end] " + ChatColor.YELLOW + TimeConverter.intToString(WorldManager.countEnd));
		
		
		
		
		/*List<Integer> nuggetList = new ArrayList<>();
		List<String> nameList = new ArrayList<>();
		String nametemp = "";
		
		for(String key : config.getConfigurationSection("players").getKeys(false)) {
			String name = getName(key);
			
			Bukkit.broadcastMessage(key);
			
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
		
		String dir = plugin.getDataFolder().getAbsoluteFile().toString() + "\\playerdata";
		
		File folder = new File(dir);
		if (!folder.exists()) {
			folder.mkdir();
		}
		
		File fileTxt = new File(dir + "\\" + "lead.txt");
		
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
