package org.x3.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;

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

	public static String[] getPluginCommands() {
		ArrayList<String> commands = new ArrayList<String>();
		try {
			for (Command c : commandMap.getCommands()) {
				commands.add("/" + c.getName());
			}
			return commands.toArray(new String[0]);
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
