package org.x3.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtil {
	private String o;
	private Logger log = Logger.getLogger("Minecraft.x3Permissions");

	public LoggerUtil(Class<?> o) {
		this.o = getClassName(o);
	}

	public String getClassName(Class<?> o) {
		String[] array = o.getName().split("\\.");
		if (array.length == 1)
			return array[0];
		return array[array.length - 1];
	}

	private void log(Level level, String o) {
		log.log(level, o);
	}

	public void info(Object o) {
		log(Level.INFO, getMessage(o));
	}

	public void warn(Object o) {
		log(Level.WARNING, getMessage(o));
	}

	public void fatal(Object o) {
		log(Level.SEVERE, getMessage(o));
	}

	private String getMessage(Object o) {
		return String.format("[%s] %s", this.o, o.toString());
	}
}
