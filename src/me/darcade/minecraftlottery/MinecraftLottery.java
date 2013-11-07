package me.darcade.minecraftlottery;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.darcade.minecraftlottery.SQLitehandler;

public class MinecraftLottery extends JavaPlugin {

	SQLitehandler sqlitehandler;

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
		this.createConfig();

		getCommand("lottery").setExecutor(
				new CommandExecutorClass(this, sqlitehandler));

		System.out.println("[MinecraftLottery] Plugin enabled!");
		System.out.println("[MinecraftLottery] Plugin Version: "
				+ descFile.getVersion());
	}

	public void createConfig() {
		this.saveDefaultConfig();
		System.out.println("[MinecraftLottery] Checking config...");
	}

}