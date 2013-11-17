package me.darcade.minecraftlottery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandExecutorClass implements CommandExecutor {

	MinecraftLottery lottery;
	SQLitehandler sqlitehandler;

	public CommandExecutorClass(MinecraftLottery lottery,
			SQLitehandler sqlitehandler) {
		this.lottery = lottery;
		this.sqlitehandler = sqlitehandler;
	}

	private boolean checkforuser(String username) {
		boolean output = false;
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getDisplayName().equalsIgnoreCase(username)) {
				output = true;
			}
		}
		return output;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {

		// init what we need

		LotteryHandler lotteryhandler = new LotteryHandler(lottery,
				sqlitehandler);

		Material itemtopay = Material.valueOf(lottery.getConfig().getString(
				"itemtopay"));
		int amounttopay = lottery.getConfig().getInt("amounttopay");

		if (cmd.getName().equalsIgnoreCase("lottery")) {
			if (!(sender instanceof Player)) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						lottery.reload(null);
						return true;
					} else if (checkforuser(args[0])) {
						int lotteryreturn = lotteryhandler.runLottery(lottery
								.getServer().getPlayer(args[0]));
						switch (lotteryreturn) {
						case (0):
							sender.sendMessage(ChatColor.GREEN
									+ "Lottery has been send.");
							break;
						case (1):
							sender.sendMessage(ChatColor.RED
									+ "The player has no more "
									+ itemtopay.toString() + ".");
							sender.sendMessage(ChatColor.RED
									+ "If you want you can execute '/lottery USERNAME force', to give him the lottery anyway.");
							break;
						case (2):
							sender.sendMessage(ChatColor.RED
									+ "The player already had lottery today.");
							sender.sendMessage(ChatColor.RED
									+ "If you want you can execute '/lottery USERNAME force', to give him the lottery anyway.");
							break;
						case (3):
							sender.sendMessage(ChatColor.RED
									+ "The player has no more space in the inventory.");
							sender.sendMessage(ChatColor.RED
									+ "If you want you can execute '/lottery USERNAME force', to give him the lottery anyway.");
							break;
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.RED
								+ "The user doesn't exist.");
						return true;
					}

				} else if (args.length == 2) {
					if (checkforuser(args[0])
							&& args[1].equalsIgnoreCase("force")) {
						lotteryhandler.forceLottery(lottery.getServer()
								.getPlayer(args[0]));
						sender.sendMessage(ChatColor.GREEN
								+ "Lottery has been send.");
					} else {
						sender.sendMessage(ChatColor.RED
								+ "The user doesn't exist.");
					}
					return true;
				}
				sender.sendMessage("Usage: 'lottery reload' to reload the plugin");
				sender.sendMessage("Usage: 'lottery USERNAME [force]' to do lottery for a specific user.");
				return true;
			}

			Player p = (Player) sender;

			if (p.hasPermission("minecraftlottery") || p.hasPermission("minecraftlottery.admin")) {

				if (args.length == 0) {

					lotteryhandler.runLottery(p);

				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("help")) {
						if (p.hasPermission("minecraftlottery.admin")) {
							p.sendMessage(ChatColor.YELLOW
									+ "/lottery "
									+ ChatColor.WHITE
									+ "Gives the player a random item, requires "
									+ amounttopay + " of "
									+ new ItemStack(itemtopay).getType());
							p.sendMessage(ChatColor.YELLOW
									+ "/lottery"
									+ ChatColor.GRAY
									+ " USERNAME [force] "
									+ ChatColor.WHITE
									+ "As an admin you can execute the lottery for other players. Use the arg "
									+ ChatColor.BOLD + "force"
									+ ChatColor.WHITE + ", to force it.");
						} else {
							p.sendMessage(ChatColor.YELLOW
									+ "/lottery "
									+ ChatColor.WHITE
									+ "Gives the player a random item, requires "
									+ amounttopay + " of "
									+ new ItemStack(itemtopay).getType());
						}
					} else if (args[0].equalsIgnoreCase("reload")) {
						if (p.hasPermission("minecraftlottery.reload"))
							lottery.reload(p);
					} else if (p.hasPermission("minecraftlottery.admin")) {
						if (checkforuser(args[0])) {
							int lotteryreturn = lotteryhandler
									.runLottery(lottery.getServer().getPlayer(
											args[0]));
							switch (lotteryreturn) {
							case (0):
								p.sendMessage(ChatColor.GREEN
										+ "Lottery has been send.");
								break;
							case (1):
								p.sendMessage(ChatColor.RED
										+ "The player has no more "
										+ itemtopay.toString() + ".");
								p.sendMessage(ChatColor.RED
										+ "If you want you can execute '/lottery USERNAME force', to give him the lottery anyway.");
								break;
							case (2):
								p.sendMessage(ChatColor.RED
										+ "The player already had lottery today.");
								p.sendMessage(ChatColor.RED
										+ "If you want you can execute '/lottery USERNAME force', to give him the lottery anyway.");
								break;
							case (3):
								p.sendMessage(ChatColor.RED
										+ "The player has no more space in the inventory.");
								p.sendMessage(ChatColor.RED
										+ "If you want you can execute '/lottery USERNAME force', to give him the lottery anyway.");
								break;
							}
						} else {
							p.sendMessage(ChatColor.RED
									+ "The user doesn't exist.");
						}
					}
				} else if (args.length == 2) {
					if (p.hasPermission("minecraftlottery.admin")) {
						if (checkforuser(args[0])
								&& args[1].equalsIgnoreCase("force")) {
							lotteryhandler.forceLottery(lottery.getServer()
									.getPlayer(args[0]));
							p.sendMessage(ChatColor.GREEN
									+ "Lottery has been send.");
						} else {
							p.sendMessage(ChatColor.RED
									+ "The user doesn't exist.");
						}
					}
				}
			}

		}
		return true;
	}
}
