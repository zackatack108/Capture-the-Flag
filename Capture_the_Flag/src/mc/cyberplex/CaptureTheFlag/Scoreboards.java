package mc.cyberplex.CaptureTheFlag;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import mc.cyberplex.CaptureTheFlag.arena.Arena;

public class Scoreboards {

	Main plugin = Main.getMain();

	Set<String> arenas = plugin.getConfig().getConfigurationSection("Arenas").getKeys(false);
	Arena data = new Arena();

	public void lobbyBoard(int arenaNum, Player player, String arenaName) {

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();		

		Team playerCount = board.registerNewTeam("Player Count");
		Team time = board.registerNewTeam("Time");

		Objective lobbyObjective = board.registerNewObjective("Lobby", "dummy");

		lobbyObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		lobbyObjective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Capture The Flag");

		//set up the players 
		playerCount.addEntry(ChatColor.YELLOW.toString());
		playerCount.setPrefix(ChatColor.YELLOW + "Players: ");
		playerCount.setSuffix(ChatColor.WHITE + Integer.toString(data.getArena(arenaNum).getGameCount()));

		//set up the time 
		time.addEntry(ChatColor.GREEN.toString());
		time.setPrefix(ChatColor.GREEN + "Time: ");
		time.setSuffix(ChatColor.WHITE + addPadding(2, Integer.toString(data.getArena(arenaNum).getMinutes())) + ":" + addPadding(2,Integer.toString(data.getArena(arenaNum).getSeconds())));	

		Score arena = lobbyObjective.getScore(ChatColor.YELLOW + "Arena: " + ChatColor.WHITE + arenaName.substring(0,1).toUpperCase() + arenaName.substring(1));
		Score blank = lobbyObjective.getScore("  ");
		Score blank2 = lobbyObjective.getScore(" ");
		Score players = lobbyObjective.getScore(ChatColor.YELLOW.toString());
		Score count = lobbyObjective.getScore(ChatColor.GREEN.toString());

		arena.setScore(5);
		blank.setScore(4);
		players.setScore(3);
		blank2.setScore(2);
		count.setScore(1);

		player.setScoreboard(board);

	}

	public void gameBoard(int arenaNum, Player player, String arenaName) {

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();

		Team red = board.registerNewTeam("Red");
		Team redFlagReturn = board.registerNewTeam("Red Flag Return");
		Team blue = board.registerNewTeam("Blue");
		Team blueFlagReturn = board.registerNewTeam("Blue Flag Return");
		Team time = board.registerNewTeam("Time");

		Objective gameObjective = board.registerNewObjective("Game", "dummy");

		gameObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		gameObjective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Capture The Flag");	

		Score arena = gameObjective.getScore(ChatColor.YELLOW + "Arena: " + ChatColor.WHITE + arenaName.substring(0,1).toUpperCase() + arenaName.substring(1));
		Score blank1 = gameObjective.getScore("  ");
		Score blank2 = gameObjective.getScore("   ");
		Score blank3 = gameObjective.getScore("    ");

		arena.setScore(9);
		blank1.setScore(8);		

		red.addEntry(ChatColor.RED.toString());
		red.setPrefix(ChatColor.RED + "Red: ");
		red.setSuffix(ChatColor.WHITE + Integer.toString(data.getCTFData(arenaNum).getRedScore()));
		gameObjective.getScore(ChatColor.RED.toString()).setScore(7);	

		redFlagReturn.addEntry(ChatColor.DARK_RED.toString());
		redFlagReturn.setPrefix(ChatColor.RED + "Flag Return: ");
		redFlagReturn.setSuffix(ChatColor.WHITE + addPadding(2, Integer.toString(data.getCTFData(arenaNum).getRedFlagMinutes())) + ":" + addPadding(2,Integer.toString(data.getCTFData(arenaNum).getRedFlagSeconds())));
		gameObjective.getScore(ChatColor.DARK_RED.toString()).setScore(6);

		blank2.setScore(5);		

		blue.addEntry(ChatColor.BLUE.toString());
		blue.setPrefix(ChatColor.BLUE + "Blue: ");
		blue.setSuffix(ChatColor.WHITE + Integer.toString(data.getCTFData(arenaNum).getBlueScore()));
		gameObjective.getScore(ChatColor.BLUE.toString()).setScore(4);

		blueFlagReturn.addEntry(ChatColor.DARK_BLUE.toString());
		blueFlagReturn.setPrefix(ChatColor.BLUE + "Flag Return: ");
		blueFlagReturn.setSuffix(ChatColor.WHITE + addPadding(2, Integer.toString(data.getCTFData(arenaNum).getBlueFlagMinutes())) + ":" + addPadding(2,Integer.toString(data.getCTFData(arenaNum).getBlueFlagSeconds())));
		gameObjective.getScore(ChatColor.DARK_BLUE.toString()).setScore(3);

		blank3.setScore(2);

		time.addEntry(ChatColor.GREEN.toString());
		time.setPrefix(ChatColor.GREEN + "Time: ");
		time.setSuffix(ChatColor.WHITE + addPadding(2,Integer.toString(data.getArena(arenaNum).getMinutes())) + ":" + addPadding(2,Integer.toString(data.getArena(arenaNum).getSeconds())));
		gameObjective.getScore(ChatColor.GREEN.toString()).setScore(1);

		player.setScoreboard(board);

	}

