package ctf;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArenaData {

	static Main main = Main.getMain();
	
	//inventory data
	private static HashMap<String, ItemStack[]> inventoryContents = new HashMap<String, ItemStack[]>();
	private static HashMap<String, ItemStack[]> armorContents = new HashMap<String, ItemStack[]>();	

	public int lobbyTimer = 0, gameTimer = 0, blueFlagTimer = 0, redFlagTimer = 0;
	private int inGameCount = 0, redTeamCount = 0, blueTeamCount = 0;
	private int redScore = 0, blueScore = 0;
	private int timeSeconds = 0, timeMinutes = 0, blueTimeCount = 61, redTimeCount = 61;

	private String redTeam[] = new String[50], blueTeam[] = new String[50], inGame[] = new String[100];
	private String gameTime= " ", redTime = " ", blueTime = " ", lobbyTime = "Waiting...";
	private String redHas = " ", blueHas = " ";
	private String scoreMsg = " ", flagMsg = " ";

	boolean redTaken = false, blueTaken = false;

	//get arenas from config and save them to arena list
	static Set<String> arenas = main.getConfig().getConfigurationSection("Arenas").getKeys(false);
	static String[] arenaList = new String[arenas.size()];
	static ArenaData[] dataArray = new ArenaData[arenas.size()];

	//--------------------
	//setters for class
	//--------------------
	public void setInGame(Player player){

		inGame[inGameCount] = new String();

		if(player == null){
			inGame[inGameCount] = null;
			inGameCount = 0;
			return;
		} else {
			inGame[inGameCount] = player.getUniqueId().toString();
		}

		inGameCount++;
	}

	public void setRedTeam(Player player){

		redTeam[redTeamCount] = new String();

		if(player == null){
			redTeam[redTeamCount] = null;
			redTeamCount = 0;
			return;
		} else {
			redTeam[redTeamCount] = player.getUniqueId().toString();
		}
		
		redTeamCount++;
	}

	public void setBlueTeam(Player player){

		blueTeam[blueTeamCount] = new String();

		if(player == null){
			blueTeam[blueTeamCount] = null;
			blueTeamCount = 0;
			return;
		} else {
			blueTeam[blueTeamCount] = player.getUniqueId().toString();
		}

		blueTeamCount++;

	}

	public void setLobbyTime(String msg){
		lobbyTime = msg;
	}

	public void setGameTime(String msg){
		gameTime = msg;
	}

	public void setRedFlagTime(String msg){
		redTime = msg;
	}

	public void setBlueFlagTime(String msg){
		blueTime = msg;
	}

	public void setScoreMsg(String msg){
		scoreMsg = msg;
	}

	public void setFlagMsg(String msg){
		flagMsg = msg;
	}

	public void setSeconds(int seconds){
		timeSeconds = seconds;
	}
	
	public void setMinutes(int minutes){
		timeMinutes = minutes;
	}

	public void setRedTimeCount(int seconds){
		redTimeCount = seconds;
	}

	public void setBlueTimeCount(int seconds){
		blueTimeCount = seconds;
	}

	public void setRedScore(int score){
		redScore = score;
	}

	public void setBlueScore(int score){
		blueScore = score;
	}

	public void setRedHas(Player player){
		
		if(player == null){
			redHas = null;
		} else {
			redHas = player.getUniqueId().toString();
		}
	}

	public void setBlueHas(Player player){
		
		if(player == null){
			blueHas = null;
		} else {
			blueHas = player.getUniqueId().toString();
		}
	}

	public void setInGameCount(int count){
		inGameCount = count;
	}

	public void setRedCount(int count){
		redTimeCount = count;
	}

	public void setBlueCount(int count){
		blueTimeCount = count;
	}

	public void setHub(Player player){

		//Create string for world
		String world = player.getWorld().getName();

		//Create variable for the locations a player is standing
		double xPos = player.getLocation().getX(), 
				yPos = player.getLocation().getY(), 
				zPos = player.getLocation().getZ(),
				yaw = player.getLocation().getYaw(),
				pitch = player.getLocation().getPitch();

		//Save spawnpoint to config
		main.getConfig().set("Hub.World", world);
		main.getConfig().set("Hub.X", xPos);
		main.getConfig().set("Hub.Y", yPos);
		main.getConfig().set("Hub.Z", zPos);
		main.getConfig().set("Hub.Yaw", yaw);
		main.getConfig().set("Hub.Pitch", pitch);
		main.saveConfig();

	}

	public void setLobby(String arenaName, Player player){

		//Create string for world
		String world = player.getWorld().getName();

		//Create variable for the locations a player is standing
		double xPos = player.getLocation().getX(), 
				yPos = player.getLocation().getY(), 
				zPos = player.getLocation().getZ(),
				yaw = player.getLocation().getYaw(),
				pitch = player.getLocation().getPitch();

		//Save spawnpoint to config
		main.getConfig().set("Arenas." + arenaName + ".Lobby.World", world);
		main.getConfig().set("Arenas." + arenaName + ".Lobby.X", xPos);
		main.getConfig().set("Arenas." + arenaName + ".Lobby.Y", yPos);
		main.getConfig().set("Arenas." + arenaName + ".Lobby.Z", zPos);
		main.getConfig().set("Arenas." + arenaName + ".Lobby.Yaw", yaw);
		main.getConfig().set("Arenas." + arenaName + ".Lobby.Pitch", pitch);
		main.saveConfig();

	}

	public void setBlueSpawn(String arenaName, Player player){

		//Create string for world
		String world = player.getWorld().getName();

		//Create variable for the locations the player is standing
		double xPos = player.getLocation().getX(), 
				yPos = player.getLocation().getY(), 
				zPos = player.getLocation().getZ(),
				yaw = player.getLocation().getYaw(),
				pitch = player.getLocation().getPitch();

		//Save spawnpoint to config
		main.getConfig().set("Arenas." + arenaName + ".Blue.Spawn.World", world);
		main.getConfig().set("Arenas." + arenaName + ".Blue.Spawn.X", xPos);
		main.getConfig().set("Arenas." + arenaName + ".Blue.Spawn.Y", yPos);
		main.getConfig().set("Arenas." + arenaName + ".Blue.Spawn.Z", zPos);
		main.getConfig().set("Arenas." + arenaName + ".Blue.Spawn.Yaw", yaw);
		main.getConfig().set("Arenas." + arenaName + ".Blue.Spawn.Pitch", pitch);
		main.saveConfig();

	}

	public void setRedSpawn(String arenaName, Player player){

		//Create string for world
		String world = player.getWorld().getName();

		//Create variable for the locations the player is standing
		double xPos = player.getLocation().getX(), 
				yPos = player.getLocation().getY(), 
				zPos = player.getLocation().getZ(),
				yaw = player.getLocation().getYaw(),
				pitch = player.getLocation().getPitch();

		//Save spawnpoint to config
		main.getConfig().set("Arenas." + arenaName + ".Red.Spawn.World", world);
		main.getConfig().set("Arenas." + arenaName + ".Red.Spawn.X", xPos);
		main.getConfig().set("Arenas." + arenaName + ".Red.Spawn.Y", yPos);
		main.getConfig().set("Arenas." + arenaName + ".Red.Spawn.Z", zPos);
		main.getConfig().set("Arenas." + arenaName + ".Red.Spawn.Yaw", yaw);
		main.getConfig().set("Arenas." + arenaName + ".Red.Spawn.Pitch", pitch);
		main.saveConfig();

	}

	public void setRedTaken(boolean taken){
		redTaken = taken;
	}
	
	public void setBlueTaken(boolean taken){
		blueTaken = taken;
	}
	
	//---------------------------------------------------
	//getters for class
	//---------------------------------------------------
	public ArenaData getArena(int index){

		if(index < 0 || index > arenas.size())
			return null;
		if(dataArray[index] == null)
			dataArray[index] = new ArenaData();

		return dataArray[index];

	}

	public int getArenaNum(String arenaName){

		int arenaNum;

		//puts the arenas to an array of arenaList
		arenas.toArray(arenaList);

		//sets areaNum to -1 and count to 0
		arenaNum = -1;
		int count = 0;

		//cycle through the arena list
		for(String arena: arenaList){

			//check to see if the arenaName matches arena
			if(arena.equalsIgnoreCase(arenaName)){

				//assigns the arena number to the count
				arenaNum = count;

			}

			count++;
		}

		return arenaNum;

	}

	public String getInGame(int index){
		return inGame[index];
	}

	public int getInGameCount(){
		return inGameCount;
	}

	public String getRedTeam(int index){
		return redTeam[index];
	}

	public int getRedTeamCount(){
		return redTeamCount;
	}

	public String getBlueTeam(int index){
		return blueTeam[index];
	}

	public int getBlueTeamCount(){
		return blueTeamCount;
	}

	public String getLobbyTime(){
		return lobbyTime;
	}

	public String getGameTime(){
		return gameTime;
	}

	public String getRedFlagTime(){
		return redTime;
	}

	public String getBlueFlagTime(){
		return blueTime;
	}

	public int getTimeSeconds(){
		return timeSeconds;
	}
	
	public int getTimeMinutes(){
		return timeMinutes;
	}

	public int getRedTimeCount(){
		return redTimeCount;
	}

	public int getBlueTimeCount(){
		return blueTimeCount;
	}

	public String getScoreMsg(){
		return scoreMsg;
	}

	public String getFlagMsg(){
		return flagMsg;
	}

	public int getRedScore(){
		return redScore;
	}

	public int getBlueScore(){
		return blueScore;
	}

	public String getRedHas(){
		return redHas;
	}

	public String getBlueHas(){
		return blueHas;
	}

	public boolean getRedTaken(){
		return redTaken;
	}
	
	public boolean getBlueTaken(){
		return blueTaken;
	}
	
	public Location getHub(){

		//get the hub coordinates  from the config
		String hubWorld = main.getConfig().getString("Hub.World");
		double hubXPos = main.getConfig().getDouble("Hub.X"),
				hubYPos = main.getConfig().getDouble("Hub.Y"),
				hubZPos = main.getConfig().getDouble("Hub.Z"),
				hubYaw = main.getConfig().getDouble("Hub.Yaw"),
				hubPitch = main.getConfig().getDouble("Hub.Pitch");

		Location hub = new Location(Bukkit.getServer().getWorld(hubWorld), hubXPos, hubYPos, hubZPos, (float) hubYaw, (float) hubPitch);

		return hub;
	}

	public Location getLobby(String arenaName){

		//get the lobby coordinates from the config
		String world = main.getConfig().getString("Arenas." + arenaName + ".Lobby.World");
		double xPos = main.getConfig().getDouble("Arenas." + arenaName + ".Lobby.X"),
				yPos = main.getConfig().getDouble("Arenas." + arenaName + ".Lobby.Y"),
				zPos = main.getConfig().getDouble("Arenas." + arenaName + ".Lobby.Z"),
				yaw = main.getConfig().getDouble("Arenas." + arenaName + ".Lobby.Yaw"),
				pitch = main.getConfig().getDouble("Arenas." + arenaName + ".Lobby.Pitch");

		Location lobby = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		return lobby;
	}

	public Location getBlueSpawn(String arenaName){

		//get the lobby coordinates from the config
		String world = main.getConfig().getString("Arenas." + arenaName + ".Blue.Spawn.World");
		double xPos = main.getConfig().getDouble("Arenas." + arenaName + ".Blue.Spawn.X"),
				yPos = main.getConfig().getDouble("Arenas." + arenaName + ".Blue.Spawn.Y"),
				zPos = main.getConfig().getDouble("Arenas." + arenaName + ".Blue.Spawn.Z"),
				yaw = main.getConfig().getDouble("Arenas." + arenaName + ".Blue.Spawn.Yaw"),
				pitch = main.getConfig().getDouble("Arenas." + arenaName + ".Blue.Spawn.Pitch");

		Location blueSpawn = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		return blueSpawn;

	}

	public Location getRedSpawn(String arenaName){

		//get the lobby coordinates from the config
		String world = main.getConfig().getString("Arenas." + arenaName + ".Red.Spawn.World");
		double xPos = main.getConfig().getDouble("Arenas." + arenaName + ".Red.Spawn.X"),
				yPos = main.getConfig().getDouble("Arenas." + arenaName + ".Red.Spawn.Y"),
				zPos = main.getConfig().getDouble("Arenas." + arenaName + ".Red.Spawn.Z"),
				yaw = main.getConfig().getDouble("Arenas." + arenaName + ".Red.Spawn.Yaw"),
				pitch = main.getConfig().getDouble("Arenas." + arenaName + ".Red.Spawn.Pitch");

		Location redSpawn = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		return redSpawn;

	}

	//------------------------------------------------------------------------------------------------------
	//inventory functions
	//------------------------------------------------------------------------------------------------------
	public void saveInventory(Player player){

		//get iventory and armor contents from player and save them to hashmap
		inventoryContents.put(player.getUniqueId().toString(), player.getInventory().getContents());
		armorContents.put(player.getUniqueId().toString(), player.getInventory().getArmorContents());

		//clear player inventory
		player.getInventory().clear();

	}

	public void returnInventory(Player player){

		//clear player inventory
		player.getInventory().clear();

		//restore player inventory
		if(inventoryContents.containsKey(player.getUniqueId().toString()) && armorContents.containsKey(player.getUniqueId().toString())){

			player.getInventory().setContents(inventoryContents.get(player.getUniqueId().toString()));
			player.getInventory().setArmorContents(armorContents.get(player.getUniqueId().toString()));

		}

	}
}
