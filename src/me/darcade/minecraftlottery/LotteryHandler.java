package me.darcade.minecraftlottery;

import java.util.Calendar;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LotteryHandler {

	MinecraftLottery lottery;

	SQLitehandler sqlitehandler;

	public LotteryHandler(MinecraftLottery lottery, SQLitehandler sqlitehandler) {
		this.lottery = lottery;

		this.sqlitehandler = sqlitehandler;
	}

	public int runLottery(Player p) {
		/* 0: all ok
		 * 1: no more item
		 * 2: already had lottery
		 */
		int output;
		
		PlayerMessageReplacer messagereplacer = new PlayerMessageReplacer(
				lottery);
		WhitelistHandler whitelisthandler = new WhitelistHandler(lottery);

		int maxprice = lottery.getConfig().getInt("max-price");
		Material itemtopay = Material.valueOf(lottery.getConfig().getString(
				"itemtopay"));
		int amounttopay = lottery.getConfig().getInt("amounttopay");

		boolean dobroadcast = lottery.getConfig().getBoolean("do-broadcast");
		boolean onceaday = lottery.getConfig().getBoolean("once-a-day");

		Calendar ca1 = Calendar.getInstance();

		// Item to pay
		ItemStack payitem = new ItemStack(itemtopay, amounttopay);

		int DAY_OF_YEAR = ca1.get(Calendar.DAY_OF_YEAR);

		String username = p.getDisplayName();
		int lastlottery = sqlitehandler.lastlottery(username);

		if (DAY_OF_YEAR != lastlottery || !onceaday) {
			if (p.getInventory().containsAtLeast(payitem, amounttopay)) {

				p.getInventory().removeItem(payitem);

				int dboutput = sqlitehandler.lastlottery(username);

				if (dboutput == 0) {
					sqlitehandler.setnewlastlottery(username, DAY_OF_YEAR);
				} else {
					sqlitehandler.setlastlottery(username, DAY_OF_YEAR);
				}

				Material randomitem = whitelisthandler.getRandomItem();
				int randomAmount = new Random().nextInt(maxprice) + 1;

				// won item
				ItemStack wonitem = new ItemStack(randomitem, randomAmount);

				p.sendMessage(ChatColor.GREEN
						+ messagereplacer.getallowmessage(p, randomAmount,
								wonitem, payitem));
				p.getInventory().addItem(wonitem);

				if (dobroadcast == true) {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						if (player != p) {
							player.sendMessage(ChatColor.GREEN
									+ messagereplacer.getbroadcast(p,
											randomAmount, wonitem, payitem));
						}
					}
				}
				
				output = 0;

			}

			else {
				p.sendMessage(ChatColor.RED
						+ messagereplacer.getnoitem(p, payitem));
				output = 1;
			}

		} else {
			p.sendMessage(ChatColor.RED
					+ messagereplacer.getdenymessage(p, payitem));
			output = 2;
		}
		return output;
	}

	public void forceLottery(Player p) {
		PlayerMessageReplacer messagereplacer = new PlayerMessageReplacer(
				lottery);
		WhitelistHandler whitelisthandler = new WhitelistHandler(lottery);

		int maxprice = lottery.getConfig().getInt("max-price");
		Material itemtopay = Material.valueOf(lottery.getConfig().getString(
				"itemtopay"));
		int amounttopay = lottery.getConfig().getInt("amounttopay");

		boolean dobroadcast = lottery.getConfig().getBoolean("do-broadcast");

		Calendar ca1 = Calendar.getInstance();
		
		// Item to pay
		ItemStack payitem = new ItemStack(itemtopay, amounttopay);
		
		int DAY_OF_YEAR = ca1.get(Calendar.DAY_OF_YEAR);

		String username = p.getDisplayName();
		
		int output = sqlitehandler.lastlottery(username);

		if (output == 0) {
			sqlitehandler.setnewlastlottery(username, DAY_OF_YEAR);
		} else {
			sqlitehandler.setlastlottery(username, DAY_OF_YEAR);
		}

		Material randomitem = whitelisthandler.getRandomItem();
		int randomAmount = new Random().nextInt(maxprice) + 1;

		// won item
		ItemStack wonitem = new ItemStack(randomitem, randomAmount);

		p.sendMessage(ChatColor.GREEN
				+ messagereplacer.getallowmessage(p, randomAmount, wonitem,
						payitem));
		p.getInventory().addItem(wonitem);

		if (dobroadcast == true) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (player != p) {
					player.sendMessage(ChatColor.GREEN
							+ messagereplacer.getbroadcast(p,
									randomAmount, wonitem, payitem));
				}
			}

		}
	}
}
