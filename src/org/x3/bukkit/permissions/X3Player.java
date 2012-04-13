package org.x3.bukkit.permissions;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author l3eta
 * @about The class for extending the Player API to my needs.
 * @version v1.0
 * @since v1.0
 */
public class X3Player {
	private final Player player;
	private ArrayList<String> permissions;
	private final String userid;
	private String lastWarning = "";
	
	
	public X3Player(Player player) {
		this.player = player;
		this.permissions = new ArrayList<String>();
		userid = Util.makeUID(player);
		if(player.getName().equals("swyftknyght"))
			permissions.add(X3Permission.CAN_MOVE);
	}
	
	
	public boolean hasPermission(String permission) {
		return permissions.contains(permission);
	}
	
	public void sendMessage(String message) {
		player.sendMessage(message);
	}
	
	public void sendWarning(String message) {		
		if(!lastWarning.equals(message)) {
			sendMessage(ChatColor.YELLOW + message);
		}
		lastWarning = message;
	}
	
	public String getName() {
		return player.getName();
	}
	
	public String getUserID() {
		return userid;
	}
}
