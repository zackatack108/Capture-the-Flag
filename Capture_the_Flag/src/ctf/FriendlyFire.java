package ctf;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FriendlyFire implements Listener {

	Main main = Main.getMain();	
	ArenaData data = new ArenaData();

	//listener to check when a player shoots another player on the same team
	@EventHandler
	public void onArrowHit(EntityDamageByEntityEvent event) {
		
		boolean shooterInGame = false, shooterOnRed = false, shooterOnBlue = false;
		boolean	victimInGame = false, victimOnRed = false, victimOnBlue = false;

		if(event.getDamager() instanceof Arrow) {

			Arrow arrow = (Arrow)event.getDamager();

			if(arrow.getShooter() instanceof Player && event.getEntity() instanceof Player) {

				Player shooter = (Player) arrow.getShooter();
				Player victim = (Player) event.getEntity();

				//cycle through arena list in the config
				for(String arenaName : main.getConfig().getConfigurationSection("Arenas").getKeys(false)) {

					int arenaNum = data.getArenaNum(arenaName);

					//cycle through the players that are in the arena
					for(int count = 0; count < data.getArena(arenaNum).getInGameCount(); count++) {

						//check to see if the shooter is in the game
						if(shooter.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(count)))
							shooterInGame = true;

						//check to see if the victim is in the game
						if(victim.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(count)))
							victimInGame = true;

					}
					
					for(int count = 0; count < data.getArena(arenaNum).getRedTeamCount(); count++) {
						
						//check to see if the shooter is on red team
						if(shooter.getUniqueId().toString().equals(data.getArena(arenaNum).getRedTeam(count)))
							shooterOnRed = true;
						
						//check to see if the victim is on red team
						if(victim.getUniqueId().toString().equals(data.getArena(arenaNum).getRedTeam(count)))
							victimOnRed = true;
						
					}
					
					for(int count = 0; count < data.getArena(arenaNum).getBlueTeamCount(); count++) {
						
						//check if the shooter is on blue team
						if(shooter.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueTeam(count)))
							shooterOnBlue = true;
						
						//check if the victim is on blue team
						if(victim.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueTeam(count)))
							victimOnBlue = true;
						
					}

				}

			}

			//check to see if both the shooter and victim are in the game
			if(shooterInGame == true && victimInGame == true) {

				//check to see if both the shooter and victim are on red team and cancel the event
				if(shooterOnRed == true && victimOnRed == true)
					event.setCancelled(true);

				//check to see if both the shooter and victim are on blue team and cancel the event
				if(shooterOnBlue == true && victimOnBlue == true)
					event.setCancelled(true);

			}
			//check if the shooter is in the game and the victim isn't and cancel the event
			else if(shooterInGame == true && victimInGame == false)
				event.setCancelled(true);

			//check if the shooter isn't in the game but the victim is and cancel the event
			else if(shooterInGame == false && victimInGame == true)
				event.setCancelled(true);

		}

	}

	//listener to check when a player hits someone of the same team
	@EventHandler
	public void playerHitEvent(EntityDamageByEntityEvent event) {
		
		boolean attackerInGame = false, attackerOnRed = false, attackerOnBlue = false;
		boolean victimInGame = false, victimOnRed = false, victimOnBlue = false;

		//check to see if the damager and the entity are instances of players
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			
			Player hitter = (Player) event.getDamager();
			Player victim = (Player) event.getEntity();

			//cycle through arena list in the config
			for(String arenaName : main.getConfig().getConfigurationSection("Arenas").getKeys(false)) {

				int arenaNum = data.getArenaNum(arenaName);

				//cycle through the players that are in the arena
				for(int subscript = 0; subscript < data.getArena(arenaNum).getInGameCount(); subscript++) {

					//check if the attacker is in the game
					if(hitter.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(subscript)))
						attackerInGame = true;
					
					//check if the victim is in the game
					if(victim.getUniqueId().toString().equals(data.getArena(arenaNum).getInGame(subscript)))
						victimInGame = true;

				}
				
				//cycle through the players that are on red team
				for(int subscript = 0; subscript < data.getArena(arenaNum).getRedTeamCount(); subscript++) {

					//check if the attacker is on red team
					if(hitter.getUniqueId().toString().equals(data.getArena(arenaNum).getRedTeam(subscript)))
						attackerOnRed = true;
					
					//check if the victim is on red team
					if(victim.getUniqueId().toString().equals(data.getArena(arenaNum).getRedTeam(subscript)))
						victimOnRed = true;

				}
				
				//cycle through the players that are on blue team
				for(int subscript = 0; subscript < data.getArena(arenaNum).getBlueTeamCount(); subscript++) {

					//check if the attacker is on blue team
					if(hitter.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueTeam(subscript)))
						attackerOnBlue = true;
					
					//check if the victim is on blue team
					if(victim.getUniqueId().toString().equals(data.getArena(arenaNum).getBlueTeam(subscript)))
						victimOnBlue = true;

				}
				

			}
			
			//check to see if the hitter and victim are both in game
			if(attackerInGame == true && victimInGame == true) {
				
				//check if the hitter and victim are on red and cancel the event
				if(attackerOnRed == true && victimOnRed == true)
					event.setCancelled(true);
				
				//check if the hitter and victim are on blue and cancel the event
				if(attackerOnBlue == true && victimOnBlue == true)
					event.setCancelled(true);
				
			}
			
			//check to see if the hitter is in the game and if the victim isn't and cancel the event
			else if(attackerInGame == true && victimInGame == false)
				event.setCancelled(true);
			
			//check to see if the hitter isn't in the game and the victim is and cancel the event
			else if(attackerInGame == false && victimInGame == true)
				event.setCancelled(true);
			
		}

	}

}
