package net.nuggetmc.core.economy;

import java.util.Arrays;
import java.util.List;

public class KitCosts {
	
	public static List<String> kitList = Arrays.asList(
			"swordsman",
			"brute",
			"scout",
			"apprentice",
			"horseman",
			"essence",
			"jouster",
			"enderman",
			"assassin",
			"pyro",
			"creeper",
			"samurai",
			"templar",
			"prestige",
			"valkyrie",
			"champion",
			"witherman",
			"sans",
			"thanos",
			"spoon");
	
	public static int cost(String kit) {
		kit = kit.toLowerCase();
		switch (kit) {
		case "swordsman":
			return 0;
		case "brute":
			return 0;
		case "scout":
			return 0;
		case "apprentice":
			return 12;
		case "horseman":
			return 12;
		case "essence":
			return 18;
		case "jouster":
			return 18;
		case "enderman":
			return 24;
		case "assassin":
			return 24;
		case "pyro":
			return 28;
		case "creeper":
			return 28;
		case "samurai":
			return 36;
		case "templar":
			return 36;
		case "prestige":
			return 42;
		case "valkyrie":
			return 42;
		case "champion":
			return 64;
		case "witherman":
			return 72;
		case "sans":
			return 164;
		case "thanos":
			return 420;
		case "spoon":
			return 1333;
		}
		return 0;
	}
	
	public static int requiredLevel(String kit) {
		kit = kit.toLowerCase();
		switch (kit) {
		case "swordsman":
			return 1;
		case "brute":
			return 1;
		case "scout":
			return 1;
		case "apprentice":
			return 1;
		case "horseman":
			return 2;
		case "essence":
			return 2;
		case "jouster":
			return 2;
		case "enderman":
			return 3;
		case "assassin":
			return 3;
		case "pyro":
			return 3;
		case "creeper":
			return 4;
		case "samurai":
			return 4;
		case "templar":
			return 4;
		case "prestige":
			return 5;
		case "valkyrie":
			return 5;
		case "champion":
			return 5;
		case "witherman":
			return 6;
		case "sans":
			return 7;
		case "thanos":
			return 8;
		case "spoon":
			return 9;
		}
		return 1;
	}
}
