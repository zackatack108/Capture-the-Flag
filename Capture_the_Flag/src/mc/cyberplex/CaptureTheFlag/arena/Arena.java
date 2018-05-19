package mc.cyberplex.CaptureTheFlag.arena;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mc.cyberplex.CaptureTheFlag.Main;

class SpawnData {

	String world;
	double x, y, z, yaw, pitch;

	SpawnData(String world_, double x_, double y_, double z_, double yaw_, double pitch_){
		world = world_;
		x = x_;
		y = y_;
		z = z_;
		yaw = yaw_;
		pitch = pitch_;
	}

}

public class Arena {

	static Main main = Main.getMain();
	private int minutes, seconds;
	private static ArrayList<String> arenaNames = new ArrayList<String>();
	private static ArrayList<Arena> arenaData = new ArrayList<Arena>();
	private static ArrayList<CTFData> ctfData = new ArrayList<CTFData>();
	private ArrayList<UUID> playerList = new ArrayList<UUID>();
	public static Random rand = new Random();

	public void setArenaList() {

		if(main.getConfig().getConfigurationSection("Arenas") != null) {
			if(main.getConfig().getConfigurationSection("Arenas").getKeys(false) != null) {

				Set<String> tempArenas = main.getConfig().getConfigurationSection("Arenas").getKeys(false);
				String [] name = new String[tempArenas.size()];

				for(int i = 0; i < tempArenas.size(); i++) {				
					arenaNames.add(name[i]);
					arenaData.add(new Arena());
					ctfData.add(new CTFData());
				}

			}
		}

	}

	//----------------------------------------------
	//Class adders will add players, arena and spawn
	//----------------------------------------------	
	public void addArena(String name) {

		if(name!= null) {

			//save arena to the config
			main.getConfig().set("Arenas." + name, name);
			main.getConfig().set("Arenas." + name + ".time", 30);
			main.getConfig().set("Arenas." + name + ".min", 2);
			main.getConfig().set("Arenas." + name + ".max", 4);
			main.getConfig().set("Arenas." + name + ".state", "waiting for players");
			main.saveConfig();

			arenaNames.add(name);

		}

	}

	public void addSpawn(String arenaName, Player player){

		if(player != null && arenaName != null) {

			int spawnCount = getNumSpawns(arenaName);

			//create variables for where the player is at in the world
			String world = player.getWorld().getName();
			double x = player.getLocation().getX(),
					y = player.getLocation().getY(),
					z = player.getLocation().getZ(),
					yaw = player.getLocation().getYaw(),
					pitch = player.getLocation().getPitch();

			//save spawnpoint to config
			main.getConfig().set("Arenas." + arenaName + ".spawn." + spawnCount + ".world", world);
			main.getConfig().set("Arenas." + arenaName + ".spawn." + spawnCount + ".x", x);
			main.getConfig().set("Arenas." + arenaName + ".spawn." + spawnCount + ".y", y);
			main.getConfig().set("Arenas." + arenaName + ".spawn." + spawnCount + ".z", z);
			main.getConfig().set("Arenas." + arenaName + ".spawn." + spawnCount + ".yaw", yaw);
			main.getConfig().set("Arenas." + arenaName + ".spawn." + spawnCount + ".pitch", pitch);
			main.saveConfig();
		}

	}

	public void addPlayer(Player player){

		if(player != null) {

			playerList.add(player.getUniqueId());

		}

	}

	//-------------------------------------------------
	//Class setters will set min players, max players,
	//match max time, lobby, hub, join sign, shop sign,
	//minutes, and seconds
	//-------------------------------------------------
	public void setMinPlayers(String arenaName, int num){

		if(arenaName != null && (num > 0 && num < getMaxPlayers(arenaName))) {

			main.getConfig().set("Arenas." + arenaName + ".min", num);
			main.saveConfig();

		}

	}

	public void setMaxPlayers(String arenaName, int num){

		if(arenaName != null && (num > 0 && num > getMinPlayers(arenaName))) {

			main.getConfig().set("Arenas." + arenaName + ".max", num);
			main.saveConfig();

		}

	}

