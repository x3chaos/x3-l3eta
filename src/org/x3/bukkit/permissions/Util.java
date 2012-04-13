package org.x3.bukkit.permissions;

import org.bukkit.entity.Player;
import org.l3eta.Crypto;

public class Util {

	public static String makeUID(Player player) {
		return Crypto.sha(player.getName() + getIP(player), "x3server", 0);
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

	public static String complete(String string, int index) {
		return complete(string.split(" "), index);
	}

}
