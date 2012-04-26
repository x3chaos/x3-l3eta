package org.l3eta.npclib;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitScheduler;

import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.NPC;
import com.topcat.npclib.pathing.NPCPath;
import com.topcat.npclib.pathing.NPCPathFinder;
import com.topcat.npclib.pathing.Node;
import com.topcat.npclib.pathing.PathReturn;

public class PathManager {
	private NPC npc;
	private BukkitScheduler task = Bukkit.getScheduler();
	private NPCPathFinder path;
	private Iterator<Node> pathIterator;
	private Node last;
	private NPCPath runningPath;
	private Runnable onFail;
	private int taskid;

	public PathManager(NPC npc) {
		this.npc = npc;
	}

	public void pathFindTo(Location l, PathReturn callback) {
		pathFindTo(l, 3000, callback);
	}

	public void pathFindTo(Location l, int maxIterations, PathReturn callback) {
		if (path != null) {
			path.cancel = true;
		}
		if (l.getWorld() != npc.getBukkitEntity().getWorld()) {
			ArrayList<Node> pathList = new ArrayList<Node>();
			pathList.add(new Node(l.getBlock()));
			callback.run(new NPCPath(null, pathList, l));
		} else {
			path = new NPCPathFinder(npc.getBukkitEntity().getLocation(), l,
					maxIterations, callback);
			path.start();
		}
	}

	public void walkTo(Location l) {
		walkTo(l, 3000);
	}

	public void walkTo(final Location l, final int maxIterations) {
		pathFindTo(l, maxIterations, new PathReturn() {
			@Override
			public void run(NPCPath path) {
				usePath(path, new Runnable() {
					@Override
					public void run() {
						walkTo(l, maxIterations);
					}
				});
			}
		});
	}

	public void usePath(NPCPath path) {
		usePath(path, new Runnable() {
			@Override
			public void run() {
				walkTo(runningPath.getEnd(), 3000);
			}
		});
	}

	public void usePath(NPCPath path, Runnable onFail) {
		if (taskid == 0) {
			taskid = task.scheduleSyncRepeatingTask(NPCManager.plugin,
					new Runnable() {
						@Override
						public void run() {
							pathStep();
						}
					}, 6L, 6L);
		}
		pathIterator = path.getPath().iterator();
		runningPath = path;
		this.onFail = onFail;
	}

	private void pathStep() {
		if (pathIterator.hasNext()) {
			Node n = pathIterator.next();
			if (n.b.getWorld() != npc.getBukkitEntity().getWorld()) {
				npc.getBukkitEntity().teleport(n.b.getLocation());
			} else {
				float angle = npc.yaw, look = npc.pitch;
				if (last == null || runningPath.checkPath(n, last, true)) {
					if (last != null) {
						angle = (float) Math.toDegrees(Math.atan2(last.b.getX()
								- n.b.getX(), n.b.getZ() - last.b.getZ()));
						look = (float) (Math.toDegrees(Math.asin(last.b.getY()
								- n.b.getY())) / 2);
					}
					npc.setPositionRotation(n.b.getX() + 0.5, n.b.getY(),
							n.b.getZ() + 0.5, angle, look);
				} else {
					onFail.run();
				}
			}
			last = n;
		} else {
			Location l = runningPath.getEnd();
			npc.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(),
					l.getPitch());
			Bukkit.getScheduler().cancelTask(taskid);
			taskid = 0;
		}
	}

}
