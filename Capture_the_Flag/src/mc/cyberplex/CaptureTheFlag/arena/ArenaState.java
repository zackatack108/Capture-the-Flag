package mc.cyberplex.CaptureTheFlag.arena;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.Timer.Timer;
import mc.cyberplex.CaptureTheFlag.Timer.TimerType;
import mc.cyberplex.CaptureTheFlag.kits.Kits;
import mc.cyberplex.CaptureTheFlag.listeners.JoinSign;
import net.md_5.bungee.api.ChatColor;

public class ArenaState {

	Arena data = new Arena();
	PlayerList playerList = new PlayerList();
	Timer time = new Timer();
	FlagData flag = new FlagData();
	Main main = Main.getMain();
	Kits kit = new Kits();

	public void waiting(String arenaName){

		int arenaNum = data.getArenaNum(arenaName);

		int minPlayers = data.getMinPlayers(arenaName);
		int maxPlayers = data.getMaxPlayers(arenaName);

		playerList.getPlayer(arenaName, Message.LOBBY);

		if(data.getArena(arenaNum).getGameCount() == minPlayers){

			time.lobbyTime(arenaName, 5);

		}
		else if(data.getArena(arenaNum).getGameCount() == maxPlayers){

			time.stopTimer(arenaName, TimerType.LOBBY);
			time.lobbyTime(arenaName, 1);

		}

	}

	public void start(String arenaName){

		randomize(arenaName);

		flag.getRedFlag(arenaName);
		flag.getBlueFlag(arenaName);

		playerList.getPlayer(arenaName, Message.GAME);

		data.setState(arenaName, "running");

		time.stopTimer(arenaName, TimerType.LOBBY);
		time.arenaTime(arenaName, 20);
		
		JoinSign sign = new JoinSign();
		sign.updateSign(arenaName);
	}

	public void stop(String arenaName){

		UUID playerID = null;
		Player player = null;

		//check if the arena name isn't null
		if(arenaName != null) {

			//get the arena number from the arena name
			int arenaNum = data.getArenaNum(arenaName);

			//stop the timer
			time.stopTimer(arenaName, TimerType.GAME);

			data.setState(arenaName, "stopping");

			//check if red team won 
			if(data.getCTFData(arenaNum).getRedScore() > data.getCTFData(arenaNum).getBlueScore()){

				//create win message
				data.getCTFData(arenaNum).setFlagMsg(ChatColor.YELLOW + "Red team wins with " + 
						ChatColor.RED + Integer.toString(data.getCTFData(arenaNum).getRedScore()) + ChatColor.YELLOW + " to " +
						ChatColor.BLUE + Integer.toString(data.getCTFData(arenaNum).getBlueScore()));

				//display win message to players
				playerList.getPlayer(arenaName, Message.FLAG);

			} else if(data.getCTFData(arenaNum).getBlueScore() > data.getCTFData(arenaNum).getRedScore()){ //check if blue team won

				//create win message
				data.getCTFData(arenaNum).setFlagMsg(ChatColor.YELLOW + "Blue team wins with " + 
						ChatColor.BLUE + Integer.toString(data.getCTFData(arenaNum).getBlueScore()) + ChatColor.YELLOW + " to " +
						ChatColor.RED + Integer.toString(data.getCTFData(arenaNum).getRedScore()));

				//display win message to players
				playerList.getPlayer(arenaName, Message.FLAG);

			} else if(data.getCTFData(arenaNum).getBlueScore() == data.getCTFData(arenaNum).getRedScore()){ //check if the teams tied

				//create win message
				data.getCTFData(arenaNum).setFlagMsg(ChatColor.YELLOW + "Score is tied with " + 
						ChatColor.BLUE + Integer.toString(data.getCTFData(arenaNum).getBlueScore()) + ChatColor.YELLOW + " to " +
						ChatColor.RED + Integer.toString(data.getCTFData(arenaNum).getRedScore()));

				//display win message to players
				playerList.getPlayer(arenaName, Message.FLAG);

			}

			int playerCount = data.getArena(arenaNum).getGameCount();			
			for(int subscript = 0; subscript < playerCount; subscript++){

				//assign the playerID to the UUID of the user in the game
				playerID = data.getArena(arenaNum).getPlayer(0);

				//get the player from the UUID
				player = Bukkit.getPlayer(playerID);

				PlayerState playerState = new PlayerState();
				playerState.leaveGame(arenaName, player);

			}

			Block block;
			FlagLocation flagLoc = new FlagLocation();
			FlagData flagData = new FlagData();

			if(data.getCTFData(arenaNum).getRedTaken() == true) {
				block = flagLoc.redDroppedLocation(arenaName).getBlock();
				block.setType(Material.AIR);
				flagData.getRedFlag(arenaName);
				main.getConfig().set("Arenas." + arenaName + ".red.flag.dropped location", null);
			}

			if(data.getCTFData(arenaNum).getBlueTaken() == true) {
				block = flagLoc.blueDroppedLocation(arenaName).getBlock();
				block.setType(Material.AIR);
				flagData.getBlueFlag(arenaName);
				main.getConfig().set("Arenas." + arenaName + ".blue.flag.dropped location", null);
			}

			//stop the timer
			time.stopTimer(arenaName, TimerType.GAME);
			time.stopTimer(arenaName, TimerType.BLUE);
			time.stopTimer(arenaName, TimerType.RED);
			time.stopTimer(arenaName, TimerType.LOBBY);

			//set red and blue scores to 0
			data.getCTFData(arenaNum).setRedScore(0);
			data.getCTFData(arenaNum).setBlueScore(0);

			//set who has what flag to empty
			data.getCTFData(arenaNum).setHasBlueFlag(null);
			data.getCTFData(arenaNum).setHasRedFlag(null);

			//set the flags taken to false
			data.getCTFData(arenaNum).setRedTaken(false);
			data.getCTFData(arenaNum).setBlueTaken(false);

			//change the arena state and save the config
			data.setState(arenaName, "waiting for players");
			
			JoinSign sign = new JoinSign();
			sign.updateSign(arenaName);
		}
	}

	private void randomize(String arenaName){

		UUID playerID;
		Player player;

		int arenaNum = data.getArenaNum(arenaName);

		int inGameTotal = data.getArena(arenaNum).getGameCount();		
		for(int count = 0; count < inGameTotal; count++){

			playerID = data.getArena(arenaNum).getPlayer(count);
			player = Bukkit.getPlayer(playerID);

			if(count % 2 == 0){

				player.teleport(data.getCTFData(arenaNum).getRedSpawn(arenaName));
				data.getCTFData(arenaNum).joinRedTeam(player);

				kit.getDefault(arenaName, player);

			} else {

				player.teleport(data.getCTFData(arenaNum).getBlueSpawn(arenaName));
				data.getCTFData(arenaNum).joinBlueTeam(player);

				kit.getDefault(arenaName, player);

			}

		}

	}

}
