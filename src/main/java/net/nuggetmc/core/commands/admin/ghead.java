package net.nuggetmc.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.nuggetmc.core.modifiers.gheads.util.TextureProfileField;

public class ghead implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		TextureProfileField TextureProfileField = new TextureProfileField();
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.getInventory().addItem(TextureProfileField.headGold());
			}
		}
		else {
			Player player = Bukkit.getPlayer(args[0]);
			if (player != null) {
				player.getInventory().addItem(TextureProfileField.headGold());
			}
		}
		return true;
	}
}
