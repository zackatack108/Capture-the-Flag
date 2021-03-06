package mc.cyberplex.CaptureTheFlag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import mc.cyberplex.CaptureTheFlag.Main;
import mc.cyberplex.CaptureTheFlag.arena.Arena;

public class FallDamage implements Listener{

	Main main = Main.getMain();
	Arena data = new Arena();

	@EventHandler
	public void playerFallDamage(EntityDamageEvent event) {

		if(event.getEntity() instanceof Player) {

			Player player = (Player) event.getEntity();
			Boolean inGame = false, hasFlag = false;

			for(String arenaName : main.getConfig().getConfigurationSection("Arenas").getKeys(false)) {

				int arenaNum = data.getArenaNum(arenaName);

				for(int count = 0; count < data.getArena(arenaNum).getGameCount(); count++) {

					if(player.getUniqueId().equals(data.getArena(arenaNum).getPlayer(count))) {

						inGame = true;

						if(player.getUniqueId().equals(data.getCTFData(arenaNum).getHasBlueFlag()) || player.getUniqueId().equals(data.getCTFData(arenaNum).getHasRedFlag())) {
							hasFlag = true;
						}

					}

				}

			}

			if(inGame == true && hasFlag == true && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
				event.setCancelled(true);
			}

		}

	}

}
