package net.nuggetmc.core.player;

import java.util.UUID;

import org.bukkit.entity.Player;

public class Levelup {
	
	public int check(Player player, String playername, UUID uuid, int kills) {
		int newlevel = 0;
		switch (kills) {
		case 12:
			newlevel = 2;
			break;
		case 56:
			newlevel = 3;
			break;
		case 114:
			newlevel = 4;
			break;
		case 440:
			newlevel = 5;
			break;
		case 880:
			newlevel = 6;
			break;
		case 1140:
			newlevel = 7;
			break;
		case 1860:
			newlevel = 8;
			break;
		case 2520:
			newlevel = 9;
			break;
		case 3740:
			newlevel = 10;
			break;
		case 5880:
			newlevel = 11;
			break;
		case 7320:
			newlevel = 12;
			break;
		}
		return newlevel;
	}
}
