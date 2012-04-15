package org.x3.bukkit.permissions;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.x3.bukkit.permissions.util.Util;

/**
 * @author l3eta
 * @about The class for extending the Player API to my needs.
 * @version v1.0
 * @since v1.0
 */
public class X3Player {
	private final Player player;
	private ArrayList<X3Permission> permissions;
	private final String userid;
	private String lastWarning = "", tempWarn = "";
	
	
	public X3Player(Player player) {
		this.player = player;
		this.permissions = new ArrayList<X3Permission>();
		userid = Util.makeUID(player);
	}
	
	
	public boolean hasPermission(X3Permission permission) {
		return permissions.contains(permission);
	}
	
	public void sendMessage(String message) {
		player.sendMessage(message);
	}
	
	public void sendWarning(String message) {
		tempWarn = message;
		if(tempWarn.equals(lastWarning))
			return;
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
	
	public Inventory getInv() {
		return player.getInventory();
	}
	
	public void removeItem(ItemStack item) {
		getInv().remove(item);
	}
}
