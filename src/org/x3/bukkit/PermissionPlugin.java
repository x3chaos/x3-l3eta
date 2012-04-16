package org.x3.bukkit;

import java.io.File;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.x3.bukkit.permissions.X3EventHandler;
import org.x3.bukkit.permissions.commands.AddCommand;
import org.x3.bukkit.permissions.db.X3Database;
import org.x3.bukkit.permissions.listeners.PlayerListener;
import org.x3.util.FileUtil;
import org.x3.util.Reflect;

public class PermissionPlugin extends JavaPlugin {
	
	private PluginManager pm = Bukkit.getPluginManager();
	private File config;
	private X3Database xdb;
	private X3EventHandler xeh;

	@Override
	public void onEnable() {
		config = new File(getRealDataFolder(), "config.yml");
		if (!config.exists()) {
			getRealDataFolder().mkdirs();
			FileUtil.copy(getResourceStream("permissions/data/config.yml"),
					config);
		} else {
			loadConfig();
		}
		xdb = new X3Database("BukkitPermissionsX66");
		xeh = new X3EventHandler(xdb);
		Reflect.insertCommand(new AddCommand());
		pm.registerEvents(new PlayerListener(xeh), this);
	}

	public void loadConfig() {

	}

	// Private methods
	private InputStream getResourceStream(String f) {
		return PermissionPlugin.class.getResourceAsStream(f);
	}

	private File getRealDataFolder() {
		return new File(System.getProperty("user.dir") + "/"
				+ getDataFolder().getPath());
	}
}
