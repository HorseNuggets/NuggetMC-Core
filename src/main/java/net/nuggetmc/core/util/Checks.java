package net.nuggetmc.core.util;

import java.util.UUID;

import org.bukkit.entity.Player;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public class Checks {
	
	private static LuckPerms api = LuckPermsProvider.get();
	
	public static boolean checkStaff(Player player) {
		UUID uuid = player.getUniqueId();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "mod":
			return true;
		case "admin":
			return true;
		case "owner":
			return true;
		}
		return false;
	}
	
	public static boolean checkHighStaff(Player player) {
		UUID uuid = player.getUniqueId();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "admin":
			return true;
		case "owner":
			return true;
		}
		return false;
	}
	
	public static boolean checkDef(Player player) {
		UUID uuid = player.getUniqueId();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "default":
			return true;
		}
		return false;
	}
	
	public static boolean cmCheck1(String base) {
		switch (base.toLowerCase()) {
		case "/plugins":
			return true;
		case "/pl":
			return true;
		case "//calc":
			return true;
		case "//calculate":
			return true;
		case "/say":
			return true;
		case "/me":
			return true;
		}
		return false;
	}
	
	public static boolean cmCheck2(String base) {
		switch (base.toLowerCase()) {
		case "/spawn":
			return true;
		case "/warp":
			return true;
		case "/tphere":
			return true;
		case "/home":
			return true;
		case "/h":
			return true;
		case "/tpyes":
			return true;
		case "/tpno":
			return true;
		case "/pv":
			return true;
		case "/vault":
			return true;
		case "/suicide":
			return true;
		case "/tpa":
			return true;
		}
		return false;
	}
}
