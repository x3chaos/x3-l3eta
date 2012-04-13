package org.x3.bukkit.permissions.db;

import java.util.ArrayList;
import java.util.Date;

import org.l3eta.Database;
import org.x3.bukkit.permissions.Util;
import org.x3.bukkit.permissions.X3Permission;
import org.x3.bukkit.permissions.X3Player;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class DBHelper {
	
	public static BasicDBObject createCommand(String cmd) {
		int slashs = Util.getLastIndex(cmd, '/');
		return createCommand(cmd.substring(slashs -1), slashs, true);
	}
	
	public static BasicDBObject createCommand(BasicDBObject obj) {
		return createCommand(obj.getString("name"), obj.getInt("slashs"), true);
	}
	
	public static BasicDBObject createCommand(String cmd, int slashs,
			boolean q) {
		
		BasicDBObject command = new BasicDBObject();
		command.put("name", cmd);
		command.put("slashs", slashs);
		if (!q) {
			command.put("permission", X3Permission.DEFAULT);
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
	
	public static BasicDBObject createUser(X3Player player, boolean q) {
		BasicDBObject user = new BasicDBObject();
		user.put("userid", player.getUserID());
		
		if(!q) {
			user.put("created", new Date().toString());
			user.put("permissions", new BasicDBList());
			//TODO add anything for DB Players
		}
		
		return user;
	}

	public static boolean inList(BasicDBList list, Object o) {
		return list.contains(o);
	}

	public static void addCommand(BasicDBObject command) {
		if(!Database.hasObject("commands", command)) {
			Database.addTo("commands", command);
		} else {
			//TODO make update methood
		}
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
	
	
	
	// GET PERMISSION GROUPS
	
	public static String[] getPermissions(X3Player player) {
		return getPermissions(player.getUserID());
	}
	
	private static String[] getPermissions(String userid) {
		return new String[] { };
	}
	
	
	
}
