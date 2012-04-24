package org.x3;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.l3eta.Crypto;
import org.x3.bukkit.PermissionPlugin;
import org.x3.bukkit.permissions.X3Permission;
import org.x3.util.Reflect;

/**
 * @author l3eta
 * @category The class for extending the Player API to my needs.
 * @version v1.0
 * @since v1.0
 */
public class X3Player {
	private final Player player;
	private ArrayList<String> permissions;
	private final String userid;
	private String lastWarning = "", tempWarn = "";
	
	public X3Player() {
		this.player = null;
		this.userid = "79af2c9048fdaa6eabe928e315627a57c1e23db9";
	}

	public X3Player(Player player) {
		this.player = player;
		this.permissions = new ArrayList<String>();
		userid = makeUID(player);
	}

	public void setPermissions(X3Permission[] permissions) {
		for (X3Permission perm : permissions) {
			setPermission(perm.toString());
		}
	}
	
	public void setPermission(String permission) {
		
		player.addAttachment(PermissionPlugin.getPlugin(), permission, true);
	}

	public boolean hasPermission(X3Permission permission) {
		return player.hasPermission(permission.toString());
	}

	public void sendMessage(String message) {
		player.sendMessage(message);
	}

	public void sendWarning(String message) {
		tempWarn = message;
		if (tempWarn.equals(lastWarning))
			return;
		if (!lastWarning.equals(message)) {
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

	public boolean hasItemAt(int index) {
		return getInv().getItem(index) != null;
	}

	public void removeItem(ItemStack item) {
		getInv().remove(item);
	}

	public boolean isOp() {
		return player.isOp();
	}

	// Static Methods

	public static String makeUID(Player player) {
		return Crypto.sha(player.getName() + getIP(player), "x3Permission", 0);
	}

	public static String getIP(Player player) {
		return player.getAddress().getAddress().getHostAddress();
	}

	public Player getPlayer() {
		return this.player;
	}

}
