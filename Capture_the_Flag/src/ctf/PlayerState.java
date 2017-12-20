package ctf;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerState {

	ArenaData data = new ArenaData();
	ArenaState arenaState = new ArenaState();
	PlayerList getPlayerList = new PlayerList();
	FlagLocation flag = new FlagLocation();
	Main main = Main.getMain();

	String state;

	FileConfiguration config = main.getConfig();

	public void joinGame(String arenaName, Player player){

		//get the state of the arena from the config
		state = config.getString("Arenas." + arenaName + ".State");

		//check if the arena has a lobby
		if(config.getString("Arenas." + arenaName + ".Lobby") == null){

			//tell user that the lobby doesn't exist
			player.sendMessage(ChatColor.RED + "Sorry, a lobby doesn't exist for that arena");

		} else if(state.equalsIgnoreCase("running")){ //check to see that the state is running

			//tells player that the arena is running
			player.sendMessage(ChatColor.RED + "Sorry, that arena is currently running");

		}			
		else {

			//save the player inventory
			data.saveInventory(player);

			//teleport player to the lobby
			player.teleport(data.getLobby(arenaName));

			int arenaNum = data.getArenaNum(arenaName);

			data.getArena(arenaNum).setInGame(player);

			arenaState.waiting(arenaName);

		}

	}

	public void leaveGame(String arenaName, Player player){

		//get the state of the arena and who has the flags
		String gameState = config.getString("Arenas." + arenaName + ".State");

		//get the minimum number of players for arena
		int minPlayers = config.getInt("Arenas." + arenaName + ".Min");

		int arenaNum = data.getArenaNum(arenaName);
		int arenaTotal = data.getArena(arenaNum).getInGameCount();									

		//return inventory to player
		data.returnInventory(player);

		String inGame;
		String[] playerList = new String[arenaTotal];
		int count = 0;

		for(int index = 0; index < arenaTotal; index++){			

			inGame = data.getArena(arenaNum).getInGame(index);

			if(inGame.equals(player.getUniqueId().toString())){
			} else {
				playerList[count] = inGame;
				count++;				
			}
		}

		UUID playerID;
		Player newPlayer;
		data.getArena(arenaNum).setInGameCount(0);
		--arenaTotal;
		for(int index = 0; index < arenaTotal; index++){

			if(playerList[index] == null){

				data.getArena(arenaNum).setInGame(null);
				main.getConfig().set("Arenas." + arenaName + ".State", "waiting for players");
				main.saveConfig();
			} else {

				playerID = UUID.fromString(playerList[index]);
				newPlayer = Bukkit.getPlayer(playerID);
				data.getArena(arenaNum).setInGame(newPlayer);

			}

		}

		for(int index = 0; index < data.getArena(arenaNum).getInGameCount(); index++){

			if(index < data.getArena(arenaNum).getBlueTeamCount() && data.getArena(arenaNum).getBlueTeam(index).equals(player.getUniqueId().toString())){ //check if player is on team blue

				String[] blueList = new String[arenaTotal];
				count = 0;

				for(int subscript = 0; subscript < data.getArena(arenaNum).getBlueTeamCount(); subscript++){

					if(data.getArena(arenaNum).getBlueTeam(subscript).equals(player.getUniqueId().toString())){
						//don't do anything
					} else {

						blueList[count] = data.getArena(arenaNum).getBlueTeam(subscript);
						count++;

					}

				}

				if(data.getArena(arenaNum).getRedTaken() == true && data.getArena(arenaNum).getBlueHas().equals(player.getUniqueId().toString())){

					//drops the flag where the player was at
					flag.redDropped(arenaName, player);

					//removes the player from the blue has list
					data.setBlueHas(null);

				}

				data.setBlueCount(0);
				for(int subscript = 0; subscript < count; subscript++){

					playerID = UUID.fromString(playerList[subscript]);
					newPlayer = Bukkit.getPlayer(playerID);
					data.getArena(arenaNum).setBlueTeam(newPlayer);

				}

			}
			else if(index < data.getArena(arenaNum).getRedTeamCount() && data.getArena(arenaNum).getRedTeam(index).equals(player.getUniqueId().toString())){ //check if player is on team red

				String[] redList = new String[arenaTotal];
				count = 0;
				for(int subscript = 0; subscript < data.getArena(arenaNum).getRedTeamCount(); subscript++){

					if(data.getArena(arenaNum).getRedTeam(subscript).equals(player.getUniqueId().toString())){
						//don't do anything
					} else {

						redList[count] = data.getArena(arenaNum).getRedTeam(subscript);
						count++;

					}

				}

				if(data.getArena(arenaNum).getBlueTaken() == true && data.getArena(arenaNum).getRedHas().equals(player.getUniqueId().toString())){ //check if player had the flag

					//drops the flag they had
					flag.blueDropped(arenaName, player);

					//removes them from the Red has list
					data.setRedHas(null);

				}

				data.setRedCount(0);
				for(int subscript = 0; subscript < count; subscript++){

					playerID = UUID.fromString(playerList[subscript]);
					newPlayer = Bukkit.getPlayer(playerID);
					data.getArena(arenaNum).setRedTeam(newPlayer);

				}				

			}

		}

		//gives player an empty scoreboard
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

		arenaTotal = data.getArena(arenaNum).getInGameCount();

		//check the state of the arena
		if(gameState.equalsIgnoreCase("waiting for players")){

			if(data.getArena(arenaNum).getInGameCount() > 0){				
				//calls the getPlayerLobby class to load the lobby scoreboard
				getPlayerList.getPlayer(arenaName, Message.LOBBY);			
			}


		} else if(gameState.equalsIgnoreCase("running")){

			//checks to see if the total players is less then the minimum for arena
			if(data.getArena(arenaNum).getInGameCount() < minPlayers){

				//stops the arena
				arenaState.stop(arenaName);

			} else {

				//Calls the getPlayerGame class to load the game scoreboard
				getPlayerList.getPlayer(arenaName, Message.GAME);

			}									

		}

		//Send player a message saying they are leaving the arena and are being teleported to the hub
		player.sendMessage(ChatColor.YELLOW + "Leaving CTF arena");
		player.sendMessage(ChatColor.YELLOW + "Sending you to the Capture the Flags hub.");									

		//teleport player to the hub
		player.teleport(data.getHub());

	}

}
