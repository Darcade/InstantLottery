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

		System.out.println(databasedir);
		boolean success = (new File(this.getDataFolder().getAbsolutePath()))
				.mkdirs();

		if (!success) {
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
	
	private void checkConfigVersion(){
		
		File oldconfig = new File(this.getDataFolder() + "/config.yml");
		File movedconfig = new File(this.getDataFolder() + "/config_old.yml"); 
		
		if (oldconfig.exists() && YamlConfiguration.loadConfiguration(this.getResource("config.yml")).getString("config-version") != this.getConfig().getString("config-version"));{
			log.warning("[MinecraftLottery] The config is not up to date moving it to config_old.yml, and creating a new one.");
			
			
			if (!movedconfig.exists()) {
				 
				System.out.println("MOVING " + oldconfig.renameTo(movedconfig) + oldconfig.toString() + movedconfig.toString());
			}
			else {
				System.err.println("[MinecraftLottery] Please first remove the old config called \"config_old.yml\", and then restart the server.");
			}
				
		}
	}

}