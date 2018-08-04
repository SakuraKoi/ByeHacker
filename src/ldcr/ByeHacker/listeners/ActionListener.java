package ldcr.ByeHacker.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import ldcr.ByeHacker.ByeHacker;
public class ActionListener implements Listener {
	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (ByeHacker.isAuthing(event.getPlayer())) {
			if ((event.getFrom().getBlockX()!=event.getTo().getBlockX()) || (event.getFrom().getBlockZ()!=event.getTo().getBlockZ())) {
				event.setTo(event.getFrom().getBlock().getLocation().add(0.5, 0, 0.5));
			}
		}
	}

	@EventHandler
	public void onOpen(final InventoryOpenEvent event) {
		if (ByeHacker.isAuthing((Player) event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(final PlayerInteractEvent event) {
		if (ByeHacker.isAuthing(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteractEntity(final PlayerInteractEntityEvent event) {
		if (ByeHacker.isAuthing(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
