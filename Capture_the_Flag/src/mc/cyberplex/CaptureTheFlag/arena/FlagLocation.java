package mc.cyberplex.CaptureTheFlag.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.cyberplex.CaptureTheFlag.Main;

public class FlagLocation {
	
	Main main = Main.getMain();
	
	public String world;
	double xPos,yPos,zPos,yaw,pitch,xPos1,xPos2,zPos1,zPos2;
	
	private double[] blueArea = new double[4];
	private double[] redArea = new double[4];
	
	FileConfiguration config = main.getConfig();
	
	public Location redFlagSpawn(String arena){

		//get the red flag coordinates from the config
		world = config.getString("Arenas." + arena + ".red.flag.world");
		xPos = config.getDouble("Arenas." + arena + ".red.flag.x");
		yPos = config.getDouble("Arenas." + arena + ".red.flag.y");
		zPos = config.getDouble("Arenas." + arena + ".red.flag.z");
		yaw = config.getDouble("Arenas." + arena + ".red.flag.yaw");
		pitch = config.getDouble("Arenas." + arena + ".red.flag.pitch");

		Location redFlagLocation = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		return redFlagLocation;

	}

	public Location blueFlagSpawn(String arena){

		//get the blue flag coordinates from the config
		world = config.getString("Arenas." + arena + ".blue.flag.world");
		xPos= config.getDouble("Arenas." + arena + ".blue.flag.x");
		yPos = config.getDouble("Arenas." + arena + ".blue.flag.y");
		zPos = config.getDouble("Arenas." + arena + ".blue.flag.z");
		yaw = config.getDouble("Arenas." + arena + ".blue.flag.yaw");
		pitch = config.getDouble("Arenas." + arena + ".blue.flag.pitch");

		Location blueFlagLocation = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		return blueFlagLocation;

	}

	public double redFlagArea(String arena, int subscript){

		//get the red flag coordinates from the config
		world = config.getString("Arenas." + arena + ".red.flag.world");
		xPos = config.getDouble("Arenas." + arena + ".red.flag.x");
		yPos = config.getDouble("Arenas." + arena + ".red.flag.y");
		zPos = config.getDouble("Arenas." + arena + ".red.flag.z");
		yaw = config.getDouble("Arenas." + arena + ".red.flag.yaw");
		pitch = config.getDouble("Arenas." + arena + ".red.flag.pitch");

		xPos1 = xPos + 2.0;
		xPos2 = xPos - 2.0;
		zPos1 = zPos + 2.0;
		zPos2 = zPos - 2.0;

		Location redPoint1 = new Location(Bukkit.getServer().getWorld(world), xPos1, yPos, zPos1, (float) yaw, (float) pitch);
		Location redPoint2 = new Location(Bukkit.getServer().getWorld(world), xPos2, yPos, zPos2, (float) yaw, (float) pitch);

		redArea[0] = redPoint1.getX();
		redArea[1] = redPoint2.getX();
		redArea[2] = redPoint1.getZ();
		redArea[3] = redPoint2.getZ();

		return redArea[subscript];
	}

	public double blueFlagArea(String arena, int subscript){

		//get the blue flag coordinates from the config
		world = config.getString("Arenas." + arena + ".blue.flag.world");
		xPos= config.getDouble("Arenas." + arena + ".blue.flag.x");
		yPos = config.getDouble("Arenas." + arena + ".blue.flag.y");
		zPos = config.getDouble("Arenas." + arena + ".blue.flag.z");
		yaw = config.getDouble("Arenas." + arena + ".blue.flag.yaw");
		pitch = config.getDouble("Arenas." + arena + ".blue.flag.pitch");

		xPos1 = xPos + 2.0;
		xPos2 = xPos - 2.0;
		zPos1 = zPos + 2.0;
		zPos2 = zPos - 2.0;

		Location bluePoint1 = new Location(Bukkit.getServer().getWorld(world), xPos1, yPos, zPos1, (float) yaw, (float) pitch);
		Location bluePoint2 = new Location(Bukkit.getServer().getWorld(world), xPos2, yPos, zPos2, (float) yaw, (float) pitch);

		blueArea[0] = bluePoint1.getX();
		blueArea[1] = bluePoint2.getX();
		blueArea[2] = bluePoint1.getZ();
		blueArea[3] = bluePoint2.getZ();

		return blueArea[subscript];
	}

	public Location redDropped(String arena, Player player){

		//Create string for world
		world = player.getWorld().getName();
		xPos = player.getLocation().getX();
		yPos = player.getLocation().getY(); 
		zPos = player.getLocation().getZ();
		yaw = player.getLocation().getYaw();
		pitch = player.getLocation().getPitch();

		Location redFlag = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		config.set("Arenas." + arena + ".red.flag.dropped location.world", world);
		config.set("Arenas." + arena + ".red.flag.dropped location.x", xPos);
		config.set("Arenas." + arena + ".red.flag.dropped location.y", yPos);
		config.set("Arenas." + arena + ".red.flag.dropped location.z", zPos);
		config.set("Arenas." + arena + ".red.flag.dropped location.yaw", yaw);
		config.set("Arenas." + arena + ".red.flag.dropped location.pitch", pitch);
		main.saveConfig();

		return redFlag;
	}

