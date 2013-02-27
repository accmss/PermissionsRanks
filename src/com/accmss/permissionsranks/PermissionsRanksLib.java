package com.accmss.permissionsranks;


import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.accmss.permissionsranks.Country;


public class PermissionsRanksLib {


//VARS
protected static int players;
public static Map<String, String> playerRank = new HashMap<String, String>();


//LOGGING
public static void Chat(CommandSender sender, String PluginName, String message)
{

	sender.sendMessage("" + PluginName + " " + message);
	
}
public static void LogCommand(String player, String command){
	 
	PermissionsRanks.zLogger.info("[PLAYER_COMMAND] " + player + ": " + command);

}
	
public static String CleanName(String name, Boolean TrimRank)
{

	String s = null;

	//BUG with java substring and the .substring(3) or stringbuilder concating to null again.
	//NOTE character § is ALT-0167
	s = name;
	
	//old format (code not needed)
	s = s.replace("miner", "");
	s = s.replace("§0", "");
	s = s.replace("§1", "");
	s = s.replace("§2", "");
	s = s.replace("§3", "");
	s = s.replace("§4", "");
	s = s.replace("§5", "");
	s = s.replace("§6", "");
	s = s.replace("§7", "");
	s = s.replace("§8", "");
	s = s.replace("§9", "");
	s = s.replace("§a", "");
	s = s.replace("§b", "");
	s = s.replace("§c", "");
	s = s.replace("§d", "");
	s = s.replace("§e", "");
	s = s.replace("§f", "");

	
	/*
		//remove all ranks
		if (TrimRank)
		{
		s = s.replace("Own", "");
		s = s.replace("Adm", "");
		s = s.replace("Mod", "");
		s = s.replace("Cre", "");
		s = s.replace("Min", "");
		s = s.replace("VIP", "");
		}
*/
	
		do
		{
		s = StripBrackets1(s);
		} while ( ! (! (s.contains("(")) | ! (s.contains(")"))));
	
		do
		{
		s = StripBrackets2(s);
		} while ( ! (! (s.contains("[")) | ! (s.contains("]"))));

	s = (String)StripSymbols(s);
	s = s.substring(0, Math.min(9, s.length()));

	return s;

}

//E
public static void PlayersListActive(boolean Dispatch)
{
	Player[] players;
	players = PermissionsRanks.zPlugin.getServer().getOnlinePlayers();
	PermissionsRanksLib.players = 0;
	
	    for (Player player : players) 
	    {
	    PlayerRank(player, CleanName(player.getDisplayName(), true), Dispatch);
	    }

}

//G
public static String getWorldName(String name)
{
	int i;
	String temp[];
	String wname= "";
	temp = name.split("_");
	
		if (temp.length == 1)
		{
		return temp[0];
		}
			
		for(i = 1; i < temp.length ; i++)
		{
		wname = wname + temp[i];	
		}

	return wname;

}
public static String GetRank(String name)
{


	return playerRank.get(name);
}

public static void PlayerRank(Player player, String cleanname, boolean dispatch)
{
	char space = ' ';
	
	String rank1;
	String rank2;
	//String code = "AU";
	String code1 = "AU";
	

		if 	    (player.hasPermission("PermissionsRanks.operator"))
		{
		rank1 = PermissionsRanksConfig.RankColorOps;// + "Operator";
		rank2 = "Ops";
		}
		else if (player.hasPermission("PermissionsRanks.admin"))
		{
		rank1 = PermissionsRanksConfig.RankColorAdm;// + "Admin";
		rank2 = "Adm";
		}
		else if (player.hasPermission("PermissionsRanks.mod"))
		{
		rank1 = PermissionsRanksConfig.RankColorMod;// + "Moderator";
		rank2 = "Mod";
		}
		else if (player.hasPermission("PermissionsRanks.creative"))
		{
		rank1 = PermissionsRanksConfig.RankColorCre;// + "Creative";
		rank2 = "Cre";
		}
		else if (player.hasPermission("PermissionsRanks.whitelist"))
		{
		rank1 = PermissionsRanksConfig.RankColorWht;// + "Whitelist";
		rank2 = "VIP";
		}
		else if (player.hasPermission("PermissionsRanks.player"))
		{
		rank1 = PermissionsRanksConfig.RankColorPly;// + "Player";
		rank2 = "Pla";
		}
		else 
		{
		rank1 = PermissionsRanksConfig.RankColorPly;// + "Player";
		rank2 = "N/A";
		}
		

	//COUNTRY CODE
	if (PermissionsRanks.country_enabled)
	{
	InetAddress add;
	Country cnt;
	add = player.getAddress().getAddress();
	cnt = PermissionsRanks.GEOIP.getCountry(add);
	code1 = cnt.getCode();
	}
	else
	{
	code1 = "CC";
	}

	
	PermissionsRanksLib.players++;
	PermissionsRanksLib.playerRank.put(player.getName(), rank1);
	OnlineAdd(player.getName(), code1 + ChatColor.GRAY + "." + ChatColor.GRAY + rank2.substring(0, 3)  + ChatColor.GRAY + "." + ChatColor.GRAY + StringPad(cleanname, 10, space) + ".");	
	if (dispatch)
	{
	player.setDisplayName(cleanname);
	}

	
}




//O
public static void OnlineAdd(String name, String code){
	 
		if(PermissionsRanks.playerCode.containsKey(name))
		{
		PermissionsRanks.playerCode.put(name, code);
		} 
		else
		{
		PermissionsRanks.playerCode.put(name, code);
		}

}
public static void Online2Console()
{

	int c = 1;
	String worldname;
		for (Map.Entry<String, String> entry : PermissionsRanks.playerCode.entrySet()) 
		{ 
		String key = entry.getKey(); 
		Object value = entry.getValue(); 
		worldname = PermissionsRanks.zPlugin.getServer().getPlayer(key.toString()).getWorld().getName().toUpperCase();
		worldname = getWorldName(worldname);
		worldname = worldname.substring(0, Math.min(5, worldname.length()));
		Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "Online",  ChatColor.GRAY + String.format("%03d", c) + ChatColor.WHITE +"." + ChatColor.GRAY + worldname + ChatColor.WHITE + "." + ChatColor.GRAY + value.toString().toUpperCase() + " " + ChatColor.WHITE + key.toString().toUpperCase());
		//Chat(player, "PermissionsRanks", ChatColor.GRAY + String.format("%03d", c) + ChatColor.WHITE +"." + ChatColor.GRAY + worldname + ChatColor.WHITE + "." + ChatColor.GRAY + value.toString().toUpperCase() + " " + ChatColor.WHITE + key.toString().toUpperCase());
		//PermissionsRanks.log.info(String.format("%03d", c) + "." + worldname + "." +  value.toString().toUpperCase() + " " + key.toString().toUpperCase());
	    c++;
		}

