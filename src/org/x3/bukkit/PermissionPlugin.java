package org.x3.bukkit;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.l3eta.Database;
import org.x3.bukkit.permissions.X3EventHandler;
import org.x3.bukkit.permissions.commands.AddCommand;
import org.x3.bukkit.permissions.db.X3Database;
import org.x3.bukkit.permissions.listeners.PlayerListener;
import org.x3.bukkit.permissions.util.FileUtil;
import org.x3.bukkit.permissions.util.Reflect;

public class PermissionPlugin extends JavaPlugin {
	private static boolean opOverride = false;
	private static final Logger log = Logger
			.getLogger("Minecraft.X3Permissions");
	private PluginManager pm = Bukkit.getPluginManager();
	private File config = new File(getDataFolder(), "config.yml");
	private X3Database xdb;
	private X3EventHandler xeh;

	@Override
	public void onEnable() {
		Database.init("BukkitPermissionsX66");
		if (!config.exists()) {
			getDataFolder().mkdirs();
			FileUtil.copy(getResourceStream("permissions/data/config.yml"),
					config);
		}
		xdb = new X3Database();
		xeh = new X3EventHandler(xdb);
		Reflect.insertCommand(new AddCommand());
		pm.registerEvents(new PlayerListener(xeh), this);
	}

	public static void log(Level level, Object o) {
		log.log(level, o.toString());
	}

	public static boolean opOverride() {
		return opOverride;
	}

	// Private methods
	private InputStream getResourceStream(String f) {
		return PermissionPlugin.class.getResourceAsStream(f);
	}
}
