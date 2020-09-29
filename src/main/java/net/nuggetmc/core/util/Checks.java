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
}
