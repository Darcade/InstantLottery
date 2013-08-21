package me.darcade.lottery;

import java.sql.*;

import org.bukkit.plugin.java.JavaPlugin;


public class SQLitehandler extends JavaPlugin{
	
	//FIXME NO HARDCODE
	private String databasedir = "jdbc:sqlite:plugins/lottery/database.sqlite";
			
			
	public void init() {
		System.out.println("init");
		Connection c = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(databasedir);
			System.out.println("[lottery] Opened Database successfully");
			
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS lotterytable (username TEXT , lastlottery NUMERIC, PRIMARY KEY(username));";
			
			stmt.executeUpdate(sql);
			stmt.close();c.close();
		} catch (Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
		}
		System.out.println("[lottery] Table successfully created");
	}
	
	public int lastlottery(String username) {
		
		System.out.println("lastlottery");
		Connection c = null;
		Statement stmt = null;
		int result = 0;
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(databasedir);
			c.setAutoCommit(false);
			
			stmt = c.createStatement();
			String sql = "SELECT lastlottery FROM lotterytable WHERE username=\""+ username + "\" LIMIT 1;";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			
			
			
			if (rs.next() ) {
				result = rs.getInt("lastlottery");
			}
			
			
			rs.close();
			stmt.close();
			c.close();
			
		} catch (Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
		}
		return result;
	}
	
	public void setlastlottery(String username, int lastlottery){
		System.out.println("setlastlottery");
		Connection c = null;
		Statement stmt = null;

		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(databasedir);
			c.setAutoCommit(false);
			stmt = c.createStatement();
			
			String sql = "UPDATE lotterytable SET lastlottery=\'"
					+ lastlottery + "\' WHERE username=\"" + username + "\";";
			
			stmt.executeUpdate(sql);
			
			
			
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	public void setnewlastlottery(String username, int lastlottery){
		Connection c = null;
		Statement stmt = null;
		
		System.out.println("setmewöastlottery");
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(databasedir);
			c.setAutoCommit(false);
			stmt = c.createStatement();
			
			String sql = "INSERT INTO lotterytable (username, lastlottery) VALUES('" + username + "', " + lastlottery +");";
			
			System.out.println(sql);
			
			stmt.executeUpdate(sql);
			
			
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
