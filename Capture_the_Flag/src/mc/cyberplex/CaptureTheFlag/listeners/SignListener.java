package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
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

public class SignListener implements Listener{

	Main main = Main.getMain();
	Arena data = new Arena();
	PlayerState state = new PlayerState();

	boolean inGame = false;

	private int signX, signY, signZ;
	private String world;

	@EventHandler
	public void onSignCreate(SignChangeEvent event){

		Player player = event.getPlayer();

		if(player.hasPermission("ctf.create.joinsign")){

			if(event.getLine(0).equalsIgnoreCase("[ctf]")){

				if(event.getLine(1).equalsIgnoreCase("join")){

					if(main.getConfig().contains("Arenas." + event.getLine(2).toLowerCase())){

						String arenaName = event.getLine(2).toLowerCase();

						Block block = event.getBlock();

						signX = block.getX();
						signY = block.getY();
						signZ = block.getZ();
						world = block.getWorld().getName();						

						main.getConfig().set("Arenas." + arenaName + ".join sign.world", world);
						main.getConfig().set("Arenas." + arenaName + ".join sign.x", signX);
						main.getConfig().set("Arenas." + arenaName + ".join sign.y", signY);
						main.getConfig().set("Arenas." + arenaName + ".join sign.z", signZ);
						main.saveConfig();

						//get the maximum number of players allowed for arena
						//int maxPlayers = config.getInt("Arenas." + arenaName + ".Max");

						event.setLine(0, ChatColor.DARK_RED + "" + ChatColor.BOLD + "[CTF]");
						event.setLine(1, ChatColor.BLUE + "Join");
						event.setLine(2, ChatColor.GOLD + arenaName.substring(0,1).toUpperCase() + arenaName.substring(1));
						//event.setLine(3, ChatColor.GREEN + "0/" + Integer.toString(maxPlayers));

					}

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

				if(ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("join") && sign.getBlock().getLocation().equals(getSignLocation(name)) && main.getConfig().contains("Arenas." + name)){

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

	public Location getSignLocation(String arenaName){

		signX = main.getConfig().getInt("Arenas." + arenaName + ".join sign.X");
		signY = main.getConfig().getInt("Arenas." + arenaName + ".Join sign.Y");
		signZ = main.getConfig().getInt("Arenas." + arenaName + ".join sign.Z");
		world = main.getConfig().getString("Arenas." + arenaName + ".join sign.world");

		Location signLocation = new Location(Bukkit.getServer().getWorld(world), signX, signY, signZ);

		return signLocation;

	}

}
