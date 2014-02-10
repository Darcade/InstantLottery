package me.darcade.minecraftlottery;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

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

	public ItemStack getRandomItem() {
		Material wonmaterial = null;
		ItemStack wonitem = null;
		int randomamount = new Random().nextInt(minecraftlottery.getConfig().getInt(
				"max-price")) + 1;
		ColorChecker colorchecker = new ColorChecker(minecraftlottery);
		
		this.reloadWhitelist();
		
		if (minecraftlottery.getConfig().getString("itemlist")
				.equalsIgnoreCase("whitelist")) {
			List<String> whitelistitems = this.getWhitelist().getStringList("whitelist");

			int randomNum = new Random().nextInt(whitelistitems.size());
			//System.out.println(randomNum);
			String item = whitelistitems.get(randomNum);
			// Check for color
			if (colorchecker.checkforcolor(item)) {
				wonitem = colorchecker.getColor(item);
			} else {
				wonmaterial = Material.valueOf(item);
				wonitem = new ItemStack(wonmaterial, randomamount);
			}
			
		} else if (minecraftlottery.getConfig().getString("itemlist")
				.equalsIgnoreCase("blacklist")) {
			List<String> whitelistitems = YamlConfiguration.loadConfiguration(
					minecraftlottery.getResource("whitelist.yml"))
					.getStringList("whitelist");

			int whitelistlenght = whitelistitems.size();
			int randomNum = new Random().nextInt(whitelistlenght);
			//System.out.println(randomNum);
			wonmaterial = Material.valueOf(whitelistitems.get(randomNum));

			while (new BlacklistHandler(minecraftlottery)
					.checkforblacklist(wonmaterial)) {
				randomNum = new Random().nextInt(whitelistlenght);
				wonmaterial = Material.valueOf(whitelistitems.get(randomNum));
			}
			wonitem = new ItemStack(wonmaterial, randomamount);
		}
		return wonitem;
	}

}
