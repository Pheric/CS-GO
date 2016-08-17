package com.arcanemc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.arcanemc.main.Main;

public class InteractEvent implements Listener{
	private final Main Main;
	public InteractEvent(Main Main){
		this.Main=Main;
		Bukkit.getServer().getPluginManager().registerEvents(this,Main);
	}
	Player target;
	double rX,rY,rZ;
	@EventHandler
	public void RC(PlayerInteractEvent event){
		if(event.getPlayer()!=null){
			target=event.getPlayer();
			if(event.getMaterial()!=null&&event.getMaterial()==Material.STONE_HOE){	
				if(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK){ //The player shoots
					//Min+(int)(Math.random()*((Max-Min)+1))
					rX=Main.min+(Math.random()*((Main.max-Main.min)+1)); //Gets a random number between the min and max that you put in the command
					rY=Main.min+(Math.random()*((Main.max-Main.min)+1)); // "
					rZ=Main.min+(Math.random()*((Main.max-Main.min)+1)); // "
					Snowball bullet=target.getWorld().spawn(target.getEyeLocation(),Snowball.class); // Put a snowball in the player's head
					bullet.setVelocity(target.getEyeLocation().getDirection().multiply(4)); // Speed up the bullet 4x
					bullet.setVelocity(bullet.getVelocity().add(new Vector(rX-.55,rY-.3,rZ))); // Modify the direction by adding the random numbers. Also, my attempted fix for the bug
					bullet.setShooter(target); //Makes sure the snowball passes through the person that shot it
				}
			}
		}
	}
}
