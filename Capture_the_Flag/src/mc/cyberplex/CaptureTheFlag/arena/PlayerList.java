package mc.cyberplex.CaptureTheFlag.arena;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.Scoreboards;

public class PlayerList {
	
	Arena data = new Arena();
	Main main = Main.getMain();

	public void getPlayer(String arenaName, Message msg){
		
		Set<String> arenas = main.getConfig().getConfigurationSection("Arenas").getKeys(false);
		Scoreboards[] board = new Scoreboards[arenas.size()];
		
		int arenaNum = data.getArenaNum(arenaName);
		
		for(int count = 0; count < data.getArena(arenaNum).getGameCount(); count++){
			
			UUID playerID = data.getArena(arenaNum).getPlayer(count);
			Player player = Bukkit.getPlayer(playerID);
			
			if(msg == Message.LOBBY){
				board[arenaNum] = new Scoreboards();
				board[arenaNum].lobbyBoard(arenaNum, player, arenaName);
			} else if(msg == Message.GAME){
				board[arenaNum] = new Scoreboards();
				board[arenaNum].gameBoard(arenaNum, player, arenaName);
			} else if(msg == Message.FLAG){
				player.sendMessage(data.getCTFData(arenaNum).getFlagMsg());
			}
			
		}
		
	}
	
}
