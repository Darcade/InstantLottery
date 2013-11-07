package me.darcade.minecraftlottery;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandExecutorClass implements CommandExecutor {

	MinecraftLottery lottery;
	SQLitehandler sqlitehandler;

	public CommandExecutorClass(MinecraftLottery lottery, SQLitehandler sqlitehandler) {
		this.lottery = lottery;
		this.sqlitehandler = sqlitehandler;
	}

	private boolean checkforblacklist(int vartocheck) {
		List<Integer> blacklist = lottery.getConfig().getIntegerList(
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

		int DAY_OF_YEAR = ca1.get(Calendar.DAY_OF_YEAR);

		int maxprice = lottery.getConfig().getInt("max-price");
		int itemtopay = lottery.getConfig().getInt("itemtopay");
		int amounttopay = lottery.getConfig().getInt("amounttopay");
		String denymessage = lottery.getConfig().getString("Message.deny");
		String allowmessage = lottery.getConfig().getString("Message.allow");
		String noitem = lottery.getConfig().getString("Message.noitem");
		String broadcast = lottery.getConfig().getString("Message.broadcast");
		boolean dobroadcast = lottery.getConfig().getBoolean("do-broadcast");

		// Item to pay
		ItemStack itemstack = new ItemStack(itemtopay, amounttopay);

		if (cmd.getName().equalsIgnoreCase("lottery")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be runned by a player!");
				return false;
			}

			Player p = (Player) sender;
			String username = p.getDisplayName();
			int lastlottery = sqlitehandler.lastlottery(username);

			if (p.hasPermission("lottery")) {

				if (args.length == 0) {
					if (DAY_OF_YEAR != lastlottery) {
						if (p.getInventory().containsAtLeast(itemstack,
								amounttopay)) {

							p.getInventory().removeItem(itemstack);

							int output = sqlitehandler.lastlottery(username);

							if (output == 0) {
								sqlitehandler.setnewlastlottery(username,
										DAY_OF_YEAR);
							} else {
								sqlitehandler.setlastlottery(username,
										DAY_OF_YEAR);
							}
							// AIR
							ItemStack airstack = new ItemStack(190);

							int randomNum = new Random().nextInt(421) + 1;
							int randomAmount = new Random().nextInt(maxprice) + 1;

							// won item
							ItemStack wonitem = new ItemStack(randomNum,
									randomAmount);

							while (wonitem.getType() == airstack.getType()
									| this.checkforblacklist(wonitem
											.getTypeId())) {
								// amount of gold
								itemstack = new ItemStack(itemtopay,
										amounttopay);

								randomNum = new Random().nextInt(421) + 1;
								randomAmount = new Random().nextInt(maxprice) + 1;

								// won item
								wonitem = new ItemStack(randomNum, randomAmount);

							}

							p.sendMessage(ChatColor.GREEN
									+ allowmessage.replaceAll("%AMOUNT%",
											String.valueOf(randomAmount))
											.replaceAll(
													"%WONITEM%",
													String.valueOf(wonitem
															.getType())));
							p.getInventory().addItem(wonitem);

							if (dobroadcast == true) {
								for (Player player : Bukkit.getServer()
										.getOnlinePlayers()) {
									if (player != p) {
										player.sendMessage(ChatColor.GREEN
												+ broadcast
														.replaceAll(
																"%PLAYER%",
																p.getDisplayName())
														.replaceAll(
																"%AMOUNT%",
																String.valueOf(randomAmount))
														.replaceAll(
																"%WONITEM%",
																wonitem.getType()
																		.toString()));
									}
								}
							}

						}

						else {
							p.sendMessage(ChatColor.RED
									+ noitem.replaceAll("%PAYITEM%", itemstack
											.getType().toString()));
						}

					} else {
						p.sendMessage(ChatColor.RED + denymessage);
					}
				} /*
				 * else if (args.length == 1) { if (args[0] == "help") {
				 * p.sendMessage(args[0] + ChatColor.YELLOW + "/lottery " +
				 * ChatColor.WHITE + "Gives the player a random item, requires "
				 * + Material.getMaterial(amounttopay) + " of " +
				 * Material.getMaterial(itemtopay)); } }
				 */
			}

		}
		return true;
	}

}
