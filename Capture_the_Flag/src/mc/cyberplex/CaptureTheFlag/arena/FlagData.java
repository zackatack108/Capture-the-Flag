package mc.cyberplex.CaptureTheFlag.arena;

import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import mc.cyberplex.CaptureTheFlag.Main;

public class FlagData {

	FlagLocation flag = new FlagLocation();
	Main main = Main.getMain();

	Block block;
	Banner banner;
	BlockFace face;

	public void setBlueFlag(String arenaName, Player player){

		//Create string for world
		String world = player.getWorld().getName();

		//Create variable for the locations the player is standing
		double xPos = player.getLocation().getX(), 
				yPos = player.getLocation().getY(), 
				zPos = player.getLocation().getZ(),
				yaw = player.getLocation().getYaw(),
				pitch = player.getLocation().getPitch();

		//Save spawnpoint to config
		main.getConfig().set("Arenas." + arenaName + ".blue.flag.world", world);
		main.getConfig().set("Arenas." + arenaName + ".blue.flag.x", xPos);
		main.getConfig().set("Arenas." + arenaName + ".blue.flag.y", yPos);
		main.getConfig().set("Arenas." + arenaName + ".blue.flag.z", zPos);
		main.getConfig().set("Arenas." + arenaName + ".blue.flag.yaw", yaw);
		main.getConfig().set("Arenas." + arenaName + ".blue.flag.pitch", pitch);
		main.saveConfig();

	}

	public void setRedFlag(String arenaName, Player player){

		//Create string for world
		String world = player.getWorld().getName();

		//Create variable for the locations the player is standing
		double xPos = player.getLocation().getX(), 
				yPos = player.getLocation().getY(), 
				zPos = player.getLocation().getZ(),
				yaw = player.getLocation().getYaw(),
				pitch = player.getLocation().getPitch();

		//Save spawnpoint to config
		main.getConfig().set("Arenas." + arenaName + ".red.flag.world", world);
		main.getConfig().set("Arenas." + arenaName + ".red.flag.x", xPos);
		main.getConfig().set("Arenas." + arenaName + ".red.flag.y", yPos);
		main.getConfig().set("Arenas." + arenaName + ".red.flag.z", zPos);
		main.getConfig().set("Arenas." + arenaName + ".red.flag.yaw", yaw);
		main.getConfig().set("Arenas." + arenaName + ".red.flag.pitch", pitch);
		main.saveConfig();

	}

	//---------------------------------------------------
	//getters for flag data
	//---------------------------------------------------
	public void getBlueFlag(String arenaName){			

		block = flag.blueFlagSpawn(arenaName).getBlock();
		block.setType(Material.BLUE_BANNER);
		faceDirection(flag.yaw);

		BlockData bd = block.getBlockData();

		if(bd instanceof Rotatable) {
			Rotatable rot = (Rotatable) bd;
			rot.setRotation(face);
			block.setBlockData(rot);
		}

	}

	public void getRedFlag(String arenaName){

		block = flag.redFlagSpawn(arenaName).getBlock();
		block.setType(Material.RED_BANNER);
		faceDirection(flag.yaw);

		BlockData bd = block.getBlockData();

		if(bd instanceof Rotatable) {
			Rotatable rot = (Rotatable) bd;
			rot.setRotation(face);
			block.setBlockData(rot);
		}

	}

	public void getBlueDropped(String arenaName, Player player){

		block = flag.blueDropped(arenaName, player).getBlock();
		block.setType(Material.BLUE_BANNER);		

		faceDirection(flag.yaw);

		BlockData bd = block.getBlockData();

		if(bd instanceof Rotatable) {
			Rotatable rot = (Rotatable) bd;
			rot.setRotation(face);
			block.setBlockData(rot);
		}

	}

	public void getRedDropped(String arenaName, Player player){

		block = flag.redDropped(arenaName, player).getBlock();
		block.setType(Material.RED_BANNER);		

		faceDirection(flag.yaw);

		BlockData bd = block.getBlockData();

		if(bd instanceof Rotatable) {
			Rotatable rot = (Rotatable) bd;
			rot.setRotation(face);
			block.setBlockData(rot);
		}

	}

	//---------------------------------------------------
	//Banner direction for the flag face
	//---------------------------------------------------
	private void faceDirection(double yaw){

		if(yaw < 0)
			yaw += 360;

		yaw %= 360;

		int playerDirection = (int)((yaw+8) / 22.5);

		if(playerDirection == 0)			
			face = BlockFace.SOUTH;
		else if(playerDirection == 1)
			face = BlockFace.SOUTH_SOUTH_WEST;
		else if(playerDirection == 2)
			face = BlockFace.SOUTH_WEST;
		else if(playerDirection == 3)
			face = BlockFace.WEST_SOUTH_WEST;
		else if(playerDirection == 4)
			face = BlockFace.WEST;
		else if(playerDirection == 5)
			face = BlockFace.WEST_NORTH_WEST;
		else if(playerDirection == 6)
			face = BlockFace.NORTH_WEST;
		else if(playerDirection == 7)
			face = BlockFace.NORTH_NORTH_WEST;
		else if(playerDirection == 8)
			face = BlockFace.NORTH;			
		else if(playerDirection == 9)
			face = BlockFace.NORTH_NORTH_EAST;			
		else if(playerDirection == 10)	
			face = BlockFace.NORTH_EAST;
		else if(playerDirection == 11)
			face = BlockFace.EAST_NORTH_EAST;
		else if(playerDirection == 12)
			face = BlockFace.EAST;			
		else if(playerDirection == 13)
			face = BlockFace.EAST_SOUTH_EAST;
		else if(playerDirection == 14)
			face = BlockFace.SOUTH_EAST;
		else if(playerDirection == 15)	
			face = BlockFace.SOUTH_SOUTH_EAST;
		else 
			face = BlockFace.SOUTH;
	}

}
