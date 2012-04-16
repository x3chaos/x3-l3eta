package org.x3.bukkit.permissions;

import java.util.HashMap;

public class X3Command {
	private static HashMap<String, X3Command> cache = new HashMap<String, X3Command>();

	private boolean opOnly = false;
	private int slashs;
	private String command;

	private X3Command(String command, boolean opOnly) {
		this.slashs = getSlash(command);
		this.command = command.substring(slashs);
		this.opOnly = opOnly;
	}

	private int getSlash(String string) {
		int index = 0;
		while (string.charAt(index) == '/') {
			index++;
		}
		return index;
	}

	public static X3Command create(String command, boolean opOnly) {
		if (!cache.containsKey(command)) {
			X3Command cmd = new X3Command(command, opOnly);
			cache.put(command, cmd);
			return cmd;
		}
		return cache.get(command);
	}

	public boolean isOpCommand() {
		return this.opOnly;
	}

	public int getSlashs() {
		return this.slashs;
	}

	public String getCommand() {
		return this.command;
	}

	public static String toPermission(String string) {
		X3Command cmd = create(string, false);
		return cmd.getSlashs() + "." + cmd.getCommand();
	}
}
