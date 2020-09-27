package net.nuggetmc.core.util;

import java.util.UUID;

import org.bukkit.ChatColor;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public class ColorCodes {
	
	private static LuckPerms api = LuckPermsProvider.get();
	
	public static String rankNameSidebar(UUID uuid) {
		String rank = ChatColor.WHITE + "Default";
		
		LuckPerms api = LuckPermsProvider.get();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "xd":
			rank = ChatColor.YELLOW + "XD";
			break;
		case "xdd":
			rank = ChatColor.GREEN + "XDD";
			break;
		case "xddd":
			rank = ChatColor.AQUA + "XDDD";
			break;
		case "promo":
			rank = ChatColor.DARK_AQUA + "Promo";
			break;
		case "youtube":
			rank = ChatColor.WHITE + "You" + ChatColor.RED + "Tube";
			break;
		case "builder":
			rank = ChatColor.BLUE + "Builder";
			break;
		case "mod":
			rank = ChatColor.DARK_GREEN + "Moderator";
			break;
		case "admin":
			rank = ChatColor.RED + "Admin";
			break;
		case "owner":
			rank = ChatColor.RED + "Owner";
			break;
		}
		return rank;
	}
	
	public static String colorName(UUID uuid, String name) {
		String rank = ChatColor.WHITE + name;
		
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "xd":
			rank = ChatColor.YELLOW + name;
			break;
		case "xdd":
			rank = ChatColor.GREEN + name;
			break;
		case "xddd":
			rank = ChatColor.AQUA + name;
			break;
		case "promo":
			rank = ChatColor.DARK_AQUA + name;
			break;
		case "youtube":
			rank = ChatColor.GOLD + name;
			break;
		case "builder":
			rank = ChatColor.BLUE + name;
			break;
		case "mod":
			rank = ChatColor.DARK_GREEN + name;
			break;
		case "admin":
			rank = ChatColor.RED + name;
			break;
		case "owner":
			rank = ChatColor.RED + name;
			break;
		}
		return rank;
	}
	
	public static String rankNameTag(UUID uuid) {
		String rank = "";
		
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "xd":
			rank = ChatColor.YELLOW + "[" + ChatColor.GOLD + "XD" + ChatColor.YELLOW + "]";
			break;
		case "xdd":
			rank = ChatColor.GREEN + "[" + ChatColor.GOLD + "XDD" + ChatColor.GREEN + "]";
			break;
		case "xddd":
			rank = ChatColor.AQUA + "[" + ChatColor.GOLD + "XDDD" + ChatColor.AQUA + "]";
			break;
		case "promo":
			rank = ChatColor.DARK_AQUA + "[PROMO]";
			break;
		case "youtube":
			rank = ChatColor.GOLD + "[" + ChatColor.WHITE + "YOU" + ChatColor.RED + "TUBE" + ChatColor.GOLD + "]";
			break;
		case "builder":
			rank = ChatColor.BLUE + "[BUILDER]";
			break;
		case "mod":
			rank = ChatColor.DARK_GREEN + "[MOD]";
			break;
		case "admin":
			rank = ChatColor.RED + "[ADMIN]";
			break;
		case "owner":
			rank = ChatColor.RED + "[OWNER]";
			break;
		}
		return rank;
	}
	
	public static String levelToTag(int level) {
		String title = ChatColor.WHITE + "I";
		
		switch (level) {
		case 2:
			title = ChatColor.GREEN + "II";
			break;
		case 3:
			title = ChatColor.AQUA + "III";
			break;
		case 4:
			title = ChatColor.DARK_AQUA + "IV";
			break;
		case 5:
			title = ChatColor.DARK_PURPLE + "V";
			break;
		case 6:
			title = ChatColor.RED + "VI";
			break;
		case 7:
			title = ChatColor.YELLOW + "VII";
			break;
		case 8:
			title = ChatColor.DARK_BLUE + "VIII";
			break;
		case 9:
			title = ChatColor.DARK_RED + "IX";
			break;
		case 10:
			title = ChatColor.BLACK + "X";
			break;
		case 11:
			title = ChatColor.GOLD + "XI";
			break;
		case 12:
			title = ChatColor.LIGHT_PURPLE + "XII";
			break;
		}
		
		return title;
	}
}
