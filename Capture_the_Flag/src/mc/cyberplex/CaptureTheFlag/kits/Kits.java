package mc.cyberplex.CaptureTheFlag.kits;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import mc.cyberplex.CaptureTheFlag.arena.Arena;

public class Kits {
	
	Arena data = new Arena();

	public void getDefault(String arenaName, Player player){
		
		int arenaNum = data.getArenaNum(arenaName);
		
		for(int subscript = 0; subscript < data.getArena(arenaNum).getGameCount(); subscript++){
			
			if(subscript < data.getCTFData(arenaNum).getRedTeamCount() && data.getCTFData(arenaNum).getPlayerOnRedTeam(subscript).equals(player.getUniqueId())){
				
				ItemStack redHelmet = new ItemStack(Material.LEATHER_HELMET);
				LeatherArmorMeta helmet = (LeatherArmorMeta) redHelmet.getItemMeta();
				helmet.setColor(Color.RED);
				redHelmet.setItemMeta(helmet);
				
				player.getInventory().setHelmet(redHelmet);
				
			} else if(subscript < data.getCTFData(arenaNum).getBlueTeamCount() && data.getCTFData(arenaNum).getPlayerOnBlueTeam(subscript).equals(player.getUniqueId())){
				
				ItemStack blueHelmet = new ItemStack(Material.LEATHER_HELMET);
				LeatherArmorMeta helmet = (LeatherArmorMeta) blueHelmet.getItemMeta();
				helmet.setColor(Color.BLUE);
				blueHelmet.setItemMeta(helmet);
				
				player.getInventory().setHelmet(blueHelmet);
				
			}
			
		}
		
		ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		ItemStack arrow = new ItemStack(Material.ARROW, 1);
		
		ItemStack ironChestPlate = new ItemStack(Material.IRON_CHESTPLATE);
		ItemStack ironLegging = new ItemStack(Material.IRON_LEGGINGS);
		ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
		
		ItemMeta bowEnchant = bow.getItemMeta();
		bowEnchant.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		bow.setItemMeta(bowEnchant);
		
		player.getInventory().addItem(diamondSword);
		player.getInventory().addItem(bow);
		player.getInventory().addItem(arrow);
		
		player.getInventory().setChestplate(ironChestPlate);
		player.getInventory().setLeggings(ironLegging);
		player.getInventory().setBoots(ironBoots);
		
	}
	
}
