package me.darcade.minecraftlottery;

import java.io.File;

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
			System.out.println("Whitelist: "
					+ new File(minecraftlottery.getDataFolder(), filename)
							.toString()
					+ "Existert:"
					+ new File(minecraftlottery.getDataFolder(), filename)
							.exists());
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

}
