package me.darcade.minecraftlottery;

import java.sql.*;

import org.bukkit.plugin.java.JavaPlugin;

public class SQLitehandler extends JavaPlugin {

	private String databasedir;

	public SQLitehandler(String databasedir) {
		this.databasedir = databasedir;
	}

	public void init() {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(databasedir);
			//System.out.println("[MinecraftLottery] Opened Database successfully");

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS lotterytable (username TEXT , lastlottery NUMERIC, lastlotteryyear NUMERIC ,PRIMARY KEY(username));";

			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
			//System.out.println("[MinecraftLottery] Table successfully created");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
		}
	}

	public int getlastlottery(String username) {

		Connection c = null;
		Statement stmt = null;
		int result = 0;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(databasedir);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "SELECT lastlottery FROM lotterytable WHERE username=\""
					+ username + "\" LIMIT 1;";

			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				result = rs.getInt("lastlottery");
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return result;
	}

	public int getlastlotteryyear(String username) {

		Connection c = null;
		Statement stmt = null;
		int result = 0;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(databasedir);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			String sql = "SELECT lastlotteryyear FROM lotterytable WHERE username=\""
					+ username + "\" LIMIT 1;";

			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				result = rs.getInt("lastlotteryyear");
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return result;
	}
	
	public void setlastlottery(String username, int lastlottery, int lastlotteryyear) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(databasedir);
			c.setAutoCommit(false);
			stmt = c.createStatement();

			String sql = "UPDATE lotterytable SET lastlottery=\'" + lastlottery
					+ "\' WHERE username=\"" + username + "\";";

			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public void setnewlastlottery(String username, int lastlottery, int lastlotteryyear) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(databasedir);
			c.setAutoCommit(false);
			stmt = c.createStatement();

			// TODO prevent SQL Injections
			String sql = "INSERT INTO lotterytable (username, lastlottery, lastlotteryyear) VALUES('"
					+ username + "', " + lastlottery + "', " + lastlotteryyear +");";

			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
