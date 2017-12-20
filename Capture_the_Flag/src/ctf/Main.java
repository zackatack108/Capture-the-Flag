package ctf;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

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
		
		//register commands
		this.getCommand("ctf").setExecutor(new Commands());

		//register listeners
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getServer().getPluginManager().registerEvents(new SignListener(), this);
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
