package mc.cyberplex.CaptureTheFlag.Timer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.arena.Arena;
import mc.cyberplex.CaptureTheFlag.arena.ArenaState;
import mc.cyberplex.CaptureTheFlag.arena.FlagData;
import mc.cyberplex.CaptureTheFlag.arena.FlagLocation;
import mc.cyberplex.CaptureTheFlag.arena.Message;
import mc.cyberplex.CaptureTheFlag.arena.PlayerList;
import net.md_5.bungee.api.ChatColor;

public class Timer {

	Arena data = new Arena();
	PlayerList playerList = new PlayerList();
	Main main = Main.getMain();
	FlagData flagData = new FlagData();
	FlagLocation flagLoc = new FlagLocation();

	Block block;

	//---------------------------------------------------
	//Timer for the lobby
	//---------------------------------------------------
	public void lobbyTime(String arenaName, int time){		

		int arenaNum = data.getArenaNum(arenaName);

		data.getArena(arenaNum).setSeconds(0);
		data.getArena(arenaNum).setMinutes(time);

		data.getCTFData(arenaNum).Timer = new BukkitRunnable() {

			@Override
			public void run() {

				int seconds = data.getArena(arenaNum).getSeconds();
				int minutes = data.getArena(arenaNum).getMinutes();

				if(data.getArena(arenaNum).getGameCount() < data.getMinPlayers(arenaName)){					
					stopTimer(arenaName, TimerType.LOBBY);					
				}

				if(seconds <= 0 && minutes >= 1){

					seconds = 60;
					data.getArena(arenaNum).setSeconds(seconds);

					playerList.getPlayer(arenaName, Message.LOBBY);					

					data.getArena(arenaNum).setMinutes(--minutes);

				} else if (minutes < 1){					

					if(seconds == 0){

						stopTimer(arenaName, TimerType.LOBBY);

						ArenaState state = new ArenaState();
						state.start(arenaName);

					}
				}

				playerList.getPlayer(arenaName, Message.LOBBY);
				data.getArena(arenaNum).setSeconds(--seconds);

			}

		}.runTaskTimer(main, 0, 20);

	}

	//---------------------------------------------------
	//Timer for arena
	//---------------------------------------------------
	public void arenaTime(String arenaName, int time){

		int arenaNum = data.getArenaNum(arenaName);

		data.getArena(arenaNum).setSeconds(0);
		data.getArena(arenaNum).setMinutes(time);

		data.getCTFData(arenaNum).Timer = new BukkitRunnable() {

			@Override
			public void run() {

				int seconds = data.getArena(arenaNum).getSeconds();
				int minutes = data.getArena(arenaNum).getMinutes();

				if(seconds <= 0 && minutes >= 1){

					seconds = 60;
					data.getArena(arenaNum).setSeconds(seconds);
					data.getArena(arenaNum).setMinutes(--minutes);

				} else if (minutes < 1){

					if(seconds == 0){

						ArenaState state = new ArenaState();
						state.stop(arenaName);

					}
				}

				playerList.getPlayer(arenaName, Message.GAME);	
				data.getArena(arenaNum).setSeconds(--seconds);

			}

		}.runTaskTimer(main, 0, 20);

	}

	//---------------------------------------------------
	//Timer for red flag when dropped
	//---------------------------------------------------
	public void redFlagTime(String arenaName, int time){

		int arenaNum = data.getArenaNum(arenaName);
		data.getCTFData(arenaNum).setRedFlagTime(time);

		data.getCTFData(arenaNum).redTimer = new BukkitRunnable() {

			@Override
			public void run(){

				int seconds = data.getCTFData(arenaNum).getRedFlagTime();

				if(seconds == 0){

					cancel();

					data.getCTFData(arenaNum).setRedTaken(false);

					block = flagLoc.redDroppedLocation(arenaName).getBlock();
					block.setType(Material.AIR);
					flagData.getRedFlag(arenaName);

					data.getCTFData(arenaNum).setFlagMsg(ChatColor.RED + "Red flag has returned to spawn");
					playerList.getPlayer(arenaName, Message.FLAG);					

				}

				playerList.getPlayer(arenaName, Message.GAME);
				data.getCTFData(arenaNum).setRedFlagTime(--seconds);

			}

		}.runTaskTimer(main, 0, 20);

	}

	//---------------------------------------------------
	//Timer for blue flag when dropped
	//---------------------------------------------------
	public void blueFlagTime(String arenaName, int time){

		int arenaNum = data.getArenaNum(arenaName);
		data.getCTFData(arenaNum).setBlueFlagTime(time);

		data.getCTFData(arenaNum).blueTimer = new BukkitRunnable() {

			@Override
			public void run(){

				int seconds = data.getCTFData(arenaNum).getBlueFlagTime();

				if(seconds == 0){

					cancel();

					data.getCTFData(arenaNum).setBlueTaken(false);

					block = flagLoc.blueDroppedLocation(arenaName).getBlock();
					block.setType(Material.AIR);
					flagData.getBlueFlag(arenaName);

					data.getCTFData(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has returned to spawn");
					playerList.getPlayer(arenaName, Message.FLAG);			

				}

				playerList.getPlayer(arenaName, Message.GAME);
				data.getCTFData(arenaNum).setBlueFlagTime(--seconds);

			}

		}.runTaskTimer(main, 0, 20);

	}

	public void stopTimer(String arenaName, TimerType type){

		int arenaNum = data.getArenaNum(arenaName);

		if(type.equals(TimerType.LOBBY) || type.equals(TimerType.GAME) ) {
			
			if(data.getCTFData(arenaNum).Timer != null) {
				data.getCTFData(arenaNum).Timer.cancel();
			}
			
		} else if(type.equals(TimerType.RED)) {
			
			if(data.getCTFData(arenaNum).redTimer != null) {
				data.getCTFData(arenaNum).Timer.cancel();
			}
			
		} else if(type.equals(TimerType.BLUE)) {
			
			if(data.getCTFData(arenaNum).blueTimer != null) {
				data.getCTFData(arenaNum).Timer.cancel();
			}
			
		}

	}

}
