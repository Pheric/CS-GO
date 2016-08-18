package com.arcanemc.main;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.arcanemc.listeners.InteractEvent;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	@Override
	public void onEnable(){
		this.saveDefaultConfig();
		new InteractEvent(this);
		guns=(Set<String>)this.getConfig().getConfigurationSection("guns").getKeys(false);
	}
	Player target;
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
						meta.setDisplayName(ChatColor.RED+name);
						gun.setItemMeta(meta);
						target.getInventory().addItem(gun);
						break;
					}
				}
			}
		}
		return true;
	}
}