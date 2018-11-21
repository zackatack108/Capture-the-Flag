package mc.cyberplex.CaptureTheFlag;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import mc.cyberplex.CaptureTheFlag.arena.Arena;
import mc.cyberplex.CaptureTheFlag.arena.ArenaState;
import mc.cyberplex.CaptureTheFlag.kits.KitListener;
import mc.cyberplex.CaptureTheFlag.listeners.FlagBererDamage;
import mc.cyberplex.CaptureTheFlag.listeners.FriendlyFire;
import mc.cyberplex.CaptureTheFlag.listeners.GetFlag;
import mc.cyberplex.CaptureTheFlag.listeners.Inventory;
import mc.cyberplex.CaptureTheFlag.listeners.JoinSign;
import mc.cyberplex.CaptureTheFlag.listeners.LeaveJoinMC;
import mc.cyberplex.CaptureTheFlag.listeners.PlayerBreakBlock;
import mc.cyberplex.CaptureTheFlag.listeners.PlayerDeath;
import mc.cyberplex.CaptureTheFlag.listeners.PlayerScore;

public class Main extends JavaPlugin {
	
	private static Main main;
	public static Main getMain(){
		return main;
	}

	public void onEnable(){
		
		main = this;

		main.getConfig();

		main.getConfig().options().copyDefaults(true);
		createConfig();
		main.saveConfig();
		
		Arena arena = new Arena();
		arena.setArenaList();
		
		//register commands
		this.getCommand("ctf").setExecutor(new Commands());

		//register listeners
		getServer().getPluginManager().registerEvents(new FriendlyFire(), this);
		getServer().getPluginManager().registerEvents(new GetFlag(), this);
		getServer().getPluginManager().registerEvents(new Inventory(), this);
		getServer().getPluginManager().registerEvents(new LeaveJoinMC(), this);
		getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
		getServer().getPluginManager().registerEvents(new PlayerScore(), this);
		getServer().getPluginManager().registerEvents(new JoinSign(), this);
		getServer().getPluginManager().registerEvents(new KitListener(), this);
		getServer().getPluginManager().registerEvents(new FlagBererDamage(), this);
		getServer().getPluginManager().registerEvents(new PlayerBreakBlock(), this);		
		
	}

	public void onDisable(){
		ArenaState state = new ArenaState();
		
		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){
			
			state.stop(arenaName);
			
		}
		
		this.saveConfig();
	}

	private void createConfig(){

		try {

			//Check to see theirs a folder
			if(!getDataFolder().exists()){
				getDataFolder().mkdirs();
			}

			//Create file 
			File configFile = new File(getDataFolder(), "config.yml");

			//Check if file doesn't exist
			if(!configFile.exists()){

				//Display error message to console
				getLogger().info("Config.yml not found, creating file");

				//create file
				saveDefaultConfig();

			} else {

				//Display message saying file was found
				getLogger().info("Config.yml found, loading file");

			}

		} catch (Exception except) {

			//display error message to console 
			except.printStackTrace();
		}

	}

}
