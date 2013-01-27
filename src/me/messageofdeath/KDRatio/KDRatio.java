package me.messageofdeath.KDRatio;

import java.lang.reflect.Field;

import me.messageofdeath.Logging.Log;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_6.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class KDRatio extends JavaPlugin implements Listener {
	
	public static KDRatio plugin;
	private static Log log;
	private static CommandMap cmap;
	
	@Override
	public void onEnable() {
		plugin = this;
		Backend.init();
		getServer().getPluginManager().registerEvents(this, this);
		try{
            if(Bukkit.getServer() instanceof CraftServer){
                final Field f = CraftServer.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap)f.get(Bukkit.getServer());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        CCommand cmd = new CCommand("manly");
        cmap.register("", cmd);
        cmd.setExecutor(this);
		log = new Log(plugin);
		log.info("is now enabled!");
	}
	
	@EventHandler
	public void onpre(PlayerCommandPreprocessEvent event) {
		String[] args = event.getMessage().split(" ");
		if(args[0].equalsIgnoreCase("kd"))
		if(event.getPlayer() instanceof Player) {
			Player player = event.getPlayer();
			if(args.length == 0) {
				player.sendMessage(ChatColor.GOLD + "Kill / Death Ratio");
				player.sendMessage(ChatColor.GREEN + "You have "+Backend.getKills(player.getName())+" kills");
				player.sendMessage(ChatColor.RED + "You have "+Backend.getDeaths(player.getName())+" deaths");
				double ratio = (double)Backend.getKills(player.getName()) / ((double)Backend.getDeaths(player.getName()) == 0 ? 1:(double)Backend.getDeaths(player.getName()));
				player.sendMessage(ratio > 2 ? ChatColor.DARK_GREEN + "Your kill/death ratio is "+ratio+". Nice!":ChatColor.DARK_RED+"Your kill/death ratio is "+ratio+". You need to improve.");
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return false;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Backend.setDeaths(event.getEntity().getName(), Backend.getDeaths(event.getEntity().getName()) + 1);
		if(event.getEntity().getKiller() != null)
			Backend.setKills(event.getEntity().getKiller().getName(), 
					Backend.getKills(event.getEntity().getKiller().getName()) + 1);
	}
}
