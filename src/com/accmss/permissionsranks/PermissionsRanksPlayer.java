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
			return;
			}

			if (PermissionsRanksConfig.RollbackComd == null) return;
			
			if (PermissionsRanksConfig.RollbackComd.length() > 0) 
			{
			String[] splits = message.split(" ");
			String command;
			command = PermissionsRanksConfig.RollbackComd.replace("<player>", splits[1]);
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

	}, 8L); 

}
@EventHandler (priority = EventPriority.NORMAL)
public void onPlayerQuit (PlayerQuitEvent event)
{

	PermissionsRanks.playerCode.remove(event.getPlayer().getName());

}
@EventHandler (priority = EventPriority.NORMAL)
public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event)
{

	event.setFormat(PermissionsRanksLib.GetRank(event.getPlayer().getName())  + "<§f%1$s§a" + PermissionsRanksLib.GetRank(event.getPlayer().getName()) + ">§f %2$s");

}



}



