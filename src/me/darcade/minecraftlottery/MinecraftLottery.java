package me.darcade.minecraftlottery;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.darcade.minecraftlottery.SQLitehandler;

public class MinecraftLottery extends JavaPlugin {

	SQLitehandler sqlitehandler;
	WhitelistHandler whitelisthandler = new WhitelistHandler(this);

	static final Logger log = Bukkit.getLogger();

	String rowcount;

	@Override
	public void onDisable() {
		System.out
				.println("[MinecraftLottery] MinecraftLottery plugin disabled!");
	}

	@Override
	public void onEnable() {
		// setup SQLite database and
		String databasedir = "jdbc:sqlite:" + this.getDataFolder().toString()
				+ "/database.sqlite";

		sqlitehandler = new SQLitehandler(databasedir);

		if (!new File(this.getDataFolder().getAbsolutePath()).mkdirs()) {
			System.out
					.println("[MinecraftLottery] Could not create plugin directory");
		}

		sqlitehandler.init();

		PluginDescriptionFile descFile = this.getDescription();

		whitelisthandler.saveDefaultWhitelist();
		whitelisthandler.reloadWhitelist();
		getCommand("lottery").setExecutor(
				new CommandExecutorClass(this, sqlitehandler));

		checkConfigVersion();

		this.createConfig();

		System.out.println("[MinecraftLottery] Plugin enabled!");
		System.out.println("[MinecraftLottery] Plugin Version: "
				+ descFile.getVersion());
	}

	private void createConfig() {
		this.saveDefaultConfig();
		System.out.println("[MinecraftLottery] Checking config...");
	}

	private void checkConfigVersion() {

		File oldconfig = new File(this.getDataFolder() + "/config.yml");
		File movedconfig = new File(this.getDataFolder() + "/config_old.yml");

		String latestversion = YamlConfiguration.loadConfiguration(this.getResource("config.yml")).getString("config-version"); 
		String localversion = this.getConfig().getString("config-version");
		
		if (!latestversion.equalsIgnoreCase(localversion) && oldconfig.exists()) {
			log.warning("[MinecraftLottery] The config is not up to date moving it to config_old.yml, and creating a new one.");

			if (!movedconfig.exists()) {
				oldconfig.renameTo(movedconfig);
				System.out.println("latest: '" + latestversion + "' localversion: '" + localversion + "'");
			} else {
				System.err
						.println("[MinecraftLottery] Please first remove the old config called \"config_old.yml\", and then restart the server.");
			}

		}
	}

}