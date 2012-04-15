package org.x3.bukkit.permissions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.x3.bukkit.permissions.X3EventHandler;

public class PlayerListener implements Listener {
	private X3EventHandler xeh;

	public PlayerListener(X3EventHandler xeh) {
		this.xeh = xeh;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTargetEvent(EntityTargetLivingEntityEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlaced(BlockPlaceEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerAnimationEvent(PlayerAnimationEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocces(PlayerCommandPreprocessEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChatEvent(PlayerChatEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerEggThrowEvent(PlayerEggThrowEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerExpChangeEvent(PlayerExpChangeEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerFishEvent(PlayerFishEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKickEvent(PlayerKickEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLoginEvent(PlayerLoginEvent event) {
		// xeh.onEvent(event); //Removed this so it doesn't cast
		// exception
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent event) {
		xeh.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerVelocityEvent(PlayerVelocityEvent event) {
		xeh.onEvent(event);
	}
}
