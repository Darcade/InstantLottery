package me.darcade.minecraftlottery;

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

	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {

		// init what we need


		Material itemtopay = Material.valueOf(lottery.getConfig().getString("itemtopay"));
		int amounttopay = lottery.getConfig().getInt("amounttopay");



		if (cmd.getName().equalsIgnoreCase("lottery")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be runned by a player!");
				return false;
			}

			Player p = (Player) sender;
			LotteryHandler lotteryhandler = new LotteryHandler(lottery, sqlitehandler);

			if (p.hasPermission("lottery") || p.hasPermission("lottery.admin")) {

				if (args.length == 0) {

					lotteryhandler.runLottery(p);
					
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("help")) {
						p.sendMessage(ChatColor.YELLOW + "/lottery "
								+ ChatColor.WHITE
								+ "Gives the player a random item, requires "
								+ amounttopay + " of "
								+ new ItemStack(itemtopay).getType());
					}
					else if(args[0].equalsIgnoreCase("reload")) {
						if(p.hasPermission("lottery.reload"))
							lottery.reload(p);
					}
					else if (p.hasPermission("lottery.admin")){
						lotteryhandler.runLottery(lottery.getServer().getPlayer(args[0]));
					}
				}
			}

		}
		return true;
	}

}
