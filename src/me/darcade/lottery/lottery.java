package me.darcade.lottery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class lottery extends JavaPlugin {

	static final Logger log = Bukkit.getLogger();

	private SQLite sqlite;

	String rowcount;

	@Override
	public void onDisable() {
		sqlite.close();
		System.out.println("Lottery plugin disabled!");

	}

	@Override
	public void onEnable() {
		sqlite = new SQLite(log, "[lottery]", this.getDataFolder()
				.getAbsolutePath(), "lotterydb", ".sqlite");

		if (!sqlite.isOpen()) {
			sqlite.open();
		}

		try {
			sqlite.query("CREATE TABLE IF NOT EXISTS lotterytable (username TEXT , lastlottery NUMERIC, PRIMARY KEY(username));");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("************************ " + rowcount);

		PluginDescriptionFile descFile = this.getDescription();
		this.createConfig();

		System.out.println("[Lottery] plugin enabled!");
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
	//FIXME verschönern
	ResultSet lastlotteryresult;
	String lastlottery;
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {

		Calendar ca1 = Calendar.getInstance();
		
		Player p = (Player) sender;
		
		String playername = p.getDisplayName();
		sqlite = new SQLite(log, "[lottery]", this.getDataFolder()
				.getAbsolutePath(), "lotterydb", ".sqlite");

		if (!sqlite.isOpen()) {
			sqlite.open();
		}
		
		try {
			lastlotteryresult = sqlite.query("SELECT lastlottery FROM lotterytable WHERE username=\"" + playername + "\" LIMIT 1;");

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			lastlottery = lastlotteryresult.getString("lastlottery");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		int DAY_OF_YEAR_int=ca1.get(Calendar.DAY_OF_YEAR);
		String DAY_OF_YEAR = Integer.toString(DAY_OF_YEAR_int);
		
		int maxprice = this.getConfig().getInt("max-price");
		int itemtopay = this.getConfig().getInt("itemtopay");
		int amounttopay = this.getConfig().getInt("amounttopay");

		// Item to pay
		ItemStack itemstack = new ItemStack(itemtopay, amounttopay);

		boolean returnvar = false;

		if (cmd.getName().equalsIgnoreCase("lottery")) {
			if (p.hasPermission("lottery")) {
				if (args.length == 0) {
					if (Integer.toString(DAY_OF_YEAR_int) != lastlottery){
					if (p.getInventory()
							.containsAtLeast(itemstack, amounttopay)) {

						
						sqlite = new SQLite(log, "[lottery]", this.getDataFolder()
								.getAbsolutePath(), "lotterydb", ".sqlite");

						if (!sqlite.isOpen()) {
							sqlite.open();
						}
						
						try{
							ResultSet output = sqlite.query("SELECT * FROM lotterytable WHERE username=\"darcade\";");
							
							rowcount = output.getString("username");
							} catch (SQLException e) {
								rowcount = null;
							}
							

						if (rowcount == null){
							try {
								sqlite.query("INSERT INTO lastlottery VALUES(\"" + playername +"\", \"" + DAY_OF_YEAR + "\");");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else{
							try {
								sqlite.query("UPDATE lotterytable SET lastlottery=\'" + DAY_OF_YEAR +"\' WHERE rowid=1;");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						p.sendMessage("lastlottery: " + lastlottery);
						// AIR
						ItemStack airstack = new ItemStack(190);
						
						p.sendMessage(ChatColor.BOLD + "QUERY:");

						int randomNum = new Random().nextInt((421 + 1) - 1) + 1;
						int randomAmount = new Random()
								.nextInt((maxprice + 1) - 1) + 1;

						// won item
						ItemStack wonitem = new ItemStack(randomNum,
								randomAmount);

						while (wonitem.getType() == airstack.getType()
								| this.checkforblacklist(wonitem.getTypeId())) {
							// amount of gold
							itemstack = new ItemStack(itemtopay, amounttopay);

							randomNum = new Random().nextInt((421 + 1) - 1) + 1;
							randomAmount = new Random()
									.nextInt((maxprice + 1) - 1) + 1;

							// won item
							wonitem = new ItemStack(randomNum, randomAmount);

						}

						p.getInventory().removeItem(itemstack);
						p.sendMessage(ChatColor.GREEN
								+ itemstack.getType().toString()
								+ " wurde abgenommen.");
						p.sendMessage("Du bekommst: " + wonitem.getType());
						p.getInventory().addItem(wonitem);

					}

					else {
						p.sendMessage(ChatColor.RED + "Du hast kein "
								+ itemstack.getType().toString()
								+ " im Inventar");
					}
					returnvar = true;
					}
					returnvar = false;
				}
				returnvar = false;
			}
			returnvar = false;
		}
		return returnvar;
	}

	public void createConfig() {
		this.saveDefaultConfig();
		System.out.println("[lottery] checking config...");
	}

}