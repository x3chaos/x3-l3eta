package org.x3.util;

import java.util.ArrayList;
import java.util.Date;

import org.l3eta.Database;
import org.l3eta.MiscUtil;
import org.x3.ConfigLoader;
import org.x3.X3Player;
import org.x3.bukkit.permissions.X3Permission;
import org.x3.bukkit.permissions.db.X3Database;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public final class DBUtil {
	private final Database db;
	private final X3Database xdb;
	private final ConfigLoader cl;

	public DBUtil(Database db, X3Database xdb, ConfigLoader cl) {
		this.db = db;
		this.xdb = xdb;
		this.cl = cl;
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

	public void newUser(X3Player player) {
		BasicDBObject user = createUser(player, false);
		if (!hasUser(player)) {
			db.addTo("users", user);
			xdb.users.put(player.getUserID(), user);
			player.sendMessage("Welcome to " + cl.getString("server.name"));
		}
	}

	private BasicDBObject createUser(X3Player player, boolean q) {
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

	// GET PERMISSION GROUPS TODO

	public BasicDBObject[] getUsers() {
		return db.getAll("users");
	}

	public BasicDBObject[] getGroups() {
		return db.getAll("groups");
	}

	public X3Permission[] getUserPermissions(X3Player player) {
		ArrayList<Object> permissions = new ArrayList<Object>();
		BasicDBObject user = null;
		if (xdb.users.containsKey(player.getUserID())) {
			user = xdb.users.get(player.getUserID());
		} else {
			if (hasUser(player)) {
				user = getUser(player);
				xdb.addUser(user);
			}
		}
		permissions = MiscUtil.merge(getPermissions(user.get("permissions")),
				permissions);
		permissions = MiscUtil.merge(
				getGroupPermissions(user.getString("group")), permissions);

		return permissions.toArray(new X3Permission[0]);
	}

	public X3Permission[] getPermissions(Object perm) {
		if (!(perm instanceof BasicDBList)) {
			return null;
		}
		ArrayList<X3Permission> permissions = new ArrayList<X3Permission>();
		BasicDBList list = (BasicDBList) perm;
		for (String p : list.toArray(new String[0])) {
			permissions.add(X3Permission.create(p));
		}
		return permissions.toArray(new X3Permission[0]);
	}

	public X3Permission[] getGroupPermissions(String group) {
		BasicDBObject g = null;
		if (xdb.groups.containsKey(group)) {
			g = xdb.groups.get(group);
		} else {
			if (hasGroup(group)) {
				g = getGroup(group);
				xdb.addGroup(g);
			}
		}
		return getPermissions(g.get("permissions"));
	}

	public void cacheUser(X3Player player) {
		if (hasUser(player)) {
			xdb.users.put(player.getUserID(), getUser(player));
		}
	}

	// TODO go through these and remove or rename them
	public boolean hasUser(X3Player player) {
		return db.hasObject("users", createUser(player, true));
	}

	private BasicDBObject getUser(X3Player player) {
		return db.get("users", createUser(player, true));
	}

	private BasicDBObject getGroup(String name) {
		return db.get("groups", createGroup(name, true));
	}

	public boolean hasGroup(String name) {
		return db.hasObject("groups", createGroup(name, true));
	}

	// Defaults
	public void createDefaultGroup() {
		if (!db.hasCol("groups"))
			db.createCollection("groups");
		if (!hasGroup("default")) {
			BasicDBObject group = new BasicDBObject();
			group.put("name", "default");
			group.put("permissions", X3Permission.generateDefault());
			db.addTo("groups", group);
		}
	}

	public void createDefaultUser() {
		if (!db.hasCol("users"))
			db.createCollection("users");
		BasicDBObject user = new BasicDBObject();
		user.put("userid", "79af2c9048fdaa6eabe928e315627a57c1e23db9");
		if (!db.hasObject("users", user)) {
			user.put("group", "default");
			user.put("created", new Date().toString());
			user.put("permissions", new BasicDBList());
			db.addTo("users", user);
		}
	}
}
