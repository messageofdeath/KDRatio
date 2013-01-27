package me.messageofdeath.KDRatio;

import me.messageofdeath.Database.YamlDatabase;

public class Backend {
	
	private static YamlDatabase database;
	
	protected static void init() {
		database = new YamlDatabase(KDRatio.plugin, "database");
		database.onStartUp();
	}
	
	public static void setDeaths(String name, int deaths) {
		database.set(name+".Deaths", deaths);
	}
	
	public static void setKills(String name, int kills) {
		database.set(name+".Kills", kills);
	}
	
	public static int getDeaths(String name) {
		return database.getInteger(name+".Deaths", 0);
	}
	
	public static int getKills(String name) {
		return database.getInteger(name+".Kills", 0);
	}
}