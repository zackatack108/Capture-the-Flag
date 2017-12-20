package ctf;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.md_5.bungee.api.ChatColor;

public class Timer {

	ArenaData data = new ArenaData();
	PlayerList playerList = new PlayerList();
	Main main = Main.getMain();
	FlagData flagData = new FlagData();
	FlagLocation flagLoc = new FlagLocation();

	int seconds, minutes, redFlagSeconds, blueFlagSeconds, redFlagMinutes, blueFlagMinutes;
	int maxGameTime = 30;
	Block block;

	//---------------------------------------------------
	//Timer for the lobby
	//---------------------------------------------------
	public void lobbyTime(String arenaName, int minPlayers, int maxLobbyTime){		

		int arenaNum = data.getArenaNum(arenaName);

		data.getArena(arenaNum).setSeconds(0);
		data.getArena(arenaNum).setMinutes(maxLobbyTime);

		data.getArena(arenaNum).lobbyTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){

			public void run(){

				seconds = data.getArena(arenaNum).getTimeSeconds();
				minutes = data.getArena(arenaNum).getTimeMinutes();

				if(data.getArena(arenaNum).getInGameCount() < minPlayers){					
					stopTimer(data.getArena(arenaNum).lobbyTimer);					
				}

				if(seconds <= 0 && minutes >= 1){

					seconds = 60;
					data.getArena(arenaNum).setSeconds(seconds);

					data.getArena(arenaNum).setLobbyTime(Integer.toString(data.getArena(arenaNum).getTimeMinutes()) + " Minute(s)");
					playerList.getPlayer(arenaName, Message.LOBBY);					

					data.getArena(arenaNum).setMinutes(--minutes);

				} else if (minutes < 1){

					data.getArena(arenaNum).setLobbyTime(Integer.toString(data.getArena(arenaNum).getTimeSeconds()) + " Second(s)");
					playerList.getPlayer(arenaName, Message.LOBBY);

					if(seconds == 0){

						stopTimer(data.getArena(arenaNum).lobbyTimer);

						ArenaState state = new ArenaState();
						state.start(arenaName);

					}
				}

				data.getArena(arenaNum).setSeconds(--seconds);

			}

		}, 0, 20);

	}

	//---------------------------------------------------
	//Timer for arena
	//---------------------------------------------------
	public void arenaTime(String arenaName){

		int arenaNum = data.getArenaNum(arenaName);

		data.getArena(arenaNum).setSeconds(0);
		data.getArena(arenaNum).setMinutes(maxGameTime);

		data.getArena(arenaNum).gameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){

			public void run(){

				seconds = data.getArena(arenaNum).getTimeSeconds();
				minutes = data.getArena(arenaNum).getTimeMinutes();

				if(seconds <= 0 && minutes >= 1){

					seconds = 60;
					data.getArena(arenaNum).setSeconds(seconds);

					data.getArena(arenaNum).setGameTime(Integer.toString(data.getArena(arenaNum).getTimeMinutes()) + " Minute(s)");
					playerList.getPlayer(arenaName, Message.GAME);					

					data.getArena(arenaNum).setMinutes(--minutes);

				} else if (minutes < 1){

					data.getArena(arenaNum).setGameTime(Integer.toString(data.getArena(arenaNum).getTimeSeconds()) + " Second(s)");
					playerList.getPlayer(arenaName, Message.GAME);

					if(seconds == 0){

						ArenaState state = new ArenaState();
						state.stop(arenaName);

					}
				}

				data.getArena(arenaNum).setSeconds(--seconds);

			}

		}, 0, 20);

	}

	//---------------------------------------------------
	//Timer for red flag when dropped
	//---------------------------------------------------
	public void redFlagTime(String arenaName, int maxTime){

		int arenaNum = data.getArenaNum(arenaName);

		redFlagMinutes = 1;
		data.getArena(arenaNum).setRedTimeCount(maxTime);

		data.getArena(arenaNum).redFlagTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){

			public void run(){

				redFlagSeconds = data.getArena(arenaNum).getRedTimeCount();

				data.getArena(arenaNum).setRedFlagTime(Integer.toString(data.getArena(arenaNum).getRedTimeCount()));
				playerList.getPlayer(arenaName, Message.GAME);

				if(redFlagSeconds == 0){

					stopTimer(data.getArena(arenaNum).redFlagTimer);

					data.getArena(arenaNum).setRedTaken(false);

					block = flagLoc.redDroppedLocation(arenaName).getBlock();
					block.setType(Material.AIR);
					flagData.getRedFlag(arenaName);

					data.getArena(arenaNum).setFlagMsg(ChatColor.RED + "Red flag has returned to spawn");
					playerList.getPlayer(arenaName, Message.FLAG);
					
					data.getArena(arenaNum).setRedFlagTime(" ");
					playerList.getPlayer(arenaName, Message.GAME);

				}


				data.getArena(arenaNum).setRedTimeCount(--redFlagSeconds);

			}

		}, 0, 20);

	}

	//---------------------------------------------------
	//Timer for blue flag when dropped
	//---------------------------------------------------
	public void blueFlagTime(String arenaName, int maxTime){

		int arenaNum = data.getArenaNum(arenaName);

		blueFlagMinutes = 1;
		data.getArena(arenaNum).setBlueTimeCount(maxTime);

		data.getArena(arenaNum).blueFlagTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){

			public void run(){

				blueFlagSeconds = data.getArena(arenaNum).getBlueTimeCount();

				data.getArena(arenaNum).setBlueFlagTime(Integer.toString(data.getArena(arenaNum).getBlueTimeCount()));
				playerList.getPlayer(arenaName, Message.GAME);

				if(blueFlagSeconds == 0){

					stopTimer(data.getArena(arenaNum).blueFlagTimer);

					data.getArena(arenaNum).setBlueTaken(false);

					block = flagLoc.blueDroppedLocation(arenaName).getBlock();
					block.setType(Material.AIR);
					flagData.getBlueFlag(arenaName);

					data.getArena(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has returned to spawn");
					playerList.getPlayer(arenaName, Message.FLAG);

					data.getArena(arenaNum).setBlueFlagTime(" ");
					playerList.getPlayer(arenaName, Message.GAME);

				}

				data.getArena(arenaNum).setBlueTimeCount(--blueFlagSeconds);

			}

		}, 0, 20);

	}

	public void stopTimer(int task){
		Bukkit.getScheduler().cancelTask(task);
	}

}