	public void setLobby(String arenaName, Player player){

		if(arenaName != null && player != null) {

			//create variables for where the player is at in the world
			String world = player.getWorld().getName();
			double x = player.getLocation().getX(),
					y = player.getLocation().getY(),
					z = player.getLocation().getZ(),
					yaw = player.getLocation().getYaw(),
					pitch = player.getLocation().getPitch();

			//save spawnpoint to config
			main.getConfig().set("Arenas." + arenaName + ".lobby.world", world);
			main.getConfig().set("Arenas." + arenaName + ".lobby.x", x);
			main.getConfig().set("Arenas." + arenaName + ".lobby.y", y);
			main.getConfig().set("Arenas." + arenaName + ".lobby.z", z);
			main.getConfig().set("Arenas." + arenaName + ".lobby.yaw", yaw);
			main.getConfig().set("Arenas." + arenaName + ".lobby.pitch", pitch);
			main.saveConfig();

		}

	}

	public void setHub(Player player){

		if(player != null) {

			//create variables for where the player is at in the world
			String world = player.getWorld().getName();
			double x = player.getLocation().getX(),
					y = player.getLocation().getY(),
					z = player.getLocation().getZ(),
					yaw = player.getLocation().getYaw(),
					pitch = player.getLocation().getPitch();

			//save spawnpoint to config
			main.getConfig().set("Hub.world", world);
			main.getConfig().set("Hub.x", x);
			main.getConfig().set("Hub.y", y);
			main.getConfig().set("Hub.z", z);
			main.getConfig().set("Hub.yaw", yaw);
			main.getConfig().set("Hub.pitch", pitch);
			main.saveConfig();

		}

	}

	public void setJoinSign(String arenaName, Block sign){

		if(arenaName != null && sign != null) {

			float signX = sign.getX(),
					signY = sign.getY(),
					signZ = sign.getZ();
			String world = sign.getWorld().getName();						

			main.getConfig().set("Arenas." + arenaName + ".join sign.world", world);
			main.getConfig().set("Arenas." + arenaName + ".join sign.x", signX);
			main.getConfig().set("Arenas." + arenaName + ".join sign.y", signY);
			main.getConfig().set("Arenas." + arenaName + ".join sign.z", signZ);
			main.saveConfig();

		}

	}

	public void setShopSign(Block sign){

		if(sign != null) {

			int num = getShopNum();

			float signX = sign.getX(),
					signY = sign.getY(),
					signZ = sign.getZ();
			String world = sign.getWorld().getName();						

			main.getConfig().set("Shop sign." + num + ".world", world);
			main.getConfig().set("Shop sign." + num + ".x", signX);
			main.getConfig().set("Shop sign." + num + ".y", signY);
			main.getConfig().set("Shop sign." + num + ".z", signZ);
			main.saveConfig();

		}

	}

	public void setMinutes(int min) {

		if(min >= 0) {
			minutes = min;
		}

	}

	public void setSeconds(int sec) {

		if(sec >= 0) {
			seconds = sec;
		}

	}

	public void setState(String arenaName, String state) {

		if(arenaName != null && state != null) {
			main.getConfig().set("Arenas." + arenaName + ".state", state);
			main.saveConfig();
		}

	}

	//------------------------------------------------
	//Class removers will remove arena, player, spawn,
	//lobby, hub, join sign, and shop sign
	//------------------------------------------------
	public void removeArena(String name) {

		if(name != null) {

			if(main.getConfig().contains("Arenas." + name)) {

				main.getConfig().set("Arenas." + name, null);
				main.saveConfig();

				if(arenaNames.isEmpty() == false) {
					arenaNames.remove(name);
				}

			}

		}

	}

	public void removePlayer(Player player) {

		if(player != null && playerList.isEmpty() == false) {
			playerList.remove(player.getUniqueId());
		}

	}

