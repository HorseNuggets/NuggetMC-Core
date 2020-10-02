package net.nuggetmc.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.nuggetmc.core.modifiers.gheads.util.TextureProfileField;

public class HeadCommand implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		TextureProfileField TextureProfileField = new TextureProfileField();
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.getInventory().addItem(TextureProfileField.headPlayer(player));
			}
		}
		else {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Player fetch = Bukkit.getPlayer(args[0]);
				if (fetch != null) {
					if (args.length == 1) {
						player.getInventory().addItem(TextureProfileField.headPlayer(fetch));
					}
					else {
						Player reciever = Bukkit.getPlayer(args[1]);
						if (reciever != null) {
							player.getInventory().addItem(TextureProfileField.headPlayer(fetch));
						}
					}
				}
				else {
					OfflinePlayer offplayer = Bukkit.getServer().getOfflinePlayer(args[0]);
					ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
					SkullMeta headMeta = (SkullMeta) head.getItemMeta();
					headMeta.setOwner(offplayer.getName());
					head.setItemMeta(headMeta);
					
					if (args.length >= 2) {
						Player reciever = Bukkit.getPlayer(args[1]);
						if (reciever != null) {
							reciever.getInventory().addItem(head);
						}
					}
					else {
						player.getInventory().addItem(head);
					}
				}
			}
		}
		return true;
	}
}