package org.x3.bukkit.testing;

import net.minecraft.server.Entity;

import org.bukkit.entity.Player;

import com.topcat.npclib.entity.NPC;

public class NPCFollowThread extends Thread {
	private NPC npc;
	private Player owner;
	private boolean allowFollow = true;

	public NPCFollowThread(NPC npc) {
		this.npc = npc;
		this.owner = npc.getOwner();
	}

	public void run() {
		while (allowFollow) {
			while (!npc.isByLocation(owner.getLocation())) {
				npc.moveTo(owner.getLocation());
				sleep(250);
			}
			sleep(250);
		}
	}

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setAllow(boolean b) {
		this.allowFollow = b;		
	}
}
