package org.x3.bukkit.permissions;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.x3.util.Reflect;

import com.mongodb.BasicDBList;

public final class X3Permission {
	private static HashMap<String, X3Permission> cache = new HashMap<String, X3Permission>();
	private final String permission;

	private X3Permission(String permission) {
		this.permission = permission;
	}

	private static String getPermission(PermissionType type, Object o) {
		if (o instanceof Event) {
			String env = camel(((Event) o).getEventName());
			if (type == PermissionType.EVENT) {
				return "event." + env;
			}
		} else if (o instanceof Material) {
			String mat = low(o.toString());
			if (type == PermissionType.ITEM) {
				return "item." + mat;
			}
		} else if (o instanceof String) {
			if (type == PermissionType.EVENT) {
				return "event." + camel(o.toString());
			}
		}
		return null;
	}

	public enum PermissionType {
		EVENT, ITEM, COMMAND;
	}

	public String toString() {
		return this.permission;
	}

	// Helpers
	public static X3Permission create(PermissionType type, Object permission) {
		String perm = getPermission(type, permission);
		if (!cache.containsKey(perm)) {
			X3Permission xp = new X3Permission(perm);
			cache.put(perm, xp);
			return xp;
		}
		return cache.get(perm);
	}

	public static X3Permission create(String permission) {
		if (!cache.containsKey(permission)) {
			X3Permission xp = new X3Permission(permission);
			cache.put(permission, xp);
			return xp;
		}
		return cache.get(permission);
	}

	private static String camel(Object o) {
		String string = o.toString();
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	private static String low(Object o) {
		return o.toString().toLowerCase();
	}

	private static boolean canAddMat(Material m) {
		switch (m) {
		case BEDROCK:
		case LAVA_BUCKET:
		case LAVA:
		case FIRE:
		case TNT:
			return false;
		default:
			return true;
		}
	}

	private static boolean opOnly(String cmd) {
		String[] opOnly = { "/save-on", "/reload", "/deop", "/op", "/xp",
				"/rl", "/save-off", "/stop", "/tp", "/time", "/pardon-ip",
				"/timings", "/pardon", "/kick", "/gamemode", "/whitelist",
				"/toggledownfall", "/give", "/ban-ip", "/ban" };
		for (String op : opOnly) {
			if (cmd.equalsIgnoreCase(op))
				return true;
		}
		return false;
	}

	public static BasicDBList generateDefault() {
		BasicDBList default_permissions = new BasicDBList();
		for (Material mat : Material.values()) {
			if (canAddMat(mat))
				default_permissions.add(create(PermissionType.ITEM, mat)
						.toString());
		}
		for (String e : Reflect.getPLEvents()) {
			default_permissions.add(create(PermissionType.EVENT, e).toString());
		}
		
		return default_permissions;
	}

}
