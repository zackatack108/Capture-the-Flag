package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.arena.Arena;
import mc.cyberplex.CaptureTheFlag.arena.PlayerState;

public class JoinSign implements Listener{

	Main main = Main.getMain();
	Arena data = new Arena();
	PlayerState state = new PlayerState();

	boolean inGame = false;

	@EventHandler
	public void onSignCreate(SignChangeEvent event){

		Player player = event.getPlayer();

		if(player.hasPermission("ctf.create.joinsign")){

			if(event.getLine(0).equalsIgnoreCase("[ctf]") && event.getLine(1).equalsIgnoreCase("join")){

				if(main.getConfig().contains("Arenas." + event.getLine(2).toLowerCase())){

					String arenaName = event.getLine(2).toLowerCase();

					Block block = event.getBlock();
					
					data.setJoinSign(arenaName, block);
					
					int arenaNum = data.getArenaNum(arenaName);

					event.setLine(0, ChatColor.DARK_RED + "" + ChatColor.BOLD + "[CTF]");
					event.setLine(1, ChatColor.GOLD + "Join");
					event.setLine(2, ChatColor.BLUE + arenaName.substring(0,1).toUpperCase() + arenaName.substring(1));
					event.setLine(3, ChatColor.GRAY + Integer.toString(data.getArena(arenaNum).getGameCount()) + " / " + Integer.toString(data.getArena(arenaNum).getMaxPlayers(arenaName)));

				}

			}

		} 

	}

	@EventHandler
	public void onJoinSign(PlayerInteractEvent event){

		Player player = event.getPlayer();		

		if((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) && event.getClickedBlock().getState() instanceof Sign){

			Sign sign = (Sign) event.getClickedBlock().getState();
			String name = ChatColor.stripColor(sign.getLine(2)).toLowerCase();

			if(main.getConfig().contains("Arenas." + name)){

				if(ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("join") && sign.getBlock().getLocation().equals(data.getJoinSign(name)) && main.getConfig().contains("Arenas." + name)){

					for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

						int arenaNum = data.getArenaNum(arenaName);

						for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++){

							if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))){

								inGame = true;

								player.sendMessage(ChatColor.RED + "Sorry you are in a game right now please do /ctf leave");

							} else {
								inGame = false;
							}

						}

					}

					if(inGame == false){
						state.joinGame(name, player);
					}

				}

			}
		}

	}

	public void updateSign(String arenaName) {
		
		String arenaState = main.getConfig().getString("Arenas." + arenaName + ".state");
		Location signLocation = data.getJoinSign(arenaName);
		Block block = signLocation.getWorld().getBlockAt(signLocation);
		BlockState state = block.getState();
		Sign sign = (Sign) state;		
		int arenaNum = data.getArenaNum(arenaName);
		
		if(arenaState.equalsIgnoreCase("waiting for players") || arenaState.equals("stopping")) {
			sign.setLine(3, ChatColor.GRAY + Integer.toString(data.getArena(arenaNum).getGameCount()) + " / " + Integer.toString(data.getArena(arenaNum).getMaxPlayers(arenaName)));
			sign.update();
		} else if(arenaState.equalsIgnoreCase("running")) {
			sign.setLine(3, ChatColor.DARK_RED + "Running");
			sign.update();
		}
		
	}

}
