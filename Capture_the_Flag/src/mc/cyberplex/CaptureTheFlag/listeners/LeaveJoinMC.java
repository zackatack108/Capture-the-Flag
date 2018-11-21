package mc.cyberplex.CaptureTheFlag.listeners;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.Timer.Timer;
import mc.cyberplex.CaptureTheFlag.Timer.TimerType;
import mc.cyberplex.CaptureTheFlag.arena.Arena;
import mc.cyberplex.CaptureTheFlag.arena.ArenaState;
import mc.cyberplex.CaptureTheFlag.arena.CTFData;
import mc.cyberplex.CaptureTheFlag.arena.FlagData;
import mc.cyberplex.CaptureTheFlag.arena.Message;
import mc.cyberplex.CaptureTheFlag.arena.PlayerList;
import mc.cyberplex.CaptureTheFlag.arena.PlayerState;

public class LeaveJoinMC implements Listener {

	Main main = Main.getMain();
	Arena data = new Arena();
	PlayerState playerState = new PlayerState();
	PlayerList playerList = new PlayerList();
	BukkitTask rejoinTime;

	private static ArrayList<UUID> leftMC = new ArrayList<UUID>();

	@EventHandler
	public void onPlayerQuitMinecraft(PlayerQuitEvent event){

		Player player = event.getPlayer();

		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++){

				if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))){

					Timer time = new Timer();

					leftMC.add(player.getUniqueId());
					FlagData flagData = new FlagData();

					//check if player had the blue flag and drop it
					if(data.getCTFData(arenaNum).getHasBlueFlag() != null && data.getCTFData(arenaNum).getHasBlueFlag().equals(player.getUniqueId())) {

						if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {

							double y = player.getLocation().getY();
							player.getLocation().setY(--y);

						}

						time.stopTimer(arenaName, TimerType.BLUE);
						time.stopTimer(arenaName, TimerType.BLUE);

						flagData.getBlueDropped(arenaName, player);

						time.blueFlagTime(arenaName, 0, 30);

						data.getCTFData(arenaNum).setHasBlueFlag(null);

						data.getCTFData(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has been dropped");
						playerList.getPlayer(arenaName, Message.FLAG);

					}

					//check if player had the red flag and drop it
					if(data.getCTFData(arenaNum).getHasRedFlag() != null && data.getCTFData(arenaNum).getHasRedFlag().equals(player.getUniqueId())) {

						if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {

							double y = player.getLocation().getY();
							player.getLocation().setY(--y);

						}

						time.stopTimer(arenaName, TimerType.RED);
						time.stopTimer(arenaName, TimerType.RED);

						flagData.getRedDropped(arenaName, player);

						time.redFlagTime(arenaName, 0, 30);

						data.getCTFData(arenaNum).setHasRedFlag(null);

						data.getCTFData(arenaNum).setFlagMsg(ChatColor.RED + "Red flag has been dropped");
						playerList.getPlayer(arenaName, Message.FLAG);

					}

					rejoinTime = new BukkitRunnable(){

						int rejoinSeconds = 60;

						@Override
						public void run() {

							if(rejoinSeconds == 0){

								//playerState.leaveGame(arenaName, player);
								kickPlayerFromGame(player, arenaName);
								cancel();

							}

							rejoinSeconds--;

						}

					}.runTaskTimer(main, 0, 20);

				}

			}

		}

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){

		Player player = event.getPlayer();

		if(leftMC.contains(player.getUniqueId())) {

			for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

				int arenaNum = data.getArenaNum(arenaName);

				for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++){

					if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))){

						rejoinTime.cancel();
						leftMC.remove(player.getUniqueId());
						playerList.getPlayer(arenaName, Message.GAME);

						new BukkitRunnable() {

							@Override
							public void run() {
								player.setHealth(0);								
							}

						}.runTaskLater(main, 10);

					}			

				}

			}
			
			if(leftMC.contains(player.getUniqueId())){
				
				rejoinTime.cancel();
				leftMC.remove(player.getUniqueId());
				
				CTFData ctfData = new CTFData();

				for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

					if(ctfData.getInArenaArea(arenaName, player) == true && !player.hasPermission("ctf.arena.override")) {
						player.teleport(data.getHub());
						Arena.returnInventory(player);

						player.setGameMode(GameMode.SURVIVAL);
						player.removePotionEffect(PotionEffectType.INVISIBILITY);
						player.removePotionEffect(PotionEffectType.SPEED);
						player.removePotionEffect(PotionEffectType.REGENERATION);
						player.removePotionEffect(PotionEffectType.HEAL);
						player.removePotionEffect(PotionEffectType.JUMP);
						player.removePotionEffect(PotionEffectType.HARM);
						player.setHealth(20);
						player.setFireTicks(0);

					}

				}
				
			}

		} else {

			CTFData ctfData = new CTFData();

			for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

				if(ctfData.getInArenaArea(arenaName, player) == true && !player.hasPermission("ctf.arena.override")) {
					player.teleport(data.getHub());
					Arena.returnInventory(player);

					player.setGameMode(GameMode.SURVIVAL);
					player.removePotionEffect(PotionEffectType.INVISIBILITY);
					player.removePotionEffect(PotionEffectType.SPEED);
					player.removePotionEffect(PotionEffectType.REGENERATION);
					player.removePotionEffect(PotionEffectType.HEAL);
					player.removePotionEffect(PotionEffectType.JUMP);
					player.removePotionEffect(PotionEffectType.HARM);
					player.setHealth(20);
					player.setFireTicks(0);

				}

			}

		}

	}

	public void kickPlayerFromGame(Player player, String arenaName) {

		int arenaNum = data.getArenaNum(arenaName);

		leftMC.remove(player.getUniqueId());

		//------------------------------------------------------------------------------------------|
		//remove player from red team if they are on red team										|
		//------------------------------------------------------------------------------------------|
		for(int count = 0; count < data.getCTFData(arenaNum).getRedTeamCount(); count++) {

			if(data.getCTFData(arenaNum).getPlayerOnRedTeam(count).equals(player.getUniqueId())) {
				data.getCTFData(arenaNum).leaveRedTeam(player);
			}

		}

		//------------------------------------------------------------------------------------------|
		//remove player from blue team if they are on blue team										|
		//------------------------------------------------------------------------------------------|
		for(int count = 0; count < data.getCTFData(arenaNum).getBlueTeamCount(); count++) {

			if(data.getCTFData(arenaNum).getPlayerOnBlueTeam(count).equals(player.getUniqueId())) {
				data.getCTFData(arenaNum).leaveBlueTeam(player);
			}

		}

		//------------------------------------------------------------------------------------------|
		//remove player kit from the arena															|
		//------------------------------------------------------------------------------------------|
		if(data.getState(arenaName).equalsIgnoreCase("running")) {
			for(int index = 0; index < data.getArena(arenaNum).getGameCount(); index++) {

				if(data.getArena(arenaNum).getPlayer(index).equals(player.getUniqueId())) {
					data.getCTFData(arenaNum).removeFromPlayerKits(index);
				}

			}
		}

		//------------------------------------------------------------------------------------------|
		//remove player from the arena																|
		//------------------------------------------------------------------------------------------|
		data.getArena(arenaNum).removePlayer(player);

		if(data.getArena(arenaNum).getGameCount() < data.getMinPlayers(arenaName) && data.getState(arenaName).equalsIgnoreCase("running")) {
			ArenaState arenaState = new ArenaState();
			arenaState.stop(arenaName);
		} else if(data.getState(arenaName).equalsIgnoreCase("running")){
			PlayerList getPlayerList = new PlayerList();
			//Calls the getPlayerGame class to load the game scoreboard
			getPlayerList.getPlayer(arenaName, Message.GAME);
			
			JoinSign sign = new JoinSign();
			sign.updateSign(arenaName);			
		} else if(data.getState(arenaName).equalsIgnoreCase("waiting for players")) {
			PlayerList getPlayerList = new PlayerList();
			//Calls the getPlayerGame class to load the lobby scoreboard
			getPlayerList.getPlayer(arenaName, Message.LOBBY);

			JoinSign sign = new JoinSign();
			sign.updateSign(arenaName);
		}

	}

}