	public String addPadding(int length, String text) {

		StringBuilder sb = new StringBuilder();

		for(int i = length - text.length(); i > 0; i--) {
			sb.append('0');
		}

		sb.append(text);
		return sb.toString();		
	}

	public void updateLobby(int arenaNum, Player player, String arenaName) {

		if(player != null) {
			Scoreboard board = player.getScoreboard();

			if(board.getObjective(DisplaySlot.SIDEBAR) != null && board.getObjective(DisplaySlot.SIDEBAR).getDisplayName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Capture The Flag")) {

				board.getTeam("Player Count").setSuffix(ChatColor.WHITE + Integer.toString(data.getArena(arenaNum).getGameCount()));
				board.getTeam("Time").setSuffix(ChatColor.WHITE + addPadding(2, Integer.toString(data.getArena(arenaNum).getMinutes())) + ":" + addPadding(2,Integer.toString(data.getArena(arenaNum).getSeconds())));

			} else {

				lobbyBoard(arenaNum, player, arenaName);

			}
		}
	}

	public void updateGame(int arenaNum, Player player, String arenaName) {

		if(player != null) {
			Scoreboard board = player.getScoreboard();

			if(board.getObjective(DisplaySlot.SIDEBAR) != null && board.getObjective(DisplaySlot.SIDEBAR).getDisplayName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Capture The Flag")) {

				board.getTeam("Red").setSuffix(ChatColor.WHITE + Integer.toString(data.getCTFData(arenaNum).getRedScore()));
				board.getTeam("Red Flag Return").setSuffix(ChatColor.WHITE + addPadding(2, Integer.toString(data.getCTFData(arenaNum).getRedFlagMinutes())) + ":" + addPadding(2,Integer.toString(data.getCTFData(arenaNum).getRedFlagSeconds())));
				board.getTeam("Blue").setSuffix(ChatColor.WHITE + Integer.toString(data.getCTFData(arenaNum).getBlueScore()));
				board.getTeam("Blue Flag Return").setSuffix(ChatColor.WHITE + addPadding(2, Integer.toString(data.getCTFData(arenaNum).getBlueFlagMinutes())) + ":" + addPadding(2,Integer.toString(data.getCTFData(arenaNum).getBlueFlagSeconds())));
				board.getTeam("Time").setSuffix(ChatColor.WHITE + addPadding(2,Integer.toString(data.getArena(arenaNum).getMinutes())) + ":" + addPadding(2,Integer.toString(data.getArena(arenaNum).getSeconds())));

			} else {

				gameBoard(arenaNum, player, arenaName);

			}
		}
	}

}
