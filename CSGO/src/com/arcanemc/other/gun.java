package com.arcanemc.other;

//import java.util.HashSet;
//import java.util.Set;

import org.bukkit.entity.Player;

public class gun {
	private Player target;
	//private Set<String> guns;
	private int totalAmmo;
	private int currentAmmo;  //Abandoned
	public gun(Player target){
		this.target=target;
		//this.guns=new HashSet<String>();
		//this.totalAmmo=totalAmmo;
		this.currentAmmo=totalAmmo;
	}
	public void addGun(String gunName,int totalAmmo){
		//guns.add(e);
	}
	public Player getHolder(){
		return target;
	}
	//public String getGun(String gun){
		//return gunName;
	//}
	public int getTotalAmmo(){
		return totalAmmo;
	}
	public int getCurrentAmmo(){
		return currentAmmo;
	}
	public boolean canDeductAmmo(){
		if(currentAmmo<=totalAmmo){
			return false;
		}else{
			return true;
		}
	}
	public void deductAmmo(){
		currentAmmo--;
	}
}
