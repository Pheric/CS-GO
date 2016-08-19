package com.arcanemc.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.arcanemc.main.Main;

import net.md_5.bungee.api.ChatColor;

public class InteractEvent implements Listener{ //Min+(int)(Math.random()*((Max-Min)+1))
	private final Main Main;
	public InteractEvent(Main Main){
		this.Main=Main;
		Bukkit.getServer().getPluginManager().registerEvents(this,Main);
		guns=(Set<String>)Main.getConfig().getConfigurationSection("guns").getKeys(true);
		sneaking=new HashSet<Player>();
		running=new HashSet<Player>();
		ammo=new HashMap<Player,Map<String,Integer>>();
	}
	Player target;
	Set<String> guns;
	Set<Player> sneaking,running;
	public Map<Player,Map<String,Integer>> ammo;
	@EventHandler
	public void RC(PlayerInteractEvent event){
		if(event.getPlayer()!=null){
			target=event.getPlayer();
			if(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK||event.getAction()==Action.PHYSICAL&&event.getItem()!=null){ //TODO: Ingame check?
				if(!sneaking.contains(target)&&!running.contains(target)){
					for(String name:guns){
						if(event.getItem().getItemMeta().getDisplayName().contains(name)){
							fire(name,"",event.getItem());
						}
					}
				}else if(sneaking.contains(target)){
					for(String name:guns){
						if(event.getItem().getItemMeta().getDisplayName().contains(name)){
							fire(name,"S",event.getItem());
							break;
						}
					}
				}else{
					for(String name:guns){
						if(event.getItem().getItemMeta().getDisplayName().contains(name)){
							fire(name,"R",event.getItem());
							break;
						}
					}
				}
			}else if(event.getAction()==Action.LEFT_CLICK_AIR||event.getAction()==Action.LEFT_CLICK_BLOCK&&event.getItem()!=null){
				for(String name:guns){
					if(event.getItem().getItemMeta().getDisplayName().contains(name)){
						target.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.prefix+"Reloading your "+Main.getConfig().getString("guns."+name+".displayName")));
						ItemMeta meta=event.getItem().getItemMeta();
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a&l- - - RELOADING - - -"));
						event.getItem().setItemMeta(meta);
						target.updateInventory();
						reload(target,name,event.getItem());
						break;
					}
				}
			}
		}
	}
	private void fire(String name,String add,ItemStack item){ /** @param add: Adds prefix to the config variable; can be R, S, or empty. */
		if(ammo.get(target).get(name).intValue()>0){
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
			bullet.setMetadata(name+","+target.getUniqueId(),new FixedMetadataValue(null,null));
			ammo.get(target).replace(name,ammo.get(target).get(name).intValue()-1);
			String code="&a";
			if (ammo.get(target).get(name).intValue()<(Main.getConfig().getInt("guns."+name+".maxAmmo")*.25)){
				if(ammo.get(target).get(name).intValue()<(Main.getConfig().getInt("guns."+name+".maxAmmo")*.1)){
					code="&c";
				}else{
					code="&6";
				}
			}else{
				code="&a";
			}
			ItemMeta meta=item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',Main.getConfig().getString("guns."+name+".displayName")+" "+code+ammo.get(target).get(name).intValue()+"&7/"+Main.getConfig().getInt("guns."+name+".maxAmmo")));
			item.setItemMeta(meta);
			target.updateInventory();
		}
	}
	public void reload(Player target,String name,ItemStack item){
		new BukkitRunnable(){
			public void run(){
				ammo.get(target).replace(name,Main.getConfig().getInt("guns."+name+".maxAmmo"));
				target.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.prefix+"Your "+Main.getConfig().getString("guns."+name+".displayName")+"&7&o has been reloaded!"));
				ItemMeta meta=item.getItemMeta();
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',Main.getConfig().getString("guns."+name+".displayName")+" &a"+ammo.get(target).get(name).intValue()+"&7/"+Main.getConfig().getInt("guns."+name+".maxAmmo")));
				item.setItemMeta(meta);
				target.updateInventory();
			}
		}.runTaskLater(Main,Main.getConfig().getLong("guns."+name+".reloadTime"));
	}
	@EventHandler
	public void sneaking(PlayerToggleSneakEvent event){
		if(event.getPlayer()!=null){
			target=event.getPlayer();
			if(event.isSneaking()){
				sneaking.add(target);
				if(target.getItemInHand()==null||target.getItemInHand().getItemMeta()==null)return;
				if(target.getItemInHand().getItemMeta().getDisplayName().contains("SSG 08")||target.getItemInHand().getItemMeta().getDisplayName().contains("AWP")){ //Sniper
					target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,2000,20,false,false));
					target.setWalkSpeed(-.15F); //Credit for number/idea? goes to Gawdzahh
				}else if(target.getItemInHand().getItemMeta().getDisplayName().contains("SSG553")||target.getItemInHand().getItemMeta().getDisplayName().contains("AUG")){ //Normal
					target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,2000,5,false,false));
					target.setWalkSpeed(-.15F);
				}
			}else{
				for(PotionEffect pe:target.getActivePotionEffects()){
					target.removePotionEffect(pe.getType());
				}
				target.setWalkSpeed(.2F);
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