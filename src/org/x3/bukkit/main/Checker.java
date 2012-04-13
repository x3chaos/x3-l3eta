package org.x3.bukkit.main;

import java.lang.reflect.Method;

import org.bukkit.event.Event;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.x3.bukkit.permissions.listeners.PlayerListener;

public class Checker {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		PlayerListener x = new PlayerListener();
		for (Method m : x.getClass().getMethods()) {
			System.out.println(m.getName());
			if (m.getParameterTypes().length > 0) {
				Class<?> c = m.getParameterTypes()[0];
				
				derp((Class<? extends Event>) c);
			}

		}
	}

	private static Class<? extends Event> derp(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
            	System.out.println(clazz.getName());
                return derp(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName());
            }
        }
    }

}