	c--;
	Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "Online", ChatColor.WHITE + Integer.toString(c) + ChatColor.GRAY + " Players online!");

}
public static void Online2Chat(Player player)
{
	
	int c = 1;
	String worldname;
		for (Map.Entry<String, String> entry : PermissionsRanks.playerCode.entrySet()) 
		{ 
		String key = entry.getKey(); 
		Object value = entry.getValue(); 
		worldname = PermissionsRanks.zPlugin.getServer().getPlayer(key.toString()).getWorld().getName().toUpperCase();
		worldname = getWorldName(worldname);
		worldname = worldname.substring(0, Math.min(5, worldname.length()));
		Chat(player, "Online", ChatColor.GRAY + String.format("%03d", c) + ChatColor.WHITE +"." + ChatColor.GRAY + worldname + ChatColor.WHITE + "." + ChatColor.GRAY + value.toString().toUpperCase() + " " + ChatColor.WHITE + key.toString().toUpperCase());
	    c++;
		}
	
	c--;
	Chat(player, "Online", ChatColor.WHITE + Integer.toString(c) + ChatColor.GRAY + " Players online!");
	
}


//S
public static String StringZero(int num, int digits)
{

    String output = Integer.toString(num); 
    while (output.length() < digits) output = "0" + output; 
    return output; 

} 
public static String StringPad(String str, int size, char padChar) 
{ 
	
	if (str.length() > size ) //CAPP 14
	{
	str=str.substring(0, size);
	}
	
  StringBuffer padded = new StringBuffer(str); 
  
 
  while (padded.length() < size) //SET to 15
  { 
    padded.append(padChar); 
  } 
  return padded.toString(); 
} 
public static String StripBrackets1(String name)
{

int a = 0;
int b = 0;

	a = name.indexOf("(");
	b = name.indexOf(")");

		if (a > -1 & b > -1)
		{
		return name.substring(0, a) + name.substring(b + 1);
		}

	return name;

}
public static String StripBrackets2(String name)
{

int a = 0;
int b = 0;

	a = name.indexOf("[");
	b = name.indexOf("]");

		if (a > -1 & b > -1)
		{
		return name.substring(0, a) + name.substring(b + 1);
		}

	return name;

}
public static String StripSymbols(String name) 
{  
   return name.replaceAll("[^a-zA-Z0-9]","");  
}  




}
