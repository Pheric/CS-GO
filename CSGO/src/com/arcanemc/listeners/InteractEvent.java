package com.arcanemc.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.arcanemc.main.Main;
import com.arcanemc.other.gun;

import net.md_5.bungee.api.ChatColor;

public class InteractEvent implements Listener{ //Min+(int)(Math.random()*((Max-Min)+1))
	private final Main Main;
	public InteractEvent(Main Main){
		this.Main=Main;
		Bukkit.getServer().getPluginManager().registerEvents(this,Main);
		guns=(Set<String>)Main.getConfig().getConfigurationSection("guns").getKeys(true);
		sneaking=new HashSet<Player>();
		running=new HashSet<Player>();
		//ammo=new HashMap<Player,Map<String,Integer>>();
		pumpkin=new ItemStack(Material.PUMPKIN);
		ItemMeta meta=pumpkin.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE+"Eye Protection");
		pumpkin.setItemMeta(meta);
		gun=new HashMap<Player,Map<String,gun>>();
	}
	Player target;
	Set<String> guns;
	Set<Player> sneaking,running;
	//public Map<Player,Map<String,Integer>> ammo;
	ItemStack pumpkin;
	public Map<Player,Map<String,gun>>gun;
	String name;
	@EventHandler
	public void RC(PlayerInteractEvent event){
		System.out.println("Event");
		if(event.getPlayer()!=null&&event.getItem()!=null&&event.getItem().getItemMeta()!=null&&event.getItem().getItemMeta().getDisplayName()!=null/*&&gun.containsKey(event.getItem())*/){
			target=event.getPlayer();
			for(String name:guns){
				if(event.getItem().getItemMeta().getDisplayName().contains(name)){
					for(gun gu:gun.get(target).get(name)){
						ItemStack g=gu.getObject();
						if(event.getItem()!=g&&event.getItem().getItemMeta().getLore().get(1).equals(g.getItemMeta().getLore().get(1))&&event.getItem().getItemMeta().getLore().get(0).equals(g.getItemMeta().getLore().get(0))){
							gun.remove(g);
							gun.get(target).put(name,new gun(Main,event.getItem().clone(),name,target,null));
						}
						break;
					}
				}
				break;
			}
			if(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK||event.getAction()==Action.PHYSICAL){ //TODO: Ingame check?
				for(String name:guns){
					if(event.getItem().getItemMeta().getLore().get(0).contains(name)){
						this.name=name;
					}
				}
				if(!sneaking.contains(target)&&!running.contains(target)){
						if(event.getItem().getItemMeta().getLore().get(0).contains(name)){
							fire(name,"",event.getItem());
						}
				}else if(sneaking.contains(target)){
						if(event.getItem().getItemMeta().getLore().get(0).contains(name)){
							fire(name,"S",event.getItem());
						}
				}else{
						if(event.getItem().getItemMeta().getLore().get(0).contains(name)){
							fire(name,"R",event.getItem());
						}
				}
			}else if(event.getAction()==Action.LEFT_CLICK_AIR||event.getAction()==Action.LEFT_CLICK_BLOCK&&event.getItem()!=null){
				for(String name:guns){
					if(event.getItem()!=null&&event.getItem().getItemMeta()!=null&&event.getItem().getItemMeta().getLore().get(0).contains(name)){ //TODO: Maybe add in another if statement
						target.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.prefix+"Reloading your "+gun.get(target).get(name).getGunName()));
						gun.get(target).get(name).setDisplayName(event.getItem(),"&a&l- - - RELOADING - - -");
						reload(event.getItem());
						break;
					}
				}
			}
		}
	}
	private void fire(String name,String add,ItemStack item){ /** @param add: Adds prefix to the config variable; can be R, S, or empty. */
		if(gun.get(target).get(name).getCurrentAmmo()>0){
			double rX=Math.random()/Main.getConfig().getDouble("guns."+name+"."+add+"accuracyDbl"); //Gets a random number between the min and max that you put in the command 
			double rY=Math.random()/Main.getConfig().getDouble("guns."+name+"."+add+"accuracyDbl"); // "                                                                       
			double rZ=Math.random()/Main.getConfig().getDouble("guns."+name+"."+add+"accuracyDbl"); // "                                                                       
			double cX=Main.getConfig().getDouble("guns."+name+"."+add+"correctXdbl");                                                                                          
			double cY=Main.getConfig().getDouble("guns."+name+"."+add+"correctYdbl");                                                                                          
			double cZ=Main.getConfig().getDouble("guns."+name+"."+add+"correctZdbl");                                                                                          
			double spd=Main.getConfig().getDouble("guns."+name+"."+add+"speedDbl");                                                                                            
			Snowball bullet=target.getWorld().spawn(target.getEyeLocation().add(0,-.5,0),Snowball.class); // Put a snowball in the player's head                                      
			bullet.setVelocity((target.getEyeLocation().getDirection().multiply(spd)).add(new Vector(rX-cX,rY-cY,rZ-cZ))); // Speed up the bullet 4x                     
			bullet.setShooter(target); //Makes sure the snowball passes through the person that shot it | -.55 -.3 -.45
			bullet.setMetadata("bulletData",new FixedMetadataValue(Main,gun.get(target).get(name).getClass()));
			gun.get(target).get(name).decAmmo(item);
			System.out.println("Fired");
		}
	}
	public void reload(ItemStack item){
		gun.get(target).get(name).reloadAmmo(item);
	}
	@EventHandler
	public void sneaking(PlayerToggleSneakEvent event){
		if(event.getPlayer()!=null){
			target=event.getPlayer();
			if(event.isSneaking()){
				sneaking.add(target);
				if(target.getItemInHand()==null||target.getItemInHand().getItemMeta()==null)return;
				if(target.getItemInHand().getItemMeta().getLore().get(0).contains("SSG 08")||target.getItemInHand().getItemMeta().getLore().get(0).contains("AWP")){ //Sniper
					System.out.println("AWP/ SSG 08");
					target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,2000,-10,false,false));
					target.setWalkSpeed(-.15F); //Credit for number/idea? goes to Gawdzahh
					target.getInventory().setHelmet(pumpkin);
					target.updateInventory();
				}else if(target.getItemInHand().getItemMeta().getLore().get(0).contains("SSG553")||target.getItemInHand().getItemMeta().getLore().get(0).contains("AUG")){ //Normal
					target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,2000,5,false,false));
					target.setWalkSpeed(-.15F);
				}
			}else{
				for(PotionEffect pe:target.getActivePotionEffects()){
					target.removePotionEffect(pe.getType());
				}
				target.setWalkSpeed(.2F);
				target.getInventory().setHelmet(new ItemStack(Material.AIR));
				target.updateInventory();
				if(sneaking.contains(target)){
					sneaking.remove(target); //-.15F .2F
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