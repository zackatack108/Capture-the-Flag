package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.arena.Arena;
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

				//player is in game
				if(inGame == true) {

					boolean redTaken = data.getCTFData(arenaNum).getRedTaken();
					boolean blueTaken = data.getCTFData(arenaNum).getBlueTaken();

					if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnRedTeam(subscript)) && player.getUniqueId().equals(data.getCTFData(arenaNum).getHasBlueFlag())) { //check to see if player is on red team

						//check to see if the flag is missing
						if(redTaken == true && player.getUniqueId().equals(data.getCTFData(arenaNum).getHasBlueFlag()) && player.getLocation().getBlock().equals(flagLocation.redFlagSpawn(arenaName).getBlock())) {

							player.sendMessage(ChatColor.YELLOW + "You can't score if your flag is missing");

						} else { //score the flag

							if(player.getLocation().getBlock().equals(flagLocation.redFlagSpawn(arenaName).getBlock())){

								player.getInventory().clear();

								kit.getDefault(arenaName, player);

								flagData.getBlueFlag(arenaName);

								data.getCTFData(arenaNum).setBlueTaken(false);
								data.getCTFData(arenaNum).setHasBlueFlag(null);

								int redScore = data.getCTFData(arenaNum).getRedScore();
								data.getCTFData(arenaNum).setRedScore(++redScore);

								playerList.getPlayer(arenaName, Message.GAME);

							}

						}

					} else if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnBlueTeam(subscript))	&& player.getUniqueId().equals(data.getCTFData(arenaNum).getHasRedFlag())){ //check to see if player is on blue team

						//check to see if the flag is missing
						if(blueTaken == true && player.getUniqueId().equals(data.getCTFData(arenaNum).getHasRedFlag()) && player.getLocation().getBlock().equals(flagLocation.blueFlagSpawn(arenaName).getBlock())){

							player.sendMessage(ChatColor.YELLOW + "You can't score if your flag is missing");

						} else { //score the flag

							if(player.getLocation().getBlock().equals(flagLocation.blueFlagSpawn(arenaName).getBlock())){

								player.getInventory().clear();

								kit.getDefault(arenaName, player);

								flagData.getRedFlag(arenaName);

								data.getCTFData(arenaNum).setRedTaken(false);
								data.getCTFData(arenaNum).setHasRedFlag(null);;

								int blueScore = data.getCTFData(arenaNum).getBlueScore();
								data.getCTFData(arenaNum).setBlueScore(++blueScore);

								playerList.getPlayer(arenaName, Message.GAME);
							}

						}

					}

				}

			}

		}

	}

}
