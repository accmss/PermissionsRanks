package com.accmss.permissionsranks;


//IMPORTS - BUKKIT
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PermissionsRanksPlayer implements Listener 
{


Player player;



public PermissionsRanksPlayer() 
{


	
} 

@EventHandler (priority = EventPriority.NORMAL)
public void onPlayerCommandPreprocess (PlayerCommandPreprocessEvent event)
{
	
String message = "";

message = event.getMessage().toLowerCase().replaceAll("/", "");

	//PermissionsRanksLib.Chat(event.getPlayer(), "PermissionsRanks", "debug1.");

		if (message.indexOf("unban") == 0 || message.indexOf("pardon") == 0 ||
		message.indexOf("op")    == 0 || message.indexOf("deop")   == 0)
		{
		PermissionsRanksLib.Chat(event.getPlayer(), "PermissionsRanks", "Command cancelled.");
		PermissionsRanksLib.Chat(PermissionsRanks.zPlugin.getServer().getConsoleSender(), "PermissionsRanks", event.getPlayer().getName() + " tried to issue a " + message + " command.");
		event.setCancelled(true);
		}

		if (message.indexOf("ban ") == 0) 	
		{
			
			//new prevent players from performing bans
			if (PermissionsRanksLib.GetRank(event.getPlayer().getName()).equalsIgnoreCase("player"))
			{
			//PermissionsRanksLib.Chat(event.getPlayer(), "PermissionsRanks", "debug3.");
			return;
			}

			if (PermissionsRanksConfig.RollbackComd == null)
			{
			//PermissionsRanksLib.Chat(event.getPlayer(), "PermissionsRanks", "debug4.");
			return;
			}
			
			if (PermissionsRanksConfig.RollbackComd.length() > 0) 
			{
			//PermissionsRanksLib.Chat(event.getPlayer(), "PermissionsRanks", "debug5.");
			String[] splits = message.split(" ");
			String command;
			command = PermissionsRanksConfig.RollbackComd.replace("<player>", splits[1]);
			//PermissionsRanksLib.Chat(event.getPlayer(), "PermissionsRanks", command);
			PermissionsRanks.zPlugin.getServer().dispatchCommand(event.getPlayer(), command);
			return;
			}
		}



}


@EventHandler (priority = EventPriority.NORMAL)
public void onPlayerJoin(final PlayerJoinEvent event)
{

	PermissionsRanks.zPlugin.getServer().getScheduler().runTaskLaterAsynchronously(PermissionsRanks.zPlugin, new Runnable() {
	
	public void run() {   
	player = event.getPlayer();

			if (player.isOnline())
			{
			PermissionsRanksLib.PlayerRank(player, PermissionsRanksLib.CleanName(player.getDisplayName(), true), true);
			}

		}

	}, 4L); 

}
@EventHandler (priority = EventPriority.NORMAL)
public void onPlayerQuit (PlayerQuitEvent event)
{

	PermissionsRanks.playerCode.remove(event.getPlayer().getName());

}
@EventHandler (priority = EventPriority.NORMAL)
public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event)
{
	
String msg = null;
//String format1 = null;
//String format2 = null;

	msg = PermissionsRanksLib.GetRank(event.getPlayer().getName());	//1 we get our format
	msg = msg.replace("<player>", "<§f%1$s§a");						//2 we replace out player with bukkit player before our 1 color code
	msg = msg + ">§f %2$s";											//3 we add the final bracket onto our existing 2nd color code 
	
	//format1 = msg;
	//format2 = PermissionsRanksLib.GetRank("§2" + "<§f%1$s§a" + "§2" + ">§f %2$s");
	
	//event.setFormat(PermissionsRanksLib.GetRank(event.getPlayer().getName())  + "<§f%1$s§a" + PermissionsRanksLib.GetRank(event.getPlayer().getName()) + ">§f %2$s");
	event.setFormat(msg);

	//PermissionsRanksLib.Chat(event.getPlayer(), "PermissionsRanks", format1);
	//PermissionsRanksLib.Chat(event.getPlayer(), "PermissionsRanks", format2);
}



}



