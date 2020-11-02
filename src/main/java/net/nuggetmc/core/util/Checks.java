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
	
	public static boolean checkStaffUUID(UUID uuid) {
		User user = api.getUserManager().getUser(uuid);
		if (user != null) {
			switch (user.getPrimaryGroup()) {
			case "mod":
				return true;
			case "admin":
				return true;
			case "owner":
				return true;
			}
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
	
	public static boolean checkXD(Player player) {
		UUID uuid = player.getUniqueId();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "default":
			return false;
		}
		return true;
	}
	
	public static boolean checkXDD(Player player) {
		UUID uuid = player.getUniqueId();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "default":
			return false;
		case "xd":
			return false;
		}
		return true;
	}
	
	public static boolean checkXDDD(Player player) {
		UUID uuid = player.getUniqueId();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "default":
			return false;
		case "xd":
			return false;
		case "xdd":
			return false;
		}
		return true;
	}
	
	public static boolean checkXDDDD(Player player) {
		UUID uuid = player.getUniqueId();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "default":
			return false;
		case "xd":
			return false;
		case "xdd":
			return false;
		case "xddd":
			return false;
		}
		return true;
	}
	
	public static boolean checkSpoon(Player player) {
		UUID uuid = player.getUniqueId();
		User user = api.getUserManager().getUser(uuid);
		
		switch (user.getPrimaryGroup()) {
		case "spoon":
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
		case "/shop":
			return true;
		case "/join":
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
	
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
}
