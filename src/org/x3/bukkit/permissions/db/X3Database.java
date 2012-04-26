package org.x3.bukkit.permissions.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.l3eta.Database;
import org.x3.ConfigLoader;
import org.x3.X3Player;
import org.x3.bukkit.permissions.X3Permission;
import org.x3.util.DBUtil;
import org.x3.util.LoggerUtil;

import com.mongodb.BasicDBObject;

/**
 * 
 * @author l3eta
 * @about This class is so it does not always have to pull from the database
 */
public class X3Database {
	private final DBUtil dbu;
	private final static LoggerUtil log = new LoggerUtil(X3Database.class);
	public HashMap<String, BasicDBObject> groups;
	public HashMap<String, BasicDBObject> users;
	private ArrayList<X3Player> players;

	public X3Database(String dbname, ConfigLoader cl, boolean firstRun) {
		this.dbu = new DBUtil(new Database(dbname), this, cl);
		if (firstRun) {
			dbu.createDefaultGroup();
			dbu.createDefaultUser();
		}
		players = new ArrayList<X3Player>();
		groups = new HashMap<String, BasicDBObject>();
		users = new HashMap<String, BasicDBObject>();
		initDatabase();
		initPermissions();
		for(Player p : Bukkit.getOnlinePlayers()) {
			X3Player x3p = getPlayer(p);
			if(x3p == null) {
				addPlayer(new X3Player(p));
			} else {
				System.out.println("Derp");
				//TODO update 
			}
		}
	}
	

	private void initPermissions() {
		for(String perm : X3Permission.generateDefault().toArray(new String[0])) {
			Bukkit.getPluginManager().addPermission(new Permission(perm));	
		}
	}

	private void initDatabase() {
		try {
			log.info("Loading Groups");
			for (BasicDBObject group : dbu.getGroups()) {
				this.groups.put(group.getString("name"), group);
			}
			log.info("Loaded " + this.groups.size() + " Groups");
			log.info("Loading Users");
			for (BasicDBObject user : dbu.getUsers()) {
				this.users.put(user.getString("userid"), user);
			}
			log.info("Loaded " + this.users.size() + " Users");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setPermissions(X3Player player) {
		player.setPermissions(dbu.getUserPermissions(player));
	}

	private boolean hasUser(X3Player player) {
		return users.containsKey(player.getUserID());
	}

	public void addPlayer(X3Player player) {
		if (!players.contains(player)) {
			players.add(player);
			if (!hasUser(player)) {
				if (!dbu.hasUser(player)) {
					dbu.newUser(player);
					setPermissions(player);
				} else {
					dbu.cacheUser(player);
					// TODO maybe a welcome back message?
				}
			} else {
				setPermissions(player);
				// TODO ^ Above todo
			}
		}
	}

	public void removePlayer(X3Player player) {
		if (players.contains(player)) {
			players.remove(player);
		}
	}

	public void addGroup(BasicDBObject group) {
		if (group.containsField("name") && group.containsField("permissions")) {
			groups.put(group.getString("name"), group);
		}
	}

	public void addUser(BasicDBObject user) {
		if (user.containsField("permissions") && user.containsField("userid")
				&& user.containsField("group")) {
			users.put(user.getString("userid"), user);
		}
	}

	public X3Player getPlayer(Player player) {
		return getPlayerByUID(X3Player.makeUID(player));
	}

	public X3Player getPlayerByUID(String uid) {
		for (X3Player player : players.toArray(new X3Player[0])) {
			if (player.getUserID().equals(uid)) {
				return player;
			}
		}
		return null;
	}

	public void reload() {
		// TODO
	}
}
