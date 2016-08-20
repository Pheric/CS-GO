package com.arcanemc.other;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.arcanemc.main.Main;

import net.md_5.bungee.api.ChatColor;

public class gun implements Iterable<gun>{
	private final Main Main;
	public gun(Main Main,ItemStack object,String gunModel,Player holder,String holderTeam){
		this.Main=Main;
		this.object=object;
		this.holder=holder;
		this.holderTeam=holderTeam;
		displayName=object.getItemMeta().getDisplayName();
		maxAmmo=Main.getConfig().getInt("guns."+gunModel+".maxAmmo");
		currentAmmo=maxAmmo;
		gunName=gunModel;
	}
	int currentAmmo;
	String gunName;
	Player holder;
	String holderTeam;
	int maxAmmo;
	String displayName;
	ItemStack object;
	
	//				Getter Methods
	
	public int getCurrentAmmo(){return currentAmmo;}
	public String getGunName(){return gunName;}
	public Player getGunHolder(){return holder;}
	public String getGunHolderTeam(){return holderTeam;}
	public int getMaxAmmo(){return maxAmmo;}
	public String getDisplayName(){return displayName;}
	public ItemStack getObject(){return object;}
	
	//				Setter Methods
	
	public gun CONVERT_ITEM(ItemStack toConvertTo){
		System.out.println("CONVERTED");
		object=toConvertTo;
		return this;
	}
	public void setDisplayName(ItemStack object,String name){
		displayName=ChatColor.translateAlternateColorCodes('&',name);
		ItemMeta meta=object.getItemMeta();
		meta.setDisplayName(displayName);
		object.setItemMeta(meta);
		holder.updateInventory();
	}
	public void decAmmo(ItemStack object){
		currentAmmo--;
		String code;
		if (this.getCurrentAmmo()<this.getMaxAmmo()*.25){
			if(this.getCurrentAmmo()<this.getMaxAmmo()*.1){
				code="&c";
			}else{
				code="&6";
			}
		}else{
			code="&a";
		}
		displayName=ChatColor.translateAlternateColorCodes('&',code+this.getCurrentAmmo()+"&7/"+this.getMaxAmmo());
		ItemMeta meta=object.getItemMeta();
		meta.setDisplayName(displayName);
		object.setItemMeta(meta);
		holder.updateInventory();
	}
	public void reloadAmmo(ItemStack object){
		new BukkitRunnable(){
			public void run(){
				currentAmmo=maxAmmo;
				holder.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.prefix+"Your "+Main.getConfig().getString("guns."+gunName+".displayName")+"&7&o has been reloaded!"));
				ItemMeta meta=object.getItemMeta();
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a"+currentAmmo+"&7/"+maxAmmo));
				object.setItemMeta(meta);
				holder.updateInventory();
			}
		}.runTaskLaterAsynchronously(Main,Main.getConfig().getLong("guns."+gunName+".reloadTime"));
	}
	@Override
	public Iterator<gun> iterator() {
		return this.iterator();
	}
}
