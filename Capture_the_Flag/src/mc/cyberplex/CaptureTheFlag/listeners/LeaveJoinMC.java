package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.arena.Arena;
import mc.cyberplex.CaptureTheFlag.arena.Message;
import mc.cyberplex.CaptureTheFlag.arena.PlayerList;
import mc.cyberplex.CaptureTheFlag.arena.PlayerState;

public class LeaveJoinMC implements Listener {
	
	Main main = Main.getMain();
	Arena data = new Arena();
	PlayerState playerState = new PlayerState();
	PlayerDeath playerDeath = new PlayerDeath();
	PlayerList playerList = new PlayerList();
	
	BukkitTask rejoinTime;
	
	@EventHandler
	public void onPlayerQuitMinecraft(PlayerQuitEvent event){

		Player player = event.getPlayer();

		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++){

				if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))){

					rejoinTime = new BukkitRunnable(){

						int rejoinSeconds = 60;

						@Override
						public void run() {

							if(rejoinSeconds == 0){

								playerState.leaveGame(arenaName, player);
								cancel();

							}

							rejoinSeconds--;

						}

					}.runTaskTimer(main, 0, 20);

				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){

		Player player = event.getPlayer();

		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++){

				if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))){

					rejoinTime.cancel();
					playerDeath.onRespawn(player);
					playerList.getPlayer(arenaName, Message.GAME);

				}			

			}

		}

	}

}
