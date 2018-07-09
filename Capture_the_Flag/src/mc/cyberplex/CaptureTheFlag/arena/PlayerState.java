package mc.cyberplex.CaptureTheFlag.arena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.Scoreboards;
import mc.cyberplex.CaptureTheFlag.listeners.JoinSign;

public class PlayerState {

	Arena data = new Arena();
	ArenaState arenaState = new ArenaState();
	PlayerList getPlayerList = new PlayerList();
	FlagLocation flag = new FlagLocation();
	Main main = Main.getMain();
	String state;

	FileConfiguration config = main.getConfig();

	public void joinGame(String arenaName, Player player){

		//get the state of the arena from the config
		state = config.getString("Arenas." + arenaName + ".state");

		//check if the arena has a lobby
		if(config.getString("Arenas." + arenaName + ".lobby.world") == null){

			//tell user that the lobby doesn't exist
			player.sendMessage(ChatColor.RED + "Sorry, a lobby doesn't exist for that arena");

		} else if(state.equalsIgnoreCase("running")){ //check to see that the state is running

			//tells player that the arena is running
			player.sendMessage(ChatColor.RED + "Sorry, that arena is currently running");

		} else if(data.getArena(data.getArenaNum(arenaName)).getGameCount() == data.getMaxPlayers(arenaName)){
			
			//tell the player that the arena is full
			player.sendMessage(ChatColor.RED + "Sorry, that arena is full");
			
		} else {

			//save the player inventory
			Arena.saveInventory(player);

			//teleport player to the lobby
			player.teleport(data.getLobby(arenaName));

			int arenaNum = data.getArenaNum(arenaName);

			data.getArena(arenaNum).addPlayer(player);
			
			Scoreboards board = new Scoreboards();
			board.lobbyBoard(arenaNum, player, arenaName);

			arenaState.waiting(arenaName);
			
			JoinSign sign = new JoinSign();
			sign.updateSign(arenaName);

		}

	}

	public void leaveGame(String arenaName, Player player){

		//get the state of the arena and who has the flags
		String gameState = config.getString("Arenas." + arenaName + ".state");

		//get the minimum number of players for arena
		int minPlayers = data.getMinPlayers(arenaName);
		int arenaNum = data.getArenaNum(arenaName);
		boolean inGame = false;

		for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++){

			//check to see if the player is in a game
			if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))) {
				inGame = true;
			}

		}

		if(inGame == true) {

			//------------------------------------------------------------------------------------------|
			//remove player from red team if they are on red team										|
			//------------------------------------------------------------------------------------------|
			for(int count = 0; count < data.getCTFData(arenaNum).getRedTeamCount(); count++) {

				if(data.getCTFData(arenaNum).getPlayerOnRedTeam(count).equals(player.getUniqueId())) {

					if(data.getCTFData(arenaNum).getHasBlueFlag() != null && data.getCTFData(arenaNum).getHasBlueFlag().equals(player.getUniqueId())) {
						flag.blueDropped(arenaName, player);
						data.getCTFData(arenaNum).setHasBlueFlag(null);
					}

					data.getCTFData(arenaNum).leaveRedTeam(player);
				}

			}

			//------------------------------------------------------------------------------------------|
			//remove player from blue team if they are on blue team										|
			//------------------------------------------------------------------------------------------|
			for(int count = 0; count < data.getCTFData(arenaNum).getBlueTeamCount(); count++) {

				if(data.getCTFData(arenaNum).getPlayerOnBlueTeam(count).equals(player.getUniqueId())) {

					if(data.getCTFData(arenaNum).getHasRedFlag() != null && data.getCTFData(arenaNum).getHasRedFlag().equals(player.getUniqueId())) {
						flag.redDropped(arenaName, player);
						data.getCTFData(arenaNum).setHasRedFlag(null);
					}

					data.getCTFData(arenaNum).leaveBlueTeam(player);
				}

			}

			//------------------------------------------------------------------------------------------|
			//remove player from the arena																|
			//------------------------------------------------------------------------------------------|
			data.getArena(arenaNum).removePlayer(player);

			//------------------------------------------------------------------------------------------|
			//return player to hub																		|
			//------------------------------------------------------------------------------------------|

			//gives player an empty scoreboard
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			
			player.setGameMode(GameMode.SURVIVAL);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.removePotionEffect(PotionEffectType.SPEED);
			player.removePotionEffect(PotionEffectType.REGENERATION);
			player.removePotionEffect(PotionEffectType.HEAL);
			player.removePotionEffect(PotionEffectType.JUMP);
			player.removePotionEffect(PotionEffectType.HARM);
			player.setHealth(20);
			player.setFireTicks(0);

			//teleport player to the hub
			player.teleport(data.getHub());

			//Send player a message saying they are leaving the arena and are being teleported to the hub
			player.sendMessage(ChatColor.YELLOW + "Leaving CTF arena");
			player.sendMessage(ChatColor.YELLOW + "Sending you to the Capture the Flags hub.");		

			//return inventory to player
			Arena.returnInventory(player);

			//------------------------------------------------------------------------------------------|
			//check if the game count is less then min while the game is running and stop the game		|
			//------------------------------------------------------------------------------------------|
			if(data.getArena(arenaNum).getGameCount() < minPlayers && gameState.equalsIgnoreCase("running")) {
				arenaState.stop(arenaName);
			} else if(gameState.equalsIgnoreCase("running")) {
				//Calls the getPlayerGame class to load the game scoreboard
				getPlayerList.getPlayer(arenaName, Message.GAME);
			} else if(gameState.equalsIgnoreCase("waiting for players")) {
				//Calls the getPlayerGame class to load the lobby scoreboard
				getPlayerList.getPlayer(arenaName, Message.LOBBY);
				
				JoinSign sign = new JoinSign();
				sign.updateSign(arenaName);
			}
			
		}

	}

}
