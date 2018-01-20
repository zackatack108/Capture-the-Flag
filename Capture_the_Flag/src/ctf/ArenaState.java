package ctf;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ArenaState {

	ArenaData data = new ArenaData();
	PlayerList playerList = new PlayerList();
	Timer time = new Timer();
	FlagData flag = new FlagData();
	Main main = Main.getMain();
	Kits kit = new Kits();

	public void waiting(String arenaName){

		int arenaNum = data.getArenaNum(arenaName);

		int minPlayers = main.getConfig().getInt("Arenas." + arenaName + ".Min");
		int maxPlayers = main.getConfig().getInt("Arenas." + arenaName + ".Max");

		playerList.getPlayer(arenaName, Message.LOBBY);

		if(data.getArena(arenaNum).getInGameCount() == minPlayers){

			time.lobbyTime(arenaName, minPlayers, 5);

		}
		else if(data.getArena(arenaNum).getInGameCount() == maxPlayers){

			time.stopTimer(data.getArena(arenaNum).lobbyTimer);
			time.lobbyTime(arenaName, minPlayers, 1);

		}

	}

	public void start(String arenaName){

		int arenaNum = data.getArenaNum(arenaName);

		randomize(arenaName);

		flag.getRedFlag(arenaName);
		flag.getBlueFlag(arenaName);

		playerList.getPlayer(arenaName, Message.GAME);

		main.getConfig().set("Arenas." + arenaName + ".State", "running");
		main.saveConfig();

		time.stopTimer(data.getArena(arenaNum).lobbyTimer);
		time.arenaTime(arenaName);		
	}

	public void stop(String arenaName){

		UUID playerID = null;
		Player player = null;

		//check if the arena name isn't null
		if(arenaName != null) {

			//get the arena number from the arena name
			int arenaNum = data.getArenaNum(arenaName);

			//stop the timer
			time.stopTimer(data.getArena(arenaNum).gameTimer);

			//check if red team won 
			if(data.getArena(arenaNum).getRedScore() > data.getArena(arenaNum).getBlueScore()){

				//create win message
				data.getArena(arenaNum).setFlagMsg(ChatColor.YELLOW + "Red team wins with " + 
						ChatColor.RED + Integer.toString(data.getArena(arenaNum).getRedScore()) + ChatColor.YELLOW + " to " +
						ChatColor.BLUE + Integer.toString(data.getArena(arenaNum).getBlueScore()));

				//display win message to players
				playerList.getPlayer(arenaName, Message.FLAG);

			} else if(data.getArena(arenaNum).getBlueScore() > data.getArena(arenaNum).getRedScore()){ //check if blue team won

				//create win message
				data.getArena(arenaNum).setFlagMsg(ChatColor.YELLOW + "Blue team wins with " + 
						ChatColor.BLUE + Integer.toString(data.getArena(arenaNum).getBlueScore()) + ChatColor.YELLOW + " to " +
						ChatColor.RED + Integer.toString(data.getArena(arenaNum).getRedScore()));

				//display win message to players
				playerList.getPlayer(arenaName, Message.FLAG);

			} else if(data.getArena(arenaNum).getBlueScore() == data.getArena(arenaNum).getRedScore()){ //check if the teams tied

				//create win message
				data.getArena(arenaNum).setFlagMsg(ChatColor.YELLOW + "Score is tied with " + 
						ChatColor.BLUE + Integer.toString(data.getArena(arenaNum).getBlueScore()) + ChatColor.YELLOW + " to " +
						ChatColor.RED + Integer.toString(data.getArena(arenaNum).getRedScore()));

				//display win message to players
				playerList.getPlayer(arenaName, Message.FLAG);

			}
			for(int subscript = 0; subscript < data.getArena(arenaNum).getInGameCount(); subscript++){

				//assign the playerID to the UUID of the user in the game
				playerID = UUID.fromString(data.getArena(arenaNum).getInGame(subscript));

				//get the player from the UUID
				player = Bukkit.getPlayer(playerID);

				//return players inventory
				data.returnInventory(player);

				//assign a new scoreboard that is empty
				player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

				//teleport player to the hub
				player.teleport(data.getHub());

			}
			
			//stop the timer
			time.stopTimer(data.getArena(arenaNum).gameTimer);
			time.stopTimer(data.getArena(arenaNum).lobbyTimer);

			//set red and blue team count to 0
			data.getArena(arenaNum).setRedCount(0);
			data.getArena(arenaNum).setBlueCount(0);

			//set red and blue scores to 0
			data.getArena(arenaNum).setRedScore(0);
			data.getArena(arenaNum).setBlueScore(0);

			//clear the teams and arena of players
			data.getArena(arenaNum).setInGame(null);
			data.getArena(arenaNum).setRedTeam(null);
			data.getArena(arenaNum).setBlueTeam(null);

			//set who has what flag to empty
			data.getArena(arenaNum).setBlueHas(null);
			data.getArena(arenaNum).setRedHas(null);

			//set the flags taken to false
			data.getArena(arenaNum).setRedTaken(false);
			data.getArena(arenaNum).setBlueTaken(false);

			//change the arena state and save the config
			main.getConfig().set("Arenas." + arenaName + ".State", "waiting for players");
			main.saveConfig();
		}
	}

	private void randomize(String arenaName){

		UUID playerID;
		Player player;

		int arenaNum = data.getArenaNum(arenaName);

		int inGameTotal = data.getArena(arenaNum).getInGameCount();		
		for(int count = 0; count < inGameTotal; count++){

			playerID = UUID.fromString(data.getArena(arenaNum).getInGame(count));
			player = Bukkit.getPlayer(playerID);

			if(count%2 == 0){
				player.teleport(data.getRedSpawn(arenaName));
				data.getArena(arenaNum).setRedTeam(player);
				kit.getDefault(arenaName, player);
			} else {
				player.teleport(data.getBlueSpawn(arenaName));
				data.getArena(arenaNum).setBlueTeam(player);
				kit.getDefault(arenaName, player);
			}

		}

	}

}
