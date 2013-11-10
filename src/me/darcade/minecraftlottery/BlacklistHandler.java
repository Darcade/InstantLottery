package me.darcade.minecraftlottery;

import java.io.File;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BlacklistHandler {
	MinecraftLottery minecraftlottery;

	String filename = "blacklist.yml";
	FileConfiguration blacklist = null;

	public BlacklistHandler(MinecraftLottery minecraftlottery) {
		this.minecraftlottery = minecraftlottery;
	}

	public void saveDefaultWhitelist() {
		if (!new File(minecraftlottery.getDataFolder(), filename).exists()) {
			System.out.println("Blacklist: "
					+ new File(minecraftlottery.getDataFolder(), filename)
							.toString()
					+ "Existert:"
					+ new File(minecraftlottery.getDataFolder(), filename)
							.exists());
			minecraftlottery.saveResource(filename, false);
		}
	}

	public FileConfiguration getWhitelist() {
		if (blacklist == null)
			return null;
		return blacklist;
	}

	public void reloadWhitelist() {
		blacklist = YamlConfiguration.loadConfiguration(new File(
				minecraftlottery.getDataFolder(), filename));
	}

	public boolean checkforblacklist(Material itemtocheck) {
		boolean itemblacklisted = false;
		List<String> blackitems = blacklist.getStringList("blacklist");
		for (int i = 0; i < blackitems.size(); i++) {
			if (itemtocheck.equals(Material.valueOf(blackitems.get(i))))
				itemblacklisted = true;
		}

		return true;
	}
}
