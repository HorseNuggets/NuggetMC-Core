package net.nuggetmc.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.Configs;
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

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Bukkit.dispatchCommand(player, "join tournament");
		}
		return true;
	}
}
