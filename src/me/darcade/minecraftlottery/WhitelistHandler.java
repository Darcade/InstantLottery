package me.darcade.minecraftlottery;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

//TODO use variable instead of creating every time a new object.
public class WhitelistHandler {

	MinecraftLottery minecraftlottery;

	String filename = "whitelist.yml";
	FileConfiguration whitelist = null;

	public WhitelistHandler(MinecraftLottery minecraftlottery) {
		this.minecraftlottery = minecraftlottery;
	}

	public void saveDefaultWhitelist() {
		if (!new File(minecraftlottery.getDataFolder(), filename).exists()) {
			/*
			 * System.out.println("Whitelist: " + new
			 * File(minecraftlottery.getDataFolder(), filename) .toString() +
			 * "Existert:" + new File(minecraftlottery.getDataFolder(),
			 * filename) .exists());
			 */
			minecraftlottery.saveResource(filename, false);
		}
	}

	public FileConfiguration getWhitelist() {
		if (whitelist == null)
			return null;
		return whitelist;
	}

	public void reloadWhitelist() {
		whitelist = YamlConfiguration.loadConfiguration(new File(
				minecraftlottery.getDataFolder(), filename));
	}

	public Material getRandomItem() {
		Material wonitem = null;
		ColorChecker colorchecker = new ColorChecker(minecraftlottery);
		if (minecraftlottery.getConfig().getString("itemlist")
				.equalsIgnoreCase("whitelist")) {
			List<String> whitelistitems = whitelist.getStringList("whitelist");

			int randomNum = new Random().nextInt(whitelistitems.size()) + 1;
			String item = whitelistitems.get(randomNum);
			// Check for color
			if (colorchecker.checkforcolor(item)) {
				// REPLACECOLOR
			} else {
				wonitem = Material.valueOf(item);
			}
		} else if (minecraftlottery.getConfig().getString("itemlist")
				.equalsIgnoreCase("blacklist")) {
			List<String> whitelistitems = YamlConfiguration.loadConfiguration(
					minecraftlottery.getResource("whitelist.yml"))
					.getStringList("whitelist");

			int whitelistlenght = whitelistitems.size();
			int randomNum = new Random().nextInt(whitelistlenght);
			wonitem = Material.valueOf(whitelistitems.get(randomNum));

			while (new BlacklistHandler(minecraftlottery)
					.checkforblacklist(wonitem)) {
				randomNum = new Random().nextInt(whitelistlenght);
				wonitem = Material.valueOf(whitelistitems.get(randomNum));
			}
		}
		return wonitem;
	}

}
