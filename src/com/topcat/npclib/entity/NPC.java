package com.topcat.npclib.entity;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.x3.bukkit.testing.NPCFollowThread;

import com.topcat.npclib.NPCManager;
import com.topcat.npclib.nms.NPCNetHandler;
import com.topcat.npclib.nms.NpcEntityTargetEvent;
import com.topcat.npclib.pathing.NPCPath;
import com.topcat.npclib.pathing.NPCPathFinder;
import com.topcat.npclib.pathing.Node;
import com.topcat.npclib.pathing.PathReturn;

public class NPC extends EntityPlayer {
	private int nid, lastTargetId, lastBounceId;
	
	private long lastBounceTick;
	private Server server = Bukkit.getServer();
	private BukkitScheduler task = server.getScheduler();
	private Player owner;
	private NPCType type;
	private NPCFollowThread followThread;

	public NPC(MinecraftServer ms, World world, String name,
			ItemInWorldManager iwm, NPCType type, Player owner) {
		super(ms, world, name, iwm);
		this.netServerHandler = new NPCNetHandler(ms, this);
		this.type = type;
		this.owner = owner;
		this.itemInWorldManager.b(0);
		lastTargetId = -1;
		lastBounceId = -1;
		lastBounceTick = 0;
	}


	public void removeFromWorld() {
		world.removeEntity(this);
	}

	public Location getLocation() {
		return getBukkitEntity().getLocation();
	}

	public void moveTo(Location l) {
		moveTo(l.getX(), l.getY(), l.getZ(), 16f);
	}
	
	private void moveTo(double x, double y, double z, float speed) {
		this.getControllerMove().a(x, y, z, speed);
	}
	
	public void sendMessage(String message) {
		this.getBukkitEntity().sendMessage(message);
	}

	public void animateArmSwing() {
		((WorldServer) world).tracker
				.a(this, new Packet18ArmAnimation(this, 1));
	}

	public void actAsHurt() {
		((WorldServer) world).tracker
				.a(this, new Packet18ArmAnimation(this, 2));
	}

	public void setItemInHand(Material m) {
		setItemInHand(m, (short) 0);
	}

	public void setItemInHand(Material m, short damage) {
		((HumanEntity) getBukkitEntity()).setItemInHand(new ItemStack(m, 1,
				damage));
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public PlayerInventory getInventory() {
		return getBukkitEntity().getInventory();
	}

	public void putInBed(Location bed) {
		setPosition(bed.getX(), bed.getY(), bed.getZ());
		a((int) bed.getX(), (int) bed.getY(), (int) bed.getZ());
	}

	public void getOutOfBed() {
		a(true, true, true);
	}

	public void setSneaking() {
		setSneak(true);
	}

	public void lookAtPoint(Location point) {
		if (getBukkitEntity().getWorld() != point.getWorld()) {
			return;
		}
		Location npcLoc = ((LivingEntity) getBukkitEntity()).getEyeLocation();
		double xDiff = point.getX() - npcLoc.getX();
		double yDiff = point.getY() - npcLoc.getY();
		double zDiff = point.getZ() - npcLoc.getZ();
		double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
		double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
		double newPitch = Math.acos(yDiff / DistanceY) * 180 / Math.PI - 90;
		if (zDiff < 0.0) {
			newYaw = newYaw + Math.abs(180 - newYaw) * 2;
		}
		yaw = (float) (newYaw - 90);
		pitch = (float) newPitch;
	}

	// NPC Overrides

	public void setBukkitEntity(org.bukkit.entity.Entity entity) {
		bukkitEntity = entity;
	}

	@Override
	public boolean b(EntityHuman entity) {
		EntityTargetEvent event = new NpcEntityTargetEvent(getBukkitEntity(),
				entity.getBukkitEntity(),
				NpcEntityTargetEvent.NpcTargetReason.NPC_RIGHTCLICKED);
		CraftServer server = ((WorldServer) world).getServer();
		server.getPluginManager().callEvent(event);

		return super.b(entity);
	}

	@Override
	public void a_(EntityHuman entity) {
		if (lastTargetId == -1 || lastTargetId != entity.id) {
			EntityTargetEvent event = new NpcEntityTargetEvent(
					getBukkitEntity(), entity.getBukkitEntity(),
					NpcEntityTargetEvent.NpcTargetReason.CLOSEST_PLAYER);
			CraftServer server = ((WorldServer) world).getServer();
			server.getPluginManager().callEvent(event);
		}
		lastTargetId = entity.id;
		super.a_(entity);
	}

	@Override
	public void c(Entity entity) {
		if (lastBounceId != entity.id
				|| System.currentTimeMillis() - lastBounceTick > 1000) {
			EntityTargetEvent event = new NpcEntityTargetEvent(
					getBukkitEntity(), entity.getBukkitEntity(),
					NpcEntityTargetEvent.NpcTargetReason.NPC_BOUNCED);
			CraftServer server = ((WorldServer) world).getServer();
			server.getPluginManager().callEvent(event);

			lastBounceTick = System.currentTimeMillis();
		}

		lastBounceId = entity.id;

		super.c(entity);
	}

	@Override
	public void move(double arg0, double arg1, double arg2) {
		setPosition(arg0, arg1, arg2);
	}

	public void setID(int id) {
		this.nid = id;
	}

	public int getID() {
		return nid;
	}

	public void setArmorSet(ArmorSet set) {
		getInventory().setArmorContents(createArmor(set.getMats()));
	}

	private ItemStack[] createArmor(Material[] mats) {
		ItemStack[] items = new ItemStack[mats.length];
		for (int i = 0; i < mats.length; i++) {
			items[i] = new ItemStack(mats[i]);
		}
		return items;
	}

	@SuppressWarnings("unused")
	public boolean isByLocation(Location l) {
		double lx1 = l.getX(), ly1 = l.getY(), lz1 = l.getZ();
		Location m = getLocation();
		double lx2 = m.getX(), ly2 = m.getY(), lz2 = m.getZ();
		// TODO maybe check y's if I need to.
		if (isWithin(lx1 - lx2, 3) && isWithin(lz1 - lz2, 3)) {
			return true;
		}
		return false;
	}

	private boolean isWithin(double n, int within) {
		return n < within && n > -within;
	}

	public enum ArmorSet {
		DIAMOND, GOLD, IRON, CHAIN, LEATHER;

		public Material[] getMats() {
			if (this == DIAMOND) {
				return new Material[] { Material.DIAMOND_HELMET,
						Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS,
						Material.DIAMOND_BOOTS };
			} else if (this == GOLD) {
				return new Material[] { Material.GOLD_HELMET,
						Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS,
						Material.GOLD_BOOTS };
			} else if (this == IRON) {
				return new Material[] { Material.IRON_HELMET,
						Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS,
						Material.IRON_BOOTS };
			} else if (this == CHAIN) {
				return new Material[] { Material.CHAINMAIL_HELMET,
						Material.CHAINMAIL_CHESTPLATE,
						Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS };
			} else {
				return new Material[] { Material.LEATHER_HELMET,
						Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS,
						Material.LEATHER_BOOTS };
			}
		}
	}

	public enum NPCType {
		TESTING, NORMAL, GUARD;
	}

	public Player getOwner() {
		return owner;
	}

	public void followOwner() {
		sendMessage("Running Follow");
		followThread = new NPCFollowThread(this);
		followThread.start();
	}

}