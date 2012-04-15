package org.x3.bukkit.permissions.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.l3eta.Database;
import org.x3.bukkit.permissions.X3Player;
import org.x3.bukkit.permissions.util.Logger;
import org.x3.bukkit.permissions.util.Util;

import com.mongodb.BasicDBObject;

/**
 * 
 * @author l3eta
 * @about This class is so it does not always have to pull from the database
 */
public class X3Database {
	private final static Logger log = new Logger(X3Database.class);
	private HashMap<String, BasicDBObject> commands;
	private HashMap<String, BasicDBObject> groups;
	private HashMap<String, BasicDBObject> users;
	private ArrayList<X3Player> players;

	public X3Database() {
		players = new ArrayList<X3Player>();
		commands = new HashMap<String, BasicDBObject>();
		groups = new HashMap<String, BasicDBObject>();
		users = new HashMap<String, BasicDBObject>();		
		initDatabase();
	}
	
	private void initDatabase() {
		try {
			BasicDBObject[] groups = Database.getAllFrom("groups");
			if(groups == null)
				Database.addTo("groups", createDefaultGroup());
			BasicDBObject[] users = Database.getAllFrom("users");
			BasicDBObject[] commands = Database.getAllFrom("commands");
			
			log.info("Loading Groups");
			for(BasicDBObject group : groups) {
				this.groups.put(group.getString("name"), group);
			}
			log.info("Loaded " + this.groups.size() + " Groups");
			log.info("Loading Users");
			for(BasicDBObject user : users) {
				this.users.put(user.getString("userid"), user);
			}
			log.info("Loaded " + this.users.size() + " Users");
			log.info("Loading Commands");
			for(BasicDBObject command : commands) {
				this.commands.put(Util.makeCommand(command), command);
			}
			log.info("Loaded " + this.commands.size() + " Commands");
		} catch(Exception ex) {
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

	public X3Player getPlayer(FindType type, Object value) {
		for (X3Player player : players.toArray(new X3Player[0])) {
			if (type == FindType.NAME) {
				if(value instanceof String) {
					if (player.getName().equals(value)) {
						return player;
					}
				} else {
					Util.throwError(new Exception("Cannot get player by Non-String"));
					return null;
				}				
			} else if (type == FindType.USERID) {
				if(value instanceof Player) {
					return getPlayer(FindType.USERID, Util.makeUID(value));
				} else if(value instanceof String) {
					if (player.getUserID().equals(value)) {
						return player;
					}
				} else {
					Util.throwError(new Exception("Cannot get player by Non-String | Non-Player"));
					return null;
				}				
			}
		}
		return null;
	}
	
	
	//Defaults
	private BasicDBObject createDefaultGroup() {
		
		
		return null;
	}
	
	//Enums
	
	public enum FindType {
		NAME, USERID;// TODO add more if needed.
	}
	
}
