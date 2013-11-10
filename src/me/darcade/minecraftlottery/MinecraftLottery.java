package me.darcade.minecraftlottery;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
		this.checkVersion();

		// setup SQLite database and
		String databasedir = "jdbc:sqlite:" + this.getDataFolder().toString()
				+ "/database.sqlite";

		sqlitehandler = new SQLitehandler(databasedir);

		File plugindir = new File(this.getDataFolder().getAbsolutePath());

		if (!plugindir.exists()) {
			if (!plugindir.mkdirs())
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
		
		System.out.println("[MinecraftLottery] MinecraftLottery Version: "
				+ descFile.getVersion() + " enabled");
	}

	private void createConfig() {
		this.saveDefaultConfig();
		//System.out.println("[MinecraftLottery] Checking config...");
	}

	private void checkConfigVersion() {

		File oldconfig = new File(this.getDataFolder() + "/config.yml");
		File movedconfig = new File(this.getDataFolder() + "/config_old.yml");

		String latestversion = YamlConfiguration.loadConfiguration(
				this.getResource("config.yml")).getString("config-version");
		String localversion = this.getConfig().getString("config-version");

		if (!latestversion.equalsIgnoreCase(localversion) && oldconfig.exists()) {
			log.warning("[MinecraftLottery] The config is not up to date moving it to config_old.yml, and creating a new one.");

			if (!movedconfig.exists()) {
				oldconfig.renameTo(movedconfig);
			} else {
				System.err
						.println("[MinecraftLottery] Please first remove the old config called \"config_old.yml\", and then restart the server.");
			}

		}
	}

	private void checkVersion() {
		UpdateChecker updatechecker = new UpdateChecker();

		if (!this.getDescription().getVersion()
				.equalsIgnoreCase(updatechecker.checkversion())) {
			System.out
					.println("[MinecraftLottery] There is a new update for MinecraftLottery!");
			System.out.println("[MinecraftLottery] Download it here "
					+ updatechecker.getDownload());
		}
	}

	public void reload(Player p){
		p.sendMessage("reloading...");
		this.reloadConfig();
		whitelisthandler.reloadWhitelist();
		p.sendMessage("reload finished.");
	}
}