package org.x3.bukkit.permissions;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.Event;

public final class X3Permission {
	private static HashMap<String, X3Permission> cache = new HashMap<String, X3Permission>();
	private final String permission;

	public X3Permission(String permission) {
		this.permission = permission;
	}

	public String toString() {
		return this.permission;
	}

	// Defaults
	public static X3Permission defaultEvent(Event event) {
		return create("default.event." + camel(event.getEventName()));
	}

	public static X3Permission defaultItem(Material m) {
		return create("default.item." + low(m));
	}
	
	public static X3Permission defaultCommand(String command) {
		return create("default.command." + getCommand(command));
	}
	
	// Permissions
	
	public static X3Permission makeEvent(Event event) {
		return create("event." + camel(event.getEventName()));
	}
	
	public static X3Permission makeCommand(String command) {
		return create("command." + getCommand(command));
	}

	public static X3Permission makeItem(Material m) {
		return create("item." + low(m));
	}
	
	// Helpers
	private static X3Permission create(String perm) {
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
