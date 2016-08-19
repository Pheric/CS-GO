package com.arcanemc.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.arcanemc.listeners.DamageEvent;
import com.arcanemc.listeners.InteractEvent;

import net.md_5.bungee.api.ChatColor;

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
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',this.getConfig().getString("guns."+name+".displayName")));
						gun.setItemMeta(meta);
						target.getInventory().addItem(gun);
						if(ie.ammo.get(target)==null||!ie.ammo.get(target).containsKey(name)){
							Map<String,Integer> gunInfo=new HashMap<String,Integer>();
							gunInfo.put(name,this.getConfig().getInt("guns."+name+".maxAmmo"));
							ie.ammo.put(target,gunInfo);
							System.out.println("Added gun");
						}
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
			if(ie.ammo.containsKey(player)){
				player.getInventory().clear();
				player.updateInventory();
			}
		}
	}
}