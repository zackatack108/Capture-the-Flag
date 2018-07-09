package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.Timer.Timer;
import mc.cyberplex.CaptureTheFlag.Timer.TimerType;
import mc.cyberplex.CaptureTheFlag.arena.Arena;
import mc.cyberplex.CaptureTheFlag.arena.ArenaState;
import mc.cyberplex.CaptureTheFlag.arena.FlagData;
import mc.cyberplex.CaptureTheFlag.arena.FlagLocation;
import mc.cyberplex.CaptureTheFlag.arena.Message;
import mc.cyberplex.CaptureTheFlag.arena.PlayerList;
import mc.cyberplex.CaptureTheFlag.kits.Kits;

public class PlayerScore implements Listener {

	Main main = Main.getMain();
	Arena data = new Arena();
	FlagLocation flagLocation = new FlagLocation();
	FlagData flagData = new FlagData();
	PlayerList playerList = new PlayerList();
	Kits kit = new Kits();
	Timer time = new Timer();
	
	private final int MAXSCORE = 5;

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){

		Player player = event.getPlayer();
		boolean inGame = false;	

		//cycle through the arena list
		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

			//get arena number
			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++){

				//check to see if the player is in a game
				if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))) {
					inGame = true;
				}

			}

			//player is in game
			if(inGame == true) {

				boolean redTaken = data.getCTFData(arenaNum).getRedTaken();
				boolean blueTaken = data.getCTFData(arenaNum).getBlueTaken();

				//cycle through red team to see if player is on red
				for(int subscript = 0; subscript < data.getCTFData(arenaNum).getRedTeamCount(); subscript++) {

					//check if player is on red team
					if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnRedTeam(subscript))) {

						//check to see if the flag is missing
						if(redTaken == true && player.getUniqueId().equals(data.getCTFData(arenaNum).getHasBlueFlag()) && player.getLocation().getBlock().equals(flagLocation.redFlagSpawn(arenaName).getBlock())) {

							player.sendMessage(ChatColor.YELLOW + "You can't score if your flag is missing");

						} else { //score the flag

							if(player.getUniqueId().equals(data.getCTFData(arenaNum).getHasBlueFlag()) && player.getLocation().getBlock().equals(flagLocation.redFlagSpawn(arenaName).getBlock())){

								player.getInventory().clear();

								kit.getKit(arenaName, player);

								flagData.getBlueFlag(arenaName);

								data.getCTFData(arenaNum).setBlueTaken(false);
								data.getCTFData(arenaNum).setHasBlueFlag(null);

								int redScore = data.getCTFData(arenaNum).getRedScore();
								data.getCTFData(arenaNum).setRedScore(++redScore);

								time.stopTimer(arenaName, TimerType.BLUE);
								time.blueFlagTime(arenaName, 0, 0);
								time.stopTimer(arenaName, TimerType.BLUE);
								
								playerList.getPlayer(arenaName, Message.GAME);
								
								//check to see if the score is 5 if it is end the game
								if(data.getCTFData(arenaNum).getRedScore() == MAXSCORE) {
									
									ArenaState state = new ArenaState();
									state.stop(arenaName);
									
								}

							}

						}

					}					

				}

				//cycle through blue team to see if player is on blue
				for(int subscript = 0; subscript < data.getCTFData(arenaNum).getBlueTeamCount(); subscript++) {

					//check if player is on blue team
					if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnBlueTeam(subscript))) {

						//check to see if the flag is missing
						if(blueTaken == true && player.getUniqueId().equals(data.getCTFData(arenaNum).getHasRedFlag()) && player.getLocation().getBlock().equals(flagLocation.blueFlagSpawn(arenaName).getBlock())){

							player.sendMessage(ChatColor.YELLOW + "You can't score if your flag is missing");

						} else { //score the flag

							if(player.getUniqueId().equals(data.getCTFData(arenaNum).getHasRedFlag()) && player.getLocation().getBlock().equals(flagLocation.blueFlagSpawn(arenaName).getBlock())){

								player.getInventory().clear();

								kit.getKit(arenaName, player);

								flagData.getRedFlag(arenaName);

								data.getCTFData(arenaNum).setRedTaken(false);
								data.getCTFData(arenaNum).setHasRedFlag(null);;

								int blueScore = data.getCTFData(arenaNum).getBlueScore();
								data.getCTFData(arenaNum).setBlueScore(++blueScore);
								
								time.stopTimer(arenaName, TimerType.RED);
								time.redFlagTime(arenaName, 0, 0);
								time.stopTimer(arenaName, TimerType.RED);

								playerList.getPlayer(arenaName, Message.GAME);
								
								//check if blue score is 5 and end the game
								if(data.getCTFData(arenaNum).getBlueScore() == MAXSCORE) {
									
									ArenaState state = new ArenaState();
									state.stop(arenaName);
									
								}
								
							}

						}

					}

				}

			}

		}

	}

}
