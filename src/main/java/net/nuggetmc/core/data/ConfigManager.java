package net.nuggetmc.core.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.nuggetmc.core.Main;

public class ConfigManager {
	
	private Main plugin;
	
	public FileConfiguration fileConfig;
	public File file;
	
	public ConfigManager(Main plugin) {
		this.plugin = plugin;
	}
	
	public void setup(String path) {
		
		String yml = path;
		ArrayList<String> dir = null;
		
		if (path.contains("/")) {
		
			String[] pathParts = path.split(Pattern.quote("/"));
			dir = new ArrayList<String>(Arrays.asList(pathParts));
			dir.remove(dir.size() - 1);
			
			yml = pathParts[pathParts.length - 1];
			
		}
		
		if (dir != null) {
			for (int i = 0; i < dir.size(); i++) {
			}
		}
		
		String absoluteDir = plugin.getDataFolder().getAbsoluteFile().toString();
		
		if (dir != null) {
			for (int i = 0; i < dir.size(); i++) {
				absoluteDir = absoluteDir + "/" + dir.get(i);
				File folder = new File(absoluteDir);
				if (!folder.exists()) {
					folder.mkdir();
				}
			}
		}
		
		file = new File(absoluteDir + "/" + yml);
		
		if (!file.exists()) {
			try {
				try {
					FileUtils.copyInputStreamToFile(plugin.getResource("resources/"
							+ path.replaceAll(Pattern.quote("/"), "/")), new File(absoluteDir + "/" + yml));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IllegalArgumentException | NullPointerException e) {
				try {
					file.createNewFile();
				} catch (IOException f) {
					f.printStackTrace();
				}
			}
		}
		
		fileConfig = YamlConfiguration.loadConfiguration(file);
		return;
	}
	
	public FileConfiguration getConfig() {
		return fileConfig;
	}
	
	public void saveConfig() {
		try {
			fileConfig.save(file);
		}
		catch (IOException | NullPointerException | ConcurrentModificationException e) {
			Bukkit.getConsoleSender().sendMessage("Failed to save data storage file.");
		}
		return;
	}
	
	public void reloadConfig() {
		fileConfig = YamlConfiguration.loadConfiguration(file);
		return;
	}
}
