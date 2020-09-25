package net.nuggetmc.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import net.nuggetmc.core.Main;
import net.nuggetmc.core.data.ConfigManager;
import net.nuggetmc.core.data.Configs;
import net.nuggetmc.core.util.TimeConverter;

@SuppressWarnings("all")
public class debug implements CommandExecutor {

	private Main plugin;

	public debug(Main plugin) {
		this.plugin = plugin;
	}

	private ConfigManager configManager;
	private TimeConverter timeConverter;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		
		final Scoreboard scoreboard = player.getScoreboard();
		/*for (Objective o : scoreboard.getObjectives()) {
			Bukkit.broadcastMessage(o.getName());
		}*/
		
		/*for (Score s : scoreboard.getScores("stats")) {
			Bukkit.broadcastMessage(ChatColor.GREEN + s.toString());
			Bukkit.broadcastMessage(s.getEntry());
		}*/
		//Objective stats = scoreboard.getObjective(DisplaySlot.SIDEBAR);
		
		for (String entry : scoreboard.getEntries()) {
			Bukkit.broadcastMessage(entry);
		}
		
		return true;
	}
}
