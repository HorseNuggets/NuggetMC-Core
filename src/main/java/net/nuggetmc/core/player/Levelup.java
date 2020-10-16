package net.nuggetmc.core.player;

public class Levelup {
	
	public int check(int kills) {
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
		case 1440:
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
	
	public static int checkKills(int level) {
		int kills = 0;
		switch (level) {
		case 2:
			kills = 12;
			break;
		case 3:
			kills = 56;
			break;
		case 4:
			kills = 114;
			break;
		case 5:
			kills = 440;
			break;
		case 6:
			kills = 880;
			break;
		case 7:
			kills = 1440;
			break;
		case 8:
			kills = 1860;
			break;
		case 9:
			kills = 2520;
			break;
		case 10:
			kills = 3740;
			break;
		case 11:
			kills = 5880;
			break;
		case 12:
			kills = 7320;
			break;
		}
		return kills;
	}
}
