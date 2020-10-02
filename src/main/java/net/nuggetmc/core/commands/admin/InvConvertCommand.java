package net.nuggetmc.core.commands.admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;
import net.nuggetmc.core.util.ItemSerializers;

public class InvConvertCommand implements CommandExecutor {
	
	private Main plugin;

	public InvConvertCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length >= 2) {
				switch (args[0]) {
				case "get":
					if (args.length >= 3) {
						ConfigManager configManager = plugin.configs.get(args[1]);
						FileConfiguration config = configManager.getConfig();
						
						ItemStack[] items = ItemSerializers.stringToItems(config.getString(args[2] + ".items"));
						ItemStack[] armor = ItemSerializers.stringToItems(config.getString(args[2] + ".armor"));
						
						player.getInventory().setContents(items);
						player.getInventory().setArmorContents(armor);
						return true;
					}
				case "set":
					String itemsString = ItemSerializers.itemsToString(player.getInventory().getContents());
					String armorString = ItemSerializers.itemsToString(player.getInventory().getArmorContents());
					
					String dir = plugin.getDataFolder().getAbsoluteFile().toString() + "\\" + args[1];
					
					File folder = new File(dir);
					if (!folder.exists()) {
						folder.mkdir();
					}
					
					File itemsTxt = new File(dir + "\\" + "items.txt");
					File armorTxt = new File(dir + "\\" + "armor.txt");
					
					if (!itemsTxt.exists()) {
						try {
							itemsTxt.createNewFile();
						} catch (IOException e) {
							sender.sendMessage(e.getCause().toString());
						}
					}
					
					if (!armorTxt.exists()) {
						try {
							armorTxt.createNewFile();
						} catch (IOException e) {
							sender.sendMessage(e.getCause().toString());
						}
					}
					
					try {
						FileWriter itemsfw = new FileWriter(itemsTxt);
						PrintWriter itemspw = new PrintWriter(itemsfw);
						itemspw.println(itemsString);
						itemspw.close();
					} catch (IOException e) {
						sender.sendMessage(e.getCause().toString());
					}
					
					try {
						FileWriter armorfw = new FileWriter(armorTxt);
						PrintWriter armorpw = new PrintWriter(armorfw);
						armorpw.println(armorString);
						armorpw.close();
					} catch (IOException e) {
						sender.sendMessage(e.getCause().toString());
					}
					
					sender.sendMessage("Successfully created directory " + args[1] + " with serialized ItemStack data.");
					return true;
				}
			}
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Usage: /invconvert <get/set> <file> (path)");
		}
		return true;
	}
}