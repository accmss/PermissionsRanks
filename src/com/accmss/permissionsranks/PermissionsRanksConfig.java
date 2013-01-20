package com.accmss.permissionsranks;


//IMPORT - JAVA
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


/*
# PermissionsRanks
#      rollbackcmd: undo <player>

onChat:
  RankColorPly: '&a'
  RankColorWht: '&7'
  RankColorCre: '&e'
  RankColorMod: '&9' 
  RankColorAdm: '&c'
  RankColorOps: '&8'
onBan:
  RollbackComd: 
Version:
  ConfigYMLVer: 1
*/


//SYNC TO VERSION: 1


public class PermissionsRanksConfig {

//VARS - SETTINGS
public static String RankColorPly = null;
public static String RankColorWht = null;
public static String RankColorCre = null;
public static String RankColorMod = null;
public static String RankColorAdm = null;
public static String RankColorOps = null;

public static String RollbackComd = null;


public static int ConfigYMLVer = 0;


//VARS - SETTINGS
public static String SlashChar = null;
public static int SyncVers = 1;
	
public static void LoadSettings(String file)
{

	//Slash
	SetSlash(file);

	//Ensure config
	EnsureConfig();

	//1 Colors
	RankColorPly = PermissionsRanks.zConfig.getString("onChat.RankColorPly", RankColorPly).replace("&", "§");
	RankColorWht = PermissionsRanks.zConfig.getString("onChat.RankColorWht", RankColorWht).replace("&", "§");
	RankColorCre = PermissionsRanks.zConfig.getString("onChat.RankColorCre", RankColorCre).replace("&", "§");
	RankColorMod = PermissionsRanks.zConfig.getString("onChat.RankColorMod", RankColorMod).replace("&", "§");
	RankColorAdm = PermissionsRanks.zConfig.getString("onChat.RankColorAdm", RankColorAdm).replace("&", "§");
	RankColorOps = PermissionsRanks.zConfig.getString("onChat.RankColorOps", RankColorOps).replace("&", "§");

	//2 Commands
    RollbackComd = PermissionsRanks.zConfig.getString("onBan.RollbackComd", RollbackComd);

    
}


public static void SetSlash(String file)
{

	if (file.contains("/"))
	{
	SlashChar = "/"; //Linux
	}
	else
	{
	SlashChar = "\\"; //Windows
	}
	
}
private static void EnsureConfig()
{

	String zFile = "plugins" + SlashChar + "PermissionsRanks" + SlashChar + "config.yml";
	File f = new File(zFile);

		if(!f.isFile())
		{ 
		PermissionsRanksLib.Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "BlockUndo", "§fWriting new configuration.yml.");
		CreateConfig(zFile);
		}
		else
		{
		PermissionsRanks.zConfig = PermissionsRanks.zPlugin.getConfig();
		}

		//Update config
	    try
	    {ConfigYMLVer = PermissionsRanks.zConfig.getInt("Version.ConfigYMLVer", ConfigYMLVer);}
		catch (Exception e)
		{ConfigYMLVer = 0;}
	    
		if(ConfigYMLVer != SyncVers)
		{
		PermissionsRanksLib.Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "PermissionsRanks", "§fUpdating new configuration.yml...");
		CreateConfig(zFile);
		}

}
private static void CreateConfig(String file) 
{

	try
	{
	InputStream is = PermissionsRanks.zPlugin.getClass().getResourceAsStream("/config.yml");
	OutputStream os = new FileOutputStream(file);  
	byte[] buffer = new byte[4096];  
	int bytesRead;  
		while ((bytesRead = is.read(buffer)) != -1)
		{  
		os.write(buffer, 0, bytesRead);  
		}  
	is.close();  
	os.close(); 
	PermissionsRanks.zConfig = PermissionsRanks.zPlugin.getConfig();
	} catch (Exception e) {
	PermissionsRanksLib.Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "PermissionsRanks", "§fWriting new configuration.yml failed!");
	PermissionsRanksLib.Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "PermissionsRanks", "§4" + e.getCause() + ": " +  e.getMessage());
	}
	

}


}
