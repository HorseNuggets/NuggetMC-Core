package net.nuggetmc.core.util;

public class SuperRandom {
	public static double generate() {
		double n = 0;
		for (int i = 0; i < 6; i++) {
			n = Math.random();
		}
		return n;
	}
}
