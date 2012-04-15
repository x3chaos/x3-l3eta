package org.x3.bukkit.permissions.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.x3.bukkit.permissions.db.DBHelper;

import com.mongodb.BasicDBObject;

public class Reflect {
	private static final Logger log = new Logger(Reflect.class);

	private static final PluginManager pm = Bukkit.getPluginManager();
	private static final SimplePluginManager spm = (SimplePluginManager) pm;
	private static SimpleCommandMap commandMap;

	static {
		commandMap = getCommandMap();
	}

	public static void insertCommand(Command command) {
		commandMap.register("x3Permission", command);
	}

	public static BasicDBObject[] getPluginCommands() {
		ArrayList<BasicDBObject> commands = new ArrayList<BasicDBObject>();
		try {
			for (Command c : commandMap.getCommands()) {
				commands.add(DBHelper.createCommand("/" + c.getName()));
			}
			return commands.toArray(new BasicDBObject[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static SimpleCommandMap getCommandMap() {
		try {
			Field cm = spm.getClass().getDeclaredField("commandMap");
			cm.setAccessible(true);
			return (SimpleCommandMap) cm.get(spm);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		log.fatal("Could not load commandMap");
		return null;
	}
}
