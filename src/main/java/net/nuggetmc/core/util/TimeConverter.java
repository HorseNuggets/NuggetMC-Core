package net.nuggetmc.core.util;

public class TimeConverter {
	
	public static int stringToInt(String input) {
		String[] args = input.split(" ");
		
		int result = 0;
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
				result = result + (Integer.parseInt(args[i].substring(0, args[i].length() - 1)) * factor);
			} catch (NumberFormatException e) {
				continue;
			}
		}
		return result;
	}
	
	public static String intToString(int input) {
		int minutes = input / 60;
		int secondsRemaining = input % 60;
		
		int hours = minutes / 60;
		int minutesRemaining = minutes % 60;
		
		int days = hours / 24;
		int hoursRemaining = hours % 24;
		
		String sdays = "";
		String shours = "";
		String smin = "";
		String ssec = "";
		
		if (days != 0) {
			sdays = " " + days + "d";
		}
		
		if (hoursRemaining != 0) {
			shours = " " + hoursRemaining + "h";
		}
		
		if (minutesRemaining != 0) {
			smin = " " + minutesRemaining + "m";
		}
		
		if (secondsRemaining != 0) {
			ssec = " " + secondsRemaining + "s";
		}
		
		String result = sdays + shours + smin + ssec;
		return result;
	}
	
	public static String intToStringElongated(int input) {
		int minutes = input / 60;
		int secondsRemaining = input % 60;
		
		int hours = minutes / 60;
		int minutesRemaining = minutes % 60;
		
		int days = hours / 24;
		int hoursRemaining = hours % 24;
		
		String sdays = "";
		String shours = "";
		String smin = "";
		String ssec = "";
		
		if (days != 0) {
			sdays = " " + days + " days,";
		}
		
		if (hoursRemaining != 0) {
			shours = " " + hoursRemaining + " hours,";
		}
		
		if (minutesRemaining != 0) {
			smin = " " + minutesRemaining + " minutes,";
		}
		
		if (secondsRemaining != 0) {
			ssec = " " + secondsRemaining + " seconds";
		}
		
		if (days == 1) sdays = " " + days + " day,";
		if (hoursRemaining == 1) shours = " " + hoursRemaining + " hour,";
		if (minutesRemaining == 1) smin = " " + minutesRemaining + " minute,";
		if (secondsRemaining == 1) ssec = " " + secondsRemaining + " second";
		
		String result = sdays + shours + smin + ssec;
		
		if (result.endsWith(",")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
}