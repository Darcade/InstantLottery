package me.darcade.minecraftlottery;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class WhitelistHandler {

	MinecraftLottery minecraftlottery;

	File whitelistfile;
	String filename = "whitelist.yml";
	FileConfiguration whitelist;

	public WhitelistHandler(MinecraftLottery minecraftlottery) {
		this.minecraftlottery = minecraftlottery;

		this.whitelistfile = new File(minecraftlottery.getDataFolder(),
				filename);

	}

	public void saveDefaultWhitelist() {
		if (!whitelistfile.exists()) {
			System.out.println("Whitelist: " + whitelistfile.toString() + "Existert:" + whitelistfile.exists());
			minecraftlottery.saveResource(filename, false);
		}
	}

	public FileConfiguration getWhitelist() {

		return whitelist;
	}

	public void reloadWhitelist() {
		whitelist = YamlConfiguration.loadConfiguration(whitelistfile);
	}

}
