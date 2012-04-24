package org.x3.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import org.x3.bukkit.testing.SpawnCommand;
import org.x3.util.Reflect;

import com.topcat.npclib.NPCManager;

public class TestingPlugin extends JavaPlugin {
	private static NPCManager manager;

	public void onEnable() {
		this.manager = new NPCManager(this);
		Reflect.insertCommand(new SpawnCommand("spawnhuman", this.manager));
	}

	public static NPCManager getManager() {
		return manager;
	}
}
