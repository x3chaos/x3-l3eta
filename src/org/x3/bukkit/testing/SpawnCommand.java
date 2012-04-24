package org.x3.bukkit.testing;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.server.MobEffect;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.NPC;
import com.topcat.npclib.entity.NPC.ArmorSet;
import com.topcat.npclib.entity.NPC.NPCType;

public class SpawnCommand extends Command {
	private NPCManager manager;
	private ArrayList<NPC> npcs;
	public SpawnCommand(String name, NPCManager manager) {
		super(name);
		this.manager = manager;
		this.npcs = new ArrayList<NPC>();
	}

	@Override
	public boolean execute(CommandSender send, String arg1, String[] args) {
		if(send instanceof Player) {
			String p = args[0];
			Player player = (Player) send;
			if (p.equals("-s")) {				
				NPC n = spawn(NPCType.TESTING, args[1], player);
				npcs.add(n);
				n.setArmorSet(ArmorSet.LEATHER);
				
				return true;
			} else if (p.equals("-k")) {
				manager.despawnAll();
				return true;
			} else if(p.equals("-f")) {
				for(NPC n : npcs) {
					n.sendMessage("Derp");
				}
				return true;
			}
		}		
		return false;
	}
	
	
	public NPC spawn(NPCType type, String name, Player owner) {
		return this.manager.spawn(type,	name, owner);
	}
	

}