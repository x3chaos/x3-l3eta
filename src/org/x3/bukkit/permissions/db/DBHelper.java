package org.x3.bukkit.permissions.db;

import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Material;
import org.l3eta.Database;
import org.x3.X3Player;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class DBHelper {

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
			user.put("groups", new BasicDBList());
			user.put("created", new Date().toString());
			user.put("permissions", new BasicDBList());
			//TODO add anything for DB Players
		}
		
		return user;
	}
	
	public static BasicDBObject createBlock(Material m, boolean q) {
		BasicDBObject block = new BasicDBObject();
		block.put("mat", m.toString().toLowerCase());
		if(!q) {
			block.put("permission", "" + m.toString().toLowerCase());
		}
		return block;
	}

	public static boolean inList(BasicDBList list, Object o) {
		return list.contains(o);
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
	//TODO
	public static String[] getPermissions(X3Player player) {
		return getPermissions(player.getUserID());
	}
	
	private static String[] getPermissions(String userid) {
		return new String[] { };
	}
	
	
	
}
