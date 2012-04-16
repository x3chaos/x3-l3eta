package org.x3.util;

import java.util.Date;

import org.l3eta.Database;
import org.x3.X3Player;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class DBUtil {
	private final Database db;

	public DBUtil(Database db) {
		this.db = db;
	}

	public BasicDBObject createGroup(String name, boolean q) {
		BasicDBObject group = new BasicDBObject();
		group.put("name", name);
		if (!q) {
			group.put("permissions", new BasicDBList());
		}
		// Append to groups as needed.
		return group;
	}

	public BasicDBObject createUser(X3Player player, boolean q) {
		BasicDBObject user = new BasicDBObject();
		user.put("userid", player.getUserID());
		if (!q) {
			user.put("group", "default");
			user.put("created", new Date().toString());
			user.put("permissions", new BasicDBList());
			// Add fields here for players
		}
		return user;
	}

	public boolean inList(BasicDBList list, Object o) {
		return list.contains(o);
	}

	// GET PERMISSION GROUPS TODO

	public BasicDBObject[] getUsers() {
		return db.getAllFrom("users");
	}

	public BasicDBObject[] getGroups() {
		return db.getAllFrom("groups");
	}

	public String[] getUserPermissions(X3Player player) {

		return null;
	}

	public String[] getGroupPermissions(String group) {

		return null;
	}

	// Defaults
	private BasicDBObject createDefaultGroup() {
		BasicDBObject group = new BasicDBObject();
		group.put("name", "default");
		
		return null;
	}

	private BasicDBObject createDefaultUser() {
		return null;
	}

}
