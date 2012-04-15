package org.x3.bukkit.permissions.util;

import org.bukkit.entity.Player;
import org.l3eta.Crypto;

import com.mongodb.BasicDBObject;

/**
 * 
 * @author l3eta
 * @about The Util class for my lazy ass.
 * 
 */

@Deprecated
public class Util {
	// Pending Removal / Move.
	private final static Logger log = new Logger(Util.class);

	public static String makeUID(Object o) {
		if (o instanceof Player) {
			Player player = (Player) o;
			return Crypto.sha(player.getName() + getIP(player), "x3server", 0);
		} else {
			throwError(new Exception("Cannot create UID by Non-Player"));
			return null;
		}

	}

	public static int getLastIndex(String string, char c) {
		// TODO Rename this method.
		int index = 0;
		while (string.charAt(index) == c) {
			index++;
		}
		return index;
	}

	public static String getIP(Player player) {
		return player.getAddress().getAddress().getHostAddress();
	}

	public static String complete(String[] array, int index) {
		String str = "";
		for (int i = index; i < array.length; i++) {
			str += array[i] + " ";
		}
		return str.trim();
	}

	public static String[] sliceArray(String[] array, int index) {
		String[] o = new String[array.length - index];
		for (int i = index; i < array.length; i++) {
			o[i - index] = array[i];
		}
		return o;
	}

	public static String complete(String string, int index) {
		return complete(string.split(" "), index);
	}

	// Tools / Util

	public static String makeCommand(BasicDBObject command) {
		String c = "";
		for (int i = 0; i < command.getInt("slashs"); i++)
			c += "/";
		return c + command.getString("name");
	}

	public static void throwError(Exception exception) {
		try {
			throw exception;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
