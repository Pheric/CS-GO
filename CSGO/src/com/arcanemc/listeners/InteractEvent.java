package com.arcanemc.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.util.Vector;

import com.arcanemc.main.Main;

public class InteractEvent implements Listener{ //Min+(int)(Math.random()*((Max-Min)+1))
	private final Main Main;
	public InteractEvent(Main Main){
		this.Main=Main;
		Bukkit.getServer().getPluginManager().registerEvents(this,Main);
		guns=(Set<String>)Main.getConfig().getConfigurationSection("guns").getKeys(true);
		sneaking=new HashSet<Player>();
		running=new HashSet<Player>();
	}
	Player target;
	Set<String> guns;
	Set<Player> sneaking,running;
	@EventHandler
	public void RC(PlayerInteractEvent event){
		if(event.getPlayer()!=null){
			target=event.getPlayer();
			if(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK&&event.getItem()!=null){ //TODO: Ingame check?
				if(!sneaking.contains(target)&&!running.contains(target)){
					for(String name:guns){
						if(event.getItem().getItemMeta().getDisplayName().contains(name)){
							double rX=Math.random()/Main.getConfig().getDouble("guns."+name+".accuracyDbl"); //Gets a random number between the min and max that you put in the command
							double rY=Math.random()/Main.getConfig().getDouble("guns."+name+".accuracyDbl"); // "
							double rZ=Math.random()/Main.getConfig().getDouble("guns."+name+".accuracyDbl"); // "
							double cX=Main.getConfig().getDouble("guns."+name+".correctXdbl");
							double cY=Main.getConfig().getDouble("guns."+name+".correctYdbl");
							double cZ=Main.getConfig().getDouble("guns."+name+".correctZdbl");
							double spd=Main.getConfig().getDouble("guns."+name+".speedDbl");
							Snowball bullet=target.getWorld().spawn(target.getEyeLocation(),Snowball.class); // Put a snowball in the player's head
							bullet.setVelocity((target.getEyeLocation().getDirection().multiply(spd)).add(new Vector(rX-cX,rY-cY,rZ-cZ))); // Speed up the bullet 4x
							bullet.setShooter(target); //Makes sure the snowball passes through the person that shot it | -.55 -.3 -.45
							break; //It can't be two guns at once...
						}
					}
				}else if(sneaking.contains(target)){
					for(String name:guns){
						if(event.getItem().getItemMeta().getDisplayName().contains(name)){
							double rX=Math.random()/Main.getConfig().getDouble("guns."+name+".SaccuracyDbl"); //Gets a random number between the min and max that you put in the command
							double rY=Math.random()/Main.getConfig().getDouble("guns."+name+".SaccuracyDbl"); // "
							double rZ=Math.random()/Main.getConfig().getDouble("guns."+name+".SaccuracyDbl"); // "
							double cX=Main.getConfig().getDouble("guns."+name+".ScorrectXdbl");
							double cY=Main.getConfig().getDouble("guns."+name+".ScorrectYdbl");
							double cZ=Main.getConfig().getDouble("guns."+name+".ScorrectZdbl");
							double spd=Main.getConfig().getDouble("guns."+name+".SspeedDbl");
							Snowball bullet=target.getWorld().spawn(target.getEyeLocation(),Snowball.class); // Put a snowball in the player's head
							bullet.setVelocity((target.getEyeLocation().getDirection().multiply(spd)).add(new Vector(rX-cX,rY-cY,rZ-cZ))); // Speed up the bullet 4x
							bullet.setShooter(target); //Makes sure the snowball passes through the person that shot it | -.55 -.3 -.45
							break; //It can't be two guns at once...
						}
					}
				}else{
					for(String name:guns){
						if(event.getItem().getItemMeta().getDisplayName().contains(name)){
							double rX=Math.random()/Main.getConfig().getDouble("guns."+name+".RaccuracyDbl"); //Gets a random number between the min and max that you put in the command
							double rY=Math.random()/Main.getConfig().getDouble("guns."+name+".RaccuracyDbl"); // "
							double rZ=Math.random()/Main.getConfig().getDouble("guns."+name+".RaccuracyDbl"); // "
							double cX=Main.getConfig().getDouble("guns."+name+".RcorrectXdbl");
							double cY=Main.getConfig().getDouble("guns."+name+".RcorrectYdbl");
							double cZ=Main.getConfig().getDouble("guns."+name+".RcorrectZdbl");
							double spd=Main.getConfig().getDouble("guns."+name+".RspeedDbl");
							Snowball bullet=target.getWorld().spawn(target.getEyeLocation(),Snowball.class); // Put a snowball in the player's head
							bullet.setVelocity((target.getEyeLocation().getDirection().multiply(spd)).add(new Vector(rX-cX,rY-cY,rZ-cZ))); // Speed up the bullet 4x
							bullet.setShooter(target); //Makes sure the snowball passes through the person that shot it | -.55 -.3 -.45
							break; //It can't be two guns at once...
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void sneaking(PlayerToggleSneakEvent event){
		if(event.getPlayer()!=null){
			target=event.getPlayer();
			if(event.isSneaking()){
				sneaking.add(target);
			}else{
				if(sneaking.contains(target)){
					sneaking.remove(target);
				}
			}
		}
	}
	@EventHandler
	public void running(PlayerToggleSprintEvent event){
		if(event.getPlayer()!=null){
			target=event.getPlayer();
			if(event.isSprinting()){
				running.add(target);
			}else{
				if(running.contains(target)){
					running.remove(target);
				}
			}
		}
	}
}