package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.Timer.Timer;
import mc.cyberplex.CaptureTheFlag.Timer.TimerType;
import mc.cyberplex.CaptureTheFlag.arena.Arena;
import mc.cyberplex.CaptureTheFlag.arena.FlagData;
import mc.cyberplex.CaptureTheFlag.arena.Message;
import mc.cyberplex.CaptureTheFlag.arena.PlayerList;
import mc.cyberplex.CaptureTheFlag.kits.Kits;

public class PlayerDeath implements Listener {

	Main main = Main.getMain();
	Arena data = new Arena();
	FlagData flagData = new FlagData();
	PlayerList playerList = new PlayerList();
	Timer time = new Timer();
	Kits kit = new Kits();

	BukkitTask redTask, blueTask;

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {

		Player player;
		boolean inGame = false;

		if(event.getEntity() instanceof Player) {

			player = event.getEntity();

			for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)) {

				int arenaNum = data.getArenaNum(arenaName);

				for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++) {

					if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))) {
						inGame = true;
						
						player.getInventory().clear();
						event.getDrops().clear();
						player.setHealth(20);
						player.setFoodLevel(20);
						player.setFireTicks(0);
						player.setGameMode(GameMode.SPECTATOR);
						
					}

				}

				if(inGame == true) {

					boolean redTaken = data.getCTFData(arenaNum).getRedTaken();
					boolean blueTaken = data.getCTFData(arenaNum).getBlueTaken();

					//cycle through red team list
					for(int subscript = 0; subscript < data.getCTFData(arenaNum).getRedTeamCount(); subscript++) {

						//check if the player is on red team
						if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnRedTeam(subscript))) {

							//check if the player that died was holding the blue flag
							if(blueTaken == true && player.getUniqueId().equals(data.getCTFData(arenaNum).getHasBlueFlag())) {
								
								time.stopTimer(arenaName, TimerType.BLUE);
								time.stopTimer(arenaName, TimerType.BLUE);

								flagData.getBlueDropped(arenaName, player);

								time.blueFlagTime(arenaName, 0, 30);

								data.getCTFData(arenaNum).setHasBlueFlag(null);

								data.getCTFData(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has been dropped");
								playerList.getPlayer(arenaName, Message.FLAG);

							}

							onRespawn(player);

						}

					}

					//cycle through blue team list
					for(int subscript = 0; subscript < data.getCTFData(arenaNum).getBlueTeamCount(); subscript++) {

						//check if the player is on blue team
						if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnBlueTeam(subscript))) {

							//check if the player that died was holding the red flag
							if(redTaken == true && player.getUniqueId().equals(data.getCTFData(arenaNum).getHasRedFlag())) {
								
								time.stopTimer(arenaName, TimerType.RED);
								time.stopTimer(arenaName, TimerType.RED);

								flagData.getRedDropped(arenaName, player);

								time.redFlagTime(arenaName, 0, 30);

								data.getCTFData(arenaNum).setHasRedFlag(null);

								data.getCTFData(arenaNum).setFlagMsg(ChatColor.RED + "Red flag has been dropped");
								playerList.getPlayer(arenaName, Message.FLAG);
							}

							onRespawn(player);

						}

					}
					
				}
				
			}
			
		}
		
	}

	public void onRespawn(Player player) {

		boolean inGame = false;

		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)) {

			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++) {

				if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))) {
					inGame = true;
				}

			}

			if(inGame == true) {

				//cycle through red team
				for(int subscript = 0; subscript < data.getCTFData(arenaNum).getRedTeamCount(); subscript++) {

					//check if player is on red team
					if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnRedTeam(subscript))) {

						redTask = new BukkitRunnable() {

							int seconds = 5;

							@Override
							public void run() {

								player.sendMessage(ChatColor.YELLOW + "Respawning in " + Integer.toString(seconds) + " second(s)");

								if(this.seconds == 0) {

									player.setGameMode(GameMode.SURVIVAL);
									player.getInventory().clear();
									player.teleport(data.getCTFData(arenaNum).getRedSpawn(arenaName));
									kit.getDefault(arenaName, player);
									player.setHealth(20);
									player.setFoodLevel(20);
									player.setFireTicks(0);
									player.removePotionEffect(PotionEffectType.INVISIBILITY);

									cancel();

								}

								this.seconds--;

							}

						}.runTaskTimer(main, 0, 20);

					}

				}

				//cycle through blue team
				for(int subscript = 0; subscript < data.getCTFData(arenaNum).getBlueTeamCount(); subscript++) {

					//check if player is on blue team
					if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnBlueTeam(subscript))) {

						blueTask = new BukkitRunnable(){

							int seconds = 5;

							@Override
							public void run(){

								player.sendMessage(ChatColor.YELLOW + "Respawning in " + Integer.toString(seconds) + " second(s)");

								if(this.seconds == 0){

									player.setGameMode(GameMode.SURVIVAL);
									player.getInventory().clear();
									player.teleport(data.getCTFData(arenaNum).getBlueSpawn(arenaName));
									kit.getDefault(arenaName, player);
									player.removePotionEffect(PotionEffectType.INVISIBILITY);
									player.setHealth(20);
									player.setFoodLevel(20);
									player.setFireTicks(0);

									cancel();	

								}

								this.seconds--;

							}

						}.runTaskTimer(main, 0, 20);

					}

				}

			}
			
		}
		
	}

}
