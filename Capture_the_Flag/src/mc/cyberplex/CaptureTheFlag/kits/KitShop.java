package mc.cyberplex.CaptureTheFlag.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitShop {
	
	public void createCTFShop(Player player) {
		
		Inventory CTFShop = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "CTF Kits");		
		
		ItemStack knight = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemMeta knightMeta = knight.getItemMeta();
		knightMeta.setDisplayName(ChatColor.YELLOW + "Choose knight kit which comes with diamond gear");
		knight.setItemMeta(knightMeta);
		
		ItemStack soldier = new ItemStack(Material.IRON_SWORD);
		ItemMeta soldierMeta = soldier.getItemMeta();
		soldierMeta.setDisplayName(ChatColor.YELLOW + "Choose soldier kit which comes with iron gear");
		soldier.setItemMeta(soldierMeta);
		
		ItemStack archer = new ItemStack(Material.BOW);
		ItemMeta archerMeta = archer.getItemMeta();
		archerMeta.setDisplayName(ChatColor.YELLOW + "Choose archer kit which comes with bow and arrow");
		archer.setItemMeta(archerMeta);
		
		ItemStack alchemist = new ItemStack(Material.POTION);
		ItemMeta alchemistMeta = alchemist.getItemMeta();
		alchemistMeta.setDisplayName(ChatColor.YELLOW + "Choose alchemist kit which comes with serveral potions");
		alchemist.setItemMeta(alchemistMeta);
		
		ItemStack scout = new ItemStack(Material.LEATHER_BOOTS);
		ItemMeta scoutMeta = scout.getItemMeta();
		scoutMeta.setDisplayName(ChatColor.YELLOW + "Choose scout kit which comes with speed potion");
		scout.setItemMeta(scoutMeta);
		
		CTFShop.setItem(11, knight);
		CTFShop.setItem(12, soldier);
		CTFShop.setItem(13, archer);
		CTFShop.setItem(14, alchemist);
		CTFShop.setItem(15, scout);
		
		player.openInventory(CTFShop);
		
	}

}
