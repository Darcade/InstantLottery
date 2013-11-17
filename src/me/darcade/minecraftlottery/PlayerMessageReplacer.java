package me.darcade.minecraftlottery;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerMessageReplacer {

	String denymessage, allowmessage, noitem, broadcast, morespace;
	MinecraftLottery lottery;

	public PlayerMessageReplacer(MinecraftLottery lottery) {
		this.lottery = lottery;

		this.denymessage = lottery.getConfig().getString("Message.deny");
		this.allowmessage = lottery.getConfig().getString("Message.allow");
		this.noitem = lottery.getConfig().getString("Message.noitem");
		this.broadcast = lottery.getConfig().getString("Message.broadcast");
		this.morespace = lottery.getConfig().getString("Message.morespace");

	}

	public String getdenymessage(Player p, ItemStack payitem) {
		return denymessage
				.replaceAll("%PLAYER%", p.getDisplayName())
				.replaceAll("%PAYITEM%", payitem.getType().toString());
	}
	
	public String getallowmessage(Player p, int randomAmount, ItemStack wonitem,
			ItemStack payitem) {
		return allowmessage
				.replaceAll("%PLAYER%", p.getDisplayName())
				.replaceAll("%AMOUNT%", String.valueOf(randomAmount))
				.replaceAll("%PAYITEM%", payitem.getType().toString())
				.replaceAll("%WONITEM%", wonitem.getType().toString());
	}
	
	public String getnoitem(Player p, ItemStack payitem) {
		return noitem
				.replaceAll("%PLAYER%", p.getDisplayName())
				.replaceAll("%PAYITEM%", payitem.getType().toString());
	}
	
	public String getbroadcast(Player p, int randomAmount, ItemStack wonitem,
			ItemStack payitem) {
		return broadcast
				.replaceAll("%PLAYER%", p.getDisplayName())
				.replaceAll("%AMOUNT%", String.valueOf(randomAmount))
				.replaceAll("%PAYITEM%", payitem.getType().toString())
				.replaceAll("%WONITEM%", wonitem.getType().toString());
	}
	
	public String getmorespace(Player p, ItemStack payitem) {
		return morespace
				.replaceAll("%PLAYER%", p.getDisplayName())
				.replaceAll("%PAYITEM%", payitem.getType().toString());
	}
}
