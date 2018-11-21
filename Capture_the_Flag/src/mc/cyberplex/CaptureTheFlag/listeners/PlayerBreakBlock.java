package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.arena.Arena;

public class PlayerBreakBlock implements Listener {
	
	Main main = Main.getMain();
	Arena data = new Arena();

	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		
		for(String arenaName: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){
			
			int arenaNum = data.getArenaNum(arenaName);
			
			for(int index = 0; index < data.getArena(arenaNum).getGameCount(); index++) {
				
				if(data.getArena(arenaNum).getPlayer(index).equals(player.getUniqueId())) {
					event.setCancelled(true);
				}
				
			}
			
		}
		
	}
	
}
