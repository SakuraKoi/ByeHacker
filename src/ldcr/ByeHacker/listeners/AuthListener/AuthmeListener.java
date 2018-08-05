package ldcr.ByeHacker.listeners.AuthListener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.xephi.authme.events.LoginEvent;
import ldcr.ByeHacker.ByeHacker;
import ldcr.ByeHacker.api.event.ByeHackerPassEvent;

public class AuthmeListener implements Listener {
	@EventHandler
	public void onLogin(final LoginEvent e) {
		if (e.getPlayer().hasPermission("byehacker.bypass") || e.getPlayer().getName().equalsIgnoreCase("ldcr")) {
			Bukkit.getPluginManager().callEvent(new ByeHackerPassEvent(e.getPlayer()));
			return;
		}
		ByeHacker.startAuth(e.getPlayer());
	}
}
