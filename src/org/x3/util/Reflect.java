package org.x3.util;

import java.awt.Event;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.x3.bukkit.permissions.listeners.PlayerListener;

public class Reflect {
	private static final LoggerUtil log = new LoggerUtil(Reflect.class);

	private static final PluginManager pm = Bukkit.getPluginManager();
	private static final SimplePluginManager spm = (SimplePluginManager) pm;
	private static SimpleCommandMap commandMap;

	static {
		commandMap = getCommandMap();
	}

	public static void insertCommand(Command command) {
		commandMap.register("x3Permission", command);
	}

	public static String[] getCommands() {
		ArrayList<String> commands = new ArrayList<String>();
		for (Command c : commandMap.getCommands()) {
			commands.add("/" + c.getName());
		}
		return commands.toArray(new String[0]);
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

	public static String[] getPLEvents() {
		ArrayList<String> events = new ArrayList<String>();
		String[] bad = { "EntityTargetLivingEntityEvent",
				"PlayerDropItemEvent", "PlayerItemHeldEvent",
				"PlayerPickupItemEvent", "PlayerJoinEvent", "PlayerKickEvent",
				"PlayerQuitEvent" };
		for (Method m : PlayerListener.class.getMethods()) {
			if (m.getParameterTypes().length > 0) {
				if (m.getParameterTypes()[0].isAssignableFrom(Event.class)) {
					boolean allowed = false;
					String c = m.getParameterTypes()[0].getName();
					c = c.replaceFirst("org.bukkit.event.([a-zA-Z]+).", "");
					for (String b : bad) {
						if (b.equals(c)) {
							allowed = false;
							break;
						} else {
							allowed = true;
						}
					}
					if (allowed)
						events.add(c);
				}
			}
		}
		return events.toArray(new String[0]);
	}
}