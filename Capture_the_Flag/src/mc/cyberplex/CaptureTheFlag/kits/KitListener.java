package mc.cyberplex.CaptureTheFlag.kits;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.kits.enumerations.KitType;

public class KitListener implements Listener{
	
	Main main = Main.getMain();
	
	@EventHandler //listener for the shop so the shop can be used
	public void shopListener(InventoryClickEvent event) {

		//get the player that is clicking in the inventory 
		Player player = (Player) event.getWhoClicked();

		//check to see if the inventory the player has open is the laser tag shop
		if(event.getInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "CTF Kits")) {

			//cancel the event so the item isn't pulled from the inventory
			event.setCancelled(true);

			switch(event.getSlot()) {

			case 11:				

				main.getConfig().set("Kits." + player.getUniqueId().toString(), KitType.KNIGHT.toString());
				main.saveConfig();
				
				player.sendMessage(ChatColor.YELLOW + "You selected the knight kit");
				
				break;

			case 12:
				
				main.getConfig().set("Kits." + player.getUniqueId().toString(), KitType.SOLDIER.toString());
				main.saveConfig();
				
				player.sendMessage(ChatColor.YELLOW + "You selected the soldier kit");

				break;

			case 13:
				
				main.getConfig().set("Kits." + player.getUniqueId().toString(), KitType.ARCHER.toString());
				main.saveConfig();
				
				player.sendMessage(ChatColor.YELLOW + "You selected the archer kit");
				
				break;

			case 14:
				
				main.getConfig().set("Kits." + player.getUniqueId().toString(), KitType.ALCHEMIST.toString());
				main.saveConfig();
				
				player.sendMessage(ChatColor.YELLOW + "You selected the alchemist kit");

				break;

			case 15:
				
				main.getConfig().set("Kits." + player.getUniqueId().toString(), KitType.SCOUT.toString());
				main.saveConfig();
				
				player.sendMessage(ChatColor.YELLOW + "You selected the scout kit");
				
				break;

			}

		}

	}

}
