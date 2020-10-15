package net.nuggetmc.core.util;

public class MathTools {
	public static String[] removeElement(String[] arr, int index) {
		if (arr == null || index < 0 || index >= arr.length) {
			return arr;
		}
		String[] anotherArray = new String[arr.length - 1];
		for (int i = 0, k = 0; i < arr.length; i++) {
			if (i == index) {
				continue;
			}
			anotherArray[k++] = arr[i];
		}
		return anotherArray;
	}
}
