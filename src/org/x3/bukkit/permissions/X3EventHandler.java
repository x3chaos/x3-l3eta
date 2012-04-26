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
import org.x3.util.LoggerUtil;

public class X3EventHandler {
	private LoggerUtil log = new LoggerUtil(X3EventHandler.class);
	private final PermissionType event = PermissionType.EVENT;
	private final PermissionType item = PermissionType.ITEM;

	public static String WARNING_START = "You do not have permission to ";

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
					cancel(x3p, event, WARNING_START + "change GameMode!");
				}
				break;
			case "PlayerQuitEvent":
			case "PlayerKickEvent":
				xdb.removePlayer(x3p);
				break;
			case "PlayerChatEvent":
				if (!x3p.hasPermission(perm)) {
					cancel(x3p, event, WARNING_START + "speak!");
				}
				break;
			case "PlayerItemHeldEvent":
				this.checkHolding((PlayerItemHeldEvent) event);
				break;
			case "PlayerMoveEvent":
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
					cancel(x3p, event, WARNING_START + "build!");
				}
				break;
			case "BlockBreakEvent":
				x3p = xdb.getPlayer(((BlockBreakEvent) event).getPlayer());
				if (!x3p.hasPermission(perm)) {
					cancel(x3p, event, WARNING_START + "break blocks!");
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
						cancel(x3p, event, "");
				}
				break;
			}
		}
	}

	private void cancel(X3Player player, Event event, String warning) {
		try {
			if (!warning.equals("") && player != null)
				player.sendWarning(warning);
			Cancellable c = Cancellable.class.cast(event);
			c.setCancelled(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void checkHolding(PlayerItemHeldEvent event) {
		X3Player player = xdb.getPlayer(event.getPlayer());
		if (player.hasItemAt(event.getPreviousSlot())) {
			ItemStack prev = player.getInv().getItem(event.getPreviousSlot());
			if (!player.hasPermission(create(item, prev.getType()))) {
				player.removeItem(prev);
				player.sendWarning(WARNING_START + "keep " + prev.getType());
			}
		}
		if (player.hasItemAt(event.getNewSlot())) {
			ItemStack next = player.getInv().getItem(event.getNewSlot());
			if (!player.hasPermission(create(item, next.getType()))) {
				player.removeItem(next);
				player.sendWarning(WARNING_START + "keep " + next.getType());
			}
		}
	}
}
