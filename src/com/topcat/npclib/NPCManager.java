package com.topcat.npclib;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.Entity;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.topcat.npclib.entity.NPC;
import com.topcat.npclib.entity.NPC.NPCType;
import com.topcat.npclib.nms.BServer;
import com.topcat.npclib.nms.BWorld;
import com.topcat.npclib.nms.NPCNetworkManager;

/**
 * 
 * @author martin
 */
public class NPCManager {
	private ArrayList<NPC> npcs = new ArrayList<NPC>();
	private BServer server;
	private int taskid;
	private Map<World, BWorld> bworlds = new HashMap<World, BWorld>();
	private static NPCNetworkManager npcNetworkManager;
	private static MinecraftServer ms;
	public static JavaPlugin plugin;

	public NPCManager(JavaPlugin plugin) {
		server = BServer.getInstance();
		ms = server.getMCServer();

		npcNetworkManager = new NPCNetworkManager();
		NPCManager.plugin = plugin;
		taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
					@Override
					public void run() {
						for (NPC npc : npcs) {
							j.aA();
							if (npc.dead) {
								npc.removeFromWorld();
								npcs.remove(npc);
							}
						}
					}
				}, 1L, 1L);
		Bukkit.getServer().getPluginManager().registerEvents(new SL(), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new WL(), plugin);
	}

	public BWorld getBWorld(World world) {
		BWorld bworld = bworlds.get(world);
		if (bworld != null) {
			return bworld;
		}
		bworld = new BWorld(world);
		bworlds.put(world, bworld);
		return bworld;
	}

	private class SL implements Listener {
		@SuppressWarnings("unused")
		@EventHandler
		public void onPluginDisable(PluginDisableEvent event) {
			if (event.getPlugin() == plugin) {
				despawnAll();
				Bukkit.getServer().getScheduler().cancelTask(taskid);
			}
		}
	}

	private class WL implements Listener {
		@SuppressWarnings("unused")
		@EventHandler
		public void onChunkLoad(ChunkLoadEvent event) {
			for (NPC npc : npcs) {
				if (npc != null
						&& event.getChunk() == npc.getBukkitEntity()
								.getLocation().getBlock().getChunk()) {
					getBWorld(event.getWorld()).getWorldServer().addEntity(npc);
				}
			}
		}
	}

	public NPC getNpcByID(int id) {
		for (NPC npc : npcs) {
			if (npc.getID() == id) {
				return npc;
			}
		}
		return null;
	}

	private NPC getNpcByName(String name) {
		for (NPC npc : npcs) {
			if (npc.getName().equals(name)) {
				return npc;
			}
		}
		return null;
	}

	private boolean despawn(NPC npc) {
		if (npc != null && npcs.contains(npc)) {
			npc.removeFromWorld();
			npcs.remove(npc);
			return true;
		}
		return false;
	}

	public void despawnById(int id) {
		despawn(getNpcByID(id));
	}

	public void despawnHumanByName(String name) {
		despawn(getNpcByName(name.substring(0,
				name.length() > 16 ? 16 : name.length())));

	}

	public void despawnAll() {
		for (NPC npc : npcs) {
			despawn(npc);
		}
	}

	public boolean isNPC(org.bukkit.entity.Entity e) {
		return ((CraftEntity) e).getHandle() instanceof NPC;
	}

	public List<NPC> getHumanNPCByName(String name) {
		List<NPC> ret = new ArrayList<NPC>();
		for (NPC npc : npcs) {
			if (npc.name.equalsIgnoreCase(name)) {
				ret.add(npc);
			}
		}
		return ret;
	}

	public List<NPC> getNPCs() {
		return npcs;
	}

	public int getNPCIdFromEntity(org.bukkit.entity.Entity e) {
		if (e instanceof HumanEntity) {
			for (NPC npc : npcs) {
				if (npc.getBukkitEntity().getEntityId() == ((HumanEntity) e)
						.getEntityId()) {
					return npc.id;
				}
			}
		}
		return -1;
	}

	public void rename(int id, String name) {
		name = name.substring(0, name.length() > 16 ? 16 : name.length());
		NPC npc = getNpcByID(id);
		npc.setName(name);
		BWorld b = getBWorld(npc.getBukkitEntity().getLocation().getWorld());
		WorldServer s = b.getWorldServer();
		try {
			Method m = s.getClass().getDeclaredMethod("d",
					new Class[] { Entity.class });
			m.setAccessible(true);
			m.invoke(s, npc);
			m = s.getClass().getDeclaredMethod("c",
					new Class[] { Entity.class });
			m.setAccessible(true);
			m.invoke(s, npc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		s.everyoneSleeping();
	}

	public BServer getServer() {
		return server;
	}

	public static NPCNetworkManager getNPCNetworkManager() {
		return npcNetworkManager;
	}

	public NPC spawn(NPCType type, String name, Player owner) {
		name = name.substring(0, name.length() > 16 ? 16 : name.length());
		Location l = owner.getLocation();
		WorldServer world = getBWorld(l.getWorld()).getWorldServer();
		NPC npc = new NPC(ms, world, name, new ItemInWorldManager(world), type,
				owner);
		npc.setID(npcs.size());
		npc.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(),
				l.getPitch());
		world.addEntity(npc);
		npcs.add(npc);
		return npc;
	}

}