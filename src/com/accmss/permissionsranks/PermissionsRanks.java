package com.accmss.permissionsranks;


//IMPORTS - JAVA
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//IMPORTS - BUKKIT
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;


public class PermissionsRanks extends JavaPlugin  {

public static PermissionsRanks zPlugin;
protected static FileConfiguration zConfig;
public static Logger zLogger = Logger.getLogger("Minecraft");



//GLOBALS
public static boolean country_enabled;
public static com.accmss.permissionsranks.LookupService GEOIP; 
public static Map<String, String> playerCode = new HashMap<String, String>();


static long idelay = 0L;
static long repeat = 1200L * 10L; // 1200 = 60 seconds


@Override
public void onEnable() {
	
	zPlugin = this;

	//Settings
	PermissionsRanksConfig.LoadSettings(zPlugin.getFile().getAbsolutePath());


		//geoip
		try
		{
		String file = this.getFile().getAbsolutePath();
		file = file.replace(PermissionsRanksConfig.SlashChar + "PermissionsRanks.jar", "");
		file = file + PermissionsRanksConfig.SlashChar + "EssentialsGeoIP" + PermissionsRanksConfig.SlashChar + "GeoIP.dat";
		PermissionsRanksLib.Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "Loading", file);
		GEOIP = new com.accmss.permissionsranks.LookupService(file);
		country_enabled = true;
		} 
		catch (IOException e)
		{
		PermissionsRanksLib.Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "Loading", PermissionsRanksConfig.SlashChar + ".." + PermissionsRanksConfig.SlashChar + "EssentialsGeoIP" + PermissionsRanksConfig.SlashChar + "GeoIP.dat Not found.");	
		country_enabled = false;
		}
		
	PermissionsRanksLib.PlayersListActive(false);

	Calendar calendar = new GregorianCalendar();
	int ss = calendar.get(Calendar.SECOND);
	int xx = calendar.get(Calendar.MILLISECOND);
	idelay = (1000 - xx) / 50;
	idelay = idelay + ((60 - ss) * 20);
	idelay = idelay - 16; 

	
	this.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
		public void run()
		{
		PermissionsRanksLib.Online2Console();
	    }
	}, idelay, repeat); //20 clicks to a second
	
	
	//Metrics
	try
	{
	PermissionsRanksMetricsLite metrics = new PermissionsRanksMetricsLite(this);
	metrics.start();
	} catch (IOException e) {
	PermissionsRanksLib.Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "[MetricsLite]", e.getCause() + " : " + e.getMessage());
	}
	
	//Listners
	getServer().getPluginManager().registerEvents(new PermissionsRanksPlayer(), this);

}
@Override
public void onDisable() 
{


}


@EventHandler
public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

int icase = 0;
Player player = null;
boolean IsPlayer = false;


		if(sender instanceof Player)
		{
		player = (Player)sender;
		//PermissionsRanksLib.LogCommand(player.getName(), cmd.toString());
		IsPlayer = true;
		}
		else
		{
		//PermissionsRanksLib.LogCommand(sender.getName(), cmd.toString());	
		}


		if (cmd.getName().equalsIgnoreCase("permissionsranks")) icase = 0;
		if (cmd.getName().equalsIgnoreCase("online"))	icase = 1;
		if (cmd.getName().equalsIgnoreCase("promote"))	icase = 2;
		if (cmd.getName().equalsIgnoreCase("demote"))	icase = 3;

		switch (icase) 
		{
		case 0:
			
			if (args.length > 0) //assume reload
			{
			reloadConfig();
			}
			return true;

		case 1:

			if (IsPlayer) 
			{
			PermissionsRanksLib.Online2Chat(player);
			}
			else
			{
			PermissionsRanksLib.Online2Console();
			}
			return true; 

		}

	return false; 

}


}