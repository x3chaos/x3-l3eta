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
import org.x3.bukkit.permissions.Permissions.FindType;

import com.mongodb.BasicDBObject;

public class X3Permission {
	// TODO remove this.
	public final static String DEFAULT = "command.default.";
	public final static String CAN_MOVE = "player.event.move";
	public final static String CAN_SPEAK = "player.event.speak";
	public final static String CAN_PLACE_BLOCK = "player.event.placeBlock";
	public final static String CAN_BREAK_BLOCK = "player.event.breakBlock";
	public final static String CAN_CHANGE_GAMEMODE = "player.event.gameModeChange";
	
	public static String DEFAULT_WARNING_START = "You do not have permission to ";
	
	public static String makeDefault(String name, int slashs) {
		return String.format("%s%s.%d", DEFAULT, name, slashs);
	}

	public static String makeDefault(BasicDBObject c) {
		if (c.containsField("name") && c.containsField("slashs")) {
			return makeDefault(c.getString("name"), c.getInt("slashs"));
		}
		return null;
	}

	public static void onEvent(Event event) {
		if (event instanceof PlayerEvent) {
			PlayerEvent pe = (PlayerEvent) event;
			X3Player x3p = null;
			if(pe.getPlayer() != null)
				 x3p = new X3Player(pe.getPlayer());
			switch (event.getEventName()) {
				case "PlayerJoinEvent":
					Permissions.addPlayer(x3p);
					break;
				case "PlayerGameModeChangeEvent":
					if(!x3p.hasPermission(CAN_CHANGE_GAMEMODE)) {
						x3p.sendWarning(DEFAULT_WARNING_START + "change GameMode!");
						cancel(event);
					}
					break;
				case "PlayerQuitEvent":
				case "PlayerKickEvent":
					Permissions.removePlayer(x3p);
					break;
				case "PlayerMoveEvent":
					if (!x3p.hasPermission(CAN_MOVE)) {
						x3p.sendWarning(DEFAULT_WARNING_START + "move!");
						cancel(event);
					}
					break;
				case "PlayerInteractEvent":
				case "PlayerAnimationEvent":
					//Ignored
					//TODO do something here.
					break;
				case "PlayerChatEvent":
					if(!x3p.hasPermission(CAN_SPEAK)) {
						x3p.sendWarning(DEFAULT_WARNING_START + "speak!");
						cancel(event);
					}
					break;
				default:
					x3p.sendMessage("Unhandled Event(X3Permission): " + event.getEventName());
					break;
			}
		} else if(event instanceof BlockEvent) {
			X3Player x3p = null;
			String userid = null;
			switch(event.getEventName()) {				
				case "BlockPlaceEvent":
					BlockPlaceEvent bp = (BlockPlaceEvent) event;
					userid = Util.makeUID(bp.getPlayer());
					x3p = Permissions.getPlayer(FindType.USERID, userid);
					if(!x3p.hasPermission(CAN_PLACE_BLOCK)) {
						x3p.sendWarning(DEFAULT_WARNING_START + "build!");
						bp.setCancelled(true);
					}
					break;
				case "BlockBreakEvent":
					BlockBreakEvent bb = (BlockBreakEvent) event;
					userid = Util.makeUID(bb.getPlayer());
					x3p = Permissions.getPlayer(FindType.USERID, userid);
					if(!x3p.hasPermission(CAN_BREAK_BLOCK)) {
						x3p.sendWarning(DEFAULT_WARNING_START + "break blocks!");
						bb.setCancelled(true);
					}
					break;
			}
		} else if(event instanceof EntityEvent) {
			switch(event.getEventName()) {
				//Makes it so mobs don't kill people who cannot move.
				case "EntityTargetLivingEntityEvent":
					EntityTargetLivingEntityEvent et = (EntityTargetLivingEntityEvent) event;
					if(et.getTarget() instanceof Player) {
						String userid = Util.makeUID((Player) et.getTarget());
						X3Player x3p = Permissions.getPlayer(FindType.USERID, userid);
						if(!x3p.hasPermission(CAN_MOVE))
							et.setCancelled(true);
					}
					break;
			}
		}
	}

	public static void cancel(Event event) {
		try {
			Cancellable c = Cancellable.class.cast(event);
			c.setCancelled(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
