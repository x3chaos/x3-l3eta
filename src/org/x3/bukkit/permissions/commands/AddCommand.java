package org.x3.bukkit.permissions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.x3.X3Player;
import org.x3.bukkit.permissions.db.X3Database;
import org.x3.util.Reflect;

public class AddCommand extends Command {
	private X3Database xdb;
	
	public AddCommand(X3Database xdb) { // Rename this
		super("grant");
		this.xdb = xdb;
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias,
			String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			X3Player x3p = xdb.getPlayer(player);
			if (!player.isOp()) {
				if (!player.hasPermission("x3permission.grant"))
					return false;
			}
			if (args.length >= 1) {
				String param = args[0];
				switch (param) {
				case "-g":
				case "-group":
					player.sendMessage("Derp");
					break;
				case "-c":
				case "-command":
					
					break;
				}
			} else {
				player.sendMessage("You need more arguements.");
				return false;
			}
			return true;
		}
		return false;
	}
}
