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

		int arenaNum = data.getArenaNum(arenaName);

		int inGameCount = data.getArena(arenaNum).getInGameCount();
		
		if(inGameCount > 0){

			time.stopTimer(data.getArena(arenaNum).gameTimer);

			if(data.getArena(arenaNum).getRedScore() > data.getArena(arenaNum).getBlueScore()){

				data.getArena(arenaNum).setFlagMsg(ChatColor.YELLOW + "Red team wins with " + 
						ChatColor.RED + Integer.toString(data.getArena(arenaNum).getRedScore()) + ChatColor.YELLOW + " to " +
						ChatColor.BLUE + Integer.toString(data.getArena(arenaNum).getBlueScore()));
				playerList.getPlayer(arenaName, Message.FLAG);

			} else if(data.getArena(arenaNum).getBlueScore() > data.getArena(arenaNum).getRedScore()){

				data.getArena(arenaNum).setFlagMsg(ChatColor.YELLOW + "Blue team wins with " + 
						ChatColor.BLUE + Integer.toString(data.getArena(arenaNum).getBlueScore()) + ChatColor.YELLOW + " to " +
						ChatColor.RED + Integer.toString(data.getArena(arenaNum).getRedScore()));
				playerList.getPlayer(arenaName, Message.FLAG);

			} else if(data.getArena(arenaNum).getBlueScore() == data.getArena(arenaNum).getRedScore()){

				data.getArena(arenaNum).setFlagMsg(ChatColor.YELLOW + "Score is tied with " + 
						ChatColor.BLUE + Integer.toString(data.getArena(arenaNum).getBlueScore()) + ChatColor.YELLOW + " to " +
						ChatColor.RED + Integer.toString(data.getArena(arenaNum).getRedScore()));
				playerList.getPlayer(arenaName, Message.FLAG);

			}

		}

		UUID playerID;
		Player player = null;

		int gameCount = data.getArena(arenaNum).getInGameCount();
		for(int subscript = 0; subscript < gameCount; subscript++){

			inGameCount = data.getArena(arenaNum).getInGameCount();
			
			playerID = UUID.fromString(data.getArena(arenaNum).getInGame(subscript));

			player = Bukkit.getPlayer(playerID);

			data.getArena(arenaNum).setInGameCount(--inGameCount);

			data.returnInventory(player);

			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

			//teleport player to the hub
			player.teleport(data.getHub());

		}

		inGameCount = data.getArena(arenaNum).getInGameCount();

		if(inGameCount == 0){
			data.getArena(arenaNum).setRedCount(0);
			data.getArena(arenaNum).setBlueCount(0);

			data.getArena(arenaNum).setRedScore(0);
			data.getArena(arenaNum).setBlueScore(0);

			data.getArena(arenaNum).setInGame(null);
			data.getArena(arenaNum).setRedTeam(null);
			data.getArena(arenaNum).setBlueTeam(null);

			data.getArena(arenaNum).setBlueHas(null);
			data.getArena(arenaNum).setRedHas(null);

			data.getArena(arenaNum).setRedTaken(false);
			data.getArena(arenaNum).setBlueTaken(false);

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
