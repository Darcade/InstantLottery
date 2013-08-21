package me.darcade.lottery;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.darcade.lottery.SQLitehandler;

public class lottery extends JavaPlugin {
	
	String databasedir = "jdbc:sqlite:plugins/lottery/database.sqlite";
	
	
	SQLitehandler sqlitehandler = new SQLitehandler(databasedir);
	static final Logger log = Bukkit.getLogger();
	

	
	String rowcount;

	@Override
	public void onDisable() {
		System.out.println("Lottery plugin disabled!");
	}

	@Override
	public void onEnable() {
		//System.out.println(databasedir);
		boolean success = (new File(this.getDataFolder().getAbsolutePath())).mkdirs();
		
		if (!success){
			System.out.println("[lottery] could not create plugin directory");
		}
		
		sqlitehandler.init();
		
		PluginDescriptionFile descFile = this.getDescription();
		this.createConfig();

		System.out.println("[lottery] plugin enabled!");
		System.out.println("Plugin Version: " + descFile.getVersion());
	}

	private boolean checkforblacklist(int vartocheck) {
		List<Integer> blacklist = this.getConfig().getIntegerList(
				"itemblacklist");

		boolean output = false;

		for (int k : blacklist) {
			if (vartocheck == k) {
				output = true;
			}
		}

		return output;
	}


	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {

		// init what we need
		Calendar ca1 = Calendar.getInstance();

		Player p = (Player) sender;

		String username = p.getDisplayName();
		
		int lastlottery = sqlitehandler.lastlottery(username); 

		int DAY_OF_YEAR = ca1.get(Calendar.DAY_OF_YEAR);

		int maxprice = this.getConfig().getInt("max-price");
		int itemtopay = this.getConfig().getInt("itemtopay");
		int amounttopay = this.getConfig().getInt("amounttopay");
		String denymessage = this.getConfig().getString("Message.deny");
		String allowmessage = this.getConfig().getString("Message.allow");
		String noitemone = this.getConfig().getString("Message.noitem.1");
		String noitemtwo = this.getConfig().getString("Message.noitem.2");
		
		// Item to pay
		ItemStack itemstack = new ItemStack(itemtopay, amounttopay);


		if (cmd.getName().equalsIgnoreCase("lottery")) {
			if (p.hasPermission("lottery")) {
				if (args.length == 0) {
					if (DAY_OF_YEAR != lastlottery) {
						if (p.getInventory().containsAtLeast(itemstack,
								amounttopay)) {
							
							
							int output = sqlitehandler.lastlottery(username); 
							
							
							if (output == 0) {
								sqlitehandler.setnewlastlottery(username, DAY_OF_YEAR);
							} else {
								sqlitehandler.setlastlottery(username, DAY_OF_YEAR);
							}
							// AIR
							ItemStack airstack = new ItemStack(190);

							int randomNum = new Random().nextInt((421 + 1) - 1) + 1;
							int randomAmount = new Random()
									.nextInt((maxprice + 1) - 1) + 1;

							// won item
							ItemStack wonitem = new ItemStack(randomNum,
									randomAmount);

							while (wonitem.getType() == airstack.getType()
									| this.checkforblacklist(wonitem
											.getTypeId())) {
								// amount of gold
								itemstack = new ItemStack(itemtopay,
										amounttopay);

								randomNum = new Random().nextInt((421 + 1) - 1) + 1;
								randomAmount = new Random()
										.nextInt((maxprice + 1) - 1) + 1;

								// won item
								wonitem = new ItemStack(randomNum, randomAmount);

							}

							p.getInventory().removeItem(itemstack);

							p.sendMessage(ChatColor.GREEN + allowmessage + " " + wonitem.getType());
							p.getInventory().addItem(wonitem);

						}

						else {
							p.sendMessage(ChatColor.RED + noitemone + " "
									+ itemstack.getType().toString()
									+ " " + noitemtwo);
						}

					}
					else {
						p.sendMessage(ChatColor.RED + denymessage);
					}
				}
			}
		}
		return true;
	}

	public void createConfig() {
		this.saveDefaultConfig();
		System.out.println("[lottery] checking config...");
	}

}