package org.x3.bukkit.permissions;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.x3.X3Player;
import org.x3.bukkit.permissions.X3Permission.PermissionType;
import org.x3.bukkit.permissions.db.X3Database;

public class X3EventHandler {

	private final PermissionType event = PermissionType.EVENT;
	private final PermissionType cmd = PermissionType.COMMAND;
	private final PermissionType item = PermissionType.ITEM;
	
	public static String DEFAULT_WARNING_START = "You do not have permission to ";

	private final X3Database xdb;

	public X3EventHandler(final X3Database xdb) {
		this.xdb = xdb;
	}
	
	public X3Permission create(PermissionType type, Object o) {
		return X3Permission.create(type, o);
	}

	public void onEvent(Event event) {
		X3Permission perm = create(this.event, event);
		if (event instanceof PlayerEvent) {			
			PlayerEvent pe = (PlayerEvent) event;
			X3Player x3p = xdb.getPlayer(pe.getPlayer());
			if (x3p == null)
				x3p = new X3Player(pe.getPlayer());
			switch (event.getEventName()) {
				case "PlayerJoinEvent":
					xdb.addPlayer(x3p);
					break;
				case "PlayerGameModeChangeEvent":
					if (!x3p.hasPermission(perm)) {
						x3p.sendWarning(DEFAULT_WARNING_START
								+ "change GameMode!");
						cancel(event);
					}
					break;
				case "PlayerQuitEvent":
				case "PlayerKickEvent":
					xdb.removePlayer(x3p);
					break;
				case "PlayerMoveEvent":
					if (!x3p.hasPermission(perm)) {
						x3p.sendWarning(DEFAULT_WARNING_START + "move!");
						cancel(event);
					}
					break;

				case "PlayerChatEvent":
					if (!x3p.hasPermission(perm)) {
						x3p.sendWarning(DEFAULT_WARNING_START + "speak!");
						cancel(event);
					}
					break;
				case "PlayerItemHeldEvent":
					this.checkHolding((PlayerItemHeldEvent) event);
					break;
				case "PlayerInteractEvent":
				case "PlayerAnimationEvent":
					// Ignored TODO do something here.
					break;
				default:
					x3p.sendMessage("Unhandled Event(X3Permission): "
							+ event.getEventName());
					break;
			}
		} else if (event instanceof BlockEvent) {
			X3Player x3p = null;
			switch (event.getEventName()) {
				case "BlockPlaceEvent":
					x3p = xdb.getPlayer(((BlockPlaceEvent) event).getPlayer());
					if (!x3p.hasPermission(perm)) {
						x3p.sendWarning(DEFAULT_WARNING_START + "build!");
						cancel(event);
					}
					break;
				case "BlockBreakEvent":
					x3p = xdb.getPlayer(((BlockBreakEvent) event).getPlayer());
					if (!x3p.hasPermission(perm)) {
						x3p.sendWarning(DEFAULT_WARNING_START + "break blocks!");
						cancel(event);
					}
					break;
			}
		} else if (event instanceof EntityEvent) {
			switch (event.getEventName()) {
				case "EntityTargetLivingEntityEvent":
					EntityTargetLivingEntityEvent et = (EntityTargetLivingEntityEvent) event;
					if (et.getTarget() instanceof Player) {
						X3Player x3p = xdb.getPlayer((Player) et.getTarget());
						if (!x3p.hasPermission(create(this.event, "PlayerMoveEvent")))
							cancel(event);
					}
					break;
			}
		}
	}

	public void cancel(Event event) {
		try {
			Cancellable c = Cancellable.class.cast(event);
			c.setCancelled(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// TODO
	private void checkHolding(PlayerItemHeldEvent event) {
		X3Player player = xdb.getPlayer(event.getPlayer());
		ItemStack prev = player.getInv().getItem(event.getPreviousSlot());
		ItemStack next = player.getInv().getItem(event.getNewSlot());
		if (!player.hasPermission(create(item, prev.getType()))) {
			player.removeItem(prev);
			player.sendWarning(DEFAULT_WARNING_START + "keep " + prev.getType().toString());
		}
		if (!player.hasPermission(create(item, next.getType()))) {
			player.removeItem(next);
			player.sendWarning(DEFAULT_WARNING_START + "keep " + next.getType().toString());
		}
	}
}
