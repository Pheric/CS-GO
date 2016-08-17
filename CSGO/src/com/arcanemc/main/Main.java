package com.arcanemc.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.arcanemc.listeners.InteractEvent;

public class Main extends JavaPlugin{
	@Override
	public void onEnable(){
		new InteractEvent(this);
	}
	Player target;
	public float min,max,x,y;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender != null && sender instanceof Player) {
			target = (Player) sender;
			if(lbl.equalsIgnoreCase("setrange")){
				min=Float.parseFloat(args[0]);
				max=Float.parseFloat(args[1]);
				x=Float.parseFloat(args[2]);
				y=Float.parseFloat(args[3]);
			}
		}
		return true;
	}
}