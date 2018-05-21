package mc.cyberplex.CaptureTheFlag.Timer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
					data.getArena(arenaNum).setSeconds(0);
					data.getArena(arenaNum).setMinutes(5);
				}

				playerList.getPlayer(arenaName, Message.LOBBY);

				if(seconds <= 0 && minutes >= 1){

					seconds = 60;
					data.getArena(arenaNum).setSeconds(seconds);
					data.getArena(arenaNum).setMinutes(--minutes);

				} else if (minutes < 1){					

					if(seconds == 0){

						stopTimer(arenaName, TimerType.LOBBY);

						ArenaState state = new ArenaState();
						state.start(arenaName);

					}
				}


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

				playerList.getPlayer(arenaName, Message.GAME);

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

				if(seconds == 30) {

					for(int count = 0; count < data.getArena(arenaNum).getGameCount(); count++) {

						Player player;
						UUID playerID = data.getArena(arenaNum).getPlayer(count);

						if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(playerID))) {
							player = Bukkit.getPlayer(playerID);						
							player.setFoodLevel(20);
						}

					}

				}

				data.getArena(arenaNum).setSeconds(--seconds);

			}

		}.runTaskTimer(main, 0, 20);

	}

	//---------------------------------------------------
	//Timer for red flag when dropped
	//---------------------------------------------------
	public void redFlagTime(String arenaName, int min, int sec){

		int arenaNum = data.getArenaNum(arenaName);
		data.getCTFData(arenaNum).setRedFlagMinutes(min);
		data.getCTFData(arenaNum).setRedFlagSeconds(sec);

		data.getCTFData(arenaNum).redTimer = new BukkitRunnable() {

			@Override
			public void run(){

				int seconds = data.getCTFData(arenaNum).getRedFlagSeconds();
				int minutes = data.getCTFData(arenaNum).getRedFlagMinutes();

				playerList.getPlayer(arenaName, Message.GAME);

				if(seconds <= 0 && minutes >= 1) {

					seconds = 60;

					data.getCTFData(arenaNum).setRedFlagMinutes(--minutes);
					data.getCTFData(arenaNum).setRedFlagSeconds(seconds);

				} else if(minutes < 1) {

					if(seconds == 0){

						stopTimer(arenaName, TimerType.RED);

						if(data.getCTFData(arenaNum).getHasRedFlag() != null) {

							Player player;
							UUID playerID = data.getCTFData(arenaNum).getHasRedFlag();

							if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(playerID))) {

								player = Bukkit.getPlayer(playerID);
								player.setHealth(0);

							}

						}

						stopTimer(arenaName, TimerType.RED);

						data.getCTFData(arenaNum).setRedTaken(false);

						block = flagLoc.redDroppedLocation(arenaName).getBlock();
						block.setType(Material.AIR);
						flagData.getRedFlag(arenaName);
						main.getConfig().set("Arenas." + arenaName + ".red.flag.dropped location", null);

						data.getCTFData(arenaNum).setFlagMsg(ChatColor.RED + "Red flag has returned to spawn");
						playerList.getPlayer(arenaName, Message.FLAG);

						seconds = 1;

					}

				}

				data.getCTFData(arenaNum).setRedFlagSeconds(--seconds);

			}

		}.runTaskTimer(main, 0, 20);

	}

	//---------------------------------------------------
	//Timer for blue flag when dropped
	//---------------------------------------------------
	public void blueFlagTime(String arenaName, int min, int sec){

		int arenaNum = data.getArenaNum(arenaName);
		data.getCTFData(arenaNum).setBlueFlagMinutes(min);
		data.getCTFData(arenaNum).setBlueFlagSeconds(sec);

		data.getCTFData(arenaNum).blueTimer = new BukkitRunnable() {

			@Override
			public void run(){

				int minutes = data.getCTFData(arenaNum).getBlueFlagMinutes();
				int seconds = data.getCTFData(arenaNum).getBlueFlagSeconds();

				playerList.getPlayer(arenaName, Message.GAME);

				if(seconds <= 0 && minutes >= 1) {

					seconds = 60;

					data.getCTFData(arenaNum).setBlueFlagMinutes(--minutes);
					data.getCTFData(arenaNum).setBlueFlagSeconds(seconds);


				} else if(minutes < 1) {

					if(seconds == 0){

						stopTimer(arenaName, TimerType.BLUE);

						if(data.getCTFData(arenaNum).getHasBlueFlag() != null) {

							Player player;
							UUID playerID = data.getCTFData(arenaNum).getHasBlueFlag();

							if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(playerID))) {

								player = Bukkit.getPlayer(playerID);
								player.setHealth(0);

							}

						}

						stopTimer(arenaName, TimerType.BLUE);

						data.getCTFData(arenaNum).setBlueTaken(false);

						block = flagLoc.blueDroppedLocation(arenaName).getBlock();
						block.setType(Material.AIR);
						flagData.getBlueFlag(arenaName);
						main.getConfig().set("Arenas." + arenaName + ".blue.flag.dropped location", null);

						data.getCTFData(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has returned to spawn");
						playerList.getPlayer(arenaName, Message.FLAG);	

						seconds = 1;

					}

				}

				data.getCTFData(arenaNum).setBlueFlagSeconds(--seconds);

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
				data.getCTFData(arenaNum).redTimer.cancel();
			}

		} else if(type.equals(TimerType.BLUE)) {

			if(data.getCTFData(arenaNum).blueTimer != null) {
				data.getCTFData(arenaNum).blueTimer.cancel();
			}

		}

	}

}
