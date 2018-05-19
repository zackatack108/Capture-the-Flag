package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.arena.Arena;

public class Inventory implements Listener {
	
	Main main = Main.getMain();
	Arena data = new Arena();

	@EventHandler
	public void denyInventoryMove(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		
		for(String arenaName : main.getConfig().getConfigurationSection("Arenas").getKeys(false)) {
			
			int arenaNum = data.getArenaNum(arenaName);
			
			for(int count = 0; count < data.getArena(arenaNum).getGameCount(); count++) {
				
				if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(count))) {
					event.setCancelled(true);
				}
				
			}
			
		}
		
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		
		Player player = (Player) event.getPlayer();
		
		for(String arenaName : main.getConfig().getConfigurationSection("Arenas").getKeys(false)) {
			
			int arenaNum = data.getArenaNum(arenaName);
			
			for(int count = 0; count < data.getArena(arenaNum).getGameCount(); count++) {
				
				if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(count))) {
					event.setCancelled(true);
				}
				
			}
			
		}
		
	}
	
}