	public Location blueDropped(String arena, Player player){

		//Create string for world
		world = player.getWorld().getName();
		xPos = player.getLocation().getX();
		yPos = player.getLocation().getY(); 
		zPos = player.getLocation().getZ();
		yaw = player.getLocation().getYaw();
		pitch = player.getLocation().getPitch();

		Location blueFlag = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		config.set("Arenas." + arena + ".blue.flag.dropped location.world", world);
		config.set("Arenas." + arena + ".blue.flag.dropped location.x", xPos);
		config.set("Arenas." + arena + ".blue.flag.dropped location.y", yPos);
		config.set("Arenas." + arena + ".blue.flag.dropped location.z", zPos);
		config.set("Arenas." + arena + ".blue.flag.dropped location.yaw", yaw);
		config.set("Arenas." + arena + ".blue.flag.dropped location.pitch", pitch);
		main.saveConfig();

		return blueFlag;

	}

	public Location redDroppedLocation(String arena){

		//get the red flag coordinates from the config
		world = config.getString("Arenas." + arena + ".red.flag.dropped location.world");
		xPos = config.getDouble("Arenas." + arena + ".red.flag.dropped location.x");
		yPos = config.getDouble("Arenas." + arena + ".red.flag.dropped location.y");
		zPos = config.getDouble("Arenas." + arena + ".red.flag.dropped location.z");
		yaw = config.getDouble("Arenas." + arena + ".red.flag.dropped location.yaw");
		pitch = config.getDouble("Arenas." + arena + ".red.flag.dropped location.pitch");

		Location redFlagLocation = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		return redFlagLocation;

	}
	
	public Location blueDroppedLocation(String arena){

		//get the red flag coordinates from the config
		world = config.getString("Arenas." + arena + ".blue.flag.dropped location.world");
		xPos = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.x");
		yPos = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.y");
		zPos = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.z");
		yaw = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.yaw");
		pitch = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.pitch");

		Location blueFlagLocation = new Location(Bukkit.getServer().getWorld(world), xPos, yPos, zPos, (float) yaw, (float) pitch);

		return blueFlagLocation;

	}

	public double redDroppedFlagArea(String arena, int subscript){

		//get the red flag coordinates from the config
		world = config.getString("Arenas." + arena + ".red.flag.dropped location.world");
		xPos = config.getDouble("Arenas." + arena + ".red.flag.dropped location.x");
		yPos = config.getDouble("Arenas." + arena + ".red.flag.dropped location.y");
		zPos = config.getDouble("Arenas." + arena + ".red.flag.dropped location.z");
		yaw = config.getDouble("Arenas." + arena + ".red.flag.dropped location.yaw");
		pitch = config.getDouble("Arenas." + arena + ".red.flag.dropped location.pitch");

		xPos1 = xPos + 2.0;
		xPos2 = xPos - 2.0;
		zPos1 = zPos + 2.0;
		zPos2 = zPos - 2.0;

		Location redPoint1 = new Location(Bukkit.getServer().getWorld(world), xPos1, yPos, zPos1, (float) yaw, (float) pitch);
		Location redPoint2 = new Location(Bukkit.getServer().getWorld(world), xPos2, yPos, zPos2, (float) yaw, (float) pitch);

		redArea[0] = redPoint1.getX();
		redArea[1] = redPoint2.getX();
		redArea[2] = redPoint1.getZ();
		redArea[3] = redPoint2.getZ();

		return redArea[subscript];
	}

	public double blueDroppedFlagArea(String arena, int subscript){

		//get the red flag coordinates from the config
		world = config.getString("Arenas." + arena + ".blue.flag.dropped location.world");
		xPos = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.x");
		yPos = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.y");
		zPos = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.z");
		yaw = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.yaw");
		pitch = config.getDouble("Arenas." + arena + ".blue.flag.dropped location.pitch");

		xPos1 = xPos + 2.0;
		xPos2 = xPos - 2.0;
		zPos1 = zPos + 2.0;
		zPos2 = zPos - 2.0;

		Location bluePoint1 = new Location(Bukkit.getServer().getWorld(world), xPos1, yPos, zPos1, (float) yaw, (float) pitch);
		Location bluePoint2 = new Location(Bukkit.getServer().getWorld(world), xPos2, yPos, zPos2, (float) yaw, (float) pitch);

		blueArea[0] = bluePoint1.getX();
		blueArea[1] = bluePoint2.getX();
		blueArea[2] = bluePoint1.getZ();
		blueArea[3] = bluePoint2.getZ();

		return blueArea[subscript];
	}
	
}
