package mc.cyberplex.CaptureTheFlag.arena;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import mc.cyberplex.CaptureTheFlag.Main;

public class CTFData {

	static Main main = Main.getMain();

	public BukkitTask Timer = null;
	public BukkitTask blueTimer = null;
	public BukkitTask redTimer = null;

	private int redScore = 0, blueScore = 0;
	private int blueSeconds = 0, redSeconds = 0, blueMinutes = 0, redMinutes = 0;

	private String scoreMsg = " ", flagMsg = " ";

	private UUID hasRedFlag = null, hasBlueFlag = null;
	private ArrayList<UUID> redTeam = new ArrayList<UUID>();
	private ArrayList<UUID> blueTeam = new ArrayList<UUID>();
	private ArrayList<String> playersKits = new ArrayList<String>();

	boolean redTaken = false, blueTaken = false;

	//--------------------------------------------
	//classes to join to the red and blue teams
	//--------------------------------------------
	public void joinRedTeam(Player player){

		if(player != null) {		
			redTeam.add(player.getUniqueId());		
		}

	}

	public void joinBlueTeam(Player player){

		if(player != null) {
			blueTeam.add(player.getUniqueId());
		}

	}

	//--------------------------------------------
	//classes to leave to the red and blue teams
	//--------------------------------------------
	public void leaveRedTeam(Player player){

		if(player != null && redTeam.isEmpty() == false) {
			redTeam.remove(player.getUniqueId());
		}

	}

	public void leaveBlueTeam(Player player){

		if(player != null && blueTeam.isEmpty() == false) {
			blueTeam.remove(player.getUniqueId());
		}

	}

	//--------------------------------------------
	//setters for class
	//--------------------------------------------
	public void setRedFlagSeconds(int sec){
		redSeconds = sec;
	}

	public void setBlueFlagSeconds(int sec){
		blueSeconds = sec;
	}

	public void setRedFlagMinutes(int min){
		redMinutes = min;
	}

	public void setBlueFlagMinutes(int min){
		blueMinutes = min;
	}

	public void setScoreMsg(String msg){
		scoreMsg = msg;
	}

	public void setFlagMsg(String msg){
		flagMsg = msg;
	}

	public void setRedScore(int score){
		redScore = score;
	}

	public void setBlueScore(int score){
		blueScore = score;
	}

	public void setHasRedFlag(Player player){

		if(player == null){
			hasRedFlag = null;
		} else {
			hasRedFlag = player.getUniqueId();
		}
	}

	public void setHasBlueFlag(Player player){

		if(player == null){
			hasBlueFlag = null;
		} else {
			hasBlueFlag = player.getUniqueId();
		}
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
		main.getConfig().set("Arenas." + arenaName + ".blue.spawn.world", world);
		main.getConfig().set("Arenas." + arenaName + ".blue.spawn.x", xPos);
		main.getConfig().set("Arenas." + arenaName + ".blue.spawn.y", yPos);
		main.getConfig().set("Arenas." + arenaName + ".blue.spawn.z", zPos);
		main.getConfig().set("Arenas." + arenaName + ".blue.spawn.yaw", yaw);
		main.getConfig().set("Arenas." + arenaName + ".blue.spawn.pitch", pitch);
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
		main.getConfig().set("Arenas." + arenaName + ".red.spawn.world", world);
		main.getConfig().set("Arenas." + arenaName + ".red.spawn.x", xPos);
		main.getConfig().set("Arenas." + arenaName + ".red.spawn.y", yPos);
		main.getConfig().set("Arenas." + arenaName + ".red.spawn.z", zPos);
		main.getConfig().set("Arenas." + arenaName + ".red.spawn.yaw", yaw);
		main.getConfig().set("Arenas." + arenaName + ".red.spawn.pitch", pitch);
		main.saveConfig();

	}

	public void setRedTaken(boolean taken){
		redTaken = taken;
	}

	public void setBlueTaken(boolean taken){
		blueTaken = taken;
	}
	
	public void addToPlayersKits(String kit) {
		playersKits.add(kit);	
	}
	
	//---------------------------------------------------
	//getters for class
	//---------------------------------------------------
	public UUID getPlayerOnRedTeam(int index){

		if(redTeam.get(index) == null) {
			return null;
		} else {		
			return redTeam.get(index);
		}
	}

	public int getRedTeamCount(){
		return redTeam.size();
	}

	public UUID getPlayerOnBlueTeam(int index){

		if(blueTeam.get(index) == null) {
			return null;
		} else {
			return blueTeam.get(index);
		}

	}

	public int getBlueTeamCount(){
		return blueTeam.size();
	}
	
	public String getPlayersKits(int index) {
		
		if(playersKits.get(index) == null) {
			return null;
		} else {
			return playersKits.get(index);
		}
		
	}
	
	public String getPlayerKit(Player player) {
		
		if(main.getConfig().getString("Kits." + player.getUniqueId().toString()) == null) {
			return null;
		}		
		return main.getConfig().getString("Kits." + player.getUniqueId().toString());		
	}

	public int getRedFlagSeconds(){
		return redSeconds;
	}

	public int getBlueFlagSeconds(){
		return blueSeconds;
	}

	public int getRedFlagMinutes(){
		return redMinutes;
	}

	public int getBlueFlagMinutes(){
		return blueMinutes;
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

	public UUID getHasRedFlag(){
		return hasRedFlag;
	}

	public UUID getHasBlueFlag(){
		return hasBlueFlag;
	}

	public boolean getRedTaken(){
		return redTaken;
	}

	public boolean getBlueTaken(){
		return blueTaken;
	}

	public Location getBlueSpawn(String arenaName){

		//get the lobby coordinates from the config
		String world = main.getConfig().getString("Arenas." + arenaName + ".blue.spawn.world");
		double xPos = main.getConfig().getDouble("Arenas." + arenaName + ".blue.spawn.x"),
				yPos = main.getConfig().getDouble("Arenas." + arenaName + ".blue.spawn.y"),
				zPos = main.getConfig().getDouble("Arenas." + arenaName + ".blue.spawn.z"),
				yaw = main.getConfig().getDouble("Arenas." + arenaName + ".blue.spawn.yaw"),
				pitch = main.getConfig().getDouble("Arenas." + arenaName + ".blue.spawn.pitch");

		Location blueSpawn = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		return blueSpawn;

	}

	public Location getRedSpawn(String arenaName){

		//get the lobby coordinates from the config
		String world = main.getConfig().getString("Arenas." + arenaName + ".red.spawn.world");
		double xPos = main.getConfig().getDouble("Arenas." + arenaName + ".red.spawn.x"),
				yPos = main.getConfig().getDouble("Arenas." + arenaName + ".red.spawn.y"),
				zPos = main.getConfig().getDouble("Arenas." + arenaName + ".red.spawn.z"),
				yaw = main.getConfig().getDouble("Arenas." + arenaName + ".red.spawn.yaw"),
				pitch = main.getConfig().getDouble("Arenas." + arenaName + ".red.spawn.pitch");

		Location redSpawn = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		return redSpawn;

	}

}