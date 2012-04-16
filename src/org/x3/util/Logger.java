package org.x3.util;

import java.util.logging.Level;

import org.x3.bukkit.PermissionPlugin;

public class Logger {
	private String o;
	
	public Logger(Class<?> o) {
		this.o = getClassName(o);
	}
	
	public String getClassName(Class<?> o) {
		String[] array = o.getClass().getName().split("\\.");
		if(array.length == 1)
			return array[0];
		return array[array.length - 1];
	}
	
	public void info(Object o) {
		PermissionPlugin.log(Level.INFO, getMessage(o));
	}
	
	public void warn(Object o) {
		PermissionPlugin.log(Level.WARNING, getMessage(o));
	}
	
	public void fatal(Object o) {
		PermissionPlugin.log(Level.SEVERE, getMessage(o));
	}
	
	private String getMessage(Object o) {
		return String.format("[%s] %s", this.o, o.toString());
	}
}
