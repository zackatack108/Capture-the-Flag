package mc.cyberplex.CaptureTheFlag.kits;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import mc.cyberplex.CaptureTheFlag.arena.Arena;

public class Kits {

	Arena data = new Arena();

	public void getKit(String arenaName, Player player) {

		int arenaNum = data.getArenaNum(arenaName);
		int index = 0;
		for(int i = 0; i < data.getArena(arenaNum).getGameCount(); i++) {
			if(data.getArena(arenaNum).getPlayer(i).equals(player.getUniqueId())) {
				index = i;
			}
		}

		String kit = data.getCTFData(arenaNum).getPlayersKits(index);

		switch(kit) {

		case "SOLDIER":
			getSoldier(arenaName, player);
			break;

		case "ARCHER":
			getArcher(arenaName, player);
			break;

		case "KNIGHT":
			getKnight(arenaName, player);
			break;

		case "ALCHEMIST":
			getAlchemist(arenaName, player);
			break;

		case "SCOUT":
			getScout(arenaName, player);
			break;

		default:
			getSoldier(arenaName, player);
			break;

		}

	}

	public void setPlayerHelmet(String arenaName, Player player) {

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

	}

	public void getSoldier(String arenaName, Player player){

		ItemStack ironSword = new ItemStack(Material.IRON_SWORD, 1);
		ItemStack shield = new ItemStack(Material.SHIELD);

		ItemStack ironChestPlate = new ItemStack(Material.IRON_CHESTPLATE);
		ItemStack ironLegging = new ItemStack(Material.IRON_LEGGINGS);
		ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);

		setPlayerHelmet(arenaName, player);

		player.getInventory().addItem(ironSword);
		player.getInventory().setItemInOffHand(shield);

		player.getInventory().setChestplate(ironChestPlate);
		player.getInventory().setLeggings(ironLegging);
		player.getInventory().setBoots(ironBoots);

	}

	public void getArcher(String arenaName, Player player) {

		ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		ItemStack arrow = new ItemStack(Material.ARROW, 1);

		ItemStack chainChest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemStack chainLegs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemStack chainBoots = new ItemStack(Material.CHAINMAIL_BOOTS);

		ItemMeta bowEnchant = bow.getItemMeta();
		bowEnchant.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		bowEnchant.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		bow.setItemMeta(bowEnchant);

		setPlayerHelmet(arenaName, player);

		player.getInventory().addItem(bow);
		player.getInventory().addItem(stoneSword);
		player.getInventory().addItem(arrow);

		player.getInventory().setChestplate(chainChest);
		player.getInventory().setLeggings(chainLegs);
		player.getInventory().setBoots(chainBoots);

	}

	public void getKnight(String arenaName, Player player) {

		ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemStack shield = new ItemStack(Material.SHIELD);

		ItemStack diamondChestPlate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack diamondLegging = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);

		setPlayerHelmet(arenaName, player);

		player.getInventory().addItem(diamondSword);
		player.getInventory().setItemInOffHand(shield);

		player.getInventory().setChestplate(diamondChestPlate);
		player.getInventory().setLeggings(diamondLegging);
		player.getInventory().setBoots(diamondBoots);

	}

	public void getAlchemist(String arenaName, Player player) {

		ItemStack leatherChest = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack leatherLegs = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);

		ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);

		PotionEffect healthEffect = new PotionEffect(PotionEffectType.HEAL, 1200, 1);
		ItemStack healthPotion = new ItemStack(Material.SPLASH_POTION, 64);
		PotionMeta healthMeta = (PotionMeta) healthPotion.getItemMeta();
		healthMeta.setDisplayName("Splash Potion of Healing");
		healthMeta.addCustomEffect(healthEffect, true);
		healthPotion.setItemMeta(healthMeta);

		PotionEffect jumpEffect = new PotionEffect(PotionEffectType.JUMP, 4800, 2);
		ItemStack jumpPotion = new ItemStack(Material.SPLASH_POTION, 64);
		PotionMeta jumpMeta = (PotionMeta) jumpPotion.getItemMeta();
		jumpMeta.setDisplayName("Splash Potion of Leaping");
		jumpMeta.setColor(Color.LIME);
		jumpMeta.addCustomEffect(jumpEffect, true);
		jumpPotion.setItemMeta(jumpMeta);

		PotionEffect regenEffect = new PotionEffect(PotionEffectType.REGENERATION, 2400, 1);
		ItemStack regenPotion = new ItemStack(Material.SPLASH_POTION, 64);
		PotionMeta regenMeta = (PotionMeta) regenPotion.getItemMeta();
		regenMeta.setDisplayName("Splash Potion of Regeneration");
		regenMeta.setColor(Color.MAROON);
		regenMeta.addCustomEffect(regenEffect, true);
		regenPotion.setItemMeta(regenMeta);

		PotionEffect harmingEffect = new PotionEffect(PotionEffectType.HARM, 1200, 1);
		ItemStack harmingPotion = new ItemStack(Material.SPLASH_POTION, 64);
		PotionMeta harmingMeta = (PotionMeta) harmingPotion.getItemMeta();
		harmingMeta.setDisplayName("Splash Potion of Harming");
		harmingMeta.setColor(Color.PURPLE);
		harmingMeta.addCustomEffect(harmingEffect, true);
		harmingPotion.setItemMeta(harmingMeta);

		setPlayerHelmet(arenaName, player);

		player.getInventory().addItem(stoneSword);
		player.getInventory().addItem(healthPotion);
		player.getInventory().addItem(jumpPotion);
		player.getInventory().addItem(regenPotion);
		player.getInventory().addItem(harmingPotion);

		player.getInventory().setChestplate(leatherChest);
		player.getInventory().setLeggings(leatherLegs);
		player.getInventory().setBoots(leatherBoots);

	}

	public void getScout(String arenaName, Player player) {

		ItemStack leatherChest = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack leatherLegs = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);		
		ItemStack ironSword = new ItemStack(Material.IRON_SWORD, 1);

		PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, 4800, 1);
		ItemStack speedPotion = new ItemStack(Material.SPLASH_POTION, 64);
		PotionMeta speedMeta = (PotionMeta) speedPotion.getItemMeta();
		speedMeta.setDisplayName("Splash Potion of Swiftness");
		speedMeta.setColor(Color.BLUE);
		speedMeta.addCustomEffect(speedEffect, true);
		speedPotion.setItemMeta(speedMeta);

		setPlayerHelmet(arenaName, player);

		player.getInventory().addItem(ironSword);
		player.getInventory().addItem(speedPotion);
		player.getInventory().setChestplate(leatherChest);
		player.getInventory().setLeggings(leatherLegs);
		player.getInventory().setBoots(leatherBoots);

	}

}
