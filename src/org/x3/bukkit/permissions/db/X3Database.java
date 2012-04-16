package org.x3.bukkit.permissions.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.l3eta.Database;
import org.x3.X3Player;
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
	private HashMap<String, BasicDBObject> groups;
	private HashMap<String, BasicDBObject> users;
	private ArrayList<X3Player> players;

	public X3Database(String dbname) {
		this.dbu = new DBUtil(new Database(dbname));
		players = new ArrayList<X3Player>();
		groups = new HashMap<String, BasicDBObject>();
		users = new HashMap<String, BasicDBObject>();
		initDatabase();
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
			//ex.printStackTrace();
		}
	}

	public void addPlayer(X3Player player) {
		if (!players.contains(player)) {
			players.add(player);
		}
	}

	public void removePlayer(X3Player player) {
		if (players.contains(player)) {
			players.remove(player);
		}
	}

	public X3Player getPlayer(Player player) {
		return getPlayerByUID(X3Player.makeUID(player));
	}

	public X3Player getPlayerByUID(String uid) {
		for(X3Player player : players.toArray(new X3Player[0])) {
			if(player.getUserID().equals(uid)) {
				return player;
			}
		}
		return null;
	}

	
	
	

}
