package net.nuggetmc.core.util;

public class TimeConverter {
	public static int stringToInt(String input) {
		String[] args = input.split(" ");
		
		int finalTimeSec = 0;
		for (int i = 0; i < args.length; i++) {
			
			int factor = 0;
			switch (args[i].charAt(args[i].length() - 1)) {
			case 's':
				factor = 1;
				break;
			case 'm':
				factor = 60;
				break;
			case 'h':
				factor = 3600;
				break;
			case 'd':
				factor = 86400;
				break;
			}
			
			try {
				finalTimeSec = finalTimeSec + (Integer.parseInt(args[i].substring(0, args[i].length() - 1)) * factor);
			} catch (NumberFormatException e) {
				continue;
			}
		}
		return finalTimeSec;
	}
	
	/*
	 * 
	 * [ARCHIVE]
	 * 
	 * 
	 * public int stringToInt(String input) {
		
		String[] args = input.split(" ");
		
		for (int i = 0; i < args.length; i++) {
			Bukkit.broadcastMessage(ChatColor.GREEN + args[i]);
		}
		
		String TimeR = " 1d";
		int TimeSeconds = 86400;
			
		int FINALTIMESEC = 0;
		for (int i = 0; i < args.length; i++) {
			if (args[i].endsWith("s")) {
				String TS = args[i].substring(0, args[i].length() - 1);
				int parsesec = Integer.parseInt(TS);
				FINALTIMESEC = FINALTIMESEC + parsesec;
			}
			if (args[i].endsWith("m")) {
				String TS = args[i].substring(0, args[i].length() - 1);
				int parsesec = Integer.parseInt(TS) * 60;
				FINALTIMESEC = FINALTIMESEC + parsesec;
			}
			if (args[i].endsWith("h")) {
				String TS = args[i].substring(0, args[i].length() - 1);
				int parsesec = Integer.parseInt(TS) * 3600;
				FINALTIMESEC = FINALTIMESEC + parsesec;
			}
			if (args[i].endsWith("d")) {
				String TS = args[i].substring(0, args[i].length() - 1);
				int parsesec = Integer.parseInt(TS) * 3600 * 24;
				FINALTIMESEC = FINALTIMESEC + parsesec;
			}
		}
		
		TimeSeconds = FINALTIMESEC;
		
		int TimeRemaining = TimeSeconds;
		
		int seconds = TimeRemaining;
		int minutes = seconds / 60;
		int secr = seconds % 60;
		int hours = minutes / 60;
		int minr = minutes % 60;
		int days = hours / 24;
		int hoursr = hours % 24;
		
		String FINAL = days + "d " + hoursr + "h " + minr + "m " + secr + "s";
		
		String sdays = "";
		String shours = "";
		String smin = "";
		String ssec = "";
		
		if (days != 0) {
			sdays = " " + days + "d";
		}
		
		if (hoursr != 0) {
			shours = " " + hoursr + "h";
		}
		
		if (minr != 0) {
			smin = " " + minr + "m";
		}
		
		if (secr != 0) {
			ssec = " " + secr + "s";
		}
		
		TimeR = sdays + shours + smin + ssec;
		return TimeSeconds;
	}*/
}
