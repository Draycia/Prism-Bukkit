package me.botsko.prism.commands;

import org.bukkit.ChatColor;

import me.botsko.prism.Prism;
import me.botsko.prism.commandlibs.CallInfo;
import me.botsko.prism.commandlibs.SubHandler;
import me.botsko.prism.settings.Settings;
import me.botsko.prism.wands.Wand;

public class ResetmyCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Prism plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public ResetmyCommand(Prism plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		String setType = null;
		if(call.getArgs().length >= 2){
			setType = call.getArg(1);
		}
		
			
		/**
		 * Inspector wand
		 */
		if( setType.equalsIgnoreCase("wand") ){
			
			// Ensure it's enabled
			if( !plugin.getConfig().getBoolean("prism.wands.allow-user-override") ){
				call.getPlayer().sendMessage( plugin.messenger.playerError("Sorry, but personalizing the wand is currently not allowed.") );
			}
			
			// Check for any wand permissions. @todo There should be some central way to handle this - some way to centralize it at least
			if( !call.getPlayer().hasPermission("prism.rollback") 
				&& !call.getPlayer().hasPermission("prism.restore")
				&& !call.getPlayer().hasPermission("prism.wand.*")
				&& !call.getPlayer().hasPermission("prism.wand.inspect")
				&& !call.getPlayer().hasPermission("prism.wand.profile")
				&& !call.getPlayer().hasPermission("prism.wand.rollback")
				&& !call.getPlayer().hasPermission("prism.wand.restore")){
				call.getPlayer().sendMessage( plugin.messenger.playerError("You do not have permission for this.") );
				return;
			}
			
			// Disable any current wand
			if(plugin.playersWithActiveTools.containsKey(call.getPlayer().getName())){
				Wand oldwand = plugin.playersWithActiveTools.get(call.getPlayer().getName());
				oldwand.disable( call.getPlayer() );
				plugin.playersWithActiveTools.remove(call.getPlayer().getName());
				call.getPlayer().sendMessage( plugin.messenger.playerHeaderMsg("Current wand " + ChatColor.RED + "disabled"+ChatColor.WHITE+".") );
			}
			
			Settings.deleteSetting( "wand.item", call.getPlayer() );
			Settings.deleteSetting( "wand.mode", call.getPlayer() );
			call.getPlayer().sendMessage( plugin.messenger.playerHeaderMsg("Your personal wand settings have been reset to server defaults.") );
			return;
	
		} else {
			call.getPlayer().sendMessage( plugin.messenger.playerError("Invalid arguments. Use /prism ? for help.") );
		}
	}
}