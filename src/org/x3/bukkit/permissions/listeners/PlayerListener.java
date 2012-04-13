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
import org.x3.bukkit.permissions.X3Permission;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTargetEvent(EntityTargetLivingEntityEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlaced(BlockPlaceEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerAnimationEvent(PlayerAnimationEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocces(PlayerCommandPreprocessEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChatEvent(PlayerChatEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerEggThrowEvent(PlayerEggThrowEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerExpChangeEvent(PlayerExpChangeEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerFishEvent(PlayerFishEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKickEvent(PlayerKickEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLoginEvent(PlayerLoginEvent event) {
		// X3Permission.onEvent(event); //Removed this so it doesn't cast
		// exception
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent event) {
		X3Permission.onEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerVelocityEvent(PlayerVelocityEvent event) {
		X3Permission.onEvent(event);
	}
}
