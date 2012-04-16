package org.x3.bukkit.permissions;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.Event;

public final class X3Permission {
	private static HashMap<String, X3Permission> cache = new HashMap<String, X3Permission>();
	private final String permission;

	private X3Permission(String permission) {
		this.permission = permission;
	}

	private static String getPermission(PermissionType type, Object o) {
		if(o instanceof Event) {
			String env = camel(((Event) o).getEventName());
			if(type == PermissionType.EVENT) {
				return "event." + env;
			}
		} else if(o instanceof Material) {
			String mat = low(o.toString());
			if(type == PermissionType.ITEM) {
				return "item." + mat;
			}
		} else if(o instanceof String) {			
			if(type == PermissionType.COMMAND) {
				String command = getCommand(o.toString());
				return "command." + command;
			} else if(type == PermissionType.EVENT) {
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

	private static String getCommand(String string) {
		int index = 0;
		while (string.charAt(index) == '/') {
			index++;
		}
		return index + "." + string.substring(index);
	}

	private static String camel(Object o) {
		String string = o.toString();
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	private static String low(Object o) {
		return o.toString().toLowerCase();
	}
}
