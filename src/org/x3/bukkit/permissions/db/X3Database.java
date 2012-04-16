package org.x3.bukkit.permissions.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.l3eta.Database;
import org.x3.X3Player;
import org.x3.util.Logger;

import com.mongodb.BasicDBObject;

/**
 * 
 * @author l3eta
 * @about This class is so it does not always have to pull from the database
 */
public class X3Database {
	private final static Logger log = new Logger(X3Database.class);
	private HashMap<String, BasicDBObject> groups;
	private HashMap<String, BasicDBObject> users;
	private ArrayList<X3Player> players;

	public X3Database() {
		players = new ArrayList<X3Player>();
		groups = new HashMap<String, BasicDBObject>();
		users = new HashMap<String, BasicDBObject>();
		initDatabase();
	}

	private void initDatabase() {
		try {
			BasicDBObject[] groups = Database.getAllFrom("groups");
			if (groups == null)
				Database.addTo("groups", createDefaultGroup());
			BasicDBObject[] users = Database.getAllFrom("users");
			if (users == null)
				Database.addTo("users", createDefaultUser());
			log.info("Loading Groups");
			for (BasicDBObject group : groups) {
				this.groups.put(group.getString("name"), group);
			}
			log.info("Loaded " + this.groups.size() + " Groups");
			log.info("Loading Users");
			for (BasicDBObject user : users) {
				this.users.put(user.getString("userid"), user);
			}
			log.info("Loaded " + this.users.size() + " Users");
		} catch (Exception ex) {
			ex.printStackTrace();
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

	// Defaults
	private BasicDBObject createDefaultGroup() {

		return null;
	}

	private Object createDefaultUser() {
		// TODO Auto-generated method stub
		return null;
	}

}
