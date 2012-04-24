package org.x3.util;

import java.awt.Event;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.x3.X3Player;
import org.x3.bukkit.permissions.X3Permission;
import org.x3.bukkit.permissions.listeners.PlayerListener;

public class Reflect {
	private static final LoggerUtil log = new LoggerUtil(Reflect.class);
	private static final Class<?> PBC = getBukkitClass("permissions.PermissibleBase");
	private static final Class<?> HUMAN = getBukkitClass("craftbukkit.entity.CraftHumanEntity");
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

	public static void setPermission(X3Player player, String perm) {
		setPermission(player.getPlayer(), perm);
	}

	public static Class<?> getBukkitClass(String c) {
		try {
			return Class.forName("org.bukkit." + c);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setField(Class<?> c, String field, Object o1, Object o2) {
		try {
			Field f = c.getDeclaredField(field);
			f.setAccessible(true);
			f.set(o1, o2);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Object getField(Class<?> c, String field, Object o) {
		try {
			Field f = c.getDeclaredField(field);
			f.setAccessible(true);
			return f.get(o);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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

	public static void insertPermission(X3Player player, X3Permission perm,
			PermissibleBase base) {
		// TODO
	}

	@SuppressWarnings("unchecked")
	public static void setPermission(Player player, String string) {
		PermissibleBase pb = (PermissibleBase) getField(HUMAN, "perm",
				player.getPlayer());
		List<PermissionAttachment> list = (List<PermissionAttachment>) getField(
				PBC, "attachments", pb);
		for (PermissionAttachment pa : list) {
			player.sendMessage(pa.getPermissible() + " " + pa.getPlugin());
		}
		Map<String, PermissionAttachmentInfo> m = (Map<String, PermissionAttachmentInfo>) getField(
				PBC, "permissions", pb);
		for (String key : m.keySet()) {
			PermissionAttachmentInfo k = m.get(key);
			player.sendMessage(k.getPermissible().getClass() + "");
		}
		// TODO
	}
}