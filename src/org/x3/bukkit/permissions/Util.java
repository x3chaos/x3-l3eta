package org.x3.bukkit.permissions;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.l3eta.Crypto;
import org.l3eta.Database;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class Util {

	public static String makeUID(Player player) {
		return Crypto.sha(player.getName() + getIP(player), "x3server", 0);
	}

	public static int getLastIndex(String string, char c) {
		//TODO Rename this method.
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

	public static class DB {

		public static BasicDBObject createCommand(String cmd, int slashs,
				boolean q) {
			BasicDBObject command = new BasicDBObject();
			command.put("name", cmd);
			command.put("slashs", slashs);
			if (!q) {
				command.put("permission", X3Permission.DEFAULT);
				command.put("userids", new BasicDBList());
				command.put("group", "unassigned");
			}
			return command;
		}

		public static BasicDBObject createGroup(String name, boolean q) {
			BasicDBObject group = new BasicDBObject();
			group.put("name", name);
			if (!q) {
				group.put("userids", new BasicDBList());
				group.put("permissions", new BasicDBList());
			}
			// Append to groups as needed.
			return group;
		}

		public static boolean inList(BasicDBList list, Object o) {
			return list.contains(o);
		}

		public static boolean hasPermission(Player player, BasicDBObject obj) {
			if (Database.hasObject("permission", obj)) {
				
			}
			return false;
		}

		public static void addCommand(BasicDBObject command) {
			//TODO
		}

		public static String[] checkGroups(String userid) {
			ArrayList<String> permissions = new ArrayList<String>();
			for (BasicDBObject obj : Database.getAllFrom("groups")) {
				BasicDBList list = (BasicDBList) obj.get("userids");
				if (inList(list, userid)) {
					for (Object o : (BasicDBList) obj.get("permissions")) {
						permissions.add(o.toString());
					}
				}
			}
			return permissions.toArray(new String[0]);
		}

	}
}
