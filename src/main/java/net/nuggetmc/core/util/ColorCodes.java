package net.nuggetmc.core.util;

import java.util.UUID;

import org.bukkit.ChatColor;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.nuggetmc.core.data.Configs;

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
			rank = ChatColor.DARK_AQUA + "Promoter";
			break;
		case "twitch":
			rank = ChatColor.DARK_PURPLE + "Twitch";
			break;
		case "youtube":
			rank = ChatColor.GOLD + "YouTube";
			break;
		case "builder":
			rank = ChatColor.BLUE + "Builder";
			break;
		case "spoon":
			rank = ChatColor.BLUE + "Spoon";
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
	
	public static String getRankName(UUID uuid) {
		LuckPerms api = LuckPermsProvider.get();
		User user = api.getUserManager().getUser(uuid);
		return user.getPrimaryGroup();
	}
	
	public static String getOfflineRankName(UUID uuid) {
		String rank = Configs.playerstats.getConfig().getString("players." + uuid + ".rank");
		if (rank != null) {
			return rank;
		}
		return "default";
	}
	
	public static String colorName(UUID uuid, String name) {
		String rank = ChatColor.WHITE + name;
		
		User user = api.getUserManager().getUser(uuid);
		if (user == null) return rank;
		
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
		case "twitch":
			rank = ChatColor.DARK_PURPLE + name;
			break;
		case "youtube":
			rank = ChatColor.GOLD + name;
			break;
		case "builder":
			rank = ChatColor.BLUE + name;
			break;
		case "spoon":
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
		String rank = ChatColor.WHITE + "";
		
		if (uuid != null) {
			User user = api.getUserManager().getUser(uuid);
			switch (user.getPrimaryGroup()) {
			case "xd":
				rank = ChatColor.YELLOW + "[" + ChatColor.GOLD + "XD" + ChatColor.YELLOW + "] ";
				break;
			case "xdd":
				rank = ChatColor.GREEN + "[" + ChatColor.GOLD + "XDD" + ChatColor.GREEN + "] ";
				break;
			case "xddd":
				rank = ChatColor.AQUA + "[" + ChatColor.GOLD + "XDDD" + ChatColor.AQUA + "] ";
				break;
			case "promo":
				rank = ChatColor.DARK_AQUA + "[PROMO] ";
				break;
			case "twitch":
				rank = ChatColor.DARK_PURPLE + "[TWITCH] ";
				break;
			case "youtube":
				rank = ChatColor.GOLD + "[YOUTUBE] ";
				break;
			case "builder":
				rank = ChatColor.BLUE + "[BUILDER] ";
				break;
			case "spoon":
				rank = ChatColor.BLUE + "[SPOON] ";
				break;
			case "mod":
				rank = ChatColor.DARK_GREEN + "[MOD] ";
				break;
			case "admin":
				rank = ChatColor.RED + "[ADMIN] ";
				break;
			case "owner":
				rank = ChatColor.RED + "[OWNER] ";
				break;
			}
		}
		return rank;
	}
	
	public static String rankNameTagName(String input) {
		String rank = ChatColor.WHITE + "";
		switch (input) {
		case "xd":
			rank = ChatColor.YELLOW + "[" + ChatColor.GOLD + "XD" + ChatColor.YELLOW + "] ";
			break;
		case "xdd":
			rank = ChatColor.GREEN + "[" + ChatColor.GOLD + "XDD" + ChatColor.GREEN + "] ";
			break;
		case "xddd":
			rank = ChatColor.AQUA + "[" + ChatColor.GOLD + "XDDD" + ChatColor.AQUA + "] ";
			break;
		case "promo":
			rank = ChatColor.DARK_AQUA + "[PROMO] ";
			break;
		case "twitch":
			rank = ChatColor.DARK_PURPLE + "[TWITCH] ";
			break;
		case "youtube":
			rank = ChatColor.GOLD + "[YOUTUBE] ";
			break;
		case "builder":
			rank = ChatColor.BLUE + "[BUILDER] ";
			break;
		case "spoon":
			rank = ChatColor.BLUE + "[SPOON] ";
			break;
		case "mod":
			rank = ChatColor.DARK_GREEN + "[MOD] ";
			break;
		case "admin":
			rank = ChatColor.RED + "[ADMIN] ";
			break;
		case "owner":
			rank = ChatColor.RED + "[OWNER] ";
			break;
		}
		return rank;
	}
	
	public static String rankNameColorName(String input) {
		String rank = "§f";
		switch (input) {
		case "xd":
			rank = "§e";
			break;
		case "xdd":
			rank = "§a";
			break;
		case "xddd":
			rank = "§b";
			break;
		case "promo":
			rank = "§3";
			break;
		case "twitch":
			rank = "§5";
			break;
		case "youtube":
			rank = "§6";
			break;
		case "builder":
			rank = "§9";
			break;
		case "spoon":
			rank = "§9";
			break;
		case "mod":
			rank = "§2";
			break;
		case "admin":
			rank = "§c";
			break;
		case "owner":
			rank = "§c";
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
	
	public static String levelToName(int level) {
		String title = ChatColor.WHITE + "Novice";
		
		switch (level) {
		case 2:
			title = ChatColor.GREEN + "Initiate";
			break;
		case 3:
			title = ChatColor.AQUA + "Knight";
			break;
		case 4:
			title = ChatColor.DARK_AQUA + "Rogue";
			break;
		case 5:
			title = ChatColor.DARK_PURPLE + "Barbarian";
			break;
		case 6:
			title = ChatColor.RED + "Warrior";
			break;
		case 7:
			title = ChatColor.YELLOW + "Paladin";
			break;
		case 8:
			title = ChatColor.DARK_BLUE + "Gladiator";
			break;
		case 9:
			title = ChatColor.DARK_RED + "Battleborn";
			break;
		case 10:
			title = ChatColor.BLACK + "Annihilator";
			break;
		case 11:
			title = ChatColor.GOLD + "Emperor";
			break;
		case 12:
			title = ChatColor.LIGHT_PURPLE + "cereal killer";
			break;
		}
		
		return title;
	}
}
