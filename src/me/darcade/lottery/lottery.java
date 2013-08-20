package me.darcade.lottery;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class lottery extends JavaPlugin{
	int[] blacklist = new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40};
	
	@Override
	public void onDisable() {
		System.out.println("Lottery plugin disabled!");
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile descFile = this.getDescription();
		System.out.println("Lottery plugin enabled!");
		System.out.println("Plugin Version: " + descFile.getVersion());
	}
	
	
	private boolean checkforblacklist(int vartocheck){
		boolean output = false;
		
	    for(int k : blacklist){
	    	if (vartocheck == k){
	    		output = true;
	    	}
	    }
		
		
		return output;
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		
		
		
		boolean returnvar = false;
		
		Player p = (Player)sender;
		
		if(cmd.getName().equalsIgnoreCase("lottery")){
			if (p.hasPermission("lottery")){
			if(args.length == 0){
				if(p.getInventory().contains(266)){
					//AIR
					ItemStack airstack = new ItemStack(190);
					
					//amount of gold
					ItemStack itemstack = new ItemStack(Material.GOLD_INGOT, 1);
					
				    int randomNum = new Random().nextInt((421 + 1) - 1) + 1;
				    int randomAmount = new Random().nextInt((64 + 1) - 1) + 1;

				    //won item
				    ItemStack wonitem = new ItemStack(randomNum, randomAmount);
				    
				    while(wonitem.getType() == airstack.getType() | this.checkforblacklist(wonitem.getTypeId())){
						//amount of gold
						itemstack = new ItemStack(Material.GOLD_INGOT, 1);
						
					    randomNum = new Random().nextInt((421 + 1) - 1) + 1;
					    randomAmount = new Random().nextInt((64 + 1) - 1) + 1;
					    
					    //won item
					    wonitem = new ItemStack(randomNum, randomAmount);

				    }
				    if(wonitem.getMaxStackSize() == 1){
					    wonitem = new ItemStack(randomNum, 1);
						p.getInventory().removeItem(itemstack);
						p.sendMessage(ChatColor.GREEN + "Gold wurde abgenommen.");
						p.sendMessage("Du bekommst: " + wonitem.getType());
						p.getInventory().addItem(wonitem);
				    }
				    else {
					p.getInventory().removeItem(itemstack);
					p.sendMessage(ChatColor.GREEN + "Gold wurde abgenommen.");
					p.sendMessage("Du bekommst: " + wonitem.getType());
					p.getInventory().addItem(wonitem);
				    }
				}
					
				else {
					p.sendMessage(ChatColor.RED + "Du hast kein Gold im Inventar");
				}
				returnvar = true;
			}
			returnvar = false;
		}
			returnvar = false;
		}
		return returnvar;
	}
	
	
	
}
