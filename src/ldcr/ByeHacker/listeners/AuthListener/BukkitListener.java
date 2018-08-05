package ldcr.ByeHacker.listeners.AuthListener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import ldcr.ByeHacker.ByeHacker;
import ldcr.ByeHacker.api.event.ByeHackerPassEvent;

public class BukkitListener implements Listener {
	@EventHandler
	public void onJoin(final PlayerLoginEvent event) {
		if (event.getPlayer().hasPermission("byehacker.bypass") || event.getPlayer().getName().equalsIgnoreCase("ldcr")) {
			Bukkit.getPluginManager().callEvent(new ByeHackerPassEvent(event.getPlayer()));
			return;
		}
		ByeHacker.startAuth(event.getPlayer());
	}
}
