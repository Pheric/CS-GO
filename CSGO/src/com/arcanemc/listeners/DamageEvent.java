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
		ie=new InteractEvent(Main);
	}
	InteractEvent ie;
	Player target;
	String name;
	@EventHandler
	public void damaged(EntityDamageByEntityEvent event){
		if(event.getDamager()!=null&&event.getEntity()!=null&&event.getEntity() instanceof Player){
			target=(Player)event.getEntity();
			if(event.getDamager().getType()==EntityType.SNOWBALL&&event.getDamager().hasMetadata("bulletData")){ //TODO: Check for target to be in-game
				for(String name:ie.guns){
					if(event.getItem().getItemMeta().getLore().get(0).contains(name)){
						this.name=name;
					}
				}
				for(String g:ie.gun.get(target).get())){
					
				}
				if(event.getDamager().getMetadata("bulletData").get(0).equals(obj)){
					
				}
				target.damage();
			}
		}
	}
}