	public void removeSpawn(String arenaName, int num) {

		if(arenaName != null && num >= 0) {

			ArrayList<SpawnData> spawnList = new ArrayList<SpawnData>();

			//copy all the spawns from the config to the spawnList
			for(int count = 0; count < getNumSpawns(arenaName); count++) {

				String world = main.getConfig().getString("Arenas." + arenaName + ".spawn." + count + ".world");
				double x = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + count + ".x"),
						y = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + count + ".y"),
						z = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + count + ".z"),
						yaw = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + count + ".yaw"),
						pitch = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + count + ".pitch");

				spawnList.add(new SpawnData(world, x, y, z, yaw, pitch));
			}
			
			//remove the spawn from the spawn list
			spawnList.remove(num);
			
			//nuke all the spawns in the spawnlist to update the spawn numbers
			main.getConfig().set("Arenas." + arenaName + ".spawn", null);
			
			//saves all the spawn data back to the config
			for(int count = 0; count < spawnList.size(); count++) {
				
				String world = spawnList.get(count).world;
				double x = spawnList.get(count).x,
						y = spawnList.get(count).y,
						z = spawnList.get(count).z,
						yaw = spawnList.get(count).yaw,
						pitch = spawnList.get(count).pitch;
				
				
				//save spawnpoint to config
				main.getConfig().set("Arenas." + arenaName + ".spawn." + count + ".world", world);
				main.getConfig().set("Arenas." + arenaName + ".spawn." + count + ".x", x);
				main.getConfig().set("Arenas." + arenaName + ".spawn." + count + ".y", y);
				main.getConfig().set("Arenas." + arenaName + ".spawn." + count + ".z", z);
				main.getConfig().set("Arenas." + arenaName + ".spawn." + count + ".yaw", yaw);
				main.getConfig().set("Arenas." + arenaName + ".spawn." + count + ".pitch", pitch);
				main.saveConfig();
				
			}

		}

	}

	public void removeLobby(String arenaName) {

		if(arenaName != null) {

			//remove the lobby
			main.getConfig().set("Arenas." + arenaName +".lobby", null);
			main.saveConfig();

		}

	}

	public void removeHub() {

		//remove the hub
		main.getConfig().set("Hub", null);
		main.saveConfig();

	}

	public void removeJoinSign(Block sign, String arenaName) {

		if(sign != null) {

			main.getConfig().set("Arenas." + arenaName + ".join sign", null);
			main.saveConfig();

		}

	}

	public void removeShopSign(Block sign, int num) {

		if(sign != null) {

			main.getConfig().set("Shop sign." + num, null);
			main.saveConfig();

		}

	}

	//------------------------------------------------
	//Class getters will get arena, player, gameCount,
	//min players, max players, spawn, lobby, hub,
	//minutes, seconds, join sign, and shop sign
	//------------------------------------------------
	public Arena getArena(int index) {

		if(index < 0 || index > arenaNames.size())
			return null;
		if(arenaData.get(index).equals(null)) {
			ctfData.add(index, new CTFData());
		}

		return arenaData.get(index);
	}
	
	public CTFData getCTFData(int index) {
		
		if(index < 0 || index > arenaNames.size())
			return null;
		if(ctfData.get(index).equals(null)) {
			ctfData.add(index, new CTFData());
		}

		return ctfData.get(index);
		
	}

	public int getArenaNum(String arenaName) {

		int arenaNum = -1;

		for(int count = 0; count < arenaNames.size(); count++) {

			if(arenaNames.get(count).equalsIgnoreCase(arenaName)) {
				arenaNum = count;
			}

		}

		return arenaNum;		
	}

	public UUID getPlayer(int index) {

		if(index >= 0) {
			return playerList.get(index);
		}

		return null;
	}

	public int getGameCount() {
		return playerList.size();
	}

	public int getMinPlayers(String arenaName) {
		return main.getConfig().getInt("Arenas." + arenaName + ".min");
	}

	public int getMaxPlayers(String arenaName) {
		return main.getConfig().getInt("Arenas." + arenaName + ".max");
	}

	public Location getSpawn(String arenaName) {

		final int min = 0;
		int max = getNumSpawns(arenaName);
		int randomNum = rand.nextInt(max) + min;

		String world = main.getConfig().getString("Arenas." + arenaName + ".spawn." + randomNum + ".world");
		double x = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + randomNum + ".x"),
				y = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + randomNum + ".y"),
				z = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + randomNum + ".z"),
				yaw = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + randomNum + ".yaw"),
				pitch = main.getConfig().getDouble("Arenas." + arenaName + ".spawn." + randomNum + ".pitch");

		Location spawn = new Location(Bukkit.getServer().getWorld(world), x, y, z,(float) yaw, (float) pitch);

		return spawn;		
	}

	public Location getLobby(String arenaName) {

		String world = main.getConfig().getString("Arenas." + arenaName + ".lobby.world");
		double x = main.getConfig().getDouble("Arenas." + arenaName + ".lobby.x"),
				y = main.getConfig().getDouble("Arenas." + arenaName + ".lobby.y"),
				z = main.getConfig().getDouble("Arenas." + arenaName + ".lobby.z"),
				yaw = main.getConfig().getDouble("Arenas." + arenaName + ".lobby.yaw"),
				pitch = main.getConfig().getDouble("Arenas." + arenaName + ".lobby.pitch");

		Location lobby = new Location(Bukkit.getServer().getWorld(world), x, y, z,(float) yaw, (float) pitch);

		return lobby;
	}

	public Location getHub() {

		String world = main.getConfig().getString("Hub.world");
		double x = main.getConfig().getDouble("Hub.x"),
				y = main.getConfig().getDouble("Hub.y"),
				z = main.getConfig().getDouble("Hub.z"),
				yaw = main.getConfig().getDouble("Hub.yaw"),
				pitch = main.getConfig().getDouble("Hub.pitch");

		Location hub = new Location(Bukkit.getServer().getWorld(world), x, y, z,(float) yaw, (float) pitch);

		return hub;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public Location getJoinSign(String arenaName) {

		float x = main.getConfig().getInt("Arenas." + arenaName + ".join sign.x"),
				y = main.getConfig().getInt("Arenas." + arenaName + ".join sign.y"),
				z = main.getConfig().getInt("Arenas." + arenaName + ".join sign.z");
		String world = main.getConfig().getString("Arenas." + arenaName + ".join sign.world");

		Location signLocation = new Location(Bukkit.getServer().getWorld(world), x, y, z);

		return signLocation;
	}

	public Location getShopSign(int num) {

		float x = main.getConfig().getInt("Shop sign." + num + ".x"),
				y = main.getConfig().getInt("Shop sign." + num + ".y"),
				z = main.getConfig().getInt("Shop sign." + num + ".z");
		String world = main.getConfig().getString("Shop sign." + num + ".world");

		Location signLocation = new Location(Bukkit.getServer().getWorld(world), x, y, z);

		return signLocation;
	}

	public int getNumSpawns(String arenaName) {

		int spawnCount;

		Set<String> spawnList = null;

		if(main.getConfig().getConfigurationSection("Arenas." + arenaName + ".spawn") != null) {
			spawnList = main.getConfig().getConfigurationSection("Arenas." + arenaName + ".spawn").getKeys(false);
		}

		if(spawnList != null && spawnList.size() > 0) {
			spawnCount = spawnList.size();
		} else {
			spawnCount = 0;
		}

		return spawnCount;		
	}

	public int getShopNum() {

		int shopCount;

		Set<String> shopList = null;

		if(main.getConfig().getConfigurationSection("Shop sign") != null) {			
			shopList = main.getConfig().getConfigurationSection("Shop sign").getKeys(false);			
		}

		if(shopList != null && shopList.size() > 0) {
			shopCount = shopList.size();
		} else {
			shopCount = 0;
		}

		return shopCount;		
	}

	//------------------------------------
	//Inventory methods to save and return
	//a players inventory
	//------------------------------------
	private static HashMap<String, ItemStack[]> inventoryContents = new HashMap<String, ItemStack[]>();
	private static HashMap<String, ItemStack[]> armorContents = new HashMap<String, ItemStack[]>();

	public static void saveInventory(Player player){

		//get iventory and armor contents from player and save them to hashmap
		inventoryContents.put(player.getUniqueId().toString(), player.getInventory().getContents());
		armorContents.put(player.getUniqueId().toString(), player.getInventory().getArmorContents());

		//clear player inventory
		player.getInventory().clear();

	}

	public static void returnInventory(Player player){

		//clear player inventory
		player.getInventory().clear();

		//restore player inventory
		if(inventoryContents.containsKey(player.getUniqueId().toString()) && armorContents.containsKey(player.getUniqueId().toString())){

			player.getInventory().setContents(inventoryContents.get(player.getUniqueId().toString()));
			player.getInventory().setArmorContents(armorContents.get(player.getUniqueId().toString()));

		}

	}


}
