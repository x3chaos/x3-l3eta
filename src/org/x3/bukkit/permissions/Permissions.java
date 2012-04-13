package org.x3.bukkit.permissions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.l3eta.Database;
import org.x3.bukkit.permissions.listeners.PlayerListener;

public class Permissions extends JavaPlugin {
	private static ArrayList<X3Player> players = new ArrayList<X3Player>();
	private static final Logger log = Logger.getLogger("Minecraft.x3Permissions");
	private PluginManager pm = Bukkit.getPluginManager();

	@Override
	public void onEnable() {
		Database.init("BukkitPermissionsX66");
		pm.registerEvents(new PlayerListener(), this);
		this.getPluginCommands();
	}

	public void getPluginCommands() {
		try {
			SimplePluginManager spm = (SimplePluginManager) pm;
			SimpleCommandMap commandMap = null;
			if (spm != null) {
				Field cm = spm.getClass().getDeclaredField("commandMap");
				cm.setAccessible(true);
				commandMap = (SimpleCommandMap) cm.get(spm);
				if (commandMap != null) {
					for (Command c : commandMap.getCommands()) {

						log.info("/" + c.getName());
					}
				} else {
					log.info("Cound not find CommandMap.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String lbl,
			String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.isOp()) {
				return false;
			}
			String c = cmd.getName();
			if (c.equals("add")) {
				if (args.length >= 1) {
					String param = args[0];
					switch (param) {
						case "-g":
						case "-group":

							break;
						case "-c":
						case "-command":

							break;

					}
				} else {
					player.sendMessage("You need more arguements.");
				}
				return true;
			}
		}
		return false;
	}

	// Static Stuff

	public enum FindType {
		NAME, USERID;// TODO add more if needed.
	}

	public static void addPlayer(X3Player player) {
		if (!players.contains(player)) {
			players.add(player);
		}
	}

	public static void removePlayer(X3Player player) {
		if (players.contains(player)) {
			players.remove(player);
		}
	}

	public static X3Player getPlayer(FindType type, String value) {
		for (X3Player player : players.toArray(new X3Player[0])) {
			if (type == FindType.NAME) {
				if (player.getName().equals(value)) {
					return player;
				}
			} else if (type == FindType.USERID) {
				if (player.getUserID().equals(value)) {
					return player;
				}
			}
		}
		return null;
	}
	
	public static void log(Object o) {
		log.info(o.toString());
	}
}
