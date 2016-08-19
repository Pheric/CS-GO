package com.arcanemc.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.arcanemc.main.Main;

public class DamageEvent implements Listener{
	private final Main Main;
	public DamageEvent(Main Main){
		this.Main=Main;
		Main.getServer().getPluginManager().registerEvents(this,Main);
	}
	Player target;
	@EventHandler
	public void damaged(EntityDamageByEntityEvent event){
		System.out.println("Event");
		if(event.getDamager()!=null&&event.getEntity()!=null&&event.getEntity() instanceof Player){
			target=(Player)event.getEntity();
			System.out.println("1st if");
			if(event.getDamager().getType()==EntityType.SNOWBALL){ //TODO: Check for target to be in-game
				System.out.println(event.getDamager().getMetadata(null));
			}
		}
	}
}
