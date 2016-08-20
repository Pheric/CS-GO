package com.arcanemc.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.arcanemc.listeners.DamageEvent;
import com.arcanemc.listeners.InteractEvent;
import com.arcanemc.other.gun;

public class Main extends JavaPlugin{
	@Override
	public void onEnable(){
		this.saveDefaultConfig();
		ie=new InteractEvent(this);
		new DamageEvent(this);
		guns=(Set<String>)this.getConfig().getConfigurationSection("guns").getKeys(false);
	}
	@Override
	public void onDisable(){
		cleanUp();
	}
	Player target;
	public String prefix="&8[&3&lCS:GO&8] &7&o";
	InteractEvent ie;
	Set<String> guns;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender != null && sender instanceof Player) {
			target = (Player) sender;
			if(lbl.equalsIgnoreCase("getgun")){
				for(String name:guns){
					if(args[0].equalsIgnoreCase(name)){
						ItemStack gun=new ItemStack(Material.getMaterial(this.getConfig().getString("guns."+name+".material")));
						ItemMeta meta=gun.getItemMeta();
						List<String>lore=new ArrayList<String>();
						lore.add(ChatColor.translateAlternateColorCodes('&',this.getConfig().getString("guns."+name+".displayName")));
						lore.add(ChatColor.BLACK+target.getName());
						meta.setLore(lore);
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a"+this.getConfig().getInt("guns."+name+".maxAmmo")+"&7/"+this.getConfig().getInt("guns."+name+".maxAmmo")));
						gun.setItemMeta(meta);
						target.getInventory().addItem(gun);
						if(ie.gun.containsKey(gun)){
							ie.gun.remove(gun);
							Map<String,gun>a=new HashMap<String,gun>();
							a.put(name,new gun(this,gun,name,target,null));
							ie.gun.put(target,a);
						}else{
							Map<String,gun>a=new HashMap<String,gun>();
							a.put(name,new gun(this,gun,name,target,null));
							ie.gun.put(target,a);
						}
//						if(ie.ammo.get(target)==null){
//							Map<String,Integer> gunInfo=new HashMap<String,Integer>();
//							gunInfo.put(name,this.getConfig().getInt("guns."+name+".maxAmmo"));
//							ie.ammo.put(target,gunInfo);
//							System.out.println("Created "+gun.getItemMeta().getDisplayName());
//						}else if(!ie.ammo.get(target).containsKey(name)){
//							ie.ammo.get(target).put(name,this.getConfig().getInt("guns."+name+".maxAmmo"));
//							System.out.println("Added "+gun.getItemMeta().getDisplayName());
//						}
						break;
					}
				}
			}
		}
		return true;
	}
	public void cleanUp(){
		//TODO: if(notingame)return;
		/**--TEMPORARY SOLUTION--*/
		for(Player player:Bukkit.getServer().getOnlinePlayers()){
			player.getInventory().clear();
			player.updateInventory();
		}
	}
}