package ctf;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Listeners implements Listener{

	Main main = Main.getMain();
	ArenaData data = new ArenaData();
	FlagLocation flagLocation = new FlagLocation();
	FlagData flagData = new FlagData();
	Timer time = new Timer();
	PlayerList playerList = new PlayerList();
	Kits kit = new Kits();
	PlayerState playerState = new PlayerState();

	boolean inGame = false, onRed = false, onBlue = false, hasRed = false, hasBlue = false;
	Block block;
	BukkitTask redTask, blueTask, rejoinTime;

	@EventHandler
	public void onGetFlag(PlayerInteractEvent event){

		Player player = event.getPlayer();

		double[] redArea = new double[4], blueArea = new double[4];

		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getInGameCount(); subscript++){

				if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(subscript)))
					inGame = true;

				if(inGame == true && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) && event.getClickedBlock().getState() instanceof Banner){

					boolean redTaken = data.getArena(arenaNum).getRedTaken();
					boolean blueTaken = data.getArena(arenaNum).getBlueTaken();
					
					if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getRedTeam(subscript))){ //check to see if player is on red team

						if(blueTaken == true){

							for(int count = 0; count < 4; count++){

								blueArea[count] = flagLocation.blueDroppedFlagArea(arenaName, count);

							}

							if(player.getLocation().getX() < blueArea[0] && player.getLocation().getX() > blueArea[1]
									&& player.getLocation().getZ() < blueArea[2] && player.getLocation().getZ() > blueArea[3]){

								Banner banner = (Banner) event.getClickedBlock().getState();

								if(banner.getBaseColor().equals(DyeColor.BLUE)){

									ItemStack blueFlag = new ItemStack(Material.BANNER);
									blueFlag.setDurability((short) 4);								

									player.getInventory().clear();
									player.getInventory().setHelmet(blueFlag);
									player.getInventory().setItemInMainHand(blueFlag);					

									block = flagLocation.blueDroppedLocation(arenaName).getBlock();	
									block.setType(Material.AIR);

									time.stopTimer(data.getArena(arenaNum).blueFlagTimer);
									data.getArena(arenaNum).setBlueFlagTime(" ");

									playerList.getPlayer(arenaName, Message.GAME);

									data.getArena(arenaNum).setBlueTaken(true);
									data.getArena(arenaNum).setRedHas(player);

									data.getArena(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has been taken by " + player.getName());
									playerList.getPlayer(arenaName, Message.FLAG);

								}
							}

						} else {

							for(int count = 0; count < 4; count++){

								blueArea[count] = flagLocation.blueFlagArea(arenaName, count);

							}

							if(player.getLocation().getX() < blueArea[0] && player.getLocation().getX() > blueArea[1]
									&& player.getLocation().getZ() < blueArea[2] && player.getLocation().getZ() > blueArea[3]){

								Banner banner = (Banner) event.getClickedBlock().getState();

								if(banner.getBaseColor().equals(DyeColor.BLUE)){

									ItemStack blueFlag = new ItemStack(Material.BANNER);
									blueFlag.setDurability((short) 4);								

									player.getInventory().clear();
									player.getInventory().setHelmet(blueFlag);
									player.getInventory().setItemInMainHand(blueFlag);					

									block = flagLocation.blueFlagSpawn(arenaName).getBlock();	
									block.setType(Material.AIR);

									data.getArena(arenaNum).setBlueTaken(true);
									data.getArena(arenaNum).setRedHas(player);

									data.getArena(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has been taken by " + player.getName());
									playerList.getPlayer(arenaName, Message.FLAG);

								}

							}

						}

					} else if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueTeam(subscript))){ //check to see if player is on blue team

						if(redTaken == true){

							for(int count = 0; count < 4; count++){

								redArea[count] = flagLocation.redDroppedFlagArea(arenaName, count);

							}

							if(player.getLocation().getX() < redArea[0] && player.getLocation().getX() > redArea[1]
									&& player.getLocation().getZ() < redArea[2] && player.getLocation().getZ() > redArea[3]){

								Banner banner = (Banner) event.getClickedBlock().getState();

								if(banner.getBaseColor().equals(DyeColor.RED)){

									ItemStack redFlag = new ItemStack(Material.BANNER);
									redFlag.setDurability((short) 1);								

									player.getInventory().clear();
									player.getInventory().setHelmet(redFlag);
									player.getInventory().setItemInMainHand(redFlag);					

									block = flagLocation.redDroppedLocation(arenaName).getBlock();	
									block.setType(Material.AIR);

									time.stopTimer(data.getArena(arenaNum).redFlagTimer);
									data.getArena(arenaNum).setRedFlagTime(" ");

									playerList.getPlayer(arenaName, Message.GAME);

									data.getArena(arenaNum).setRedTaken(true);
									data.getArena(arenaNum).setBlueHas(player);

									data.getArena(arenaNum).setFlagMsg(ChatColor.RED + "Red flag has been taken by " + player.getName());
									playerList.getPlayer(arenaName, Message.FLAG);

								}

							}

						} else {

							for(int count = 0; count < 4; count++){

								redArea[count] = flagLocation.redFlagArea(arenaName, count);

							}

							if(player.getLocation().getX() < redArea[0] && player.getLocation().getX() > redArea[1]
									&& player.getLocation().getZ() < redArea[2] && player.getLocation().getZ() > redArea[3]){

								Banner banner = (Banner) event.getClickedBlock().getState();

								if(banner.getBaseColor().equals(DyeColor.RED)){

									ItemStack redFlag = new ItemStack(Material.BANNER);
									redFlag.setDurability((short) 1);								

									player.getInventory().clear();
									player.getInventory().setHelmet(redFlag);
									player.getInventory().setItemInMainHand(redFlag);					

									block = flagLocation.redFlagSpawn(arenaName).getBlock();	
									block.setType(Material.AIR);

									data.getArena(arenaNum).setRedTaken(true);
									data.getArena(arenaNum).setBlueHas(player);

									data.getArena(arenaNum).setFlagMsg(ChatColor.RED + "Red flag has been taken by " + player.getName());
									playerList.getPlayer(arenaName, Message.FLAG);

								}

							}

						}

					}

				}

			}

		}

	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){

		Player player = event.getPlayer();

		//cycle through the arena list
		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

			//get arena number
			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getInGameCount(); subscript++){
				
				//check to see if the player is in a game
				if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(subscript)))
					inGame = true;

				//player is in game
				if(inGame == true){
					
					boolean redTaken = data.getArena(arenaNum).getRedTaken();
					boolean blueTaken = data.getArena(arenaNum).getBlueTaken();

					if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getRedTeam(subscript)) 
							&& player.getUniqueId().toString().equals(data.getArena(arenaNum).getRedHas())){ //check to see if player is on red team

						//check to see if the flag is missing
						if(redTaken == true && player.getUniqueId().toString().equals(data.getArena(arenaNum).getRedHas())
								&& player.getLocation().getBlock().equals(flagLocation.redFlagSpawn(arenaName).getBlock())){

							player.sendMessage(ChatColor.YELLOW + "You can't score if your flag is missing");

						} else { //score the flag

							if(player.getLocation().getBlock().equals(flagLocation.redFlagSpawn(arenaName).getBlock())){

								player.getInventory().clear();

								kit.getDefault(arenaName, player);

								flagData.getBlueFlag(arenaName);

								data.getArena(arenaNum).setBlueTaken(false);
								data.getArena(arenaNum).setRedHas(null);

								int redScore = data.getArena(arenaNum).getRedScore();
								data.getArena(arenaNum).setRedScore(++redScore);

								playerList.getPlayer(arenaName, Message.GAME);

							}

						}

					} else if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueTeam(subscript))
							&& player.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueHas())){ //check to see if player is on blue team

						//check to see if the flag is missing
						if(blueTaken == true && player.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueHas())
								&& player.getLocation().getBlock().equals(flagLocation.blueFlagSpawn(arenaName).getBlock())){

							player.sendMessage(ChatColor.YELLOW + "You can't score if your flag is missing");

						} else { //score the flag

							if(player.getLocation().getBlock().equals(flagLocation.blueFlagSpawn(arenaName).getBlock())){

								player.getInventory().clear();

								kit.getDefault(arenaName, player);

								flagData.getRedFlag(arenaName);

								data.getArena(arenaNum).setRedTaken(false);
								data.getArena(arenaNum).setBlueHas(null);

								int blueScore = data.getArena(arenaNum).getBlueScore();
								data.getArena(arenaNum).setBlueScore(++blueScore);

								playerList.getPlayer(arenaName, Message.GAME);
							}

						}

					}

				}

			}

		}

	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){

		Player player;

		if(event.getEntity() instanceof Player){

			player = event.getEntity();

			for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

				int arenaNum = data.getArenaNum(arenaName);

				for(int subscript = 0; subscript < data.getArena(arenaNum).getInGameCount(); subscript++){

					if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(subscript)))
						inGame = true;
					
					if(inGame == true){
						
						boolean redTaken = data.getArena(arenaNum).getRedTaken();
						boolean blueTaken = data.getArena(arenaNum).getBlueTaken();

						if(subscript < data.getArena(arenaNum).getRedTeamCount() && player.getUniqueId().toString().equals(data.getArena(arenaNum).getRedTeam(subscript))){ //check to see if player is on red team

							if(blueTaken == true && player.getUniqueId().toString().equals(data.getArena(arenaNum).getRedHas())){

								flagData.getBlueDropped(arenaName, player);

								time.blueFlagTime(arenaName, 30);

								data.getArena(arenaNum).setRedHas(null);

								data.getArena(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has been dropped");
								playerList.getPlayer(arenaName, Message.FLAG);

							}
							
							player.getInventory().clear();
							event.getDrops().clear();
							player.setHealth(20);
							player.setGameMode(GameMode.SPECTATOR);

							onRespawn(player);

						} else if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueTeam(subscript))){ //check to see if player is on blue team {

							if(redTaken == true && player.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueHas())){

								flagData.getRedDropped(arenaName, player);

								time.redFlagTime(arenaName, 30);

								data.getArena(arenaNum).setBlueHas(null);

								data.getArena(arenaNum).setFlagMsg(ChatColor.BLUE + "Red flag has been dropped");
								playerList.getPlayer(arenaName, Message.FLAG);
							}
							
							player.getInventory().clear();
							event.getDrops().clear();
							player.setHealth(20);
							player.setGameMode(GameMode.SPECTATOR);

							onRespawn(player);

						}												
					}
				} 
			}
		}
	}

	public void onRespawn(Player player){

		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getInGameCount(); subscript++){

				if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(subscript)))
					inGame = true;
				
				if(inGame == true){

					if(subscript < data.getArena(arenaNum).getRedTeamCount() && player.getUniqueId().toString().equals(data.getArena(arenaNum).getRedTeam(subscript))){ //check to see if player is on red team

						redTask = new BukkitRunnable(){
							
							int seconds = 5;

							@Override
							public void run(){

								player.sendMessage(ChatColor.YELLOW + "Respawning in " + Integer.toString(seconds) + " second(s)");

								if(this.seconds == 0){

									player.setGameMode(GameMode.SURVIVAL);
									player.getInventory().clear();
									player.teleport(data.getRedSpawn(arenaName));
									kit.getDefault(arenaName, player);
									player.setHealth(20);
									player.removePotionEffect(PotionEffectType.INVISIBILITY);

									cancel();

								}

								this.seconds--;

							}
							
						}.runTaskTimer(main, 0, 20);

					} else if(subscript < data.getArena(arenaNum).getBlueTeamCount() && player.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueTeam(subscript))){ //check to see if player is on blue team {

						blueTask = new BukkitRunnable(){

							int seconds = 5;

							@Override
							public void run(){

								player.sendMessage(ChatColor.YELLOW + "Respawning in " + Integer.toString(seconds) + " second(s)");

								if(this.seconds == 0){

									player.setGameMode(GameMode.SURVIVAL);
									player.getInventory().clear();
									player.teleport(data.getBlueSpawn(arenaName));
									kit.getDefault(arenaName, player);
									player.removePotionEffect(PotionEffectType.INVISIBILITY);
									player.setHealth(20);

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

	@EventHandler
	public void onPlayerQuitMinecraft(PlayerQuitEvent event){

		Player player = event.getPlayer();

		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getInGameCount(); subscript++){

				if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(subscript))){

					rejoinTime = new BukkitRunnable(){

						int rejoinSeconds = 60;

						@Override
						public void run() {

							if(rejoinSeconds == 0){

								playerState.leaveGame(arenaName, player);
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

		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getInGameCount(); subscript++){

				if(player.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(subscript))){

					rejoinTime.cancel();
					onRespawn(player);
					playerList.getPlayer(arenaName, Message.GAME);

				}			

			}

		}

	}

}
