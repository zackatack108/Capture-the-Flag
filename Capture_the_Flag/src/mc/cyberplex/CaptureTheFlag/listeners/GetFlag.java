package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.Timer.Timer;
import mc.cyberplex.CaptureTheFlag.Timer.TimerType;
import mc.cyberplex.CaptureTheFlag.arena.Arena;
import mc.cyberplex.CaptureTheFlag.arena.FlagData;
import mc.cyberplex.CaptureTheFlag.arena.FlagLocation;
import mc.cyberplex.CaptureTheFlag.arena.Message;
import mc.cyberplex.CaptureTheFlag.arena.PlayerList;
import mc.cyberplex.CaptureTheFlag.arena.PlayerState;
import mc.cyberplex.CaptureTheFlag.kits.Kits;

public class GetFlag implements Listener{

	Main main = Main.getMain();
	Arena data = new Arena();
	FlagLocation flagLocation = new FlagLocation();
	FlagData flagData = new FlagData();
	Timer time = new Timer();
	PlayerList playerList = new PlayerList();
	Kits kit = new Kits();
	PlayerState playerState = new PlayerState();

	boolean inGame = false, onRed = false, onBlue = false, hasRed = false, hasBlue = false;
	Block block;
	
	private final int maxTime = 3;

	@EventHandler
	public void onGetFlag(PlayerInteractEvent event) {

		Player player = event.getPlayer();

		double[] redArea = new double[4], blueArea = new double[4];

		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)) {

			int arenaNum = data.getArenaNum(arenaName);

			for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++) {

				if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(subscript))) {
					inGame = true;
				}

			}

			if(inGame == true && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) && event.getClickedBlock().getState() instanceof Banner) {

				boolean redTaken = data.getCTFData(arenaNum).getRedTaken();
				boolean blueTaken = data.getCTFData(arenaNum).getBlueTaken();

				//cycle through players on red team
				for(int subscript = 0; subscript < data.getCTFData(arenaNum).getRedTeamCount(); subscript++) {

					//check if the player is on red team
					if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnRedTeam(subscript))) {

						//check if blue flag is taken
						if(blueTaken == true && data.getCTFData(arenaNum).getHasBlueFlag() == null) {

							//get where the blue flag was dropped
							for(int count = 0; count < 4; count++) {
								blueArea[count] = flagLocation.blueDroppedFlagArea(arenaName, count);
							}

							//check if the player is near the blue flag drop area and picks up the flag
							if(player.getLocation().getX() < blueArea[0] && player.getLocation().getX() > blueArea[1]
									&& player.getLocation().getZ() < blueArea[2] && player.getLocation().getZ() > blueArea[3]) {

								Banner banner = (Banner) event.getClickedBlock().getState();

								if(banner.getBaseColor().equals(DyeColor.BLUE)) {

									ItemStack blueFlag = new ItemStack(Material.BLUE_BANNER);
									blueFlag.setDurability((short) 4);

									//create colored armor for red
									ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
									ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
									ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

									LeatherArmorMeta chest = (LeatherArmorMeta) chestplate.getItemMeta();
									LeatherArmorMeta legs = (LeatherArmorMeta) leggings.getItemMeta();
									LeatherArmorMeta feet = (LeatherArmorMeta) boots.getItemMeta();

									chest.setColor(Color.RED);
									legs.setColor(Color.RED);
									feet.setColor(Color.RED);

									chestplate.setItemMeta(chest);
									leggings.setItemMeta(legs);
									boots.setItemMeta(feet);

									ItemStack ironSword = new ItemStack(Material.IRON_SWORD, 1);

									player.getInventory().clear();
									player.getInventory().setHelmet(blueFlag);		
									player.getInventory().setChestplate(chestplate);
									player.getInventory().setLeggings(leggings);
									player.getInventory().setBoots(boots);
									player.getInventory().addItem(ironSword);

									block = flagLocation.blueDroppedLocation(arenaName).getBlock();	
									block.setType(Material.AIR);

									time.stopTimer(arenaName, TimerType.BLUE);
									time.blueFlagTime(arenaName, maxTime, 0);

									playerList.getPlayer(arenaName, Message.GAME);

									data.getCTFData(arenaNum).setBlueTaken(true);
									data.getCTFData(arenaNum).setHasBlueFlag(player);

									data.getCTFData(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has been taken by " + player.getName());
									playerList.getPlayer(arenaName, Message.FLAG);

								}
							}

						} else {

							//get the blue flag spawn area
							for(int count = 0; count < 4; count++) {
								blueArea[count] = flagLocation.blueFlagArea(arenaName, count);
							}

							//check if the player is near the flag spawn area and pick up the flag if in the area
							if(player.getLocation().getX() < blueArea[0] && player.getLocation().getX() > blueArea[1]
									&& player.getLocation().getZ() < blueArea[2] && player.getLocation().getZ() > blueArea[3]) {

								Banner banner = (Banner) event.getClickedBlock().getState();

								if(banner.getBaseColor().equals(DyeColor.BLUE)) {

									ItemStack blueFlag = new ItemStack(Material.BLUE_BANNER);
									blueFlag.setDurability((short) 4);								

									//create colored armor for red
									ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
									ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
									ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

									LeatherArmorMeta chest = (LeatherArmorMeta) chestplate.getItemMeta();
									LeatherArmorMeta legs = (LeatherArmorMeta) leggings.getItemMeta();
									LeatherArmorMeta feet = (LeatherArmorMeta) boots.getItemMeta();

									chest.setColor(Color.RED);
									legs.setColor(Color.RED);
									feet.setColor(Color.RED);

									chestplate.setItemMeta(chest);
									leggings.setItemMeta(legs);
									boots.setItemMeta(feet);

									ItemStack ironSword = new ItemStack(Material.IRON_SWORD, 1);

									player.getInventory().clear();
									player.getInventory().setHelmet(blueFlag);		
									player.getInventory().setChestplate(chestplate);
									player.getInventory().setLeggings(leggings);
									player.getInventory().setBoots(boots);
									player.getInventory().addItem(ironSword);				

									block = flagLocation.blueFlagSpawn(arenaName).getBlock();	
									block.setType(Material.AIR);
									
									time.stopTimer(arenaName, TimerType.BLUE);
									time.blueFlagTime(arenaName, maxTime, 0);

									data.getCTFData(arenaNum).setBlueTaken(true);
									data.getCTFData(arenaNum).setHasBlueFlag(player);

									data.getCTFData(arenaNum).setFlagMsg(ChatColor.BLUE + "Blue flag has been taken by " + player.getName());
									playerList.getPlayer(arenaName, Message.FLAG);

								}

							}

						}

					}

				}				

				//cycle through players on blue team
				for(int subscript = 0; subscript < data.getCTFData(arenaNum).getBlueTeamCount(); subscript++) {

					//check if the player is on blue team
					if(player.getUniqueId().equals(data.getCTFData(arenaNum).getPlayerOnBlueTeam(subscript))) {

						//check to see if red flag is taken
						if(redTaken == true && data.getCTFData(arenaNum).getHasRedFlag() == null) {

							//get the area the red flag was dropped
							for(int count = 0; count < 4; count++) {
								redArea[count] = flagLocation.redDroppedFlagArea(arenaName, count);
							}

							//check to see if the player is in the area the red flag was dropped and pick the flag up if true
							if(player.getLocation().getX() < redArea[0] && player.getLocation().getX() > redArea[1]
									&& player.getLocation().getZ() < redArea[2] && player.getLocation().getZ() > redArea[3]){

								Banner banner = (Banner) event.getClickedBlock().getState();

								if(banner.getBaseColor().equals(DyeColor.RED)){

									ItemStack redFlag = new ItemStack(Material.RED_BANNER);
									redFlag.setDurability((short) 1);								

									//create colored armor for blue
									ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
									ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
									ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

									LeatherArmorMeta chest = (LeatherArmorMeta) chestplate.getItemMeta();
									LeatherArmorMeta legs = (LeatherArmorMeta) leggings.getItemMeta();
									LeatherArmorMeta feet = (LeatherArmorMeta) boots.getItemMeta();

									chest.setColor(Color.BLUE);
									legs.setColor(Color.BLUE);
									feet.setColor(Color.BLUE);

									chestplate.setItemMeta(chest);
									leggings.setItemMeta(legs);
									boots.setItemMeta(feet);

									ItemStack ironSword = new ItemStack(Material.IRON_SWORD, 1);

									player.getInventory().clear();
									player.getInventory().setHelmet(redFlag);		
									player.getInventory().setChestplate(chestplate);
									player.getInventory().setLeggings(leggings);
									player.getInventory().setBoots(boots);
									player.getInventory().addItem(ironSword);				

									block = flagLocation.redDroppedLocation(arenaName).getBlock();	
									block.setType(Material.AIR);

									time.stopTimer(arenaName, TimerType.RED);
									time.redFlagTime(arenaName, maxTime, 0);

									playerList.getPlayer(arenaName, Message.GAME);

									data.getCTFData(arenaNum).setRedTaken(true);
									data.getCTFData(arenaNum).setHasRedFlag(player);;

									data.getCTFData(arenaNum).setFlagMsg(ChatColor.RED + "Red flag has been taken by " + player.getName());
									playerList.getPlayer(arenaName, Message.FLAG);

								}

							}

						} else {

							//get the spawn area of the red flag
							for(int count = 0; count < 4; count++){
								redArea[count] = flagLocation.redFlagArea(arenaName, count);
							}

							//check to see if the player is in the area of the red flag and pick up the flag if true
							if(player.getLocation().getX() < redArea[0] && player.getLocation().getX() > redArea[1]
									&& player.getLocation().getZ() < redArea[2] && player.getLocation().getZ() > redArea[3]){

								Banner banner = (Banner) event.getClickedBlock().getState();

								if(banner.getBaseColor().equals(DyeColor.RED)){

									ItemStack redFlag = new ItemStack(Material.RED_BANNER);
									redFlag.setDurability((short) 1);								

									//create colored armor for blue
									ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
									ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
									ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

									LeatherArmorMeta chest = (LeatherArmorMeta) chestplate.getItemMeta();
									LeatherArmorMeta legs = (LeatherArmorMeta) leggings.getItemMeta();
									LeatherArmorMeta feet = (LeatherArmorMeta) boots.getItemMeta();

									chest.setColor(Color.BLUE);
									legs.setColor(Color.BLUE);
									feet.setColor(Color.BLUE);

									chestplate.setItemMeta(chest);
									leggings.setItemMeta(legs);
									boots.setItemMeta(feet);

									ItemStack ironSword = new ItemStack(Material.IRON_SWORD, 1);

									player.getInventory().clear();
									player.getInventory().setHelmet(redFlag);		
									player.getInventory().setChestplate(chestplate);
									player.getInventory().setLeggings(leggings);
									player.getInventory().setBoots(boots);
									player.getInventory().addItem(ironSword);					

									block = flagLocation.redFlagSpawn(arenaName).getBlock();	
									block.setType(Material.AIR);
									
									time.stopTimer(arenaName, TimerType.RED);
									time.redFlagTime(arenaName, maxTime, 0);

									data.getCTFData(arenaNum).setRedTaken(true);
									data.getCTFData(arenaNum).setHasRedFlag(player);

									data.getCTFData(arenaNum).setFlagMsg(ChatColor.RED + "Red flag has been taken by " + player.getName());
									playerList.getPlayer(arenaName, Message.FLAG);

								}

							}

						}

					}

				}

			}

		}

	}

}