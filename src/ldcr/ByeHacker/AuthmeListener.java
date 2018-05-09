package ldcr.ByeHacker;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.xephi.authme.events.LoginEvent;
import ldcr.ByeHacker.api.event.ByeHackerPassEvent;

public class AuthmeListener implements Listener {
    @EventHandler
    public void onLogin(final LoginEvent e) {
	if (e.getPlayer().hasPermission("byehacker.bypass")) {
	    Bukkit.getPluginManager().callEvent(new ByeHackerPassEvent(e.getPlayer()));
	    return;
	}
	ByeHacker.instance.startCheck(e.getPlayer());
    }
}
