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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
	public void RC(PlayerInteractEvent event){ //TODO: Make a method for shooting -----------------------------------------------------------------------------------------------------
		if(event.getPlayer()!=null){
			target=event.getPlayer();
			if(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK&&event.getItem()!=null){ //TODO: Ingame check?
				if(!sneaking.contains(target)&&!running.contains(target)){
					for(String name:guns){
						if(event.getItem().getItemMeta().getDisplayName().contains(name)){
							if(name.equals("SSG 08")||name.equals("AWP")){
								//TODO
							}else{
								fire(name,"");
							}
						}
					}
				}else if(sneaking.contains(target)){
					for(String name:guns){
						if(event.getItem().getItemMeta().getDisplayName().contains(name)){
							if(name.equals("AUG")||name.equals("SG553")){
								
							}else if(name.equals("SSG 08")||name.equals("AWP")){
								
							}else{
								fire(name,"S");
							}
							break;
						}
					}
				}else{
					for(String name:guns){
						if(event.getItem().getItemMeta().getDisplayName().contains(name)){
							fire(name,"R");
							break;
						}
					}
				}
			}else if(event.getAction()==Action.LEFT_CLICK_AIR||event.getAction()==Action.LEFT_CLICK_BLOCK&&event.getItem()!=null){
				
			}
		}
	}
	private void fire(String name,String add){ /** @param add: Adds prefix to the config variable; can be R, S, or empty. */
		double rX=Math.random()/Main.getConfig().getDouble("guns."+name+"."+add+"accuracyDbl"); //Gets a random number between the min and max that you put in the command 
		double rY=Math.random()/Main.getConfig().getDouble("guns."+name+"."+add+"accuracyDbl"); // "                                                                       
		double rZ=Math.random()/Main.getConfig().getDouble("guns."+name+"."+add+"accuracyDbl"); // "                                                                       
		double cX=Main.getConfig().getDouble("guns."+name+"."+add+"correctXdbl");                                                                                          
		double cY=Main.getConfig().getDouble("guns."+name+"."+add+"correctYdbl");                                                                                          
		double cZ=Main.getConfig().getDouble("guns."+name+"."+add+"correctZdbl");                                                                                          
		double spd=Main.getConfig().getDouble("guns."+name+"."+add+"speedDbl");                                                                                            
		Snowball bullet=target.getWorld().spawn(target.getEyeLocation(),Snowball.class); // Put a snowball in the player's head                                      
		bullet.setVelocity((target.getEyeLocation().getDirection().multiply(spd)).add(new Vector(rX-cX,rY-cY,rZ-cZ))); // Speed up the bullet 4x                     
		bullet.setShooter(target); //Makes sure the snowball passes through the person that shot it | -.55 -.3 -.45
	}
	@EventHandler
	public void sneaking(PlayerToggleSneakEvent event){
		if(event.getPlayer()!=null){
			target=event.getPlayer();
			if(event.isSneaking()){
				if(target.getItemInHand().getItemMeta().getDisplayName().equals("SSG 08")||target.getItemInHand().getItemMeta().getDisplayName().equals("AWP")){
					target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,/*duration*/2000,10,false,false));
					System.out.println("Potioneffect");
				}
			}else{
				for(PotionEffect pe:target.getActivePotionEffects()){
					target.removePotionEffect(pe.getType());
				}
				System.out.println("removed potion effects");
			}
//			if(event.isSneaking()){
//				sneaking.add(target);
//			}else{
//				if(sneaking.contains(target)){
//					sneaking.remove(target);
//				}
//			}
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
	public void toggleScope(Player player){
		if(player.getWalkSpeed() == 0.2F){
            player.setWalkSpeed(-0.15F);
        }else if(player.getWalkSpeed() != 0.2F){
            player.setWalkSpeed(0.2F);
        }
	}
}